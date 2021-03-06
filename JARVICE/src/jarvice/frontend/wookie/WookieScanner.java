package jarvice.frontend.wookie;

//WookieScanner Class

import static jarvice.frontend.Source.EOF;
import static jarvice.frontend.wookie.WookieErrorCode.INVALID_CHARACTER;
import jarvice.frontend.EofToken;
import jarvice.frontend.Scanner;
import jarvice.frontend.Source;
import jarvice.frontend.Token;
import jarvice.frontend.wookie.tokens.WookieErrorToken;
import jarvice.frontend.wookie.tokens.WookieNumberToken;
import jarvice.frontend.wookie.tokens.WookieSpecialSymbolToken;
import jarvice.frontend.wookie.tokens.WookieStringToken;
import jarvice.frontend.wookie.tokens.WookieWordToken;

public class WookieScanner extends Scanner {
	// Constructor
	public WookieScanner(Source source) {
		super(source);
	}

	/**
	 * Extract and return the next Wookie token from the source.
	 * 
	 * @return the next token.
	 * @throws Exception
	 *             if an error occurred.
	 */
	protected Token extractToken() throws Exception {
		skipWhiteSpace();

		Token token;
		char currentChar = currentChar();

		// Construct the next token. The current character determines the
		// token type.
		if (currentChar == EOF) {
			token = new EofToken(source);
		} else if (Character.isLetter(currentChar)) {
			token = new WookieWordToken(source);
		} else if (Character.isDigit(currentChar)) {
			token = new WookieNumberToken(source);
		} 
		
//**************CHANGE THIS FOR CHAR[] in c		
		
		else if (currentChar == '\'') {
			token = new WookieStringToken(source);
		}
		
		
		

		else if (WookieTokenType.SPECIAL_SYMBOLS.containsKey(Character
				.toString(currentChar))) {
			token = new WookieSpecialSymbolToken(source);
		} else {
			token = new WookieErrorToken(source, INVALID_CHARACTER,
					Character.toString(currentChar));
			nextChar(); // consume character
		}

		return token;
	}

	/**
	 * Skip whitespace characters by consuming them. A comment is whitespace.
	 * 
	 * @throws Exception
	 *             if an error occurred.
	 */
	private void skipWhiteSpace() throws Exception {

		
		char currentChar = currentChar();

		while (Character.isWhitespace(currentChar) || (currentChar == '/') || (currentChar == '#')) {

			// Start of import statement?
			if (currentChar == '#') {
				do {
					currentChar = nextChar(); // consume import characters
				} while ((currentChar != ';'));
				// Found closing '}'?
				if (currentChar == ';') {
					currentChar = nextChar(); // consume the '}'
				}
			}
			
			// comment line
			
			
			else if ((currentChar == '/') && (source.peekChar() == '*')) {


				currentChar = nextChar(); //consumes '/'				
				if (currentChar == '*') {
					do {
						currentChar = nextChar(); // consume comment characters
					} while (!((currentChar == '*') && (source.peekChar() == '/')));
					// found closing */ ?
					if ((currentChar == '*') && (source.peekChar() == '/')) {
						currentChar = nextChar(); // consume the '*'
						currentChar = nextChar(); // consume the '/'
					}
				}
			}

			else if ((currentChar == '/') && (source.peekChar() == '/')) {

					currentChar = nextChar(); //consumes '/'				
					if (currentChar == '/') {
						do {
							
							currentChar = nextChar(); // consume comment characters
							} while (!(currentChar == source.EOL)); 	
					}
			}			
			else if (currentChar == '/'){
				break;
			}
			// Not a comment.
			else {
				currentChar = nextChar(); // consume whitespace character
			}
		}
	}
}
