package jarvice.frontend.wookie.parsers;



import java.util.EnumSet;

import jarvice.frontend.TokenType;
import jarvice.frontend.wookie.parsers.DeclaredRoutineParser;

import jarvice.frontend.wookie.WookieTokenType;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VARIABLE;
import static jarvice.frontend.wookie.WookieTokenType.BEGIN;
import static jarvice.frontend.wookie.WookieTokenType.CONST;
import static jarvice.frontend.wookie.WookieTokenType.FUNCTION;
import static jarvice.frontend.wookie.WookieTokenType.PROCEDURE;
import static jarvice.frontend.wookie.WookieTokenType.TYPE;
import static jarvice.frontend.wookie.WookieTokenType.VAR;

/**
 * <h1>DeclarationsParser</h1>
 *
 * <p>Parse Pascal declarations.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class DeclarationsParser extends WookieParserTD
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    public DeclarationsParser(WookieParserTD parent)
    {
        super(parent);
    }
//*******************************************************Added this for programparser*****************************************
    static final EnumSet<WookieTokenType> DECLARATION_START_SET =
            EnumSet.of(CONST, TYPE, VAR, PROCEDURE, FUNCTION, BEGIN);
    
//*******************************************************CAN DELETE CONST< TYPE< VAR< BEGIN< PROCEDURE*********************************************
    static final EnumSet<WookieTokenType> INT_START_SET =
        EnumSet.of(INT, CHAR, PROCEDURE, FUNCTION, LEFT_BRACE);
      
    static final EnumSet<WookieTokenType> CHAR_START_SET =
            INT_START_SET.clone();
        static {
            CHAR_START_SET.remove(INT);
        }
        static final EnumSet<WookieTokenType> VAR_START_SET =
                INT_START_SET.clone();
            static {
                VAR_START_SET.remove(TYPE);
            }
//******************************************************************************
    static final EnumSet<WookieTokenType> ROUTINE_START_SET =
        CHAR_START_SET.clone();
    static {
        ROUTINE_START_SET.remove(CHAR);
    }

    /**
     * Parse declarations.
     * To be overridden by the specialized declarations parser subclasses.
     * @param token the initial token.
     * @throws Exception if an error occurred.
     */
    public SymTabEntry parse(Token token, SymTabEntry parentId)
        throws Exception
    {
     
//*********************************************************************ADDED BY ROB  This needs to be modified to not look for INT like Pascal looks for VAR, but look for "int name; and then the sublists"  
//The IntDeclarationsParser class will create the symbol table entries needed to compile the code, This section just sees that there is about to be an int declared and gets to the correct declarations parser.
        token = synchronize(INT_START_SET);										
        if (token.getType() == INT) {											

            //token = nextToken();  // consume INT								

            IntDeclarationsParser intDeclarationsParser =						
                new IntDeclarationsParser(this);								
            intDeclarationsParser.setDefinition(VARIABLE);
            intDeclarationsParser.parse(token);
           
        }
        
        token = synchronize(CHAR_START_SET);									
        if (token.getType() == CHAR) {											
            //token = nextToken();  // consume CHAR								

            IntDeclarationsParser intDeclarationsParser =						
                    new IntDeclarationsParser(this);							
                intDeclarationsParser.setDefinition(VARIABLE);
                intDeclarationsParser.parse(token);
        }        
//*************************************************************************************        
        
        
        token = synchronize(ROUTINE_START_SET);
        TokenType tokenType = token.getType();

        while ((tokenType == PROCEDURE) || (tokenType == FUNCTION)) {
            DeclaredRoutineParser routineParser =
                new DeclaredRoutineParser(this);
            routineParser.parse(token, parentId);

            // Look for one or more semicolons after a definition.
            token = currentToken();
            if (token.getType() == SEMICOLON) {
                while (token.getType() == SEMICOLON) {
                    token = nextToken();  // consume the ;
                }
            }

            token = synchronize(ROUTINE_START_SET);
            tokenType = token.getType();
        }
        return null;
    }
}
