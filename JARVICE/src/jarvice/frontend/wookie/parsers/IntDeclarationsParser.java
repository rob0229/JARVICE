package jarvice.frontend.wookie.parsers;

import java.util.ArrayList;
import java.util.EnumSet;

import static jarvice.intermediate.symtabimpl.Predefined.*;
import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.*;
import static jarvice.intermediate.typeimpl.TypeFormImpl.*;
import static jarvice.intermediate.typeimpl.TypeKeyImpl.*;

/**
 * </p>
 */
public class IntDeclarationsParser extends DeclarationsParser
{
	
	 private Definition definition;  // how to define the identifier

    

    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public IntDeclarationsParser(WookieParserTD parent)
    {
    
        super(parent);
    }

    protected void setDefinition(Definition definition)
    {
        this.definition = definition;
    }
    
    public void parse(Token token)
            throws Exception
        {
    	   	  
    	// Parse the type specification (Will be "int" here.
        TypeSpec type = parseTypeSpec(token); 
        token = nextToken();      
        SymTabEntry id = parseIdentifier(token);    
        id.setTypeSpec(integerType);

                //currentToken should be a semiColon here
               // 
                
                final EnumSet<WookieTokenType> INT_FOLLOW_SET = EnumSet.of(SEMICOLON);

                token = synchronize(INT_FOLLOW_SET);
                
            if ( token.getType() == SEMICOLON){
            	
            	token = nextToken();
            	
            }      
            
          
            
        }
    
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

    protected TypeSpec parseTypeSpec(Token token)
            throws Exception
        {
            // Synchronize on the : token.
           // token = synchronize(INTEGER);

            if (token.getType() == INT) {
               
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