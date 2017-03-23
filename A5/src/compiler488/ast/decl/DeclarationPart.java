package compiler488.ast.decl;

import compiler488.ast.AST;
import compiler488.symbol.SymbolTable;

/**
 * The common features of declarations' parts.
 */
public abstract class DeclarationPart extends AST {

	/** The name of the thing being declared. */
	protected String name;
	
	public DeclarationPart(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	/**
	 * All declarations have the same lexic level as their parents.
	 * @return Returns lexic-level associated with this node.
	 */
	@Override
	protected final int calculateLexicLevel() {
		return getParent().getLexicLevel();
	}
}
