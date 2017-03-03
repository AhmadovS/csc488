package compiler488.ast.decl;

import java.util.ArrayList;

import compiler488.ast.AST;
import compiler488.ast.type.Type;
import compiler488.symbol.*;

/**
 * Holds the declaration part of an array.
 */
public class ArrayDeclPart extends DeclarationPart {

	/* The lower and upper boundaries of the array. */
    private Integer lb, ub, lb2,up2;
    private String name;
    private boolean is2d; //TODO: do we really have 2-dim array
    private Type type = null;
        
	/* The number of objects the array holds. */
	private Integer size;
	
	public ArrayDeclPart(String name, int lb, int up){
		super(name);
		
		this.lb = lb;
		this.ub = up;
	}
	
	
	public ArrayDeclPart(String name, Type type, int lb, int up){
		super(name);
		
		this.lb = lb;
		this.ub = up;
		this.type = type;
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
	public void checkSemantics(AST syntaxTree, SymbolTable symbols, ArrayList<String> errors){
		if(symbols.getSymbol(this.name) != null){
			errors.add("Variable has been already declared");
		}

		// S46 Check that lower bound is <= upper bound
		if(this.lb>this.ub || (this.is2d && (this.lb2>this.up2))){
			errors.add("The lower bound must be smaller than upper bound");
		}

		// S19 and S48 Declaring the array by adding it to the symbol table
        ArraysSymbol array = new ArraysSymbol(this.name,this.type, this.lb, this.ub);
        symbols.addSymbol(array);
	}
}
