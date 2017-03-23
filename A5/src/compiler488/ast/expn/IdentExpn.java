package compiler488.ast.expn;

import compiler488.DebugTool;
import compiler488.ast.Readable;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.ArraysSymbol;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.VariablesSymbol;

/**
 *  References to a scalar variable.
 */
public class IdentExpn extends Expn implements Readable {
    private String ident;  	// name of the identifier
    private VariablesSymbol varSym;
    
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
	public void checkSemantics(SymbolTable symbols) {
		// S25, S26 - set result type to type of variablename
		DebugTool.print("IdentExpn: " + ident);
		Symbol sym = symbols.getSymbol(this.getIdent());
		if (sym != null) {
			this.setType(sym.getType());
			varSym = (VariablesSymbol)sym;
		} else {
			SemanticError.addIdentNotDeclaredError(this);
		}
//		DebugTool.print("IdentExpn: " + sym.toString());
	}
	@Override
	public void doCodeGen(MachineWriter writer) {
		
		writer.add(Machine.ADDR, varSym.getLexicLevel(), varSym.getOrderNumber());
		writer.add(Machine.LOAD);

	}
}
