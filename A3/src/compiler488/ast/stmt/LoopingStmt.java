package compiler488.ast.stmt;

import java.util.ArrayList;

import compiler488.ast.AST;
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
	public void checkSemantics(AST syntaxTree, SymbolTable symbols, ArrayList<String> errors) {

        // We need to check children expression, so we can know it's type for S30 check.
        this.getExpn().checkSemantics(, symbols, errors);

        // S30 - check if condition expression is boolean
        if (!(this.getExpn().getType() instanceof BooleanType)){
            errors.add("The expression of loops must be boolean");
        }

        // Check semantics on the body (statements) of the loop.
		if (this.getBody() != null){
			this.getBody().checkSemantics(, symbols, errors);
		}
		
		
	}
}
