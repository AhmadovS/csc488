package compiler488.symbol;

import compiler488.ast.type.Type;

class VariablesSymbol extends Symbol{
	
	private String name;
	private Type type;

	public VariablesSymbol(String name, Type type){
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public void setType(Type type) {
		this.type = type;
		
	}
	
}