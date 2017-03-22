package compiler488.ast.decl;

import compiler488.codegen.MachineWriter;
import compiler488.symbol.SymbolTable;

/**
 * Represents the declaration of a simple variable.
 */
public class ScalarDeclPart extends DeclarationPart {
	
	public ScalarDeclPart(String name) {
		super(name);
	}

	/**
	 * Returns a string describing the name of the object being
	 * declared.
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {
	    // Nothing to do here.
	}

	@Override
	public void doCodeGen(SymbolTable symbols, MachineWriter writer) {
		// Nothing to do here.
	}
}
