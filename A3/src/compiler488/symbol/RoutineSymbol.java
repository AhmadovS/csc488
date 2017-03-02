package compiler488.symbol;

import compiler488.ast.ASTList;
import compiler488.ast.type.Type;

import java.util.ListIterator;


public class RoutineSymbol extends Symbol{
	
	private String name;
	private Type type;
	private ASTList<Type> params;
	private int paramCount;
	
	public RoutineSymbol(String name, Type type, ASTList<Type> params){
		this.name = name;
		this.type = type;
		this.params = params;
		this.paramCount = params.size();
	}
	
	public ASTList<Type> getParams(){
		return this.params;
	}
	
	public void setParams(ASTList<Type> params){
		this.params = params;
		this.paramCount = params.size();
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