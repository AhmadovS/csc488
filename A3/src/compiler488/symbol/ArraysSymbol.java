package compiler488.symbol;

import compiler488.ast.type.Type;

public class ArraysSymbol extends VariablesSymbol{
	
	int lowerBound1;
	int upperBound1;
	int lowerBound2;
	int upperBound2;

	// TODO: do we actually have 2-dim arrays?
	boolean twoDim = false;
	
	public ArraysSymbol(String name, Type type, int lb1, int up1) {
		super(name, type);
		this.lowerBound1 = lb1;
		this.upperBound1 = up1;
	}
	
	public ArraysSymbol(String name, Type type, int lb1, int up1, int lb2, int up2) {
		super(name, type);
		this.lowerBound1 = lb1;
		this.upperBound1 = up1;
		this.lowerBound2 = lb2;
		this.upperBound2 = up2;
		this.twoDim = true;
	}
	
	public boolean isTwoDim(){
		return this.twoDim;
	}
	
}