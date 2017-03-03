package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.decl.Declaration;
import compiler488.symbol.SymbolTable;

import java.util.ArrayList;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {

	public Program(ASTList<Declaration> declarations, ASTList<Stmt> statements) {
		super(declarations, statements);
	}

	@Override
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {
		// S00 - start program scope.
		symbols.startScope();
			// Walk of AST for semantic checking
            super.checkSemantics(symbols, errors);
		// S01 - end program scope.
		symbols.exitScope();
	}
}
