package compiler488.ast.stmt;

import java.util.ArrayList;

import compiler488.ast.AST;
import compiler488.ast.expn.*;
import compiler488.ast.type.BooleanType;
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
    }

    public ExitStmt(Expn expn) {
        this.expn = expn;
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
    public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {

        if (this.getLevel() < 1) {
            errors.add("Invalid integer exit statement");
        }

        // Counts number of parents loops
        int parentLoopsCount = 0;
        AST currentNode = this.getParent();
        while (currentNode != null && parentLoopsCount < this.getLevel()) {
            // For each parent loop node found, increment parentLoopsCount
            if (currentNode instanceof LoopingStmt) {
                parentLoopsCount++;
            }
            // Go up the tree!
            currentNode = currentNode.getParent();
        }

        // S50, S53 - check that exit statement is in correct number of loop.
        if (parentLoopsCount == 0) {
            errors.add("Exit statement is not contained in a loop statement");
        } else if (parentLoopsCount != this.getLevel()) {
            errors.add("Invalid Exit statement integer");
        }


        // If expression is present, check if it is bool S30
        if(this.expn != null){
            this.expn.checkSemantics(symbols, errors);

            // S30 - checks that type of expression is boolean.
            if(!(this.expn.getType() instanceof BooleanType)){
                errors.add("Expression of exit must be boolean");
            }
        }

    }

}
