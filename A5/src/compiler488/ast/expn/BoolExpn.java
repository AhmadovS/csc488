package compiler488.ast.expn;

import compiler488.ast.OPSYMBOL;
import compiler488.ast.type.*;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;
import compiler488.semantics.SemanticError;
import compiler488.symbol.SymbolTable;

/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {
	
	public BoolExpn(String opSymbol, Expn left, Expn right) {
    	super(opSymbol, left, right);
    }
	
	public void checkSemantics(SymbolTable symbols) throws Exception {

		// Semantics check on children must be performed before getting their type.
        this.getLeft().checkSemantics(symbols);
        this.getRight().checkSemantics(symbols);

		// S30 - checks left expression is boolean
		if(!(this.getLeft().getType() instanceof BooleanType)){
			SemanticError.add(30, this,"Left side of boolean operation must be boolean");
		}

		// S30 - checks right expression is boolean
		if(!(this.getRight().getType() instanceof BooleanType)){
			SemanticError.add(30, this, "Right side of boolean operation must be boolean");
		}

		// S20 - sets result type to boolean
		this.setType(new BooleanType());
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		switch(this.getOpSymbol()) {
			case OPSYMBOL.AND:
				this.getLeft().doCodeGen(writer);
				writer.add(Machine.PUSH, Machine.MACHINE_FALSE);
				writer.add(Machine.EQ);

				this.getRight().doCodeGen(writer);
				writer.add(Machine.PUSH, Machine.MACHINE_FALSE);
				writer.add(Machine.EQ);

				writer.add(Machine.OR);
				writer.add(Machine.PUSH, Machine.MACHINE_FALSE);
				writer.add(Machine.EQ);
				break;
			case OPSYMBOL.OR:
				this.getLeft().doCodeGen(writer);
				this.getRight().doCodeGen(writer);
				writer.add(Machine.OR);
				break;
		}
	}
}
