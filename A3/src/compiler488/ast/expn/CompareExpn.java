package compiler488.ast.expn;

import compiler488.ast.type.*;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all ordered comparisions expression where both operands must
 * be integer expressions. e.g. < , > etc. comparisons
 */
public class CompareExpn extends BinaryExpn {

	public CompareExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }

	public void checkSemantics(SymbolTable symbols){
		
		// S31 - checks left expression is integer
		this.getLeft().checkSemantics(symbols);
		if(!(this.getLeft().getType() instanceof IntegerType)){
			throw new Exception("Left side of arithmetic operation must be integer");
		}

		// S31 - checks right expression is integer
		this.getRight().checkSemantics(symbols);
		if(!(this.getRight().getType() instanceof IntegerType)){
			throw new Exception("Right side of arithmetic operation must be integer");
		}

		// S20 - sets result type to boolean
		this.setType(new BooleanType());
	}
}
