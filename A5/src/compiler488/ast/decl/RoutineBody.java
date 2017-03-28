package compiler488.ast.decl;

import java.io.PrintStream;
import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.stmt.ReturnStmt;
import compiler488.ast.stmt.Scope;
import compiler488.ast.stmt.Stmt;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.ParamsSymbol;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.SymbolTable;

/**
 * Represents the parameters and instructions associated with a
 * function or procedure.
 */
public class RoutineBody extends Indentable {

	private ASTList<ParameterDecl> parameters; // The formal parameters of the routine.

	private Scope body; // Execute this scope when routine is called.
	
	public RoutineBody(ASTList<ParameterDecl> parameters, Scope body) {
		this.body = body;
		this.parameters = parameters;
		
		this.body.setParent(this);
		this.parameters.setParent(this);
	}
	
	public RoutineBody(Scope body) {
		this.body = body;
		this.parameters = new ASTList<ParameterDecl>();
		
		this.body.setParent(this);
		this.parameters.setParent(this);
	}

	/**
	 * Print a description of the formal parameters and the scope for this
	 * routine.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		if (parameters != null)
			out.println("(" + parameters + ")");
		else
			out.println(" ");
		body.printOn(out, depth);
	}

	public Scope getBody() {
		return body;
	}

	public void setBody(Scope body) {
		this.body = body;
	}

	public ASTList<ParameterDecl> getParameters() {
		return parameters;
	}

	public void setParameters(ASTList<ParameterDecl> parameters) {
		this.parameters = parameters;
	}

	@Override
	protected final int calculateLexicLevel() {
	    return getParent().getLexicLevel() + 1;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) throws Exception {

		// Note: Parameters don't need semantic check.

		// S04, S08 - Starts function/procedure scope, and
        // and S13 - associates scope with function/procedure.
        RoutineSymbol sym = ((RoutineDecl) getParent()).getRoutineSym();
		symbols.startRoutineScope(sym);

            // Adds parameters to the scope
            ListIterator<ParameterDecl> typeli = this.parameters.getIterator();
            while (typeli.hasNext()) {
                ParameterDecl param = typeli.next();
                if (!symbols.addSymbol(new ParamsSymbol(param.getName(), param.getType(), getLexicLevel()))) {
					SemanticError.addIdentAlreadyDeclaredError(this);
				}
            }

            // Semantic check on the routine body
			this.body.checkSemantics(symbols);

		// S05, S09 - Closing function/procedure scope.
		symbols.exitScope();

	}

    @Override
    public void doCodeGen(MachineWriter writer) {
	    // Routine's lexical level
		int L = getLexicLevel();

	    // Calculates base address of activation record
        // and sets the display[L] value.
        writer.add(Machine.PUSHMT);
		writer.add(Machine.PUSH, parameters.size());
		writer.add(Machine.PUSH, 4);
		writer.add(Machine.ADD);
		writer.add(Machine.SUB);
        writer.add(Machine.SETD, L);

		// Stack :: ret val -> dynamic link -> ret addr -> static link -> params

		// Only display[L] has valid value.
		// We follow static links (which is always ADDR L 3) to
		// update the rest of the display.
        while (L > 0) {
            writer.add(Machine.ADDR, L, 3);   // Pushes the address of static link
			writer.add(Machine.LOAD);         // Loads the value of the static link.
            writer.add(Machine.SETD, L - 1);  // Updates the display
            L--;
        }

        body.doCodeGen(writer);

        // If the routine is a procedure, a return statement is not required
		// by the language.
		// Since the ReturnStmt node contains the procedure exit code, the same
        // functionality needs to be implemented here again.
        // This is only applied if the procedure doesn't contain a ReturnStmt as its last node
        if (((RoutineDecl) getParent()).isProcedure()) {
            Stmt lastStatement = null;
            ListIterator<Stmt> bodyStatmentsIterator = getBody().getStatements().getIterator();
            while(bodyStatmentsIterator.hasNext()) {
                lastStatement = bodyStatmentsIterator.next();
            }

            // Procedure may not have any statements, need to check for nullity.
            if (lastStatement != null && lastStatement instanceof ReturnStmt) {
                // Do not do anything
            } else {
                // There's no last return statement. Run exit code.
                // Calculate the number of words to pop (everything from top of stack till return address)

                // check if it is procedure and doesn't have return statement.
                // Move pointer to top of the stack
                writer.add(Machine.PUSHMT);

                // Push 1 above the address of return address (we don't want to pop the return address)
                writer.add(Machine.ADDR, getLexicLevel(), 3);

                // Subtract address (1 above the return address) from top of the stack to get number of words and pop all
                // This pops all the local variables and parameters, and only leaves the bottom 3 fields
                // of the activation record.
                writer.add(Machine.SUB);
                writer.add(Machine.POPN);

                // At this point Stack :: return value -> dynamic link -> return address
                // Return address is now on top of the stack, branch back to it
                writer.add(Machine.BR);
            }
        }

    }
}
