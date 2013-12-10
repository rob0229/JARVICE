package jarvice.frontend.wookie.parsers;

import java.util.ArrayList;
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
import static jarvice.intermediate.symtabimpl.DefinitionImpl.*;
import static jarvice.intermediate.symtabimpl.RoutineCodeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;
import static jarvice.intermediate.typeimpl.TypeFormImpl.*;

public class CallParser extends StatementParser {

	public CallParser(WookieParserTD parent) {
		super(parent);
	}

	public ICodeNode parse(Token token) throws Exception {
		SymTabEntry pfId = symTabStack.lookup(token.getText().toLowerCase());
		RoutineCode routineCode = (RoutineCode) pfId.getAttribute(ROUTINE_CODE);
		StatementParser callParser = (routineCode == DECLARED)
				|| (routineCode == FORWARD) ? new CallDeclaredParser(this)
				: new CallStandardParser(this);

		return callParser.parse(token);
	}

	// Synchronization set for the , token.
	private static final EnumSet<WookieTokenType> COMMA_SET = EnumSet.of(PLUS,
			MINUS, IDENTIFIER, INTEGER, INT, REAL, STRING, WookieTokenType.NOT,
			LEFT_PAREN);

	static {
		COMMA_SET.add(COMMA);
		COMMA_SET.add(RIGHT_PAREN);
	};

	protected ICodeNode parseActualParameters(Token token, SymTabEntry pfId,
			boolean isDeclared, boolean isReadReadln, boolean isWriteWriteln)
			throws Exception {
		System.out.println(token.getText());
		ExpressionParser expressionParser = new ExpressionParser(this);
		ICodeNode parmsNode = ICodeFactory.createICodeNode(PARAMETERS);
		ArrayList<SymTabEntry> formalParms = null;
		int parmCount = 0;
		int parmIndex = -1;

		if (isDeclared) {
			formalParms = (ArrayList<SymTabEntry>) pfId.getAttribute(ROUTINE_PARMS);
			parmCount = formalParms != null ? formalParms.size() : 0;
		}

		if (token.getType() != LEFT_PAREN) {
			if (parmCount != 0) {
				errorHandler.flag(token, WRONG_NUMBER_OF_PARMS, this);
			}

			return null;
		}

		token = nextToken(); // consume opening (

		// Loop to parse each actual parameter.
		while (token.getType() != RIGHT_PAREN) {
			ICodeNode actualNode = expressionParser.parse(token);
			// System.out.println(actualNode.getAttribute(CONSTANT_VALUE));
			// Declared procedure or function: Check the number of actual
			// parameters, and check each actual parameter against the
			// corresponding formal parameter.
		
			if (isDeclared) {
				if (++parmIndex < parmCount) {
					SymTabEntry formalId = formalParms.get(parmIndex);

					checkActualParameter(token, formalId, actualNode);
				} else if (parmIndex == parmCount) {
					errorHandler.flag(token, WRONG_NUMBER_OF_PARMS, this);
				}
			}

			// read or readln: Each actual parameter must be a variable that is
			// a scalar, boolean, or subrange of integer.
			else if (isReadReadln) {
				TypeSpec type = actualNode.getTypeSpec();
				TypeForm form = type.getForm();

				if (!((actualNode.getType() == ICodeNodeTypeImpl.VARIABLE) && ((form == SCALAR)
						|| (type == Predefined.booleanType) || ((form == SUBRANGE) && (type
						.baseType() == Predefined.integerType))))) {
					errorHandler.flag(token, INVALID_VAR_PARM, this);
				}
			}

			// write or writeln: The type of each actual parameter must be a
			// scalar, boolean, or a Pascal string. Parse any field width and
			// precision.
			else if (isWriteWriteln) {

				// Create a WRITE_PARM node which adopts the expression node.
				ICodeNode exprNode = actualNode;
				actualNode = ICodeFactory.createICodeNode(WRITE_PARM);
				actualNode.addChild(exprNode);

				TypeSpec type = exprNode.getTypeSpec().baseType();
				TypeForm form = type.getForm();

				if (!((form == SCALAR) || (type == Predefined.booleanType) || (type
						.isPascalString()))) {
					errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
				}

				// Optional field width.
				token = currentToken();
				actualNode.addChild(parseWriteSpec(token));

				// Optional precision.
				token = currentToken();
				actualNode.addChild(parseWriteSpec(token));
			}

			parmsNode.addChild(actualNode);
			token = synchronize(COMMA_SET);

			TokenType tokenType = token.getType();

			// Look for the comma.
			if (tokenType == COMMA) {
				token = nextToken(); // consume ,
			} else if (ExpressionParser.EXPR_START_SET.contains(tokenType)) {
				errorHandler.flag(token, MISSING_COMMA, this);
			} else if (tokenType != RIGHT_PAREN) {
				token = synchronize(ExpressionParser.EXPR_START_SET);
			}
		}

		token = nextToken(); // consume closing )

		if ((parmsNode.getChildren().size() == 0)
				|| (isDeclared && (parmIndex != parmCount - 1))) {
			errorHandler.flag(token, WRONG_NUMBER_OF_PARMS, this);
		}

		return parmsNode;
	}

	private void checkActualParameter(Token token, SymTabEntry formalId,
			ICodeNode actualNode) {
		Definition formalDefn = formalId.getDefinition();

		TypeSpec formalType = formalId.getTypeSpec();
		TypeSpec actualType = actualNode.getTypeSpec();

		// VAR parameter: The actual parameter must be a variable of the same
		// type as the formal parameter.
		if (formalDefn == VAR_PARM) {
			if ((actualNode.getType() != ICodeNodeTypeImpl.VARIABLE)
					|| (actualType != formalType)) {
				errorHandler.flag(token, INVALID_VAR_PARM, this);
			}
		}

		// Value parameter: The actual parameter must be assignment-compatible
		// with the formal parameter.
		else if (!TypeChecker.areAssignmentCompatible(formalType, actualType)) {
			errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
		}
	}

	/**
	 * Parse the field width or the precision for an actual parameter of a call
	 * to write or writeln.
	 * 
	 * @param token
	 *            the current token.
	 * @return the INTEGER_CONSTANT node or null
	 * @throws Exception
	 *             if an error occurred.
	 */
	private ICodeNode parseWriteSpec(Token token) throws Exception {
		if (token.getType() == COLON) {
			token = nextToken(); // consume :

			ExpressionParser expressionParser = new ExpressionParser(this);
			ICodeNode specNode = expressionParser.parse(token);

			if (specNode.getType() == INTEGER_CONSTANT) {
				return specNode;
			} else {
				errorHandler.flag(token, INVALID_NUMBER, this);
				return null;
			}
		} else {
			return null;
		}
	}
}
