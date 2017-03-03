package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.AST;
import compiler488.ast.Indentable;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.expn.Expn;
import compiler488.symbol.SymbolTable;

/**
 * The command to return from a function or procedure.
 */
public class ReturnStmt extends Stmt {
	// The value to be returned by a function.
	private Expn value = null;
	
	public ReturnStmt(Expn value) {
		this.value = value;
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
	public void checkSemantics(SymbolTable symbols) throws Exception {

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
		    throw new Exception("Return statement is not inside a routine");
		}

		if (value != null) {
			if (parentNode.getType() == null) {
				// parent node is a procedure, whereas we expected a function.
				throw new Exception("Procedure cannot accept return statement with expression");
			}

			// S35 - check if expression type matches the return type of the enclosing function.
			if (parentNode.getType().getClass() != value.getType().getClass()) {
				throw new Exception("Return type doesn't match function signature return type");
			}
		}


	}
}
