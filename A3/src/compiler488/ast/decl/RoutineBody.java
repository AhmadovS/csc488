package compiler488.ast.decl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ListIterator;

import compiler488.ast.AST;
import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.stmt.Scope;
import compiler488.symbol.ParamsSymbol;
import compiler488.symbol.SymbolTable;

/**
 * Represents the parameters and instructions associated with a
 * function or procedure.
 */
public class RoutineBody extends Indentable {

	private ASTList<ScalarDecl> parameters; // The formal parameters of the routine.

	private Scope body; // Execute this scope when routine is called.
	
	public RoutineBody(ASTList<ScalarDecl> parameters, Scope body) {
		this.body = body;
		this.parameters = parameters;
	}
	
	public RoutineBody(Scope body) {
		this.body = body;
		this.parameters = new ASTList<ScalarDecl>();
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

	public ASTList<ScalarDecl> getParameters() {
		return parameters;
	}

	public void setParameters(ASTList<ScalarDecl> parameters) {
		this.parameters = parameters;
	}

	@Override
	public void checkSemantics(AST syntaxTree, SymbolTable symbols, ArrayList<String> errors) {

		// Depth-first walk through the parameters
		ListIterator<ScalarDecl> li = this.parameters.getIterator();
		while (li.hasNext()) {
			ScalarDecl param = li.next();
			param.checkSemantics(, symbols, errors);
		}

		// S04, S08 - Starts function and procedure scope.
		symbols.startScope();

            // Adds parameters to the scope
            ListIterator<ScalarDecl> typeli = this.parameters.getIterator();
            while (typeli.hasNext()) {
                ScalarDecl param = typeli.next();
                symbols.addSymbol(new ParamsSymbol(param.getName(), param.getType()));
            }

			this.body.checkSemantics(, symbols, errors);

		// S05, S09 - Closing function scope.
		symbols.exitScope();

	}
}
