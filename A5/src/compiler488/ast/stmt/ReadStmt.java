package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Readable;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.type.IntegerType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

import java.util.ListIterator;

/**
 * The command to read data into one or more variables.
 */
public class ReadStmt extends Stmt {
	
	private ASTList<Readable> inputs; // A list of locations to put the values read.

	public ReadStmt(ASTList<Readable> inputs) {
		this.inputs = inputs;
		
		this.inputs.setParent(this);
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
				// Note: do semantic check first
				((IdentExpn)input).checkSemantics(symbols);

				// S31 - Check that type of Expn is integer.
				if (!(((IdentExpn) input).getType() instanceof IntegerType)) {
                    SemanticError.add(31, this, "input expression must be type integer");
				}
			} else if (input instanceof SubsExpn) {
				((SubsExpn)input).checkSemantics(symbols);
				// S31 - Check that type of Expn is integer.
				if (!(((SubsExpn) input).getType() instanceof IntegerType)) {
                    SemanticError.add(31, this, "input expression must be type integer");
				}
			}
		}
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		// Create an iterator to iterate through all read commands
		ListIterator<Readable> it = this.inputs.getIterator();
		while(it.hasNext()) {
			// Cast the iterator object as Readable to be able to cast to down further
			Readable read = (Readable) it.next();
			
			if(read instanceof IdentExpn) {
				// If the object is normal variable, we can simply add it to the stack
				IdentExpn i = (IdentExpn) read;
				writer.add(Machine.ADDR, i.getLexicLevel(), i.getOrderNumber());
			} else if(read instanceof SubsExpn) {
				// If the object is an array index, we need to evalute the addres then push it to the stack
				SubsExpn s = (SubsExpn) read;
				writer.add(Machine.ADDR, s.getLexicLevel(), s.getOrderNumber());
				s.getOperand().doCodeGen(writer);
		        writer.add(Machine.PUSH, s.getLowerBound());
		        writer.add(Machine.SUB);
		        writer.add(Machine.ADD);
			}
			// Get the user input then store it into the address from above
			writer.add(Machine.READI);
			writer.add(Machine.STORE);
		}
	}
}
