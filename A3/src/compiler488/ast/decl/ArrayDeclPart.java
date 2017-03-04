package compiler488.ast.decl;

import compiler488.DebugTool;
import compiler488.semantics.SemanticError;
import compiler488.symbol.*;

/**
 * Holds the declaration part of an array.
 */
public class ArrayDeclPart extends DeclarationPart {

	/* The lower and upper boundaries of the array. */
    private Integer lb, ub;

	/* The number of objects the array holds. */
	private Integer size;
	
	public ArrayDeclPart(String name, int lb, int up){
		super(name);
		
		this.lb = lb;
		this.ub = up;
	}
	
	/**
	 * Returns a string that describes the array.
	 */
	@Override
	public String toString() {
		return name + "[" + lb + ".." + ub + "]";
	}

	public Integer getSize() {
		return size;
	}

	public Integer getLowerBoundary() {
		return lb;
	}

	public Integer getUpperBoundary() {
		return ub;
	}

    public void setLowerBoundary(Integer lb) {
    	this.lb = lb;
    }

    public void setUpperBoundary(Integer ub) {
    	this.ub = ub;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {
		// S46 - Check that lower bound is <= upper bound
		if(this.lb > this.ub){
			SemanticError.add(46, this, "The lower bound must be smaller than upper bound");
		}

	}
}
