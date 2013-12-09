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
    	   Token dataType = token;//save the int/char data type for use later	        
	       Token name = nextToken();
	       Token third = nextToken();
	       
	        if(third.getType() == LEFT_PAREN){

	        	 DeclaredRoutineParser routineParser = new DeclaredRoutineParser(this);
		         routineParser.parse(dataType, null);	        	
	        }
	        else{
		        SymTabEntry id = parseIdentifier(name); 
		       if(dataType.getType() == INT){
		    	   id.setTypeSpec(integerType);
		       }
		       else if(dataType.getType() == CHAR){
		    	   id.setTypeSpec(charType);
		       }
		     		       
		       //currentToken should be a semiColon here
		       final EnumSet<WookieTokenType> INT_FOLLOW_SET = EnumSet.of(SEMICOLON);
		       token = synchronize(INT_FOLLOW_SET);
	        }
     }
    
    public SymTabEntry parseIdentifier(Token token)
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
                    int slot = id.getSymTab().nextSlotNumber();
                    id.setAttribute(SLOT,  slot);
                }
                else {
                    errorHandler.flag(token, IDENTIFIER_REDEFINED, this);
                }
            }
            else {
                errorHandler.flag(token, MISSING_IDENTIFIER, this);
            }

            return id;
        }
   
}