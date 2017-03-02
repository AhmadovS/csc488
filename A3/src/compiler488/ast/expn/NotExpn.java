package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {

    public NotExpn(String opSymbol, Expn operand) {
    	super(opSymbol, operand);
    }
	
}
