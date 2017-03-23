package compiler488.ast;

import compiler488.codegen.MachineWriter;
import compiler488.symbol.SymbolTable;
import java.util.ArrayList;

/**
 * This is a placeholder at the top of the Abstract Syntax Tree hierarchy. It is
 * a convenient place to add common behaviour.
 * @author  Dave Wortman, Marsha Chechik, Danny House
 */
public abstract class AST {

	public final static String version = "Winter 2017";
	
	private AST parent = null;
	private ArrayList<AST> children = new ArrayList<AST>();
	private LOC loc = new LOC();

	private int lexicLevel = -1;
	
	// Getters & Setters
	
	public LOC getLOC() {
		return loc;
	}

	public void setLOC(LOC loc) {
		this.loc = loc;
	}

	public AST getParent() {
		return parent;
	}
	
	public void setParent(AST parent) {
		this.parent = parent;
	}
	
	public ArrayList<AST> getChildren() {
		return children;
	}
	
	public void setChildren(ArrayList<AST> children) {
		this.children = children;
	}

    /**
     * Returns nodes lexic-level.
     */
    public int getLexicLevel() {
        if (this.lexicLevel == -1) {
            throw new IllegalStateException("Lexical level is not set");
        }
        return this.lexicLevel;
    }

    protected abstract int calculateLexicLevel();

	abstract public void checkSemantics(SymbolTable symbols);

	public void doCodeGen(MachineWriter writer) {
	    // TODO: change this to abstract later.
    }


}
