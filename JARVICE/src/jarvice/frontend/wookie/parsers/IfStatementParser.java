package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;


import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.icodeimpl.*;
import jarvice.intermediate.symtabimpl.*;

import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;


/**
 * <h1>IfStatementParser</h1>
 *
 * <p>Parse a Wookie IF statement.</p>
 */
public class IfStatementParser extends StatementParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public IfStatementParser(WookieParserTD parent)
    {
        super(parent);
    }

    // Synchronization set for THEN.
    private static final EnumSet<WookieTokenType> LEFT_BRACE_SET =
        StatementParser.STMT_START_SET.clone();
    static {
        LEFT_BRACE_SET.add(LEFT_BRACE);
        LEFT_BRACE_SET.addAll(StatementParser.STMT_FOLLOW_SET);
    }

    /**
     * Parse an IF statement.
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    public ICodeNode parse(Token token)
            throws Exception
        {
            token = nextToken();  // consume the IF

            // Create an IF node.
            ICodeNode ifNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl.IF);

            // Parse the expression.
            // The IF node adopts the expression subtree as its first child.
            ExpressionParser expressionParser = new ExpressionParser(this);
            ifNode.addChild(expressionParser.parse(token));

            // Synchronize at the LEFT_BRACE.
            token = synchronize(LEFT_BRACE_SET);
            if (token.getType() == LEFT_BRACE) {
               // token = nextToken();  // consume the Left_Brace
            }
            else {
                errorHandler.flag(token, MISSING_LEFT_BRACE, this);
            }

            // Parse the { statement.
            // The IF node adopts the statement subtree as its second child.
            StatementParser statementParser = new StatementParser(this);
            ifNode.addChild(statementParser.parse(token));
            token = currentToken();
            
            // Look for an ELSE.
            if (token.getType() == ELSE) {
                token = nextToken();  // consume the {

                // Parse the ELSE statement.
                // The IF node adopts the statement subtree as its third child.
                ifNode.addChild(statementParser.parse(token));
            }

            return ifNode;
        }
    }
