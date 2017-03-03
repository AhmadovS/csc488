package compiler488.ast.decl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ListIterator;

import compiler488.ast.AST;
import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import compiler488.symbol.SymbolTable;

/**
 * Holds the declaration of multiple elements.
 */
public class MultiDeclarations extends Declaration {
	/* The elements being declared */
	private ASTList<DeclarationPart> elements;

	public MultiDeclarations(String name, Type type, ASTList<DeclarationPart> elements) {
		super(name, type);
		this.elements = elements;
	}
	
	public MultiDeclarations(Type type, ASTList<DeclarationPart> elements) {
		super("", type);
		this.elements = elements;
	}
	
	/**
	 * Returns a string that describes the array.
	 */
	@Override
	public String toString() {
		return  " : " + type ;
	}


	/**
	 * Print the multiple declarations of the same type.
	 * 
	 * @param out
	 *            Where to print the description.
	 * @param depth
	 *            How much indentation to use while printing.
	 */
	@Override
	public void printOn(PrintStream out, int depth) {
		out.println(elements);
		Indentable.printIndentOn (out, depth, this + " ");
	}

	public ASTList<DeclarationPart> getElements() {
		return elements;
	}

	public void setElements(ASTList<DeclarationPart> elements) {
		this.elements = elements;
	}

	@Override
	public void checkSemantics(AST syntaxTree, SymbolTable symbols, ArrayList<String> errors){

		ListIterator li = this.getElements().getIterator();
		
		while(li.hasNext()){
			DeclarationPart decl =  (DeclarationPart) li.next(); 
			decl.checkSemantics(, symbols, errors);

			// TODO: each implementation of DeclarationPart already checks for this
			if(symbols.getSymbol(decl.getName()) != null){
				errors.add("Variable has already been declared");
			}
			// TODO add multiple decls to symbol table (is this needed?)
		}
		
	}
}
