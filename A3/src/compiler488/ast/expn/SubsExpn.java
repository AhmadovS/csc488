package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;
import compiler488.ast.Readable;
import compiler488.ast.type.IntegerType;
import compiler488.semantics.SemanticError;
import compiler488.symbol.ArraysSymbol;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;

/**
 * References to an array element variable
 * 
 * Treat array subscript operation as a special form of unary expression.
 * operand must be an integer expression
 */
public class SubsExpn extends UnaryExpn implements Readable {
	
	private String variable; // name of the array variable
	
    public SubsExpn(Expn operand, String variable) {
    	super(OPSYMBOL.SUB, operand);
    	this.variable = variable;
    }

	/** Returns a string that represents the array subscript. */
	@Override
	public String toString() {
		return (variable + "[" + operand + "]");
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {
	    // S31 - check return type of operand is integer.
		this.getOperand().checkSemantics(symbols);
		if (!(this.getOperand().getType() instanceof IntegerType)) {
            SemanticError.add(31, this, "Subscript expression must have type integer");
		}

		Symbol sm = symbols.getSymbol(this.getVariable());
		if (sm == null) {
		    SemanticError.addIdentNotDeclaredError(this);
        } else {
            // S38 - Check variable has been declared as an array.
            if (sm.getClass() != ArraysSymbol.class) {
                SemanticError.addIdentNotDeclaredError(38, this);
            }
            // S27 - Set result type to type of the array element.
            this.setType(sm.getType());
        }
	}
}
