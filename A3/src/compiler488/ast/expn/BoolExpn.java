package compiler488.ast.expn;

import java.util.ArrayList;

import compiler488.ast.type.*;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {
	
	public BoolExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){

		// S30 - checks left expression is boolean
		this.getLeft().checkSemantics(symbols, errors);
		if(!(this.getLeft().getType() instanceof BooleanType)){
			errors.add("Left side of boolean operation must be boolean");
		}

		// S30 - checks right expression is boolean
		this.getRight().checkSemantics(symbols, errors);
		if(!(this.getRight().getType() instanceof BooleanType)){
			errors.add("Right side of boolean operation must be boolean");
		}

		// S20 - sets result type to boolean
		this.setType(new BooleanType());
	}
}
