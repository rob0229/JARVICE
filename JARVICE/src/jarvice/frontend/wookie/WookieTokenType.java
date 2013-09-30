package jarvice.frontend.wookie;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.HashSet;

import jarvice.frontend.TokenType;

/**
 * <h1>PascalTokenType</h1>
 * 
 * <p>
 * Pascal token types.
 * </p>
 */
public enum WookieTokenType implements TokenType {
	// Reserved words.
	AND, CASE, DO, ELSE, FOR, IF, NOT, OR, REPEAT, THEN, VAR, WHILE,
	// Special symbols.
	PLUS("+"), MINUS("-"), POUND_SIGN("#"), STAR("*"), SLASH("/"), COMMA(","), SEMICOLON(";"), COLON(":"), QUOTE("'"), EQUALS("="), NOT_EQUALS("!="), DOT("."), LESS_THAN("<"), LESS_EQUALS("<="), GREATER_EQUALS(">="), GREATER_THAN(
			">"), LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACKET("["), RIGHT_BRACKET("]"), LEFT_BRACE("{"), RIGHT_BRACE("}"), IDENTIFIER, INTEGER, REAL, STRING, ERROR, END_OF_FILE;
	private static final int FIRST_RESERVED_INDEX = AND.ordinal();
	private static final int LAST_RESERVED_INDEX = WHILE.ordinal();
	private static final int FIRST_SPECIAL_INDEX = PLUS.ordinal();
	private static final int LAST_SPECIAL_INDEX = RIGHT_BRACE.ordinal();
	private String text; // token text

	/**
	 * Constructor.
	 */
	WookieTokenType() {
		this.text = this.toString().toLowerCase();
	}

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            the token text.
	 */
	WookieTokenType(String text) {
		this.text = text;
	}

	/**
	 * Getter.
	 * 
	 * @return the token text.
	 */
	public String getText() {
		return text;
	}
	
	public static void print (Hashtable<String, WookieTokenType> ht) { 
        Enumeration<String> e = ht.keys (); 
        while (e.hasMoreElements ()) { 
            String key = (String) e.nextElement (); 
            WookieTokenType value = (WookieTokenType) ht.get (key); 
            System.out.println ("{ " + key + ", " + value + " }"); 
        }
        
    }

	// Set of lower-cased Pascal reserved word text strings.
	public static HashSet<String> RESERVED_WORDS = new HashSet<String>();
	static {
		WookieTokenType values[] = WookieTokenType.values();
		for (int i = FIRST_RESERVED_INDEX; i <= LAST_RESERVED_INDEX; ++i) {
			RESERVED_WORDS.add(values[i].getText().toLowerCase());
		}
	}
	// Hash table of Pascal special symbols. Each special symbol's text
	// is the key to its Pascal token type.
	public static Hashtable<String, WookieTokenType> SPECIAL_SYMBOLS = new Hashtable<String, WookieTokenType>();
	static {
		WookieTokenType values[] = WookieTokenType.values();
		for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) {
			SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
		}
		
		print(SPECIAL_SYMBOLS);
	}
	
	
	  
	 
	 
	 

	
	
	
	
}
