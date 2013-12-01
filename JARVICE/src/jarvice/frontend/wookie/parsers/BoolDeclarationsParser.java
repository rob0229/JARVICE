package jarvice.frontend.wookie.parsers;

import static jarvice.frontend.wookie.WookieErrorCode.IDENTIFIER_REDEFINED;
import static jarvice.frontend.wookie.WookieErrorCode.MISSING_COLON;
import static jarvice.frontend.wookie.WookieErrorCode.MISSING_COMMA;
import static jarvice.frontend.wookie.WookieErrorCode.MISSING_IDENTIFIER;
import static jarvice.frontend.wookie.WookieErrorCode.MISSING_SEMICOLON;
import static jarvice.frontend.wookie.WookieTokenType.COLON;
import static jarvice.frontend.wookie.WookieTokenType.COMMA;
import static jarvice.frontend.wookie.WookieTokenType.END;
import static jarvice.frontend.wookie.WookieTokenType.IDENTIFIER;
import static jarvice.frontend.wookie.WookieTokenType.SEMICOLON;

import java.util.ArrayList;
import java.util.EnumSet;

import jarvice.frontend.Token;
import jarvice.frontend.TokenType;
import jarvice.frontend.wookie.WookieParserTD;
import jarvice.frontend.wookie.WookieTokenType;
import jarvice.intermediate.Definition;
import jarvice.intermediate.SymTabEntry;
import jarvice.intermediate.TypeSpec;

public class BoolDeclarationsParser extends DeclarationsParser {
	
    private Definition definition;  // how to define the identifier
	
	public BoolDeclarationsParser(WookieParserTD parent)
    {
    
        super(parent);
    }
	
	 protected void setDefinition(Definition definition)
	    {
	        this.definition = definition;
	    }

	    // Synchronization set for a integer identifier.
	    static final EnumSet<WookieTokenType> IDENTIFIER_SET =
	        DeclarationsParser.INT_START_SET.clone();
	    static {
	        IDENTIFIER_SET.add(IDENTIFIER);
	        IDENTIFIER_SET.add(END);
	        IDENTIFIER_SET.add(SEMICOLON);
	        
	    }

	    // Synchronization set for the start of the next definition or declaration.
	    static final EnumSet<WookieTokenType> NEXT_START_SET =
	        DeclarationsParser.ROUTINE_START_SET.clone();
	    static {
	        NEXT_START_SET.add(IDENTIFIER);
	        NEXT_START_SET.add(SEMICOLON);
	    }

	    /**
	     * Parse variable declarations.
	     * @param token the initial token.
	     * @throws Exception if an error occurred.
	     */
	    public void parse(Token token)
	        throws Exception
	    {
	    	
	 System.out.println("Inside IntDeclarationsParser!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");	
	        token = synchronize(IDENTIFIER_SET);
	//**************************************************************************************************************************************
	//THIS WILL NEVER HAPPEN! THE DECLARATIONS WILL BE SEPERATED BY COMMAS AND COULD POSSIBLY HAVE AN EQUALS SIGN (EX int foo, bar = 1, i;)
	        
	        // Loop to parse a sequence of variable declarations
	        // separated by semicolons.
	        while (token.getType() != SEMICOLON) {

	            // Parse the identifier sublist and its type specification.
	            parseIdentifierSublist(token);

	            token = currentToken();
	            TokenType tokenType = token.getType();

	            // Look for one or more semicolons after a definition.
	            if (tokenType == SEMICOLON) {
	                while (token.getType() == SEMICOLON) {
	                    token = nextToken();  // consume the ;
	                }
	            }

	            // If at the start of the next definition or declaration,
	            // then missing a semicolon.
	            else if (NEXT_START_SET.contains(tokenType)) {
	                errorHandler.flag(token, MISSING_SEMICOLON, this);
	            }

	            token = synchronize(IDENTIFIER_SET);
	        }
	    }

	    // Synchronization set to start a sublist identifier.
	    static final EnumSet<WookieTokenType> IDENTIFIER_START_SET =
	        EnumSet.of(IDENTIFIER, COMMA);

