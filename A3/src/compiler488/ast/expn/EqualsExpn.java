package compiler488.ast.expn;

import java.util.ArrayList;

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
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){
		
		this.getLeft().checkSemantics(symbols, errors);
		this.getRight().checkSemantics(symbols, errors);
		
		if(!(this.getLeft().getType().toString().equals(this.getRight().getType().toString()))){
			errors.add("The type of left and right operand do not match");
		}
		
		this.setType(new BooleanType());
	}
}
