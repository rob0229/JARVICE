package jarvice.frontend.wookie.tokens;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;

import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;

/**
 * <h1>PascalSpecialSymbolToken</h1>
 * 
 * <p>
 * Pascal special symbol tokens.
 * </p>
 * 
 * <p>
 * Copyright (c) 2009 by Ronald Mak
 * </p>
 * <p>
 * For instructional purposes only. No warranties.
 * </p>
 */
public class WookieSpecialSymbolToken extends WookieToken {
	/**
	 * Constructor.
	 * 
	 * @param source
	 *            the source from where to fetch the token's characters.
	 * @throws Exception
	 *             if an error occurred.
	 */
	public WookieSpecialSymbolToken(Source source) throws Exception {
		super(source);
	}

	/**
	 * Extract a Pascal special symbol token from the source.
	 * 
	 * @throws Exception
	 *             if an error occurred.
	 */
	protected void extract() throws Exception {
		char currentChar = currentChar();

		text = Character.toString(currentChar);
		type = null;

		switch (currentChar) {

		// Single-character special symbols.
		case '+':
		case '-':
		case '#':
		case ',':
		case '*':
		case ';':
		case '\'':
		case '/':
		case '(':
		case ')':
		case '[':
		case ']':
		case '{':
		case '}':
		case '^': {
			nextChar(); // consume character
			break;
		}

		
		case '=': {
			

			if (currentChar == '=' && peekChar() == '=') {
				text += currentChar;
				nextChar(); // consume '='
				nextChar(); // consume '='
			}else{
				nextChar();
			}

			break;
		}

		// < or <= or <>
		case '<': {
			currentChar = nextChar(); // consume '<';

			if (currentChar == '=') {
				text += currentChar;
				nextChar(); // consume '='
			} else if (currentChar == '>') {
				text += currentChar;
				nextChar(); // consume '>'
			}

			break;
		}

		// > or >=
		case '>': {
			currentChar = nextChar(); // consume '>';

			if (currentChar == '=') {
				text += currentChar;
				nextChar(); // consume '='
			}

			break;
		}

		// . or ..
		case '.': {
			currentChar = nextChar(); // consume '.';

			if (currentChar == '.') {
				text += currentChar;
				nextChar(); // consume '.'
			}

			break;
		}

		default: {
			nextChar(); // consume bad character
			type = ERROR;
			value = INVALID_CHARACTER;
		}
		}

		// Set the type if it wasn't an error.
		if (type == null) {
			type = SPECIAL_SYMBOLS.get(text);
		}
	}
}
