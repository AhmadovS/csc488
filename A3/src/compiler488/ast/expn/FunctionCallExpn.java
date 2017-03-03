package compiler488.ast.expn;

import compiler488.ast.ASTList;
import compiler488.ast.type.Type;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;

import java.util.ListIterator;

/**
 * Represents a function call with or without arguments.
 */
public class FunctionCallExpn extends Expn {
	private String ident; // The name of the function.
	private ASTList<Expn> arguments; // The arguments passed to the function.
	
	public FunctionCallExpn(String ident, ASTList<Expn> arguments) {
		this.ident = ident;		
		this.arguments = arguments;
		
		ListIterator<Expn> it = this.arguments.getIterator();
		while(it.hasNext()) {
			it.next().setParent(this);
		}
		
	}

	/** Returns a string describing the function call. */
	@Override
	public String toString() {
		if (arguments!=null) {
			return ident + " (" + arguments + ")";
		}
		else
			return ident + " " ;
	}

	public ASTList<Expn> getArguments() {
		return arguments;
	}

	public void setArguments(ASTList<Expn> args) {
		this.arguments = args;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	@Override
	public void checkSemantics(SymbolTable symbols) throws Exception {

		RoutineSymbol routineSym = null;

		try {
			routineSym = (RoutineSymbol) symbols.getSymbol(this.getIdent());
			if (!routineSym.isFunction()) {
				throw new ClassCastException();
			}
		} catch (ClassCastException e) {
			// S40 - check that the identifier has been declared as a function.
			throw new Exception("Identifier has not been declared as a function");
		}

		// part of S43 - checks if any arguments are passed.
		if(this.getArguments() == null && routineSym.getParams() != null){
			throw new Exception("Calling procedure without arguments");
		}

		// S42 - Checks that function does not have parameters
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

		// S28 - Set result type to declared function type.
		this.setType(routineSym.getType());
	}
}
