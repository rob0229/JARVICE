package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;







import jarvice.frontend.wookie.WookieTokenType;
import jarvice.frontend.wookie.parsers.IfStatementParser;
import jarvice.frontend.wookie.parsers.WhileStatementParser;
import jarvice.frontend.wookie.WookieTokenType;
import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl;
import jarvice.intermediate.symtabimpl.DefinitionImpl;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.icodeimpl.ICodeNodeTypeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VARIABLE;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VALUE_PARM;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VAR_PARM;
import static jarvice.frontend.wookie.WookieErrorCode.MISSING_SEMICOLON;
import static jarvice.frontend.wookie.WookieErrorCode.UNEXPECTED_TOKEN;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.UNDEFINED;
import static jarvice.intermediate.symtabimpl.SymTabImpl.*;

public class StatementParser extends WookieParserTD {
	
	public StatementParser(WookieParserTD parent) {
		super(parent);
	}
	// Synchronization set for starting a statement.
	protected static final EnumSet<WookieTokenType> STMT_START_SET = EnumSet
			.of(LEFT_BRACE, WookieTokenType.IF, WHILE, IDENTIFIER, INT, CHAR);

	// Synchronization set for following a statement.
	protected static final EnumSet<WookieTokenType> STMT_FOLLOW_SET = EnumSet
			.of(SEMICOLON, RIGHT_BRACE, ELSE);

	public ICodeNode parse(Token token) throws Exception {
		ICodeNode statementNode = null;
		
		switch ((WookieTokenType) token.getType()) {	
		case RETURN:{				
			SymTab symTab = symTabStack.getLocalSymTab();		
			AssignmentStatementParser assignmentParser = new AssignmentStatementParser(this);
	        ICodeNode icodenode = assignmentParser.parseReturn(token);
	        
	        setLineNumber(icodenode, token);
	        statementNode = ICodeFactory.createICodeNode(COMPOUND);
	        statementNode.addChild(icodenode); 
	        setLineNumber(statementNode, token);
	       // ICodeNode returnNode = ICodeFactory.createICodeNode(ICodeNodeTypeImpl._RETURN );
	        //setLineNumber(returnNode, token);
	        //statementNode.addChild(returnNode);			
			break;			
		}
		
		case LEFT_BRACE: {
			CompoundStatementParser compoundParser = new CompoundStatementParser(this);
			statementNode = compoundParser.parse(token);
			break;
		}
	
		case INT: {
	        IntDeclarationsParser intDeclarationsParser = new IntDeclarationsParser(this);								
	        intDeclarationsParser.setDefinition(VARIABLE);
	        intDeclarationsParser.parse(token);            
	        break;
	    }
	
		case CHAR: {
            IntDeclarationsParser intDeclarationsParser = new IntDeclarationsParser(this);								
            intDeclarationsParser.setDefinition(VARIABLE);
            intDeclarationsParser.parse(token);
            break;
        }

		case IDENTIFIER: {
             String name = token.getText().toLowerCase();
             SymTabEntry id = symTabStack.lookup(name);
             Definition idDefn = id != null ? id.getDefinition()
                                            : UNDEFINED;

             // Assignment statement or procedure call.
             switch ((DefinitionImpl) idDefn) {

                 case VARIABLE:
                 case VALUE_PARM:
                 case VAR_PARM:
                 case UNDEFINED: {
                     AssignmentStatementParser assignmentParser =
                         new AssignmentStatementParser(this);
                     statementNode = assignmentParser.parse(token);
                     break;
                 }

                 case FUNCTION: {
                     AssignmentStatementParser assignmentParser =
                         new AssignmentStatementParser(this);
                     statementNode =
                         assignmentParser.parseFunctionNameAssignment(token);
                     break;
                 }

                 case PROCEDURE: {
                     CallParser callParser = new CallParser(this);
                     statementNode = callParser.parse(token);
                     break;
                 }

                 default: {
                     errorHandler.flag(token, UNEXPECTED_TOKEN, this);
                     token = nextToken();  // consume identifier
                 }
             }

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

		setLineNumber(statementNode, token);	
		return statementNode;
	}

	protected void setLineNumber(ICodeNode node, Token token) {
		if (node != null) {
			node.setAttribute(LINE, token.getLineNumber());
		}
	}

	protected void parseList(Token token, ICodeNode parentNode,
			WookieTokenType terminator, WookieErrorCode errorCode)
			throws Exception {
		
		//without this the compound statements must be followed by a semiColon
		boolean oldToken = false;
		
		
		// Synchronization set for the terminator.
		EnumSet<WookieTokenType> terminatorSet = STMT_START_SET.clone();
		terminatorSet.add(terminator);
		terminatorSet.add(END_OF_FILE);
		terminatorSet.add(RETURN);

		// Loop to parse each statement until the } token
		// or the end of the source file.
		while (!(token instanceof EofToken) && (token.getType() != terminator)) {
			
		
			// Parse a statement. The parent node adopts the statement node.
			if((token.getType() == WookieTokenType.IF ) || (token.getType() == WHILE)){
				oldToken = true;
			}
			
		
			ICodeNode statementNode = parse(token);		
			parentNode.addChild(statementNode);
			token = currentToken();
			TokenType tokenType = token.getType();
			if(oldToken && tokenType != SEMICOLON){
				oldToken = false;			
			}
			else if (tokenType == SEMICOLON) {
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
			token = nextToken(); // consume the terminator token	
		}
		else {			
			errorHandler.flag(token, errorCode, this);
		}
	}
}
