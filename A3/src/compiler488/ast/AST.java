package compiler488.ast;

import java.util.ArrayList;

/**
 * This is a placeholder at the top of the Abstract Syntax Tree hierarchy. It is
 * a convenient place to add common behaviour.
 * @author  Dave Wortman, Marsha Chechik, Danny House
 */
public class AST {

	public final static String version = "Winter 2017";
	
	private AST parent = null;
	private ArrayList<AST> children = new ArrayList<AST>();
	
	// Getters & Setters
	
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

}
