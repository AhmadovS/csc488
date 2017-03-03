package compiler488.symbol;

import java.util.*;

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
     * and returns earliest declaration found, otherwise null.
     * @param name Identifier name
     * @return Returns null of no symbol is found.
     */
	public Symbol getSymbol(String name){
	    ListIterator<HashMap<String, Symbol>> li = symbolTable.listIterator();
	    while(li.hasPrevious()) {
	        HashMap<String, Symbol> scopeTable = li.previous();
	        Symbol sym = scopeTable.get(name);
	        if (sym != null)
                return sym;
        }
        return null;
	}

    /**
     * Adds symbol to current scope (i.e. the last scope)
     * @param sm Symbol to add to current scope.
     * @return true if symbol was added successfully, false otherwise.
     */
	public boolean addSymbol(Symbol sm){
        HashMap<String, Symbol> currentScope = symbolTable.peek();

        if (currentScope.get(sm.getName()) != null) {
            return false;
        }

        currentScope.put(sm.getName(), sm);
        return true;
	}

	/**  Initialize - called once by semantic analysis  
	 *                at the start of  compilation     
	 *                May be unnecessary if constructor
 	 *                does all required initialization	
	 */
	public void Initialize() {
	
	   /**   Initialize the symbol table             
	    *	Any additional symbol table initialization
	    *  GOES HERE                                	
	    */
	   
	}

	/**  Finalize - called once by Semantics at the end of compilation
	 *              May be unnecessary 		
	 */
	public void Finalize(){
	
	  /**  Additional finalization code for the 
	   *  symbol table  class GOES HERE.
	   *  
	   */
	}
	

	/** The rest of Symbol Table
	 *  Data structures, public and private functions
 	 *  to implement the Symbol Table
	 *  GO HERE.				
	 */

}
