package compiler488.ast.stmt;

import compiler488.ast.expn.Expn;
import compiler488.ast.type.BooleanType;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

import java.util.Arrays;

/**
 * Represents the common parts of loops.
 */
public abstract class LoopingStmt extends Stmt {
    protected Stmt body;	  // body of the loop
    protected Expn expn;          // Loop condition
	protected short[] exitAddrToBePatched = {};	// List of addresses (used by exit statements) to be patched
    
    public LoopingStmt(Expn expn, Stmt body) {
    	this.body = body;
    	this.expn = expn;
		
		this.expn.setParent(this);
		this.body.setParent(this);
    }

	public Expn getExpn() {
		return expn;
	}

	public void setExpn(Expn expn) {
		this.expn = expn;
	}

	public Stmt getBody() {
		return body;
	}

	public void setBody(Stmt body) {
		this.body = body;
	}
	
	public void addExitAddr(short addr) {
		exitAddrToBePatched = Arrays.copyOf(exitAddrToBePatched, exitAddrToBePatched.length + 1);
		exitAddrToBePatched[exitAddrToBePatched.length - 1] = addr;
	}
	
	@Override
    public void checkSemantics(SymbolTable symbols) throws Exception {
        // We need to check children expression, so we can know it's type for S30 check.
        this.getExpn().checkSemantics(symbols);

        // S30 - check if condition expression is boolean
        if (!(this.getExpn().getType() instanceof BooleanType)){
            SemanticError.add(30, this, "The expression of loops must be boolean");
        }

        // Check semantics on the body (statements) of the loop.
        if (this.getBody() != null){
			this.getBody().checkSemantics(symbols);
		}
	}
}
