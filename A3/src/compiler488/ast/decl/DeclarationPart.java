package compiler488.ast.decl;

import java.util.ArrayList;

import compiler488.ast.AST;
import compiler488.symbol.SymbolTable;

/**
 * The common features of declarations' parts.
 */
public abstract class DeclarationPart extends AST {

	/** The name of the thing being declared. */
	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract void checkSemantics(SymbolTable symbols, ArrayList<String> errors);

}
