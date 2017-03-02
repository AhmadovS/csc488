package compiler488.ast.expn;

import java.util.ArrayList;

import compiler488.ast.type.IntegerType;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {
	
	public ArithExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){
		
		this.getLeft().checkSemantics(symbols, errors);
		this.getRight().checkSemantics(symbols, errors);
		
		if(!(this.getLeft().getType() instanceof IntegerType)){
			errors.add("Left side of arithmetic operation must be integer");
		}
		
		if(!(this.getRight().getType() instanceof IntegerType)){
			errors.add("Right side of arithmetic operation must be integer");
		}
		this.setType(new IntegerType());
	}
}
