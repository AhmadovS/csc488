package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;
import compiler488.ast.type.*;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all ordered comparisions expression where both operands must
 * be integer expressions. e.g. < , > etc. comparisons
 */
public class CompareExpn extends BinaryExpn {

	public CompareExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }

	public void checkSemantics(SymbolTable symbols) {

		// Semantics check on children must be performed before getting their type.
        this.getLeft().checkSemantics(symbols);
		this.getRight().checkSemantics(symbols);

		if (this.getLeft().getType() == null || this.getRight().getType() == null) {
			SemanticError.add(this, "RHS or LHS of CompareExpn has null type");
		} else {

			if (getOpSymbol().equals(OPSYMBOL.EQUALS)) {
				// S32
				if (this.getLeft().getType().getClass() != this.getRight().getType().getClass()) {
					SemanticError.add(32, this, "LHS and RHS must have same type");
				}
			} else if (getOpSymbol().equals(OPSYMBOL.NOT_EQUALS)) {
				// S32
				if (this.getLeft().getType().getClass() != this.getRight().getType().getClass()) {
					SemanticError.add(32, this, "LHS and RHS must have same type");
				}
			} else {

				// S31 - checks left expression is integer
				if (!(this.getLeft().getType() instanceof IntegerType)) {
					SemanticError.add(31, this, "Left side of arithmetic operation must be integer");
				}

				// S31 - checks right expression is integer
				if (!(this.getRight().getType() instanceof IntegerType)) {
					SemanticError.add(31, this, "Right side of arithmetic operation must be integer");
				}
			}
		}

		// S20 - sets result type to boolean
		this.setType(new BooleanType());
	}
}