	    // Synchronization set to follow a sublist identifier.
	    private static final EnumSet<WookieTokenType> IDENTIFIER_FOLLOW_SET =
	        EnumSet.of( SEMICOLON);
	    static {
	        IDENTIFIER_FOLLOW_SET.addAll(DeclarationsParser.INT_START_SET);
	    }

	    // Synchronization set for the , token.
	    private static final EnumSet<WookieTokenType> COMMA_SET =
	        EnumSet.of(COMMA, IDENTIFIER, SEMICOLON);

	    /**
	     * Parse a sublist of identifiers and their type specification.
	     * @param token the current token.
	     * @return the sublist of identifiers in a declaration.
	     * @throws Exception if an error occurred.
	     */
	    protected ArrayList<SymTabEntry> parseIdentifierSublist(Token token)
	        throws Exception
	    {
	        ArrayList<SymTabEntry> sublist = new ArrayList<SymTabEntry>();

	        do {
	            token = synchronize(IDENTIFIER_START_SET);
	            SymTabEntry id = parseIdentifier(token);

	            if (id != null) {
	                sublist.add(id);
	            }

	            token = synchronize(COMMA_SET);
	            TokenType tokenType = token.getType();
	//******************************************************************looks for comma to accept multiple declarations
	            // Look for the comma.
	            if (tokenType == COMMA) {
	                token = nextToken();  // consume the comma

	                if (IDENTIFIER_FOLLOW_SET.contains(token.getType())) {
	                    errorHandler.flag(token, MISSING_IDENTIFIER, this);
	                }
	            }
	            else if (IDENTIFIER_START_SET.contains(tokenType)) {
	                errorHandler.flag(token, MISSING_COMMA, this);
	            }
	        } while (!IDENTIFIER_FOLLOW_SET.contains(token.getType()));

	        // Parse the type specification.
	        TypeSpec type = parseTypeSpec(token);

	        // Assign the type specification to each identifier in the list.
	        for (SymTabEntry variableId : sublist) {
	            variableId.setTypeSpec(type);
	        }

	        return sublist;
	    }

	    /**
	     * Parse an identifier.
	     * @param token the current token.
	     * @return the symbol table entry of the identifier.
	     * @throws Exception if an error occurred.
	     */
	    private SymTabEntry parseIdentifier(Token token)
	        throws Exception
	    {
	        SymTabEntry id = null;

	        if (token.getType() == IDENTIFIER) {
	            String name = token.getText().toLowerCase();
	            id = symTabStack.lookupLocal(name);

	            // Enter a new identifier into the symbol table.
	            if (id == null) {
	                id = symTabStack.enterLocal(name);
	                id.setDefinition(definition);
	                id.appendLineNumber(token.getLineNumber());
	            }
	            else {
	                errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
	            }

	            token = nextToken();   // consume the identifier token
	        }
	        else {
	            errorHandler.flag(token, MISSING_IDENTIFIER, this);
	        }

	        return id;
	    }
	//THIS IS NOT NEEDED HERE BUT I LEFT IT FOR DEBUGGING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    // Synchronization set for the : token.
	    private static final EnumSet<WookieTokenType> COLON_SET =
	        EnumSet.of(COLON, SEMICOLON);

	    /**
	     * Parse the type specification.
	     * @param token the current token.
	     * @return the type specification.
	     * @throws Exception if an error occurs.
	     */
	    protected TypeSpec parseTypeSpec(Token token)
	        throws Exception
	    {
	        // Synchronize on the : token.
	        token = synchronize(COLON_SET);
	        if (token.getType() == COLON) {
	            token = nextToken(); // consume the :
	        }
	        else {
	            errorHandler.flag(token, MISSING_COLON, this);
	        }

	        // Parse the type specification.
	        TypeSpecificationParser typeSpecificationParser =
	            new TypeSpecificationParser(this);
	        TypeSpec type = typeSpecificationParser.parse(token);

	        return type;
	    }
	
	
	
	
	
}
