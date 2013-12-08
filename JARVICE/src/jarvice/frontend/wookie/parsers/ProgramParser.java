package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;

import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;

public class ProgramParser extends DeclarationsParser
{  
    public ProgramParser(WookieParserTD parent)
    {
        super(parent);
    }
    // Synchronization set to start a program.
    static final EnumSet<WookieTokenType> PROGRAM_START_SET =
        EnumSet.of(PROGRAM, SEMICOLON, INT);
    static {
        PROGRAM_START_SET.addAll(DeclarationsParser.DECLARATION_START_SET);
    }

    
    public SymTabEntry parse(Token token, SymTabEntry parentId)
        throws Exception
    {
    	token = synchronize(PROGRAM_START_SET);
    	DeclaredRoutineParser routineParser = new DeclaredRoutineParser(this);
    	
    	// Parse the program.
      do {     
        routineParser.parse(token, parentId);
        token = currentToken();        
      } while (token.getType() == INT ||token.getType() == CHAR ||token.getType() == VOID);
        
    return null;
    }
}
