package compiler488.ast.expn;

import java.util.ArrayList;

import compiler488.ast.type.BooleanType;
import compiler488.symbol.SymbolTable;

/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn {
    private boolean  value ;	/* value of the constant */
    
    public BoolConstExpn(boolean value) {
    	this.value = value;
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
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){
		this.setType(new BooleanType());
	}
}
