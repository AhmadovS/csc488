package compiler488.ast.stmt;

import compiler488.ast.expn.Expn;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Holds the assignment of an expression to a variable.
 */
public class AssignStmt extends Stmt {
	/*
	 * lval is the location being assigned to, and rval is the value being
	 * assigned.
	 */
	private Expn lval, rval;
	
	public AssignStmt(Expn lval, Expn rval) {
		this.lval = lval;
		this.rval = rval;
		
		this.lval.setParent(this);
		this.rval.setParent(this);
	}

	/** Returns a string that describes the assignment statement. */
	@Override
	public String toString() {
		return "Assignment: " + lval + " := " + rval;
	}

	public Expn getLval() {
		return lval;
	}

	public void setLval(Expn lval) {
		this.lval = lval;
	}

	public Expn getRval() {
		return rval;
	}

	public void setRval(Expn rval) {
		this.rval = rval;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {

	    // Note: Have to perform semantic check on children first, to get their type.
		this.getLval().checkSemantics(symbols);
		this.getRval().checkSemantics(symbols);

		// S34 - Check that variable (lval) and expression (rval) are the same type.
        if (lval.getType() != null && rval.getType() != null) {
			if (lval.getType().getClass() != rval.getType().getClass()) {
				SemanticError.add(34, this, "The type left and right hand of assignments do not match");
			}
		}
	}
}
