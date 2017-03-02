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

	abstract public void checkSemantics(SymbolTable symbols, ArrayList<String> errors);

}
