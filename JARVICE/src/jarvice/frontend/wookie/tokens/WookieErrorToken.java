package jarvice.frontend.wookie.tokens;


import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import static jarvice.frontend.wookie.WookieTokenType.*;

/**
 * <h1>PascalErrorToken</h1>
 * 
 * <p>
 * Pascal error token.
 * </p>
 */
public class WookieErrorToken extends WookieToken {
	/**
	 * Constructor.
	 * 
	 * @param source
	 *            the source from where to fetch subsequent characters.
	 * @param errorCode
	 *            the error code.
	 * @param tokenText
	 *            the text of the erroneous token.
	 * @throws Exception
	 *             if an error occurred.
	 */
	public WookieErrorToken(Source source, WookieErrorCode errorCode,
			String tokenText) throws Exception {
		super(source);
		this.text = tokenText;
		this.type = ERROR;
		this.value = errorCode;
	}

	/**
	 * Do nothing. Do not consume any source characters.
	 * 
	 * @throws Exception
	 *             if an error occurred.
	 */
	protected void extract() throws Exception {
	}
}
