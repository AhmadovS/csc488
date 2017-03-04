package compiler488.ast.stmt;

import compiler488.DebugTool;
import compiler488.ast.AST;
import compiler488.ast.expn.*;
import compiler488.ast.type.BooleanType;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Represents the command to exit from a loop.
 */

public class ExitStmt extends Stmt {

    // condition for 'exit when'
    private Expn expn = null;

    // Implicit value of level is always 1, unless set by parser.
    private Integer level = 1 ;

    public ExitStmt(Expn expn, Integer level) {
        this.expn = expn;
        this.level = level;
		
		this.expn.setParent(this);
    }

    public ExitStmt(Expn expn) {
        this.expn = expn;
		
		this.expn.setParent(this);
    }

    public ExitStmt(Integer level) {
        this.level = level;
    }

    public ExitStmt() {

    }

    /** Returns the string <b>"exit"</b> or <b>"exit when e"</b>"
            or  <b>"exit"</b> level  or  <b>"exit"</b> level  when e 
    */
    @Override
    public String toString() {
        String stmt = "exit " ;
        if( level >= 0 )
            stmt = stmt + level + " " ;
        if( expn != null )
            stmt = stmt + "when " + expn + " " ;
        return stmt ;
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
    public void checkSemantics(SymbolTable symbols) {

        if (this.getLevel() < 1) {
            SemanticError.add(53,this, "Exit integer must be > 0");
        }

        // Counts number of parents loops
        int parentLoopsCount = 0;
        AST parentNode = this.getParent();

        if (parentNode == null) {
            DebugTool.print("exit parent node is null");
        }

        while (parentNode != null && parentLoopsCount < this.getLevel()) {
            // For each parent loop node found, increment parentLoopsCount
            if (parentNode instanceof LoopingStmt) {
                parentLoopsCount++;
            }
            // Go up the tree!
            parentNode = parentNode.getParent();
        }

        // S50, S53 - check that exit statement is in correct number of loop.
        if (parentLoopsCount == 0) {
            SemanticError.add(50, this, "Exit statement is not contained in a loop statement");
        } else if (parentLoopsCount != this.getLevel()) {
            SemanticError.add(53, this, "Exit integer is more than number of contained loops");
        }

        // If expression is present, check if it is bool S30
        if(this.expn != null){
            this.expn.checkSemantics(symbols);

            // S30 - checks that type of expression is boolean.
            if(!(this.expn.getType() instanceof BooleanType)){
                SemanticError.add(30, this,"Expression of exit must be boolean");
            }
        }

    }

}
