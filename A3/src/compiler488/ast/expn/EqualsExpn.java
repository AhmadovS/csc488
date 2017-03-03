package compiler488.ast.expn;

import compiler488.ast.type.*;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all binary expression where both operands could be either
 * integer or boolean expressions. e.g. = and not = comparisons
 */
public class EqualsExpn extends BinaryExpn {

	public EqualsExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }
	
	public void checkSemantics(SymbolTable symbols){
		
		this.getLeft().checkSemantics(symbols);
		this.getRight().checkSemantics(symbols);
		
		if(!(this.getLeft().getType().toString().equals(this.getRight().getType().toString()))){
			throw new Exception("The type of left and right operand do not match");
		}
		
		this.setType(new BooleanType());
	}
}
