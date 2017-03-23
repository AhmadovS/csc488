package compiler488.ast.decl;

import java.io.PrintStream;
import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.stmt.Scope;
import compiler488.codegen.MachineWriter;
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
    public void checkSemantics(SymbolTable symbols) {

		// Note: Parameters don't need semantic check.

		// S04, S08 - Starts function/procedure scope, and
		// and S13 - associates scope with function/procedure.
        RoutineSymbol sym = ((RoutineDecl) getParent()).getRoutineSym();
		symbols.startRoutineScope(sym);

            // Adds parameters to the scope
            ListIterator<ParameterDecl> typeli = this.parameters.getIterator();
            while (typeli.hasNext()) {
                ParameterDecl param = typeli.next();
                if (!symbols.addSymbol(new ParamsSymbol(param.getName(), param.getType()))) {
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
        // not implemented yet
    }
}
