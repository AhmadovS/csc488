package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;

/**
 *  References to a scalar variable.
 */
public class IdentExpn extends Expn implements Readable {
    private String ident;  	// name of the identifier
    
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
		Symbol sym = symbols.getSymbol(this.getIdent());
		this.setType(sym.getType());
	}
}
