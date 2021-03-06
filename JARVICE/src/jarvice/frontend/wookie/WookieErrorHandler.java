package jarvice.frontend.wookie;


import jarvice.frontend.*;
import jarvice.message.Message;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.message.MessageType.SYNTAX_ERROR;

/**
 * <h1>WookieErrorHandler</h1>
 * 
 * <p>
 * Error handler Wookie syntax errors.
 * </p>
 */
public class WookieErrorHandler {
	private static final int MAX_ERRORS = 25;
	private static int errorCount = 0; // count of syntax errors

	/**
     * Getter.
     * @return the syntax error count.
     */
    public int getErrorCount()
    {
        return errorCount;
    }

	
	
	
	/**
	 * Flag an error in the source line.
	 * 
	 * @param token
	 *            the bad token.
	 * @param errorCode
	 *            the error code.
	 * @param parser
	 *            the parser.
	 * @return the flagger string.
	 */
	public void flag(Token token, WookieErrorCode errorCode, Parser parser) {
		// Notify the parser's listeners.
		parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] {
				token.getLineNumber(), token.getPosition(), token.getText(),
				errorCode.toString() }));
		if (++errorCount > MAX_ERRORS) {
			abortTranslation(TOO_MANY_ERRORS, parser);
		}
	}

	/**
	 * Abort the translation.
	 * 
	 * @param errorCode
	 *            the error code.
	 * @param parser
	 *            the parser.
	 */
	public void abortTranslation(WookieErrorCode errorCode, Parser parser) {
		// Notify the parser's listeners and then abort.
		String fatalText = "FATAL ERROR: " + errorCode.toString();
		parser.sendMessage(new Message(SYNTAX_ERROR, new Object[] { 0, 0, "",
				fatalText }));
		System.exit(errorCode.getStatus());
	}
}
