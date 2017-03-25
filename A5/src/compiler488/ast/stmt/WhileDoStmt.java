package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;

/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {

	public WhileDoStmt(Expn expn, Stmt body) {
		super(expn, body);
		
		expn.setParent(this);
		body.setParent(this);
	}

	/**
	 * Print a description of the <b>while-do</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "while " + expn + " do");
		body.printOn(out, depth + 1);
		Indentable.printIndentOnLn(out, depth, "End while-do");
	}
	
	@Override
	public void doCodeGen(MachineWriter writer) {
		// Get the start of the address for returning to the while loop
		short startWhileAddr = writer.getNextAddr();
		
		// Execute the condition
		this.getExpn().doCodeGen(writer);
		
		// Push the address to the end of the loop
		writer.add(Machine.PUSH, Machine.UNDEFINED);
		short endWhileAddr = writer.getPrevAddr();
		
		// If the expression is false, go to the end of the loop
		writer.add(Machine.BF);
		
		// If the expression is true, execute the body
		this.getBody().doCodeGen(writer);
		
		// Push the startOfWhile to the stack and branch to it
		writer.add(Machine.PUSH, startWhileAddr);
		writer.add(Machine.BR);
		
		// Replace the brAddr with the number of instructions
		writer.replace(endWhileAddr, (short) writer.getNextAddr());
	}
}
