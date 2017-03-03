package compiler488.ast.expn;

import compiler488.ast.type.*;
import compiler488.semantics.SemanticError;
import compiler488.symbol.*;

/** Represents a conditional expression (i.e., x>0?3:4). */
public class ConditionalExpn extends Expn {
	private Expn condition; // Evaluate this to decide which value to yield.
	private Expn trueValue; // The value is this when the condition is true.
	private Expn falseValue; // Otherwise, the value is this.
	
	public ConditionalExpn(Expn condition, Expn trueValue, Expn falseValue) {
		this.condition = condition;
		this.condition.setParent(this);
		
		this.trueValue = trueValue;
		this.trueValue.setParent(this);
		
		this.falseValue = falseValue;
		this.falseValue.setParent(this);
	}

	/** Returns a string that describes the conditional expression. */
	@Override
	public String toString() {
		return "(" + condition + " ? " + trueValue + " : " + falseValue + ")";
	}

	public Expn getCondition() {
		return condition;
	}

	public void setCondition(Expn condition) {
		this.condition = condition;
	}

	public Expn getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(Expn falseValue) {
		this.falseValue = falseValue;
	}

	public Expn getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(Expn trueValue) {
		this.trueValue = trueValue;
	}
	
	public void checkSemantics(SymbolTable symbols) {

	    // Note: do semantic check on children before checking their type.
        this.getCondition().checkSemantics(symbols);
		this.getTrueValue().checkSemantics(symbols);
		this.getFalseValue().checkSemantics(symbols);

		// S30 - check that type of expression is boolean
		if (!(this.getCondition().getType() instanceof BooleanType)){
			SemanticError.add("The condition of conditional expression must be boolean");
		}

		// S33 - check both conditional expression have same type
		if (this.getFalseValue().getType().getClass() != this.getTrueValue().getType().getClass()) {
			SemanticError.add("Both side of conditional expression must be the same type");
		}

		// S24 - Set result type to type of conditional expression
		this.setType(this.getTrueValue().getType());
	}
}
