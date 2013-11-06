package jarvice.backend.interpreter.executors;

import jarvice.intermediate.*;
import jarvice.intermediate.icodeimpl.*;
import jarvice.backend.interpreter.*;
import jarvice.message.*;

import static jarvice.intermediate.ICodeNodeType.*;
import static jarvice.intermediate.icodeimpl.ICodeKeyImpl.*;
import static jarvice.backend.interpreter.RuntimeErrorCode.*;
import static jarvice.message.MessageType.SOURCE_LINE;

/**
 * <h1>StatementExecutor</h1>
 * 
 * <p>
 * Execute a statement.
 * </p>
 * 
 * <p>
 * Copyright (c) 2009 by Ronald Mak
 * </p>
 * <p>
 * For instructional purposes only. No warranties.
 * </p>
 */
public class StatementExecutor extends Executor {
	/**
	 * Constructor.
	 * 
	 * @param the
	 *            parent executor.
	 */
	public StatementExecutor(Executor parent) {
		super(parent);
	}

	/**
	 * Execute a statement. To be overridden by the specialized statement
	 * executor subclasses.
	 * 
	 * @param node
	 *            the root node of the statement.
	 * @return null.
	 */
	public Object execute(ICodeNode node) {
		ICodeNodeTypeImpl nodeType = (ICodeNodeTypeImpl) node.getType();

		// Send a message about the current source line.
		sendSourceLineMessage(node);

		switch (nodeType) {

		case COMPOUND: {
			CompoundExecutor compoundExecutor = new CompoundExecutor(this);
			return compoundExecutor.execute(node);
		}

		case ASSIGN: {
			AssignmentExecutor assignmentExecutor = new AssignmentExecutor(this);
			return assignmentExecutor.execute(node);
		}
		/*
		 * case LOOP: { LoopExecutor loopExecutor = new LoopExecutor(this);
		 * return loopExecutor.execute(node); }
		 * 
		 * case IF: { IfExecutor ifExecutor = new IfExecutor(this); return
		 * ifExecutor.execute(node); }
		 * 
		 * case SELECT: { SelectExecutor selectExecutor = new
		 * SelectExecutor(this); return selectExecutor.execute(node); }
		 */
		case NO_OP:
			return null;

		default: {
			errorHandler.flag(node, UNIMPLEMENTED_FEATURE, this);
			return null;
		}
		}
	}

	/**
	 * Send a message about the current source line.
	 * 
	 * @param node
	 *            the statement node.
	 */
	private void sendSourceLineMessage(ICodeNode node) {
		Object lineNumber = node.getAttribute(LINE);

		// Send the SOURCE_LINE message.
		if (lineNumber != null) {
			sendMessage(new Message(SOURCE_LINE, lineNumber));
		}
	}
}
