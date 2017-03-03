package compiler488.ast.decl;

import java.util.ArrayList;

import compiler488.symbol.SymbolTable;
import compiler488.ast.type.Type;

/**
 * Represents the declaration of a simple variable.
 */

public class ScalarDecl extends Declaration {
	
	public ScalarDecl(String name, Type type) {
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
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){

	    //TODO: does this check need to happen here,
		// should SymbolTable.addSymbol already have taken care of this?

		if(symbols.getSymbol(this.getName()) != null){
			errors.add("Scalar variable has been already declared");
		}
	}
}
