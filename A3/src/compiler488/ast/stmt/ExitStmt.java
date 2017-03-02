package compiler488.ast.stmt;

import java.util.ArrayList;

import compiler488.ast.expn.*;
import compiler488.ast.type.BooleanType;
import compiler488.symbol.SymbolTable;

/**
 * Represents the command to exit from a loop.
 */

public class ExitStmt extends Stmt {

	// condition for 'exit when'
    private Expn expn = null;
	private Integer level = -1 ;
	
	public ExitStmt(Expn expn, Integer level) {
		this.expn = expn;
		this.level = level;
	}
	
	public ExitStmt(Expn expn) {
		this.expn = expn;
	}
	
	public ExitStmt(Integer level) {
		this.level = level;
	}

	/** Returns the string <b>"exit"</b> or <b>"exit when e"</b>" 
            or  <b>"exit"</b> level  or  <b>"exit"</b> level  when e 
	*/
	@Override
	public String toString() {
		  {
		    String stmt = "exit " ;
	 	    if( level >= 0 )
			stmt = stmt + level + " " ;
                    if( expn != null )
		        stmt = stmt + "when " + expn + " " ;
		    return stmt ;
		  }
	}

	public Expn getExpn() {
		return expn;
	}

	public void setExpn(Expn expn) {
		this.expn = expn;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {
		// TODO S50 (check inside of loop), S53
		
		if(this.expn != null){
			this.expn.checkSemantics(symbols, errors);
			
			if(!(this.expn.getType() instanceof BooleanType)){
				errors.add("Expression of exit must be boolean");
			}
		}
		
	}

}
