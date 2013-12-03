package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.VARIABLE;

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
//*******************************************************CAN DELETE CONST< TYPE< VAR< BEGIN< PROCEDURE*********************************************
    static final EnumSet<WookieTokenType> DECLARATION_START_SET =
        EnumSet.of(INT, BOOL, PROCEDURE, FUNCTION, LEFT_BRACE);

    static final EnumSet<WookieTokenType> TYPE_START_SET =
        DECLARATION_START_SET.clone();
    static {
        TYPE_START_SET.remove(CONST);
    }

    static final EnumSet<WookieTokenType> VAR_START_SET =
        TYPE_START_SET.clone();
    static {
        VAR_START_SET.remove(TYPE);
    }
    
   //*************************ADDED BY ROB*******************************************
    static final EnumSet<WookieTokenType> INT_START_SET =
            VAR_START_SET.clone();
        static {
            INT_START_SET.remove(VAR);
        }
        
    static final EnumSet<WookieTokenType> BOOL_START_SET =
            INT_START_SET.clone();
        static {
            BOOL_START_SET.remove(INT);
        }
        
//******************************************************************************
    static final EnumSet<WookieTokenType> ROUTINE_START_SET =
        VAR_START_SET.clone();
    static {
        ROUTINE_START_SET.remove(VAR);
    }

    /**
     * Parse declarations.
     * To be overridden by the specialized declarations parser subclasses.
     * @param token the initial token.
     * @throws Exception if an error occurred.
     */
    public void parse(Token token)
        throws Exception
    {

        token = synchronize(DECLARATION_START_SET);


        token = synchronize(VAR_START_SET);

        if (token.getType() == VAR) {
            token = nextToken();  // consume VAR

            VariableDeclarationsParser variableDeclarationsParser =
                new VariableDeclarationsParser(this);
            variableDeclarationsParser.setDefinition(VARIABLE);
            variableDeclarationsParser.parse(token);
        }
//*********************************************************************ADDED BY ROB  This needs to be modified to not look for INT like Pascal looks for VAR, but look for "int name; and then the sublists"  
//The IntDeclarationsParser class will create the symbol table entries needed to compile the code, This section just sees that there is about to be an int declared and gets to the correct declarations parser.
        token = synchronize(INT_START_SET);										//*
    
        if (token.getType() == INT) {											//*

            //token = nextToken();  // consume INT								//*

            IntDeclarationsParser intDeclarationsParser =						//*
                new IntDeclarationsParser(this);								//*
            intDeclarationsParser.setDefinition(VARIABLE);
            intDeclarationsParser.parse(token);
           
        }
        
        token = synchronize(BOOL_START_SET);										//*
        if (token.getType() == BOOL) {											//*
            token = nextToken();  // consume BOOL								//*

            BoolDeclarationsParser boolDeclarationsParser =						//*
                new BoolDeclarationsParser(this);								//*
            boolDeclarationsParser.setDefinition(VARIABLE);
            boolDeclarationsParser.parse(token);
        }        
//*************************************************************************************        
        
        
        token = synchronize(ROUTINE_START_SET);
    }
}
