package jarvice.frontend;


import jarvice.frontend.Source;
import jarvice.frontend.Token;

/**
 * Scanner
 *
 * 
 */
public abstract class Scanner
{
    protected Source source;     // source
    private Token currentToken;  // current token

    /**
     * Constructor
     * @param source the source to be used with this scanner.
     */
    public Scanner(Source source)
    {
        this.source = source;
    }

    /**
     * @return the current token.
     */
    public Token currentToken()
    {	
    	System.out.println(currentToken);
        return currentToken;
    }

    /**
     * Return next token from the source.
     * @return the next token.
     * @throws Exception if an error occurred.
     */
    public Token nextToken()
        throws Exception
    {
        currentToken = extractToken();
        return currentToken;
    }

    /**
     * Do the actual work of extracting and returning the next token from the
     * source. Implemented by scanner subclasses.
     * @return the next token.
     * @throws Exception if an error occurred.
     */
    protected abstract Token extractToken()
        throws Exception;

    /**
     * Call the source's currentChar() method.
     * @return the current character from the source.
     * @throws Exception if an error occurred.
     */
    public char currentChar()
        throws Exception
    {
        return source.currentChar();
    }

    /**
     * Call the source's nextChar() method.
     * @return the next character from the source.
     * @throws Exception if an error occurred.
     */
    public char nextChar()
        throws Exception
    {
        return source.nextChar();
    }
}