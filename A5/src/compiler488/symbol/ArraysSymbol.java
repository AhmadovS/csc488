package compiler488.symbol;

import compiler488.ast.type.Type;

public class ArraysSymbol extends VariablesSymbol{
	
	private int lowerBound;
	private int upperBound;

	public ArraysSymbol(String name, Type type, int lb1, int up1) {
		super(name, type);
		this.lowerBound = lb1;
		this.upperBound = up1;
	}

	public int getLowerBound() {
		return this.lowerBound;
	}

	public int getUpperBound() {
		return this.upperBound;
	}
	
}