 
package compiler488.ast.stmt;

import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.Type;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.Symbol;
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
	}
	
	public ProcedureCallStmt(String name){
		this.name = name;
		this.arguments = new ASTList<Expn>();
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
	public void checkSemantics(SymbolTable symbols) throws Exception {

	    RoutineSymbol routineSym = null;

	    try {
	        routineSym = (RoutineSymbol) symbols.getSymbol(this.getName());
	        if (routineSym.isFunction()) {
	            throw new ClassCastException();
            }
        } catch (ClassCastException e) {
	        // S41 - check that the identifier has been declared as a procedure.
	        throw new Exception("Identifier has not been declared as a procedure");
        }

        // part of S43 - checks if any arguments are passed.
        if(this.getArguments() == null && routineSym.getParams() != null){
            throw new Exception("Calling procedure without arguments");
        }

        // S42 - Checks that procedure does not have parameters
        if(this.getArguments() != null && routineSym.getParams() == null){
            throw new Exception("Procedure does not have parameters");
        }

        // S43, (S45 implicitly) - Check if argument and parameter sizes match
        if(this.getArguments().size() != routineSym.getParamCount()){
            throw new Exception("Number of arguments and parameters do not match");
        }

        // S36 - Check that type of argument expression matches type of corresponding formal parameter.
        ListIterator<Expn> args = this.getArguments().getIterator();
        ListIterator<Type> params = routineSym.getParams().getIterator();

        while(args.hasNext() && params.hasNext()){
            Expn arg =  args.next();
            Type paramType = params.next();
            arg.checkSemantics(symbols);
            if (arg.getType().getClass() != paramType.getClass()) {
                throw new Exception("Type of arguments and parameter do not match");
            }
        }

	}
}
