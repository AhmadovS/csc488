package compiler488.ast.stmt;

import compiler488.ast.Indentable;
import compiler488.symbol.SymbolTable;

/**
 * A placeholder for statements.
 */
public abstract class Stmt extends Indentable {
	
	public abstract void checkSemantics(SymbolTable symbols);

	/**
	 * Most statements have the same lexic level as their parents.
     * Program node is an exception. It has root lexic-level of 0.
	 * @return Returns lexic-level associated with this node.
	 */
	@Override
	protected int calculateLexicLevel() {
		return getParent().getLexicLevel();
	}
}
