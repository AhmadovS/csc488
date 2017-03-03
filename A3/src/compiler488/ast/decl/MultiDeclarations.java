package compiler488.ast.decl;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import compiler488.symbol.ArraysSymbol;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.VariablesSymbol;

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
	public void checkSemantics(SymbolTable symbols) throws Exception {

		ListIterator<DeclarationPart> li = this.getElements().getIterator();
		while(li.hasNext()){
			DeclarationPart decl =  li.next();

			if (decl instanceof ScalarDeclPart) {
				// S47 - Associate type with variables
				VariablesSymbol sm = new VariablesSymbol(decl.getName(), this.getType());
				symbols.addSymbol(sm);
			} else if (decl instanceof ArrayDeclPart) {
			    // S19, S48 - Declare array varialbe with specified lower and upper bounds.
			    ArrayDeclPart arrayDecl = (ArrayDeclPart) decl;
				ArraysSymbol sm = new ArraysSymbol(arrayDecl.getName(), this.getType(), arrayDecl.getLowerBoundary(), arrayDecl.getLowerBoundary());
				symbols.addSymbol(sm);
			}
		}
		
	}
}
