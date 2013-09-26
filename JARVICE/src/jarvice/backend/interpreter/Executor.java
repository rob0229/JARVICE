package jarvice.backend.interpreter;


import jarvice.backend.*;
import jarvice.intermediate.ICode;
import jarvice.intermediate.SymTab;
import jarvice.message.*;

import static jarvice.message.MessageType.INTERPRETER_SUMMARY;

/**
 * <h1>Executor</h1>
 *
 * <p>The executor for an interpreter back end.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class Executor extends Backend
{
    /**
     * Process the intermediate code and the symbol table generated by the
     * parser to execute the source program.
     * @param iCode the intermediate code.
     * @param symTab the symbol table.
     * @throws Exception if an error occurred.
     */
    public void process(ICode iCode, SymTab symTab)
        throws Exception
    {
        long startTime = System.currentTimeMillis();
        float elapsedTime = (System.currentTimeMillis() - startTime)/1000f;
        int executionCount = 0;
        int runtimeErrors = 0;

        // Send the interpreter summary message.
        sendMessage(new Message(INTERPRETER_SUMMARY,
                                new Number[] {executionCount,
                                              runtimeErrors,
                                              elapsedTime}));
    }
}