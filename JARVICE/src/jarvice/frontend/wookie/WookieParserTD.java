package jarvice.frontend.wookie;

import java.util.EnumSet;

import jarvice.frontend.Token;
import jarvice.frontend.wookie.parsers.ProgramParser;
import jarvice.intermediate.symtabimpl.Predefined;
import jarvice.message.Message;
import jarvice.frontend.*;
import jarvice.frontend.wookie.parsers.*;
import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;
import jarvice.intermediate.typeimpl.*;
import jarvice.message.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.typeimpl.TypeFormImpl.*;
import static jarvice.message.MessageType.PARSER_SUMMARY;
import static jarvice.frontend.wookie.WookieErrorCode.IO_ERROR;
import static jarvice.message.MessageType.PARSER_SUMMARY;

/**
 * PascalParserTD
 * 
 * 
 */
public class WookieParserTD extends Parser {
	protected static WookieErrorHandler errorHandler = new WookieErrorHandler();

	public SymTabEntry routineId; // name of the routine being parsed

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
	        Predefined.initialize(symTabStack);

	        try {
	        	
	            Token token = nextToken();
		        
	           // We do not need to use program parser in C. Each function must be declared before it can be used in the int main() function so we do 
	           //parse them first and create the symbol tables for all the functions before int main()
	           
	            // Parse a program.
	            ProgramParser programParser = new ProgramParser(this);
	            programParser.parse(token, null);            
	             	
	          
		     
	            // Send the parser summary message.
	            float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
	            sendMessage(new Message(PARSER_SUMMARY, new Number[] {token.getLineNumber(), getErrorCount(), elapsedTime}));
	        }
	        catch (java.io.IOException ex) {
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

	public Token synchronize(EnumSet syncSet) throws Exception {
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
			} while (!(token instanceof EofToken)
					&& !syncSet.contains(token.getType()));
		}

		return token;
	}
}
