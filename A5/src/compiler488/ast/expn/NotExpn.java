package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;
import compiler488.ast.type.BooleanType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {

    public NotExpn(Expn operand) {
    	super(OPSYMBOL.NOT, operand);
    }

    @Override
    public void checkSemantics(SymbolTable symbols) throws Exception {
        // S30 - check that type of expression is boolean
        // Note: must check semantics on child first.
        this.getOperand().checkSemantics(symbols);

        if (!(this.getOperand().getType() instanceof BooleanType)) {
            SemanticError.add(30, this,"Expected boolean expression");
        }

        // S21 - Set return type to boolean
        this.setType(new BooleanType());
    }

    @Override
    public void doCodeGen(MachineWriter writer) {
        // We compute a not operation by comparing a value with false
        // i.e. (0 == 0) -> 1
        //      (1 == 0) -> 0
        this.getOperand().doCodeGen(writer);
        writer.add(Machine.PUSH, Machine.MACHINE_FALSE);
        writer.add(Machine.EQ);
    }
}
