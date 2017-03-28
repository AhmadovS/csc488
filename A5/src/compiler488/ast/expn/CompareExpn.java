package compiler488.ast.expn;

import compiler488.DebugTool;
import compiler488.ast.OPSYMBOL;
import compiler488.ast.type.*;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
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


	public void checkSemantics(SymbolTable symbols) throws Exception {

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

	@Override
	public void doCodeGen(MachineWriter writer) {
		// Get operation symbol
		String op = this.getOpSymbol();
		Expn operand1 = getLeft();
		Expn operand2 = getRight();

		// Order the memory load based on operator, since there is no greater than operator.
		if(op.equals(OPSYMBOL.GREATER_THAN) || op.equals(OPSYMBOL.GREATER_THAN_EQUALS)) {
		    // switches the order of the operands.
			operand1 = getRight();
			operand2 = getLeft();
		}

		// Emits code to evaluate the operands
		operand1.doCodeGen(writer);
		operand2.doCodeGen(writer);

		// Stack :: oprand1 -> operand2
		if (op.equals(OPSYMBOL.GREATER_THAN_EQUALS) || op.equals(OPSYMBOL.LESS_THAN_EQUALS)) {

			// Performs LT operation, and then emits code
			// that calculates the operands again, so we can do
			// equality comparison
            writer.add(Machine.LT);
            operand1.doCodeGen(writer);
            operand2.doCodeGen(writer);
            writer.add(Machine.EQ);
            writer.add(Machine.OR);

        } else if (op.equals(OPSYMBOL.GREATER_THAN) || op.equals(OPSYMBOL.LESS_THAN)) {
		    writer.add(Machine.LT);
		} else if (op.equals(OPSYMBOL.EQUALS)) {
			writer.add(Machine.EQ);
		} else if (op.equals(OPSYMBOL.NOT_EQUALS)) {
		    writer.add(Machine.EQ);
		    // Stack :: result of equality testing (either 1 or 0).
		    writer.add(Machine.PUSH, 1);
		    writer.add(Machine.SWAP);
		    // Stack :: 1 -> equailty-result
		    writer.add(Machine.SUB);
		}


	}
}
