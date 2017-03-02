package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {

	public UnaryMinusExpn(Expn operand, String opSymbol) {
		super(operand, OPSYMBOL.MINUS);
	}

}
