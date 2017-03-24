package compiler488.ast.expn;

import compiler488.ast.ASTList;
import compiler488.ast.type.Type;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.SymbolTable;

import java.util.ListIterator;

/**
 * Represents a function call with or without arguments.
 */
public class FunctionCallExpn extends Expn {
	private String ident; // The name of the function.
	private ASTList<Expn> arguments; // The arguments passed to the function.
	private RoutineSymbol routineSymbol;
	
	public FunctionCallExpn(String ident, ASTList<Expn> arguments) {
		this.ident = ident;		
		this.arguments = arguments;
		this.arguments.setParent(this);
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
    public void checkSemantics(SymbolTable symbols) {

		RoutineSymbol routineSym = null;

		try {
			routineSym = (RoutineSymbol) symbols.getSymbol(this.getIdent());
			if (routineSym == null) {
			    throw new ClassCastException();
			}
		} catch (ClassCastException e) {
			// S40 - check that the identifier has been declared as a function.
			SemanticError.add(this, "Identifier has not been declared as a function/procedure");
		}

		// NOTE: Ignoring variable that is not declared, might have a cascading
		//       side effect, this is only per piazza post @90 to ignore all
		//       errors and just collect them.
		if (routineSym != null) {

			if (!routineSym.isFunction()) {
				// S40 - check that the identifier has been declared as a function.
				SemanticError.addIdentNotDeclaredError(40, this);
			}

			// part of S43 - checks if any arguments are passed.
			if (this.getArguments() == null && routineSym.getParams() != null) {
				SemanticError.add(43, this, "Calling procedure without arguments");
			}

			// S42 - Checks that function does not have parameters
			if (this.getArguments() != null && routineSym.getParams() == null) {
				SemanticError.add(42, this, "Procedure does not have parameters");
			}

			// S43, (S45 implicitly) - Check if argument and parameter sizes match
			if (this.getArguments().size() != routineSym.getParamCount()) {
				SemanticError.add(43, this,"Number of arguments and parameters do not match");
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

			// S28 - Set result type to declared function type.
			this.setType(routineSym.getType());
			routineSymbol = routineSym;
		}
	}
	
	@Override
	public void doCodeGen(MachineWriter writer) {
		
		// Emits codes to set display[$L] to starting word of it's (soob to be) activation record
	    writer.add(Machine.PUSHMT);
	    writer.add(Machine.SETD, routineSymbol.getLexicLevel());

	    // Emits codes for the four fields of callee's activation record.
        writer.add(Machine.PUSH, Machine.UNDEFINED); // return value
        
        writer.add(Machine.ADDR, routineSymbol.getLexicLevel() - 1, 0); // dynamic link
        
        writer.add(Machine.PUSH, Machine.UNDEFINED); // return address

        // Start counting number of instructions
        short retAddrLoc = writer.startCountingInstruction();

        writer.add(Machine.ADDR, getLexicLevel(), 0); // static link

        // Emits codes for the arguments
        getArguments().doCodeGen(writer);

        // Emits code to branch to the callee.
        writer.add(Machine.PUSH, routineSymbol.getBaseAddr());
        writer.add(Machine.BR);

        // Replaces the return address with the calculated value.
        short numInstructions = writer.stopCountingInstruction();
        writer.replace(retAddrLoc, numInstructions + retAddrLoc + 1);

		// After function call returns:
		// Stack :: return-value -> dynamic link

		// Emits code to update the rest of the display.
		// display of current lexic-level display[$curL] has been already
		// set the procedure returned.
		// We now need to update the rest of the display display[0 to $curL -1]
		// Same as RoutineBody we follow static links to update the display
		int L = getLexicLevel();
		while (L > 0) {
			writer.add(Machine.ADDR, L, 3); // static link
            writer.add(Machine.LOAD);       // Loads the value of the static link.
			writer.add(Machine.SETD, L - 1);
			L--;
		}

		// At this point return-value of the function:
		// Stack :: return-value -> dynamic link
		
		// Dynamic link is now on top of the stack, update display 
		writer.add(Machine.SETD, getLexicLevel());				
		
		// At this point return-value is now on top of the stack
	}
}
