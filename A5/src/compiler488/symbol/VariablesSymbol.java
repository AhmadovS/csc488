package compiler488.symbol;

import compiler488.ast.type.Type;

public class VariablesSymbol extends Symbol{
	
	private String name;
	private Type type;
	private int orderNumber;
	private int lexicLevel;

	public VariablesSymbol(String name, Type type){
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public void setType(Type type) {
		this.type = type;
	}

	public int getOrderNumber() {
		return this.orderNumber;
	}

	public void setOrderNumber(int on) {
	    this.orderNumber = on;
    }

    public int getLexicLevel() {
        return lexicLevel;
    }

    public void setLexicLevel(int lexicLevel) {
        this.lexicLevel = lexicLevel;
    }

    @Override
    public String toString() {
        String typeName;
        if (this.getType() == null)
            typeName = "";
        else
            typeName = this.getType().getClass().getSimpleName();
        return String.format("%s(name=%s, type=%s, lexicLevel=%d, orderNumber=%d)",
                this.getClass().getSimpleName(), this.getName(), typeName, lexicLevel, orderNumber);
    }
}