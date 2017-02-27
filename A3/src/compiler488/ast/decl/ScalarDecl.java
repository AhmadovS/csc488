package compiler488.ast.decl;

import java.util.ArrayList;

import compiler488.symbol.SymbolTable;

/**
 * Represents the declaration of a simple variable.
 */

public class ScalarDecl extends Declaration {

	/**
	 * Returns a string describing the name and type of the object being
	 * declared.
	 */
	@Override
	public String toString() {
		return  name + " : " + type ;
	}
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){
		
		if(symbols.getSymbol(this.getName()) != null){
			errors.add("Scalar variable has been already declared");
		}
	}
}
