package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;

/**
 * Represents a loop in which the exit condition is evaluated after each pass.
 */
public class RepeatUntilStmt extends LoopingStmt {
	
	public RepeatUntilStmt(Expn expn, Stmt body) {
		super(expn, body);
		
		expn.setParent(this);
		body.setParent(this);
	}

	/**
	 * Print a description of the <b>repeat-until</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "repeat");
		body.printOn(out, depth + 1);
		Indentable.printIndentOnLn(out, depth, " until "  + expn );

	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		// Get the start of the address for return
		short returnAddr = writer.getNextAddr();
		// Execute the body
		this.getBody().doCodeGen(writer);
		// Push the condition to the stack
		this.getExpn().doCodeGen(writer);
		// Don't need to negate the condition, as we only branch to the
		// beginning of the body if the condition evaluates to false
		
		// Push the return address to the stack
		writer.add(Machine.PUSH, returnAddr);
		// Branch
		writer.add(Machine.BF);
		
		// For each of the exit statements contained in the loop body, update 
		// branch address
		for (int i = 0; i < exitAddrToBePatched.length; i++) {
			writer.replace(exitAddrToBePatched[i], writer.getNextAddr());
		}
	}

}
