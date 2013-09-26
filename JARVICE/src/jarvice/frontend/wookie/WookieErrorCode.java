package jarvice.frontend.wookie;

public enum WookieErrorCode
{
	
	
	IDENTIFIER_REDEFINED("Redefined identifier"),
    IDENTIFIER_UNDEFINED("Undefined identifier"),
    INCOMPATIBLE_ASSIGNMENT("Incompatible assignment"),
    INCOMPATIBLE_TYPES("Incompatible types"),
    INVALID_ASSIGNMENT("Invalid assignment statement"),
    INVALID_CHARACTER("Invalid character"),
    INVALID_CONSTANT("Invalid constant"),
    INVALID_EXPONENT("Invalid exponent"),
    INVALID_EXPRESSION("Invalid expression"),
    INVALID_FRACTION("Invalid fraction"),
    INVALID_NUMBER("Invalid number"),
    INVALID_STATEMENT("Invalid statement"),
    INVALID_TYPE("Invalid type"),
    MISSING_IDENTIFIER("Missing identifier"),
    MISSING_LEFT_BRACKET("Missing ["),
    MISSING_RIGHT_BRACKET("Missing ]"),
    MISSING_RIGHT_PAREN("Missing )"),
    MISSING_SEMICOLON("Missing ;"),  
    MISSING_THEN("Missing THEN"),
    MISSING_VARIABLE("Missing variable"),
    NOT_TYPE_IDENTIFIER("Not a type identifier"),
    RANGE_INTEGER("Integer literal out of range"),
    RANGE_REAL("Real literal out of range"),
    STACK_OVERFLOW("Stack overflow"),
    TOO_MANY_SUBSCRIPTS("Too many subscripts"),
    UNEXPECTED_TOKEN("Unexpected token"),
    UNRECOGNIZABLE("Unrecognizable input"),
    WRONG_NUMBER_OF_PARMS("Wrong number of actual parameters"),
    
	IO_ERROR(-101, "Object I/O error"),
	TOO_MANY_ERRORS(-102, "Too many syntax errors");
	
	
	
	
	
	    private int status;      // exit status
	    private String message;  // error message

	    /**
	     * Constructor.
	     * @param message the error message.
	     */
	    WookieErrorCode(String message)
	    {
	        this.status = 0;
	        this.message = message;
	    }

	    /**
	     * Constructor.
	     * @param status the exit status.
	     * @param message the error message.
	     */
	    WookieErrorCode(int status, String message)
	    {
	        this.status = status;
	        this.message = message;
	    }

	    /**
	     * Getter.
	     * @return the exit status.
	     */
	    public int getStatus()
	    {
	        return status;
	    }

	    /**
	     * @return the message.
	     */
	    public String toString()
	    {
	        return message;
	    }
	}