package compiler488.ast.decl;

import java.io.PrintStream;
import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import compiler488.symbol.RoutineSymbol;
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
	
	public RoutineDecl(String name, Type type, RoutineBody routineBody) {
		super(name, type);
		this.routineBody = routineBody;
	}
	
	public RoutineDecl(String name, RoutineBody routineBody) {
		super(name, null);
		this.routineBody = routineBody;
	}

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
	public void checkSemantics(SymbolTable symbols) {

		// TODO: check if the symbol table already contains declaration
        // should this be the job of SymbolTable.addSymbol()?

		// Iterates through the parameters of the body and gets their type
		ASTList<Type> paramsTypes = new ASTList<>();
		ListIterator li = this.getRoutineBody().getParameters().getIterator();
		while (li.hasNext()) {
		    Declaration param = (Declaration) li.next();
		    paramsTypes.addLast(param.getType());
		}
		RoutineSymbol sym = new RoutineSymbol(this.name, this.type, paramsTypes);

		// S11, S12, S17, S18 - Adding routine symbol to symbol table
		symbols.addSymbol(sym);

        // Calls semantics check on the body
        this.routineBody.checkSemantics(symbols);
	}
}
