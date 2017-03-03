package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;
import compiler488.ast.type.BooleanType;
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
            throw new Exception("Expected boolean expression");
        }

        // S21 - Set return type to boolean
        this.setType(new BooleanType());
    }
}
