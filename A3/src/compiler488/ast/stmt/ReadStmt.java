package compiler488.ast.stmt;

import java.util.ArrayList;

import compiler488.ast.ASTList;
import compiler488.ast.Readable;
import compiler488.symbol.SymbolTable;

/**
 * The command to read data into one or more variables.
 */
public class ReadStmt extends Stmt {
	
	private ASTList<Readable> inputs; // A list of locations to put the values read.

	public ReadStmt(ASTList<Readable> inputs) {
		this.inputs = inputs;
	}
	
	/** Returns a string describing the <b>read</b> statement. */
	@Override
	public String toString() {
		return "read " + inputs;
	}

	public ASTList<Readable> getInputs() {
		return inputs;
	}

	public void setInputs(ASTList<Readable> inputs) {
		this.inputs = inputs;
	}

	@Override
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {
		// TODO Auto-generated method stub
		
	}
}