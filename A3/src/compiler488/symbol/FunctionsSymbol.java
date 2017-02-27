package compiler488.symbol;

import compiler488.ast.ASTList;
import compiler488.ast.type.Type;


public class FunctionsSymbol extends Symbol{
	
	private String name;
	private Type type;
	private ASTList<Symbol> params;
	
	public FunctionsSymbol(String name, Type type, ASTList<Symbol> params){
		this.name = name;
		this.type = type;
		this.params = params;
	}
	
	public ASTList<Symbol> getParams(){
		return this.params;
	}
	
	public void setParams(ASTList<Symbol>  params){
		this.params = params;
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