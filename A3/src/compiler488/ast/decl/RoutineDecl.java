package compiler488.ast.decl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.symbol.ParamsSymbol;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;

/**
 * Represents the declaration of a function or procedure.
 */
public class RoutineDecl extends Declaration {
	/*
	 * The formal parameters of the function/procedure and the
	 * statements to execute when the procedure is called.
	 */
	private RoutineBody routineBody;

	/**
	 * Returns a string indicating that this is a function with
	 * return type or a procedure, name, Type parameters, if any,
	 * are listed later by routineBody
	 */
	@Override
	public String toString() {
	  if(type==null)
	    {
	      return " procedure " + name;
	    }
	  else
	    {
	      return " function " + name + " : " + type ;
	    }
	}

	/**
	 * Prints a description of the function/procedure.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		Indentable.printIndentOn(out, depth, this + " ");
		routineBody.printOn(out, depth);
	}

	public RoutineBody getRoutineBody() {
		return routineBody;
	}

	public void setRoutineBody(RoutineBody routineBody) {
		this.routineBody = routineBody;
	}

	@Override
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {
	    // S11 - Declare function with no parameters and specified type.
		// S11, S17, S14,

		// Iterates through the parameters of the body and
		ASTList<Symbol> paramsSyms = new ASTList<>();
		ListIterator li = this.getRoutineBody().getParameters().getIterator();
		while (li.hasNext()) {
		    Declaration param = (Declaration) li.next();
		    paramsSyms.addLast(new ParamsSymbol(param.getName(), param.getType()));
		}
		RoutineSymbol sym = new RoutineSymbol(this.name, this.type, paramsSyms);

		// Adding routine symbol to symbol table
		symbols.addSymbol(sym);
	}
}
