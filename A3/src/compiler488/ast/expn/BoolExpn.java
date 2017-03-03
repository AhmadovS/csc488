package compiler488.ast.expn;

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
	
	public void checkSemantics(SymbolTable symbols) throws Exception {

		// Semantics check on children must be performed before getting their type.
		this.getLeft().checkSemantics(symbols);
		this.getRight().checkSemantics(symbols);

		// S30 - checks left expression is boolean
		if(!(this.getLeft().getType() instanceof BooleanType)){
			throw new Exception("Left side of boolean operation must be boolean");
		}

		// S30 - checks right expression is boolean
		if(!(this.getRight().getType() instanceof BooleanType)){
			throw new Exception("Right side of boolean operation must be boolean");
		}

		// S20 - sets result type to boolean
		this.setType(new BooleanType());
	}
}
