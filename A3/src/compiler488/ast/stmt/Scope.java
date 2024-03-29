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

    private ASTList<Declaration> declarations = new ASTList<Declaration>(); // The declarations at the top.
    private ASTList<Stmt> statements = new ASTList<Stmt>(); // The statements to execute.

    public Scope(ASTList<Declaration> declarations, ASTList<Stmt> statements) {
        this.declarations = declarations;
        this.statements = statements;
		
		this.declarations.setParent(this);
		this.statements.setParent(this);
    }

    public Scope(ASTList<Stmt> statements) {
        this.statements = statements;
		this.statements.setParent(this);
        this.declarations.setParent(this);
    }

    public Scope() {
        this.statements.setParent(this);
        this.declarations.setParent(this);
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

    @Override
    public void checkSemantics(SymbolTable symbols) {

        ListIterator decls = this.getDeclarations().getIterator();
        ListIterator stmts = this.getStatements().getIterator();

        // S02 - Associate declaration with scope (happens inside each semantic check)
        while(decls.hasNext()){
            ((Declaration) decls.next()).checkSemantics(symbols);
        }

        // Checking semantics on child statement nodes.
        while(stmts.hasNext()){
            Stmt currentStmt = (Stmt) stmts.next();

            // If the statement is a scope, we need to start a
            // scope within the symbol table.
            if (currentStmt instanceof Scope) {
                // S06 - Start ordinary scope
                symbols.startScope();
                    currentStmt.checkSemantics(symbols);
                // S07 - End ordinary scope
                symbols.exitScope();
            } else {
                currentStmt.checkSemantics(symbols);
            }
        }


    }

}
