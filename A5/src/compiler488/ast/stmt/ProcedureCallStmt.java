 
package compiler488.ast.stmt;

import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.Type;
import compiler488.semantics.SemanticError;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.SymbolTable;

/**
 * Represents calling a procedure.
 */
public class ProcedureCallStmt extends Stmt {
	private String name; // The name of the procedure being called.

	private ASTList<Expn> arguments; // The arguments passed to the procedure.

	public ProcedureCallStmt(String name, ASTList<Expn> arguments){
		this.name = name;
		this.arguments = arguments;
		
		this.arguments.setParent(this);
	}
	
	public ProcedureCallStmt(String name){
		this.name = name;
		this.arguments = new ASTList<Expn>();
		
		this.arguments.setParent(this);
	}
	
	/** Returns a string describing the procedure call. */
	@Override
	public String toString() {
		if (arguments!=null)
			return "Procedure call: " + name + " (" + arguments + ")";
		else
			return "Procedure call: " + name + " ";
	}

	public ASTList<Expn> getArguments() {
		return arguments;
	}

	public void setArguments(ASTList<Expn> args) {
		this.arguments = args;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {

	    RoutineSymbol routineSym = null;

	    try {
	        routineSym = (RoutineSymbol) symbols.getSymbol(this.getName());
	        if (routineSym == null) {
	            throw new ClassCastException();
            }
        } catch (ClassCastException e) {
	        // S41 - check that the identifier has been declared as a procedure.
            SemanticError.add(this, "Identifier has not been declared as a function/procedure");
        }

        if (routineSym != null) {

	    	// S41 - routineSym needs to be declared as a procedure
            if (routineSym.isFunction()) {
                SemanticError.addIdentNotDeclaredError(41, this);
            }

            // part of S43 - checks if any arguments are passed.
            if (this.getArguments() == null && routineSym.getParams() != null) {
                SemanticError.add(43, this, "Calling procedure without arguments");
            }

            // S42 - Checks that procedure does not have parameters
            if (this.getArguments() != null && routineSym.getParams() == null) {
                SemanticError.add(42, this, "Procedure does not have parameters");
            }

            // S43, (S45 implicitly) - Check if argument and parameter sizes match
            if (this.getArguments().size() != routineSym.getParamCount()) {
                SemanticError.add(43, this, "Number of arguments and parameters do not match");
            }

            // S36 - Check that type of argument expression matches type of corresponding formal parameter.
            ListIterator<Expn> args = this.getArguments().getIterator();
            ListIterator<Type> params = routineSym.getParams().getIterator();

            while (args.hasNext() && params.hasNext()) {
                Expn arg = args.next();
                Type paramType = params.next();
                arg.checkSemantics(symbols);
                if (arg.getType().getClass() != paramType.getClass()) {
                    SemanticError.add(36, this,"Type of arguments and parameter do not match");
                }
            }
        }

	}
}
