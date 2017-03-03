package compiler488.symbol;

import java.util.*;
import compiler488.DebugTool;

/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *  
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> Samud Ahmadov</B>
 *  @author <B>Amir Hossein Heidari Zadi</B>
 */

/**
 * Global symbol table that contains a stack symbol tables
 */
public class SymbolTable {
	
	/** String used by Main to print symbol table
         *  version information.
         */

	public final static String version = "Winter 2017" ;
	
	private Stack<HashMap<String, Symbol>> symbolTable;

	/** Symbol Table  constructor
         *  Create and initialize a symbol table 
	 */
	public SymbolTable  (){
		symbolTable = new Stack<>();
	}
	
	public void startScope(){
		symbolTable.push(new HashMap<>());
	}
	
	public void exitScope(){
		symbolTable.pop();
	}

    /**
     * Traverses the scopes of SymbolTable from top to bottom,
     * and returns earliest declaration found.
     * @param name Identifier name
     */
	public Symbol getSymbol(String name) throws Exception{
	    ListIterator<HashMap<String, Symbol>> li = symbolTable.listIterator(symbolTable.size());
	    while(li.hasPrevious()) {
	        HashMap<String, Symbol> scopeTable = li.previous();
	        Symbol sym = scopeTable.get(name);
	        if (sym != null)
                return sym;
        }
        throw new Exception(String.format("Identifier (%s) has not been declared", name));
	}

    /**
     * Adds symbol to current scope (i.e. the last scope)
     * @param sm Symbol to add to current scope.
     */
	public void addSymbol(Symbol sm) throws Exception {

	    DebugTool.print("Adding symbol: " + sm.toString());

        HashMap<String, Symbol> currentScope = symbolTable.peek();

        if (currentScope.get(sm.getName()) != null) {
            throw new Exception(String.format("Identifier with name (%s) has already been declared", sm.getName()));
        }

        currentScope.put(sm.getName(), sm);
	}

    @Override
    public String toString() {
	    String output = "";

        ListIterator<HashMap<String, Symbol>> li = symbolTable.listIterator(symbolTable.size());
        while(li.hasPrevious()) {
            HashMap<String, Symbol> scopeTable = li.previous();
            output += "ScopeTable: " + scopeTable.toString() + "\n";
        }

        return output;
    }
}
