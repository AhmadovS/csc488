package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.BooleanType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Represents an if-then or an if-then-else construct.
 */
public class IfStmt extends Stmt {
	// The condition that determines which branch to execute.
	private Expn condition;

	// Represents the statement to execute when the condition is true.
	private Stmt whenTrue;

	// Represents the statement to execute when the condition is false.
	private Stmt whenFalse = null;
	
	public IfStmt(Expn condition, Stmt whenTrue, Stmt whenFalse) {
		this.condition = condition;
		this.whenTrue = whenTrue;
		this.whenFalse = whenFalse;
		
		this.condition.setParent(this);
		this.whenTrue.setParent(this);
		this.whenFalse.setParent(this);
	}
	
	public IfStmt(Expn condition, Stmt whenTrue) {
		this.condition = condition;
		this.whenTrue = whenTrue;
		
		this.condition.setParent(this);
		this.whenTrue.setParent(this);
	}

	/**
	 * Print a description of the <b>if-then-else</b> construct. If the
	 * <b>else</b> part is empty, just print an <b>if-then</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "if " + condition + " then ");
		whenTrue.printOn(out, 	depth + 1);
		if (whenFalse != null) {
			Indentable.printIndentOnLn(out, depth, "else");
			whenFalse.printOn(out, depth + 1);
		}
		Indentable.printIndentOnLn(out, depth, "End if");
	}

	public Expn getCondition() {
		return condition;
	}

	public void setCondition(Expn condition) {
		this.condition = condition;
	}

	public Stmt getWhenFalse() {
		return whenFalse;
	}

	public void setWhenFalse(Stmt whenFalse) {
		this.whenFalse = whenFalse;
	}

	public Stmt getWhenTrue() {
		return whenTrue;
	}

	public void setWhenTrue(Stmt whenTrue) {
		this.whenTrue = whenTrue;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) throws Exception {

		// We need to check children expression, so we can know it's type for S30 check.
        this.getCondition().checkSemantics(symbols);

	    // S30 - check if condition expression is boolean
		if (!(this.getCondition().getType() instanceof BooleanType)){
			SemanticError.add(30, this,"The expression of IF statement must be boolean");
		}

		// Check semantics on body of if
		this.getWhenTrue().checkSemantics(symbols);

		// Check semantics on body of else
		if (this.getWhenFalse() != null){
			this.getWhenFalse().checkSemantics(symbols);
		}
		
	}
	
	@Override
	public void doCodeGen(MachineWriter writer) {
		// Emits code that evaluates the condition expression
		this.getCondition().doCodeGen(writer);
		
		// Emits code to allocate memory that stores the address
		// to else statement or function exit.
		writer.add(Machine.PUSH, Machine.UNDEFINED);
		short exitFalseAddr = writer.getPrevAddr();
		writer.add(Machine.BF);
		
		// Emits code that executes the true statements
		this.getWhenTrue().doCodeGen(writer);

		if(this.whenFalse != null) {
		    // After the true statements are executed, we need to jump
            // over the false statements.
			writer.add(Machine.PUSH, Machine.UNDEFINED);
			short exitAddr = writer.getPrevAddr();
			writer.add(Machine.BR);
			
			// Update false address
			writer.replace(exitFalseAddr, writer.getNextAddr());
			
			// Emits code that executes the false statements
			this.getWhenFalse().doCodeGen(writer);

			// Update exit address
			writer.replace(exitAddr, writer.getNextAddr());
		} else {
		    // If there is no else statement, and condition evaluates to false,
            // jump to next available instruction.
			writer.replace(exitFalseAddr, writer.getNextAddr());
		}

	}
}
