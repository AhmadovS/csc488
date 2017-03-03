package compiler488.ast;

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

	abstract public void checkSemantics(SymbolTable symbols) throws Exception;

}
