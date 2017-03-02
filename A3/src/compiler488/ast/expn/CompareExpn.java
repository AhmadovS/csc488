package compiler488.ast.expn;

import java.util.ArrayList;

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

	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){
		
		// S31 - checks left expression is integer
		this.getLeft().checkSemantics(symbols, errors);
		if(!(this.getLeft().getType() instanceof IntegerType)){
			errors.add("Left side of arithmetic operation must be integer");
		}

		// S31 - checks right expression is integer
		this.getRight().checkSemantics(symbols, errors);
		if(!(this.getRight().getType() instanceof IntegerType)){
			errors.add("Right side of arithmetic operation must be integer");
		}

		// S20 - sets result type to boolean
		this.setType(new BooleanType());
	}
}
