package jarvice.frontend.wookie;

import jarvice.frontend.Source;
import jarvice.frontend.Token;

/**
 * <h1>PascalToken</h1>
 * 
 * <p>
 * Base class for Pascal token classes.
 * </p>
 */
public class WookieToken extends Token {
	/**
	 * Constructor.
	 * 
	 * @param source
	 *            the source from where to fetch the token's characters.
	 * @throws Exception
	 *             if an error occurred.
	 */
	protected WookieToken(Source source) throws Exception {
		super(source);
	}
}