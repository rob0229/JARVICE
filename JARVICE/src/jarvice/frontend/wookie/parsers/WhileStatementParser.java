package jarvice.frontend.wookie.parsers;



import java.util.EnumSet;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.icodeimpl.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;
import jarvice.intermediate.typeimpl.TypeChecker;

import jarvice.intermediate.symtabimpl.*;

/**
 * <h1>WhileStatementParser</h1>
 * 
 * <p>
 * Parse a Pascal WHILE statement.
 * </p>
 * 
 * <p>
 * Copyright (c) 2009 by Ronald Mak
 * </p>
 * <p>
 * For instructional purposes only. No warranties.
 * </p>
 */
public class WhileStatementParser extends StatementParser {
	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the parent parser.
	 */
	public WhileStatementParser(WookieParserTD parent) {
		super(parent);
	}

	// Synchronization set for LEFT_BRACE.
	private static final EnumSet<WookieTokenType> LEFT_BRACE_SET = StatementParser.STMT_START_SET
			.clone();
	static {
		LEFT_BRACE_SET.add(LEFT_BRACE);
		LEFT_BRACE_SET.addAll(StatementParser.STMT_FOLLOW_SET);
	}

	/**
	 * Parse a WHILE statement.
	 * 
	 * @param token
	 *            the initial token.
	 * @return the root node of the generated parse tree.
	 * @throws Exception
	 *             if an error occurred.
	 */
	public ICodeNode parse(Token token)
	        throws Exception
	    {
	        token = nextToken();  // consume the WHILE

	        // Create LOOP, and TEST nodes 
	        ICodeNode loopNode = ICodeFactory.createICodeNode(LOOP);
	        ICodeNode testNode = ICodeFactory.createICodeNode(TEST);
	        ICodeNode notNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.NOT);
	        // The LOOP node adopts the TEST node as its first child.	        
	        loopNode.addChild(testNode);
	        testNode.addChild(notNode);
	        // Parse the expression.
	        // The testNode node adopts the expression subtree as its only child.
	        ExpressionParser expressionParser = new ExpressionParser(this);
	        ICodeNode exprNode = expressionParser.parse(token);	
	        // The TEST node adopts the exprNode node as its only child.
	       notNode.addChild(exprNode);
	        	        
	        TypeSpec exprType = exprNode != null ? exprNode.getTypeSpec()
                    : Predefined.undefinedType;
	        		if (!TypeChecker.isBoolean(exprType)) {
errorHandler.flag(token, INCOMPATIBLE_TYPES, this);
}
	        //  Synchronize at the LEFT_BRACE.
	        token = synchronize(LEFT_BRACE_SET);
			if (token.getType() == LEFT_BRACE) {
				
				
				//token = nextToken(); // consume the LEFTBRACE
			} else {
				errorHandler.flag(token, MISSING_LEFT_BRACE, this);
			}

	        // Parse the statement.
	        // The LOOP node adopts the statement subtree as its second child.
	        StatementParser statementParser = new StatementParser(this);
	        loopNode.addChild(statementParser.parse(token));

	        return loopNode;
	    }
	}
