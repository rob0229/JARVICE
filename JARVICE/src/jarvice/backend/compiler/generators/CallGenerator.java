package jarvice.backend.compiler.generators;

import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;
import jarvice.intermediate.typeimpl.RoutineCode;
import jarvice.backend.compiler.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.RoutineCodeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;

/**
 * <h1>CallExecutor</h1>
 *
 * <p>Generate code to call to a procedure or function.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class CallGenerator extends StatementGenerator
{
    /**
     * Constructor.
     * @param the parent executor.
     */
    public CallGenerator(CodeGenerator parent)
    {
        super(parent);
    }

    /**
     * Generate code to call a procedure or function.
     * @param node the root node of the call.
     */
    public void generate(ICodeNode node)
    {
        SymTabEntry routineId = (SymTabEntry) node.getAttribute(ID);
        RoutineCode routineCode =
                        (RoutineCode) routineId.getAttribute(ROUTINE_CODE);
        CallGenerator callGenerator = routineCode == DECLARED
                                          ? new CallDeclaredGenerator(this)
                                          : new CallStandardGenerator(this);

        callGenerator.generate(node);
    }
}
