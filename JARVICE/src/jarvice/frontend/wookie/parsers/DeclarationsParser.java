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

public class DeclarationsParser extends WookieParserTD
{
    public DeclarationsParser(WookieParserTD parent)
    {
        super(parent);
    }

    static final EnumSet<WookieTokenType> DECLARATION_START_SET =
        EnumSet.of(VAR);
    
    static final EnumSet<WookieTokenType> INT_START_SET =
        EnumSet.of(INT, CHAR, LEFT_BRACE);
      
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
    static final EnumSet<WookieTokenType> ROUTINE_START_SET = EnumSet.of(LEFT_BRACE);
        
    static {
        ROUTINE_START_SET.remove(CHAR);
    }

    public SymTabEntry parse(Token token, SymTabEntry parentId)
        throws Exception
    {     
        token = synchronize(INT_START_SET);	
        
        if (token.getType() == INT || token.getType() == CHAR ) {											

            //token = nextToken();  // consume INT								
            IntDeclarationsParser intDeclarationsParser =						
                new IntDeclarationsParser(this);								
            intDeclarationsParser.setDefinition(VARIABLE);
            intDeclarationsParser.parse(token);           
        }
									
        
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
            
        }
        return null;
    }
}
