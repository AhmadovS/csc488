package compiler488.ast.expn;

import compiler488.ast.type.BooleanType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.symbol.SymbolTable;

/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn {
    private boolean value ;	/* value of the constant */
    
    public BoolConstExpn(boolean value) {
    	this.value = value;
    	// S20 - set result type to boolean.
    	this.setType(new BooleanType());
    }

    /** Returns the value of the boolean constant */
    @Override
	public String toString () { 
    	return ( value ? "(true)" : "(false)" );
    }

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {
    	// Nothing to do here
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		writer.add(Machine.PUSH, value ? Machine.MACHINE_TRUE : Machine.MACHINE_FALSE);
	}
}
