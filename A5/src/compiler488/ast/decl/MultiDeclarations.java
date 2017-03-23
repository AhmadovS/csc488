package compiler488.ast.decl;

import java.io.PrintStream;
import java.util.ListIterator;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.type.Type;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.ArraysSymbol;
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
		
		this.elements.setParent(this);
	}
	
	public MultiDeclarations(Type type, ASTList<DeclarationPart> elements) {
		super("", type);
		this.elements = elements;
		
		this.elements.setParent(this);
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
    public void checkSemantics(SymbolTable symbols) {

		ListIterator<DeclarationPart> li = this.getElements().getIterator();
		while(li.hasNext()){
			DeclarationPart decl =  li.next();

			// Performing semantic check on each decl.
            decl.checkSemantics(symbols);

			if (decl instanceof ScalarDeclPart) {
				// S10, S47 - Declare scalar variable, Associate type with variable
				VariablesSymbol sm = new VariablesSymbol(decl.getName(), this.getType());
				if (!symbols.addSymbol(sm)) {
				    SemanticError.addIdentAlreadyDeclaredError(this);
                }
			} else if (decl instanceof ArrayDeclPart) {
			    // S19, S48 - Declare array varialbe with specified lower and upper bounds.
			    ArrayDeclPart arrayDecl = (ArrayDeclPart) decl;
				ArraysSymbol sm = new ArraysSymbol(arrayDecl.getName(), this.getType(), arrayDecl.getLowerBoundary(), arrayDecl.getLowerBoundary(), arrayDecl.getSize());
				if (!symbols.addSymbol(sm)) {
                    SemanticError.addIdentAlreadyDeclaredError(this);
                }
			}
		}
		
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
	    ListIterator<DeclarationPart> li = getElements().getIterator();

	    // Iterates over all the declarations.
	    while(li.hasNext()) {

	    	DeclarationPart decl = li.next();

	    	// Calls to emit codes that allocated memory depending on size.
	    	if (decl instanceof ScalarDeclPart) {
	    		// Emits code that allocated storage for a scalar variable
				writer.add(Machine.PUSH, Machine.UNDEFINED);

			} else if (decl instanceof ArrayDeclPart) {
	    		int size = ((ArrayDeclPart) decl).getSize();
	    		// Emits code to allocated storage for the array
				writer.add(Machine.PUSH, Machine.UNDEFINED);
				writer.add(Machine.PUSH, size);
				writer.add(Machine.DUPN);
			}
		}
	}
}
