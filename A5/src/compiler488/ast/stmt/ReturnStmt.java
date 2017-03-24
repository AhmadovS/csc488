package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.AST;
import compiler488.ast.Indentable;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.expn.Expn;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * The command to return from a function or procedure.
 */
public class ReturnStmt extends Stmt {
	// The value to be returned by a function.
	private Expn value = null;
	
	public ReturnStmt(Expn value) {
		this.value = value;
		
		this.value.setParent(this);
	}
	
	public ReturnStmt() {
		this.value = null;
	}

	/**
	 * Print <b>return</b> or <b>return with </b> expression on a line, by itself.
	 * 
	 * @param out
	 *            Where to print.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOn(out, depth);
		if (value == null)
			out.println("return ");
		else
			out.println("return with " + value );
	}

	public Expn getValue() {
		return value;
	}

	public void setValue(Expn value) {
		this.value = value;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {

	    // If contained expression is not null, do semantic check on children.
		if (value != null) {
			value.checkSemantics(symbols);
		}

		// Finds RoutineDecl parent node
		AST node = this.getParent();
		while (node != null && !(node instanceof RoutineDecl)) {
			// Traverse up the tree, until a RoutineDecl parent is found.
		    node = node.getParent();
		}
		RoutineDecl parentNode = (RoutineDecl) node;

		// S51 - check that return is inside a function
		// S52 - check that return is inside a procedure
		if (parentNode == null) {
			SemanticError.add(this, "(S51/S52) Return statement is not inside a routine");
		} else {
			if (value == null) {
				// (value == null) we have a 'return' statement.

				if (parentNode.getType() != null) {
					// parent node is a function, and we're not returning an expression
					SemanticError.add(52, this, "Function expects 'return with' statement");
				}
			} else {
				// (value != null) we have 'return with' statement.

				if (parentNode.getType() == null) {
					// parent node is a procedure, whereas we expected a function.
					SemanticError.add(51, this, "Procedure cannot accept return statement with expression");
				} else {
					// parentNode.getType() != null here.
					// S35 - check if expression type matches the return type of the enclosing function.
					if (parentNode.getType().getClass() != value.getType().getClass()) {
						SemanticError.add(35, this, "Return type doesn't match function signature return type");
					}
				}
			}
		}

	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		// Only functions have a return value.
		// Procedures don't store a return value
		if (value != null) {
		    // First we push the address of 'return value' field of the activation record.
			writer.add(Machine.ADDR, getLexicLevel(), 0);

			// Evaluates the return expression
			value.doCodeGen(writer);

			// Stack :: return address -> return value
			// Emits code to store returned value.
			writer.add(Machine.STORE);
		}
	}
}
