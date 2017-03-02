package compiler488.ast.expn;

import java.util.ArrayList;

import compiler488.ast.AST;
import compiler488.ast.Printable;
import compiler488.ast.type.Type;
import compiler488.symbol.SymbolTable;

/**
 * A placeholder for all expressions.
 */
public class Expn extends AST implements Printable {
	
	public Type type;
	
	public void checkSemantics(SymbolTable symbols, ArrayList<String> errors) {
		
	}
	
	public void setType(Type type){
		this.type = type;
	}
	
	public Type getType(){
		return this.type;
	}
}
