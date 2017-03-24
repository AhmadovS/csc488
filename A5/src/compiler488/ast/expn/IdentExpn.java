package compiler488.ast.expn;

import compiler488.DebugTool;
import compiler488.ast.Readable;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.*;

/**
 *  References to a scalar variable.
 */
public class IdentExpn extends Expn implements Readable {
    private String ident;  	// name of the identifier

    // If identifier is a variable, varSym is not null.
    // If identifier is a function call, routineSymbol is not null.
    private VariablesSymbol varSym;
    private RoutineSymbol routineSymbol;
    
    public IdentExpn(String ident) {
    	this.ident = ident;
    }

    /**
     * Returns the name of the variable or function.
     */
    @Override
	public String toString () { 
    	return ident;
    }

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) throws Exception {
		// S25, S26 - set result type to type of variablename
		DebugTool.print("IdentExpn: " + ident);
		Symbol sym = symbols.getSymbol(this.getIdent());
		if (sym != null) {
            this.setType(sym.getType());
			if (sym instanceof VariablesSymbol) {
				varSym = (VariablesSymbol) sym;
			} else if (sym instanceof RoutineSymbol){
			    routineSymbol = (RoutineSymbol) sym;
			}
		} else {
			SemanticError.addIdentNotDeclaredError(this);
		}
//		DebugTool.print("IdentExpn: " + sym.toString());
	}
	@Override
	public void doCodeGen(MachineWriter writer) {
        if (varSym != null) {
            // Loads the address of the variable onto stack
            writer.add(Machine.ADDR, varSym.getLexicLevel(), varSym.getOrderNumber());
            // Loads the value of the variable.
            writer.add(Machine.LOAD);
        } else {
            // Code below is largely same as FunctionCallExpn
            // Ideally we would implement a class specializing in code emitting, to prevent code duplication,
            // but this language is too simple to be worth the effort for refactoring.

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
}
