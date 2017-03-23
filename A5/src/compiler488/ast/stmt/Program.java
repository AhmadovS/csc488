package compiler488.ast.stmt;

import compiler488.ast.AST;
import compiler488.ast.ASTList;
import compiler488.ast.decl.Declaration;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
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

	@Override
	public void doCodeGen(MachineWriter writer) {

		// Program initialization
        writer.add(Machine.PUSHMT);    // base address of main scope activation record.
        writer.add(Machine.SETD, 0);   // sets display[0] = MSP pointer.
		writer.add(Machine.PUSH, Machine.UNDEFINED); // legacy two free words.
        writer.add(Machine.PUSH, 2);
        writer.add(Machine.DUPN);

		// Codegen for declarations
		getDeclarations().doCodeGen(writer);

		// Codegen for statements
		getStatements().doCodeGen(writer);

		// Halt.
		writer.add(Machine.HALT);

	}
}
