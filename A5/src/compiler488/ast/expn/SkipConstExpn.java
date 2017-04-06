package compiler488.ast.expn;

import compiler488.ast.Printable;
import compiler488.codegen.MachineWriter;
import compiler488.runtime.Machine;

/**
 * Represents the special literal constant associated with writing a new-line
 * character on the output device.
 */
public class SkipConstExpn extends ConstExpn implements Printable {
	/** Returns the string <b>"skip"</b>. */
	@Override
	public String toString() {
		return "\n";
	}
	
	@Override
	public void doCodeGen(MachineWriter writer) {
		// 10 is the ASCII decimal equivalent of newline
		// When we convert 10 to char, it becomes \n
        writer.add(Machine.PUSH, 10);
	}
}
