package compiler488.symbol;

import java.util.*;
import compiler488.DebugTool;
import compiler488.semantics.SemanticError;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

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

	private static class ScopeTable {
		RoutineSymbol owner;
		// A linked hash map is required by the {@link calculateOrderNumber}
		// function, to guarantee iteration is ordered by insertion order.
		LinkedHashMap<String, Symbol> map;

		public ScopeTable(RoutineSymbol owner) {
			this.owner = owner;
			this.map = new LinkedHashMap<>();
		}
	}
	
	/** String used by Main to print symbol table
         *  version information.
         */

	public final static String version = "Winter 2017" ;
	
	private Stack<ScopeTable> symbolTable;

	/** Symbol Table  constructor
         *  Create and initialize a symbol table 
	 */
	public SymbolTable (){
		symbolTable = new Stack<>();
	}
	
	public void startScope(){
		symbolTable.push(new ScopeTable(null));
	}

	/**
	 * Creates a new scope with an owner.
	 * @param owner
	 */
	public void startScope(RoutineSymbol owner) {
		symbolTable.push(new ScopeTable(owner));
	}
	
	public void exitScope(){
		symbolTable.pop();
	}

    /**
     * Traverses the scopes of SymbolTable from top to bottom,
     * and returns earliest declaration found.
     * @param name Identifier name
     * @return returns Symbol if found, null otherwise (if Symbol has not been declared in enclosing scopes).
     */
	public Symbol getSymbol(String name){
		ListIterator<ScopeTable> sli = symbolTable.listIterator(symbolTable.size());
		while(sli.hasPrevious()) {
			Map<String, Symbol> scopeTable = sli.previous().map;
			Symbol sym = scopeTable.get(name);
			if (sym != null) {
				// sym is null if it's not defined only in this scopeTable
				// have to check previous scopeTables in the stack first.
				return sym;
			}
		}
		return null;
	}

    /**
     * Adds symbol to current scope (i.e. the last scope)
     * @param sm Symbol to add to current scope.
     * @return returns true if addSymbol was successful, false otherwise.
     */
	public boolean addSymbol(Symbol sm){


        Map<String, Symbol> currentScope = symbolTable.peek().map;

        if (currentScope.get(sm.getName()) != null) {
            return false;
        }

        if (sm instanceof VariablesSymbol) {
			((VariablesSymbol) sm).setLexicLevel(calculateLexicLevel());
			((VariablesSymbol) sm).setOrderNumber(calculateOrderNumber());
		}

		DebugTool.print("Adding symbol: " + sm.toString());

        currentScope.put(sm.getName(), sm);
        return true;
	}

	/**
	 * Calculates lexic level of top scope.
	 */
	private int calculateLexicLevel() {
		return symbolTable.size() - 1;
	}

	/**
	 * Calculates the order number of new {@link VariablesSymbol}.
	 * IMPORTANT: Must call this function <b>before</b> adding it to the symbol table.
	 * @return Order number of variable that is to be added to symbol table.
	 */
	private int calculateOrderNumber() {
		ScopeTable currentScope = symbolTable.peek();
		boolean isMainScope = symbolTable.size() == 1;

		// Below we iterate over the LinkedHashMap to calculate ON, this is
		// guaranteed to be well-defined, since we'll be iterating over the
		// values of symbol table in order of insertion.
		int scopeAllocCount = 0;

		for (Symbol sym : currentScope.map.values()) {
			if (sym instanceof ArraysSymbol) {
				// Increment by storage requirement of the array
				scopeAllocCount += ((ArraysSymbol) sym).getSize();
			} else if (sym instanceof VariablesSymbol) {
				scopeAllocCount ++;
			}
		}
		if (isMainScope) {
			// Main scope has the legacy of starting it's order number from 2.
			return 2 + scopeAllocCount;
		} else {
			if (currentScope.owner == null) {
			    // if scope is not the main scope, it needs to have a function/procedure owner
				// anonymous scopes '{}' should not need to call this function, since in the
				// generated code, their values are in the same activation record as their
				// ancestor which is either main scope or a function/procedure.
				throw new IllegalStateException("Scope owner is null");
			}

			// Routine activation records' local variables ON starts after parameters.
			RoutineSymbol sym = currentScope.owner;
			return 4 + sym.getParamCount() + scopeAllocCount;
		}
	}
}
