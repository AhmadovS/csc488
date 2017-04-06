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
	
//	public short getLexicalLevel() {
//		return (short) (this.varSym.getLexicLevel());
//	}

	public short getOrderNumber() {
		return (short) (this.varSym.getOrderNumber());
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
        // Either IdentExpn is a variable or a function call
        if (varSym != null) {
            // Loads the address of the variable onto stack
            writer.add(Machine.ADDR, varSym.getLexicLevel(), varSym.getOrderNumber());
            // Loads the value of the variable.
            writer.add(Machine.LOAD);
        } else {
			// DebugTool.print(String.format("IdentExpn lexel-levl=%d", getLexicLevel()));
            // Emits code that creates activation record and branches to function
            writer.emitCodeRoutineCall(getLexicLevel(), routineSymbol, null);
        }

	}
}
