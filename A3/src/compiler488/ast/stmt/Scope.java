package compiler488.ast.stmt;

import java.io.PrintStream;
import java.util.*;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.decl.Declaration;
import compiler488.symbol.SymbolTable;

/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt {

	private ASTList<Declaration> declarations; // The declarations at the top.
	private ASTList<Stmt> statements; // The statements to execute.

	public Scope(ASTList<Declaration> declarations, ASTList<Stmt> statements) {
		this.declarations = declarations;
		this.statements = statements;
	}

	/**
	 * Print a description of the <b>scope</b> construct.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOnLn(out, depth, "Scope");
		Indentable.printIndentOnLn(out, depth, "declarations");

		declarations.printOnSeperateLines(out, depth + 1);

		Indentable.printIndentOnLn(out, depth, "statements");

		statements.printOnSeperateLines(out, depth + 1);

		Indentable.printIndentOnLn(out, depth, "End Scope");
	}

	public ASTList<Declaration> getDeclarations() {
		return declarations;
	}

	public ASTList<Stmt> getStatements() {
		return statements;
	}

	public void setDeclarations(ASTList<Declaration> declarations) {
		this.declarations = declarations;
	}

	public void setStatements(ASTList<Stmt> statements) {
		this.statements = statements;
	}
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors){

        ListIterator stmts = this.getStatements().getIterator();
        ListIterator decls = this.getDeclarations().getIterator();

        while(stmts.hasNext()){
            ((Stmt) stmts.next()).checkSemantics(symbols,  errors);
        }

        while(decls.hasNext()){
            ((Declaration) decls.next()).checkSemantics(symbols,  errors);
        }

	}

}
