package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;
import jarvice.intermediate.icodeimpl.*;
import jarvice.intermediate.typeimpl.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.FUNCTION;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.UNDEFINED;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VARIABLE;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VALUE_PARM;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VAR_PARM;
import static jarvice.intermediate.typeimpl.TypeFormImpl.ARRAY;
import static jarvice.intermediate.typeimpl.TypeFormImpl.RECORD;
import static jarvice.intermediate.typeimpl.TypeKeyImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;

public class VariableParser extends StatementParser {
	// Set to true to parse a function name
	// as the target of an assignment.
	private boolean isFunctionTarget = false;

	public VariableParser(WookieParserTD parent) {
		super(parent);
	}
	public boolean isReturn = false;

	 private static final EnumSet<WookieTokenType> SUBSCRIPT_FIELD_START_SET =
	 EnumSet.of(LEFT_BRACKET, DOT);

	 public ICodeNode parse(Token token) throws Exception {
		 	// Look up the identifier in the symbol table stack.
		 	String name = token.getText().toLowerCase();
		 	SymTabEntry variableId = null;
		 	 
		 	// If not found, flag the error and enter the identifier
		 	// as an undefined identifier with an undefined type.
		 	if(token.getType() == WookieTokenType.RETURN && isReturn){
		 	 SymTab symTab = symTabStack.getLocalSymTab();
		 	 name = ((SymTabImpl)symTab).getfuncName();
		 	  
		 	 /**/
		 	}
		 	 
		 	 variableId = symTabStack.lookup(name);
		 	  
		 	if(variableId == null){
		 	 errorHandler.flag(token, IDENTIFIER_UNDEFINED, this);
		 	 variableId = symTabStack.enterLocal(name);
		 	 variableId.setDefinition(UNDEFINED);
		 	 variableId.setTypeSpec(Predefined.undefinedType);
		 	}
		 	return parse(token, variableId);
		}



	public ICodeNode parseFunctionNameTarget(Token token) throws Exception {
		isFunctionTarget = true;
		return parse(token);
	}

	public ICodeNode parse(Token token, SymTabEntry variableId)
			throws Exception {

		// Check how the variable is defined.
		Definition defnCode = variableId.getDefinition();

		if (!((defnCode == VARIABLE) || (defnCode == DefinitionImpl.RETURN) ||(defnCode == VALUE_PARM)
				|| (defnCode == VAR_PARM) || (isReturn && (defnCode == FUNCTION)))) {
			errorHandler.flag(token, INVALID_IDENTIFIER_USAGE, this);
		}

		variableId.appendLineNumber(token.getLineNumber());

		ICodeNode variableNode = ICodeFactory
				.createICodeNode(ICodeNodeTypeImpl.VARIABLE);
		variableNode.setAttribute(ID, variableId);

		token = nextToken(); // consume the identifier
		TypeSpec variableType = variableId.getTypeSpec();
		
		 if (!isFunctionTarget) {
		 
		 // Parse array subscripts or record fields. 
			 while (SUBSCRIPT_FIELD_START_SET.contains(token.getType())) { ICodeNode
		 subFldNode = token.getType() == LEFT_BRACKET ?
		  parseSubscripts(variableType) : parseField(variableType); token =
		  currentToken();
		  
		  // Update the variable's type. // The variable node adopts the SUBSCRIPTS or FIELD node. variableType = subFldNode.getTypeSpec();
		  variableNode.addChild(subFldNode); } }
		variableNode.setTypeSpec(variableType);
		return variableNode;
	}

	// Synchronization set for the ] token.
	private static final EnumSet<WookieTokenType> RIGHT_BRACKET_SET = EnumSet
			.of(RIGHT_BRACKET, EQUALS, SEMICOLON);
	
	private ICodeNode parseSubscripts(TypeSpec variableType) throws Exception {
		Token token;
		ExpressionParser expressionParser = new ExpressionParser(this);
		// Create a SUBSCRIPTS node.
		ICodeNode subscriptsNode = ICodeFactory.createICodeNode(SUBSCRIPTS);

		do {
			token = nextToken(); // consume the [ or , token

			// The current variable is an array.
			if (variableType.getForm() == ARRAY) {

				// Parse the subscript expression.
				ICodeNode exprNode = expressionParser.parse(token);
				TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec()
						: Predefined.undefinedType;

				// The subscript expression type must be assignment
				// compatible with the array index type.
				TypeSpec indexType = (TypeSpec) variableType
						.getAttribute(ARRAY_INDEX_TYPE);
				if (!TypeChecker.areAssignmentCompatible(indexType, exprType)) {
					errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
				}

				// The SUBSCRIPTS node adopts the subscript expression tree.
				subscriptsNode.addChild(exprNode);

				// Update the variable's type.
				variableType = (TypeSpec) variableType
						.getAttribute(ARRAY_ELEMENT_TYPE);
			}

			// Not an array type, so too many subscripts.
			else {
				errorHandler.flag(token, TOO_MANY_SUBSCRIPTS, this);
				expressionParser.parse(token);
			}

			token = currentToken();
		} while (token.getType() == COMMA);

		// Synchronize at the ] token.
		token = synchronize(RIGHT_BRACKET_SET);
		if (token.getType() == RIGHT_BRACKET) {
			token = nextToken(); // consume the ] token
		} else {
			errorHandler.flag(token, MISSING_RIGHT_BRACKET, this);
		}

		subscriptsNode.setTypeSpec(variableType);
		return subscriptsNode;
	}

	private ICodeNode parseField(TypeSpec variableType) throws Exception {
		// Create a FIELD node.
		ICodeNode fieldNode = ICodeFactory.createICodeNode(FIELD);

		Token token = nextToken(); // consume the . token
		TokenType tokenType = token.getType();
		TypeForm variableForm = variableType.getForm();

		if ((tokenType == IDENTIFIER) && (variableForm == RECORD)) {
			SymTab symTab = (SymTab) variableType.getAttribute(RECORD_SYMTAB);
			String fieldName = token.getText().toLowerCase();
			SymTabEntry fieldId = symTab.lookup(fieldName);

			if (fieldId != null) {
				variableType = fieldId.getTypeSpec();
				fieldId.appendLineNumber(token.getLineNumber());

				// Set the field identifier's name.
				fieldNode.setAttribute(ID, fieldId);
			} else {
				errorHandler.flag(token, INVALID_FIELD, this);
			}
		} else {
			errorHandler.flag(token, INVALID_FIELD, this);
		}

		token = nextToken(); // consume the field identifier

		fieldNode.setTypeSpec(variableType);
		return fieldNode;
	}

public ICodeNode parseReturn(Token token) throws Exception {
 	isReturn = true;
 	return parse(token);
 }

}


