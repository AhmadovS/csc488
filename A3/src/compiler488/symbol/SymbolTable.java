package compiler488.symbol;

import java.io.*;
import java.util.*;

/** Symbol Table
 *  This almost empty class is a framework for implementing
 *  a Symbol Table class for the CSC488S compiler
 *  
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  <B> Samud Ahmadov</B>
 */

class ScopeSymbol{
	
	HashMap<String, Symbol> symbols;
	
	public ScopeSymbol(){
		symbols = new HashMap<String, Symbol>();

	}
	
	public void addSymbol(String name, Symbol value){
		symbols.put(name, value);
	}
	
	public Symbol getSymbol(String name){
		return symbols.get(name);
	}
	
}

public class SymbolTable {
	
	/** String used by Main to print symbol table
         *  version information.
         */

	public final static String version = "Winter 2017" ;
	
	public ArrayDeque<ScopeSymbol> symbolTable;

	/** Symbol Table  constructor
         *  Create and initialize a symbol table 
	 */
	public SymbolTable  (){
		symbolTable = new ArrayDeque<ScopeSymbol>();	
	}
	
	public void startScope(){
		symbolTable.push(new ScopeSymbol());
	}
	
	public void exitScope(){
		symbolTable.pop();
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
