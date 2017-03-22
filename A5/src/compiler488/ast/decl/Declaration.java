package compiler488.ast.decl;

import java.util.ArrayList;

import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;

/**
 * The common features of declarations.
 */
public abstract class Declaration extends Indentable {

	/** The type of thing being declared. */
	protected Type type=null;

	/** The name of the thing being declared. */
	protected String name;

	public Symbol sym;
	
	public Declaration(String name, Type type) {
		this.name = name;
		this.type = type;
		
		if (this.type != null)
			this.type.setParent(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Symbol getSymbol() {
		return this.sym;
	}

	public void setSymbol(Symbol sym) {
		this.sym = sym;
	}

}
