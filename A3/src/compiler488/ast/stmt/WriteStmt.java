package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Printable;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.IntegerType;
import compiler488.symbol.SymbolTable;

import java.util.ListIterator;

/**
 * The command to write data on the output device.
 */
public class WriteStmt extends Stmt {

	private ASTList<Printable> outputs; // The objects to be printed.

	public WriteStmt(ASTList<Printable> outputs) {
		this.outputs = outputs;
	}
	
	/** Returns a description of the <b>write</b> statement. */
	@Override
	public String toString() {
		return "write " + outputs;
	}

	public ASTList<Printable> getOutputs() {
		return outputs;
	}

	public void setOutputs(ASTList<Printable> outputs) {
		this.outputs = outputs;
	}

	@Override
	public void checkSemantics(SymbolTable symbols) throws Exception {
		ListIterator<Printable> li = outputs.getIterator();
		while(li.hasNext()) {
			Printable output = li.next();
			if (output instanceof Expn) {
				// S31 - Check that type of Expn is integer.
                if (!(((Expn) output).getType() instanceof IntegerType)) {
                    throw new Exception("output expression must be type integer");
				}
			}
		}
	}
}
