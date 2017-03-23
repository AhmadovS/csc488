package compiler488.symbol;

import compiler488.ast.type.Type;

public class VariablesSymbol extends Symbol{
	
	private String name;
	private Type type;
	private int orderNumber;

	public VariablesSymbol(String name, Type type, int lexicLevel){
		super(lexicLevel);
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

    @Override
    public String toString() {
        String typeName;
        if (this.getType() == null)
            typeName = "";
        else
            typeName = this.getType().getClass().getSimpleName();
        return String.format("%s(name=%s, type=%s, orderNumber=%d)",
                this.getClass().getSimpleName(), this.getName(), typeName, orderNumber);
    }
}