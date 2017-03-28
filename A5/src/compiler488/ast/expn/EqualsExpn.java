package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;
import compiler488.ast.type.*;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all binary expression where both operands could be either
 * integer or boolean expressions. e.g. = and not = comparisons
 */
public class EqualsExpn extends BinaryExpn {

	public EqualsExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }
	
	@Override
    public void checkSemantics(SymbolTable symbols) throws Exception {

		// Note: do semantic check on children before checking their type.
        this.getLeft().checkSemantics(symbols);
		this.getRight().checkSemantics(symbols);

		// S32 - left and right operand must have the same type
		if (this.getLeft().getType().getClass() != this.getRight().getType().getClass()) {
			SemanticError.add(32, this, "The type of left and right operand do not match");
		}

		// S20 - sets the result type to boolean
		this.setType(new BooleanType());
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		this.getLeft().doCodeGen(writer);
		this.getRight().doCodeGen(writer);

		if (getOpSymbol().equals(OPSYMBOL.NOT_EQUALS)) {
		    writer.add(Machine.MACHINE_FALSE);
        }

		writer.add(Machine.EQ);

	}
}
