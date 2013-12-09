package jarvice.backend.interpreter.executors;

import jarvice.intermediate.*;
import jarvice.intermediate.symtabimpl.*;
import jarvice.intermediate.typeimpl.RoutineCode;
import jarvice.backend.interpreter.*;
import static jarvice.intermediate.symtabimpl.SymTabKeyImpl.*;
import static jarvice.intermediate.symtabimpl.RoutineCodeImpl.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;

public class CallExecutor extends StatementExecutor
{

    public CallExecutor(Executor parent)
    {
        super(parent);
    }

    public Object execute(ICodeNode node)
    {
        SymTabEntry routineId = (SymTabEntry) node.getAttribute(ID);
        RoutineCode routineCode =
                        (RoutineCode) routineId.getAttribute(ROUTINE_CODE);
        CallExecutor callExecutor = routineCode == DECLARED
                                    ? new CallDeclaredExecutor(this)
                                    : new CallStandardExecutor(this);

        ++executionCount;  // count the call statement
        return callExecutor.execute(node);
    }
}
