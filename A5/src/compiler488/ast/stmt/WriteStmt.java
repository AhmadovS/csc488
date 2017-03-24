package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Printable;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.SkipConstExpn;
import compiler488.ast.expn.TextConstExpn;
import compiler488.ast.type.IntegerType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

import java.util.ListIterator;

/**
 * The command to write data on the output device.
 */
public class WriteStmt extends Stmt {

	private ASTList<Printable> outputs; // The objects to be printed.

	public WriteStmt(ASTList<Printable> outputs) {
		this.outputs = outputs;
		
		this.outputs.setParent(this);
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

			if (output instanceof TextConstExpn || output instanceof SkipConstExpn) {
			    // Nothing to do.
			} else if (output instanceof Expn) {
			    // Expn now must be type integer.

				// Run semantic check on child first
				((Expn) output).checkSemantics(symbols);

				// S31 - Check that type of Expn is integer.
                if (!(((Expn) output).getType() instanceof IntegerType)) {
                    SemanticError.add(31, this, "output expression must be type integer");
				}
			}
		}
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		// TODO: add documentation
		ASTList<Printable> rev = this.outputs.reverse();
		rev.doCodeGen(writer);
		ListIterator<Printable> it = this.outputs.getIterator();

		while(it.hasNext()) {
			Printable output = it.next();

			if(output instanceof TextConstExpn) {
				TextConstExpn t = (TextConstExpn) output;
				for(int i = 0; i < t.getLength(); i++) {
					writer.add(Machine.PRINTC);
				}
			} else if(output instanceof SkipConstExpn) {
				writer.add(Machine.PRINTC);
			} else {
				writer.add(Machine.PRINTI);
			}
		}
	}
}
