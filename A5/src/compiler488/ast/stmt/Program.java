package compiler488.ast.stmt;

import compiler488.ast.AST;
import compiler488.ast.ASTList;
import compiler488.ast.decl.Declaration;
import compiler488.symbol.SymbolTable;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {

	public Program(ASTList<Declaration> declarations, ASTList<Stmt> statements) {
		super(declarations, statements);
	}

	@Override
	public void setParent(AST parent) {
	    // Program can't have a parent.
	}

	@Override
	public void checkSemantics(SymbolTable symbols) {
		// S00 - start program scope.
		symbols.startScope();
			// Walk of AST for semantic checking
		super.checkSemantics(symbols);
		// S01 - end program scope.
		symbols.exitScope();
	}
}
