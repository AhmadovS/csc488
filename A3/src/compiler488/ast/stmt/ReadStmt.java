package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Readable;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.type.IntegerType;
import compiler488.symbol.SymbolTable;

import java.util.ListIterator;

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
	public void checkSemantics(SymbolTable symbols) throws Exception {
		ListIterator<Readable> li = inputs.getIterator();
		while(li.hasNext()) {
			Readable input = li.next();
			if (input instanceof IdentExpn) {
				// S31 - Check that type of Expn is integer.
				if (!(((IdentExpn) input).getType() instanceof IntegerType)) {
					throw new Exception("input expression must be type integer");
				}
			}
		}
	}
}
