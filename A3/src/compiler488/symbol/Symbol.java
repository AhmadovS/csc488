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

	@Override
	public String toString() {
	    return String.format("%s(name=%s, type=%s)",
                this.getClass().getSimpleName(), this.getName(), this.getType().getClass().getSimpleName());
	}
}



