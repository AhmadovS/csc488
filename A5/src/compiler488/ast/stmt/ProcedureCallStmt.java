 
package compiler488.ast.stmt;

import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
import compiler488.ast.type.Type;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.RoutineSymbol;
import compiler488.symbol.SymbolTable;

/**
 * Represents calling a procedure.
 */
public class ProcedureCallStmt extends Stmt {
	private String name; // The name of the procedure being called.

	private ASTList<Expn> arguments; // The arguments passed to the procedure.

	private RoutineSymbol routineSym;

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

	@Override
	public void doCodeGen(MachineWriter writer) {
	    // Emits codes to set display[$L] to starting word of it's (soob to be) activation record
	    writer.add(Machine.PUSHMT);
	    writer.add(Machine.SETD, routineSym.getLexicLevel());

	    // Emits codes for the four fields of callee's activation record.
        writer.add(Machine.PUSH, Machine.UNDEFINED); // activation record return value
        writer.add(Machine.ADDR, getLexicLevel(), 0); // activation record dynamic link
        writer.add(Machine.PUSH, Machine.UNDEFINED); // activation record return address

        // start counting how many instructions are added,
        // in order to calculate where to branch to when the
        // called procedure returns
        short retAddrLoc = writer.startCountingInstruction();
        writer.add(Machine.ADDR, routineSym.getLexicLevel() - 1, 0); // static link
        

        // Emits codes for the arguments
        getArguments().doCodeGen(writer);

        // Emits code to branch to the callee.
        writer.add(Machine.PUSH, routineSym.getBaseAddr());
        writer.add(Machine.BR);

        // Replaces the return address with the calculated value.
        short numInstructions = writer.stopCountingInstruction();
        writer.replace(retAddrLoc, numInstructions + retAddrLoc + 1);

        // After procedure call returns:
		// Stack :: return-value->dynamic link
        // At this point dynamic link is now on top of the stack, update display 
        writer.add(Machine.SETD, getLexicLevel());
        
        // (Since we called a procedure, return-value is UNDEFINED)
        // Emits code to pop the return-value
        writer.add(Machine.POP);

        // Emits code to update the rest of the display.
        // display of current lexic-level display[$curL] has been already
        // set the procedure returned.
        // We now need to update the rest of the display display[0 to $curL -1]
        // Same as RoutineBody we follow static links to update the display
        int L = getLexicLevel();
        while (L > 0) {
            writer.add(Machine.ADDR, L, 3);  // static link
            writer.add(Machine.LOAD);       // Loads the value of the static link.
            writer.add(Machine.SETD, L - 1);
            L--;
        }

	}
}
