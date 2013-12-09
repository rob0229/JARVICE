package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;

import jarvice.intermediate.ICodeNode;
import jarvice.intermediate.TypeSpec;
import jarvice.intermediate.typeimpl.TypeChecker;
import jarvice.intermediate.symtabimpl.Predefined;
import jarvice.frontend.wookie.WookieTokenType;
import jarvice.frontend.wookie.parsers.ExpressionParser;
import jarvice.frontend.wookie.parsers.StatementParser;
import jarvice.frontend.wookie.WookieTokenType;
import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;
import static jarvice.intermediate.symtabimpl.SymTabEntryImpl.*;

public class AssignmentStatementParser extends StatementParser {
	
	// Set to true to parse a function name
    // as the target of an assignment.
    private boolean isFunctionTarget = false;
	public boolean isReturn = false;

	public AssignmentStatementParser(WookieParserTD parent) {
		super(parent);
	}

	 // Synchronization set for the := token.
    private static final EnumSet<WookieTokenType> EQUALS_SET = EnumSet.of(PLUS,
			MINUS, IDENTIFIER, INTEGER, INT, REAL, STRING, WookieTokenType.NOT,
			LEFT_PAREN);
      
    static {
        EQUALS_SET.add(EQUALS);
        EQUALS_SET.addAll(StatementParser.STMT_FOLLOW_SET);
        EQUALS_SET.add(RETURN);
    }
    
	public ICodeNode parse(Token token) throws Exception {
		// Create the ASSIGN node.
					
		ICodeNode assignNode = ICodeFactory.createICodeNode(ASSIGN);							
		TokenType tokenType = token.getType();		
		SymTabEntry tokenId = null;
		String TokenName = token.getText();
		
        VariableParser variableParser = new VariableParser(this);     
        ICodeNode targetNode = isReturn
                               ? variableParser.parseReturn(token)
                               : variableParser.parse(token);
                               
        TypeSpec targetType = targetNode != null ? targetNode.getTypeSpec()
                                                 : Predefined.undefinedType;			
		token=currentToken();		
		// The ASSIGN node adopts the variable node as its first child.
		assignNode.addChild(targetNode);		
		if(tokenType != RETURN){
			// Synchronize on the = token.
	        token = synchronize(EQUALS_SET);
	        
				if (token.getType() == EQUALS) {
					token = nextToken(); // consume the =
					
				} else {
					errorHandler.flag(token, MISSING_EQUALS, this);
				}
		}
		// Parse the expression. The ASSIGN node adopts the expression's
		// node as its second child.
		ExpressionParser expressionParser = new ExpressionParser(this);		
		ICodeNode exprNode = expressionParser.parse(token);		
		token = currentToken();		
		assignNode.addChild(exprNode);
		 // Type check: Assignment compatible?
        TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec()
                                             : Predefined.undefinedType;
        
        assignNode.setTypeSpec(targetType);
		return assignNode;
	}


/**
 * Parse an assignment to a function name.
 * @param token Token
 * @return ICodeNode
 * @throws Exception
 */
public ICodeNode parseFunctionNameAssignment(Token token)
    throws Exception
{
    isFunctionTarget = true;
    return parse(token);
}

public ICodeNode parseReturn(Token token) throws Exception{
	
	isReturn = true;
	return parse(token);
	
}
}

