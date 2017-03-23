package compiler488.symbol;

import compiler488.ast.type.ArrayType;
import compiler488.ast.type.Type;

public class ArraysSymbol extends VariablesSymbol{

	private int lowerBound;
	private int upperBound;
	private int size;

	public ArraysSymbol(String name, Type elementType, int lb, int up, int size, int lexicLevel) {
		super(name, new ArrayType(elementType), lexicLevel);
		this.lowerBound = lb;
		this.upperBound = up;
		this.size = size;
	}

	public int getLowerBound() {
		return this.lowerBound;
	}

	public int getUpperBound() {
		return this.upperBound;
	}

	public int getSize() {
		return this.size;
	}
	
}