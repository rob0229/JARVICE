package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;

import jarvice.frontend.wookie.WookieTokenType;
import jarvice.frontend.wookie.parsers.IfStatementParser;
import jarvice.frontend.wookie.parsers.WhileStatementParser;
import jarvice.frontend.wookie.WookieTokenType;
import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;
import static jarvice.frontend.wookie.WookieErrorCode.MISSING_SEMICOLON;

/**
 * <h1>StatementParser</h1>
 * 
 * 
 * </p>
 */
public class StatementParser extends WookieParserTD {
	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            the parent parser.
	 */
	public StatementParser(WookieParserTD parent) {
		super(parent);
	}

	// Synchronization set for starting a statement.
	protected static final EnumSet<WookieTokenType> STMT_START_SET = EnumSet
			.of(LEFT_BRACE, WookieTokenType.IF, WHILE, IDENTIFIER, SEMICOLON);

	// Synchronization set for following a statement.
	protected static final EnumSet<WookieTokenType> STMT_FOLLOW_SET = EnumSet
			.of(SEMICOLON, RIGHT_BRACE, ELSE);

	/**
	 * Parse a statement. To be overridden by the specialized statement parser
	 * subclasses.
	 * 
	 * @param token
	 *            the initial token.
	 * @return the root node of the generated parse tree.
	 * @throws Exception
	 *             if an error occurred.
	 */
	public ICodeNode parse(Token token) throws Exception {
		ICodeNode statementNode = null;
		switch ((WookieTokenType) token.getType()) {

		// //Added by Rob
		// An assignment statement in c starts with the data type if the
		// variable has not been declared.
		case LEFT_BRACE: {

			CompoundStatementParser compoundParser = new CompoundStatementParser(
					this);
			statementNode = compoundParser.parse(token);
			break;
		}

		// An assignment statement begins with a variable's identifier.
		case IDENTIFIER: {
			AssignmentStatementParser assignmentParser = new AssignmentStatementParser(
					this);
			statementNode = assignmentParser.parse(token);
			break;
		}
		case WHILE: {
			WhileStatementParser whileParser = new WhileStatementParser(this);
			statementNode = whileParser.parse(token);
			break;
		}

		case IF: {
			IfStatementParser ifParser = new IfStatementParser(this);
			statementNode = ifParser.parse(token);
			break;
		}

		default: {
			statementNode = ICodeFactory.createICodeNode(NO_OP);
			break;
		}
		}

		// Set the current line number as an attribute.
		setLineNumber(statementNode, token);

		return statementNode;
	}

	/**
	 * Set the current line number as a statement node attribute.
	 * 
	 * @param node
	 *            ICodeNode
	 * @param token
	 *            Token
	 */
	protected void setLineNumber(ICodeNode node, Token token) {
		if (node != null) {
			node.setAttribute(LINE, token.getLineNumber());
		}
	}

	/**
	 * Parse a statement list.
	 * 
	 * @param token
	 *            the curent token.
	 * @param parentNode
	 *            the parent node of the statement list.
	 * @param terminator
	 *            the token type of the node that terminates the list.
	 * @param errorCode
	 *            the error code if the terminator token is missing.
	 * @throws Exception
	 *             if an error occurred.
	 */
	protected void parseList(Token token, ICodeNode parentNode,
			WookieTokenType terminator, WookieErrorCode errorCode)
			throws Exception {
 int count = 0;
		// Synchronization set for the terminator.
		EnumSet<WookieTokenType> terminatorSet = STMT_START_SET.clone();
		terminatorSet.add(terminator);

		// Loop to parse each statement until the } token
		// or the end of the source file.
		while (!(token instanceof EofToken) && (token.getType() != terminator)) {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			
 count++;
 System.out.println("Count = " + count);
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// Parse a statement. The parent node adopts the statement node.
			ICodeNode statementNode = parse(token);
			parentNode.addChild(statementNode);

			token = currentToken();
			
			TokenType tokenType = token.getType();
			

			// Look for the semicolon between statements.
			if (tokenType == SEMICOLON) {
				token = nextToken(); // consume the ;
				
			}

			// If at the start of the next statement, then missing a semicolon.
			else if (STMT_START_SET.contains(tokenType)) {
				errorHandler.flag(token, MISSING_SEMICOLON, this);
			}

			// Synchronize at the start of the next statement
			// or at the terminator.
			token = synchronize(terminatorSet);
			
		}

		// Look for the terminator token.
		if (token.getType() == terminator) {
			//System.out.println("Token is ++++++++++++++++" + token.getType());
			token = nextToken(); // consume the terminator token
			//System.out.println("Token is ++++++++++++++++" + token.getType());
		}

		else {
			errorHandler.flag(token, errorCode, this);
		}
	}
}
