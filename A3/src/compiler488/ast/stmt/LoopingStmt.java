package compiler488.ast.stmt;

import java.util.ArrayList;

import compiler488.ast.expn.Expn;
import compiler488.ast.type.BooleanType;
import compiler488.symbol.SymbolTable;


/**
 * Represents the common parts of loops.
 */
public abstract class LoopingStmt extends Stmt {
    protected Stmt body;	  // body of ther loop
    protected Expn expn;          // Loop condition
    
    public LoopingStmt(Expn expn, Stmt body) {
    	this.body = body;
    	this.expn = expn;
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
	
	@Override
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {
		
		if(this.getExpn() != null){
		
			this.getExpn().checkSemantics(symbols, errors);
			
			if (!(this.getExpn().getType() instanceof BooleanType)){
				errors.add("The expression of loops must be boolean");
			}
		}

		if (this.getBody() != null){
			this.getBody().checkSemantics(symbols, errors);
		}
		
		
	}
}
