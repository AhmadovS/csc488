 
package compiler488.ast.stmt;

import java.util.ArrayList;
import java.util.ListIterator;

import compiler488.ast.AST;
import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
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
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {

		if(symbols.getSymbol(this.name) == null){
			errors.add("Procedure has not been declared");
		}else if(!(symbols.getSymbol(this.name) instanceof RoutineSymbol)){
			errors.add("The identifier has not beed declared as procedure");
		}else{
			
			RoutineSymbol proc = (RoutineSymbol) symbols.getSymbol(this.name);
			
			//Check if argument and parameter sizes match			
			if(this.getArguments() == null && proc.getParams() != null){
				errors.add("Calling procedure without arguments");
			}
			
			if(this.getArguments() != null && proc.getParams() == null){
				errors.add("Procedure does not have parameters");
			}
			
			if(this.getArguments().size() != proc.getParamCount()){
				errors.add("Number of arguments and parameters do not match");
			}
			ListIterator args = this.getArguments().getIterator(); 
			ListIterator params = proc.getParams().getIterator();
			
			while(args.hasNext() && params.hasNext()){
				Expn arg = (Expn) args.next();
				arg.checkSemantics(symbols,errors); 				
				if(!arg.getType().toString().equals(params.next().toString())){
					errors.add("Type of arguments and parameter do not match");
				}	
			}
		}
		
	}
}
