package compiler488.ast.stmt;

import compiler488.ast.Indentable;
import compiler488.symbol.SymbolTable;

/**
 * A placeholder for statements.
 */
public abstract class Stmt extends Indentable {
	
	public abstract void checkSemantics(SymbolTable symbols);
	
}
