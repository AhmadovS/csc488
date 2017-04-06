package compiler488.ast.expn;

import compiler488.ast.Printable;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;

/**
 * Represents a literal text constant.
 */
public class TextConstExpn extends ConstExpn implements Printable {
	private String value; // The value of this literal.
	
	public TextConstExpn(String value) {
		this.value = value;
	}


	/** Returns a description of the literal text constant. */
	@Override
	public String toString() {
		return "\"" + value + "\"";
	}
	
	public int getLength() {
		return this.value.length();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void doCodeGen(MachineWriter writer) {
		// Because the machine is a stack machine, it is first in last out
		// So, we need to push the text in a reverse order so it pops out 
		// in the correct order. We also convert the char to a short 
		// ASCII decimal representation
		for(int i = this.value.length()-1; 0 <= i; i--) {
			writer.add(Machine.PUSH, (short) this.value.charAt(i));
		}
	}
}
