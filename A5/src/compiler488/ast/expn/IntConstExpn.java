package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.symbol.SymbolTable;

/**
 * Represents a literal integer constant.
 */
public class IntConstExpn extends ConstExpn {
    private Integer value;	// The value of this literal.
    
    public IntConstExpn(Integer value) {
    	this.value = value;
    	// S21 - Set result type to integer
    	this.setType(new IntegerType());
    }

    /** Returns a string representing the value of the literal. */
    @Override
	public String toString () {
    	return value.toString ();
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) throws Exception {
        // Nothing to do here.
    }

	@Override
	public void doCodeGen(MachineWriter writer) {
        writer.add(Machine.PUSH, value);
	}
}
