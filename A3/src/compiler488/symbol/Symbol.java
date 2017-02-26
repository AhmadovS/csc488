package compiler488.symbol;

import compiler488.ast.ASTList;
import compiler488.ast.type.Type;

/**
 * Symbol entries for variables, arrays and function/procedures
 * 
 * @author Samud Ahmadov
 *
 */
public abstract class Symbol{
	
	public Symbol(){}
	
	public abstract String getName();
	public abstract Type getType();
	
	public abstract void setName(String name);
	public abstract void setType(Type type);
}

class Variables extends Symbol{
	
	private String name;
	private Type type;

	public Variables(String name, Type type){
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

class Arrays extends Variables{
	
	int lowerBound1;
	int upperBound1;
	int lowerBound2;
	int upperBound2;
	
	boolean twoDim = false;
	
	public Arrays(String name, Type type, int lb1, int up1) {
		super(name, type);
		this.lowerBound1 = lb1;
		this.upperBound1 = up1;
	}
	
	public Arrays(String name, Type type, int lb1, int up1, int lb2, int up2) {
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

class Functions extends Symbol{
	
	private String name;
	private Type type;
	private ASTList<Symbol> params;
	
	public Functions(String name, Type type, ASTList<Symbol> params){
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
