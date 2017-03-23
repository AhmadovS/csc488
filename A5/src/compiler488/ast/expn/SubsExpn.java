package compiler488.ast.expn;

import compiler488.DebugTool;
import compiler488.ast.OPSYMBOL;
import compiler488.ast.Readable;
import compiler488.ast.type.ArrayType;
import compiler488.ast.type.IntegerType;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.ArraysSymbol;
import compiler488.symbol.Symbol;
import compiler488.symbol.SymbolTable;

/**
 * References to an array element variable
 * 
 * Treat array subscript operation as a special form of unary expression.
 * operand must be an integer expression
 */
public class SubsExpn extends UnaryExpn implements Readable {
	
	private String variable; // name of the array variable
    private ArraysSymbol arraySym;
	
    public SubsExpn(Expn operand, String variable) {
    	super(OPSYMBOL.SUB, operand);
    	this.variable = variable;
    }

	/** Returns a string that represents the array subscript. */
	@Override
	public String toString() {
		return (variable + "[" + operand + "]");
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
    public void checkSemantics(SymbolTable symbols) {

		DebugTool.print("SubsExpn: variable: " + variable);

	    // S31 - check return type of operand is integer.
		this.getOperand().checkSemantics(symbols);
		if (!(this.getOperand().getType() instanceof IntegerType)) {
            SemanticError.add(31, this, "Subscript expression must have type integer");
		}

		Symbol sm = symbols.getSymbol(this.getVariable());
		if (sm == null) {
		    SemanticError.addIdentNotDeclaredError(this);
        } else {
            // S38 - Check variable has been declared as an array.
            if (sm.getClass() != ArraysSymbol.class) {
                SemanticError.addIdentNotDeclaredError(38, this);
            }
            // S27 - Set result type to type of the array element.
            this.setType(((ArrayType) sm.getType()).getElementType());

            // Store arraySym for code-generation
            arraySym = (ArraysSymbol) sm;
        }
	}

	@Override
	public void doCodeGen(SymbolTable symbols, MachineWriter writer) {
	    // follows array indexing template
		writer.add(Machine.ADDR, arraySym.getLexicLevel(), arraySym.getOrderNumber());

		// Emits the code for index expression
        getOperand().doCodeGen(symbols, writer);

        writer.add(Machine.PUSH, arraySym.getLowerBound());
        writer.add(Machine.SUB);
        writer.add(Machine.ADD);
        writer.add(Machine.LOAD);

        // Value of SubsExpn should now be on top of stack.
	}
}
