package jarvice.frontend.wookie.parsers;

import java.util.EnumSet;
import java.util.ArrayList;

import jarvice.frontend.*;
import jarvice.frontend.wookie.*;
import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;

import static jarvice.frontend.wookie.WookieTokenType.*;
import static jarvice.frontend.wookie.WookieErrorCode.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.DefinitionImpl.*;
import static jarvice.intermediate.typeimpl.TypeFormImpl.RECORD;
import static jarvice.intermediate.typeimpl.TypeKeyImpl.*;

/**
 * <h1>RecordTypeParser</h1>
 *
 * <p>Parse a Pascal record type specification.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
class RecordTypeParser extends TypeSpecificationParser
{
    /**
     * Constructor.
     * @param parent the parent parser.
     */
    protected RecordTypeParser(WookieParserTD parent)
    {
        super(parent);
    }

    // Synchronization set for the END.
    private static final EnumSet<WookieTokenType> END_SET =
        DeclarationsParser.VAR_START_SET.clone();
    static {
        END_SET.add(RIGHT_BRACE);
        END_SET.add(SEMICOLON);
    }

    /**
     * Parse a Pascal record type specification.
     * @param token the current token.
     * @return the record type specification.
     * @throws Exception if an error occurred.
     */
    public TypeSpec parse(Token token)
        throws Exception
    {
        TypeSpec recordType = TypeFactory.createType(RECORD);
        token = nextToken();  // consume RECORD

        // Push a symbol table for the RECORD type specification.
        recordType.setAttribute(RECORD_SYMTAB, symTabStack.push());

        // Parse the field declarations.
        VariableDeclarationsParser variableDeclarationsParser =
            new VariableDeclarationsParser(this);
        variableDeclarationsParser.setDefinition(FIELD);
        variableDeclarationsParser.parse(token);

        // Pop off the record's symbol table.
        symTabStack.pop();

        // Synchronize at the END.
        token = synchronize(END_SET);

        // Look for the END.
        if (token.getType() == RIGHT_BRACE) {
            token = nextToken();  // consume END
        }
        else {
            errorHandler.flag(token, MISSING_RIGHT_BRACE, this);
        }

        return recordType;
    }
}
