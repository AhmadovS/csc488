package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;
import compiler488.ast.type.IntegerType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {

	public UnaryMinusExpn(Expn operand) {
		super(OPSYMBOL.MINUS, operand);
	}
	
	@Override
    public void checkSemantics(SymbolTable symbols) throws Exception {
		// S31 - Check that expression type is integer
		// Note semantic check on operand must be performed first.
		this.getOperand().checkSemantics(symbols);

		if (!(this.getOperand().getType() instanceof IntegerType)) {
			SemanticError.add(31, this, "Expected integer expression");
		}

		// S21 - Set result type to integer.
		this.setType(new IntegerType());
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		this.getOperand().doCodeGen(writer);
		writer.add(Machine.PUSH, -1);
		writer.add(Machine.MUL);
	}
}
