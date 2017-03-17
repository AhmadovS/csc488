package compiler488.ast.expn;

import compiler488.ast.type.IntegerType;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {
	
	public ArithExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }
	
	public void checkSemantics(SymbolTable symbols) {

		// Semantics check on children must be performed before getting their type.
        this.getLeft().checkSemantics(symbols);
		this.getRight().checkSemantics(symbols);

		// S31 - checks left expression is integer
		if(!(this.getLeft().getType() instanceof IntegerType)){
			SemanticError.add(31, this,"Left side of arithmetic operation must be integer");
		}

		// S31 - checks right expression is integer
		if(!(this.getRight().getType() instanceof IntegerType)){
			SemanticError.add(31, this, "Right side of arithmetic operation must be integer");
		}

		// S21 - sets result type to integer
		this.setType(new IntegerType());
	}
}
