package compiler488.ast.stmt;

import java.io.PrintStream;
import java.util.ArrayList;

import compiler488.ast.AST;
import compiler488.ast.Indentable;
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
	public void checkSemantics(AST syntaxTree, SymbolTable symbols, ArrayList<String> errors) {



	}
}
