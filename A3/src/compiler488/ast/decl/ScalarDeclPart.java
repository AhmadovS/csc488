package compiler488.ast.decl;

import java.util.ArrayList;

import compiler488.symbol.SymbolTable;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDeclPart extends DeclarationPart {

	/**
	 * Returns a string describing the name of the object being
	 * declared.
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {
		// TODO Auto-generated method stub
		
	}
}
