package jarvice.frontend.wookie;

import java.util.EnumSet;

import jarvice.intermediate.symtabimpl.SymTabKeyImpl;
import jarvice.intermediate.SymTabEntry;
import jarvice.intermediate.symtabimpl.DefinitionImpl;
import jarvice.frontend.wookie.parsers.BlockParser;
import jarvice.frontend.*;
import jarvice.frontend.wookie.parsers.*;
import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.Predefined;
import jarvice.message.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.message.MessageType.PARSER_SUMMARY;
import static jarvice.frontend.wookie.WookieErrorCode.UNEXPECTED_TOKEN;


/**
 * PascalParserTD
 * 
 * 
 */
public class WookieParserTD extends Parser {
	protected static WookieErrorHandler errorHandler = new WookieErrorHandler();

	 private SymTabEntry routineId;  // name of the routine being parsed
	
	
	/**
	 * Constructor.
	 * 
	 * @param scanner
	 *            the scanner to be used with this parser.
	 */
	public WookieParserTD(Scanner scanner) {
		super(scanner);
	}

	/**
	 * Constructor for subclasses.
	 * 
	 * @param parent
	 *            the parent parser.
	 */
	public WookieParserTD(WookieParserTD parent) {
		super(parent.getScanner());
	}

	
		
	/**
	 * Getter.
	 * 
	 * @return the error handler.
	 */
	public WookieErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * Parse a Wookie source program and generate the symbol table and the
	 * intermediate code.
	 * 
	 * @throws Exception
	 *             if an error occurred.
	 */
	public void parse() throws Exception {
		long startTime = System.currentTimeMillis();
		iCode = ICodeFactory.createICode();
		
		// Create a dummy program identifier symbol table entry.
        routineId = symTabStack.enterLocal("DummyProgramName".toLowerCase());
        routineId.setDefinition(DefinitionImpl.PROGRAM);
        symTabStack.setProgramId(routineId);

        // Push a new symbol table onto the symbol table stack and set
        // the routine's symbol table and intermediate code.
        routineId.setAttribute(ROUTINE_SYMTAB, symTabStack.push());
        routineId.setAttribute(ROUTINE_ICODE, iCode);

        BlockParser blockParser = new BlockParser(this);
			
		

		try {

			Token token = nextToken();
			ICodeNode rootNode = blockParser.parse(token, routineId);
			iCode.setRoot(rootNode);
			symTabStack.pop();
			
			
			// Look for the ( token to parse a compound statement.

			if (token.getType() == LEFT_BRACE) {
				StatementParser statementParser = new StatementParser(this);
				rootNode = statementParser.parse(token);
				token = currentToken();
			}

			else {
				errorHandler.flag(token, UNEXPECTED_TOKEN, this);
			}

			token = currentToken();

			// Set the parse tree root node.
			if (rootNode != null) {
				iCode.setRoot(rootNode);
			}

			// Send the parser summary message.
			float elapsedTime = (System.currentTimeMillis() - startTime) / 1000f;
			sendMessage(new Message(PARSER_SUMMARY, new Number[] {
					token.getLineNumber(), getErrorCount(), elapsedTime }));
		} catch (java.io.IOException ex) {
			errorHandler.abortTranslation(IO_ERROR, this);
		}
	}

	/**
	 * Return the number of syntax errors found by the parser.
	 * 
	 * @return the error count.
	 */
	public int getErrorCount() {
		return errorHandler.getErrorCount();
	}
	
	 public Token synchronize(EnumSet syncSet)
		        throws Exception
		    {
		        Token token = currentToken();
		  
		        // If the current token is not in the synchronization set,
		        // then it is unexpected and the parser must recover.
		        if (!syncSet.contains(token.getType())) {

		            // Flag the unexpected token.
		            errorHandler.flag(token, UNEXPECTED_TOKEN, this);

		            // Recover by skipping tokens that are not
		            // in the synchronization set.
		            do {
		                token = nextToken();
		            } while (!(token instanceof EofToken) &&
		                     !syncSet.contains(token.getType()));
		       }

		       return token;
		    }
}
