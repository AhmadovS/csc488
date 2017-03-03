package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {

    public NotExpn(Expn operand) {
    	super(OPSYMBOL.NOT, operand);
    }
	
}
