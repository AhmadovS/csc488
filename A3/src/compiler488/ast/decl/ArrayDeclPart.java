package compiler488.ast.decl;

import java.util.ArrayList;

import compiler488.ast.type.Type;
import compiler488.symbol.*;

/**
 * Holds the declaration part of an array.
 */
public class ArrayDeclPart extends DeclarationPart {

	/* The lower and upper boundaries of the array. */
    private Integer lb, ub, lb2,up2;
    private String name;
    private boolean is2d;
    private Type type;
        
	/* The number of objects the array holds. */
	private Integer size;
	
	public ArrayDeclPart(String name,Type type, int lb, int up){
		this.name = name;
		this.lb = lb;
		this.ub = up;
		this.is2d = false;
		this.type = type;
	}
	
	public ArrayDeclPart(String name, Type type, int lb, int up, int lb2, int up2){
		this.name = name;
		this.lb = lb;
		this.ub = up;
		this.lb2 = lb2;
		this.up2 = up2;
		this.is2d = true;
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
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){
		if(symbols.getSymbol(this.name) != null){
			errors.add("Variable has been already declared");
		}
		
		if(this.lb>this.ub || (this.is2d && (this.lb2>this.up2))){
			errors.add("The lower bound must be smaller than upper bound");
		}
		if(!this.is2d){
			ArraysSymbol array = new ArraysSymbol(this.name,this.type, this.lb, this.ub);
			symbols.addSymbol(array);
		}else{
			ArraysSymbol array = new ArraysSymbol(this.name,this.type, this.lb, this.ub, this.lb2,this.up2);
			symbols.addSymbol(array);
		}
	}
}
