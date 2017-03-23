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

	private int lexicLevel;
	
	public Symbol(int lexicLevel){
		this.lexicLevel = lexicLevel;
	}
	
	public abstract String getName();
	public abstract Type getType();
	
	public abstract void setName(String name);
	public abstract void setType(Type type);

	public int getLexicLevel() {
		return lexicLevel;
	}

	@Override
	public String toString() {
	    String typeName;
	    if (this.getType() == null)
	        typeName = "";
	    else
	        typeName = this.getType().getClass().getSimpleName();
	    return String.format("%s(name=%s, type=%s)",
                this.getClass().getSimpleName(), this.getName(), typeName);
	}
}



