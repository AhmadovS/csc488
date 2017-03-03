package compiler488.ast.decl;

import compiler488.symbol.SymbolTable;
import compiler488.ast.type.Type;

/**
 * Represents the declaration of a simple variable.
 */

public class ParameterDecl extends Declaration {
	
	public ParameterDecl(String name, Type type) {
		super(name, type);
	}

	/**
	 * Returns a string describing the name and type of the object being
	 * declared.
	 */
	@Override
	public String toString() {
		return  name + " : " + type ;
	}

	@Override
	public void checkSemantics(SymbolTable symbols) throws Exception {
	    // Nothing to do here.
	}
}
