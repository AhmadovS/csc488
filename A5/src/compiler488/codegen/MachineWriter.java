package compiler488.codegen;

import compiler488.ast.ASTList;
import compiler488.ast.expn.Expn;
import compiler488.runtime.ExecutionException;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.symbol.RoutineSymbol;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Stack;


public final class MachineWriter {

    private static MachineWriter _instance;

    // Next available free memory address on the machine to write to.
    private short nextAddr = 0;

    // Stack of counters used by different programs.
    private Stack<Short> instructionCounter = new Stack<>();

    private MachineWriter() {}

    public static MachineWriter getInstance() {
        if (_instance == null) {
            _instance = new MachineWriter();
        }
        return _instance;
    }

    /**
     * Adds a new counter to the instructionCounter stack.
     * @return Returns the address of last instruction written to memory.
     */
    public short startCountingInstruction() {
        instructionCounter.push(nextAddr);
        return (short) (nextAddr - 1);
    }

    public short stopCountingInstruction() {
        short count = (short) (nextAddr - instructionCounter.pop());
        return count;
    }

    /**
     * Replaces current value at memory 'address' with provided value.
     */
    public void replace(int address, int value) {
        try {
            Machine.writeMemory((short) address, (short) value);
        } catch (MemoryAddressException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public short getNextAddr() {
        return (short) (nextAddr);
    }
    
    public short getPrevAddr() {
    	return (short) (nextAddr-1);
    }

    /**
     * Simple method for writing instructions to the next
     * available free memory slot.
     * @param values values to write to the memory (from left to right).
     */
    public void add(short... values) {
        if (values.length == 0) {
            throw new RuntimeException("Can't pass empty values");
        }

        // Writes all the values to the machine memory
        for (short val : values) {
            try {
                Machine.writeMemory(nextAddr, val);
            } catch (MemoryAddressException e) {
                e.printStackTrace();
            }
            nextAddr++;
        }
    }

    /**
     * Convenience method.
     * Same as add(short... values), but casts ints to shorts.
     * @param values values to write to memory (from left to right).
     */
    public void add(int... values) {
        for (int val : values) {
            add((short) val);
        }
    }

    /**
     * Emits code that pushes value of MSP to Register A
     * Register A is always ADDR 0 0
     */
    public void emitCodePushMTtoRegA() {
        _instance.add(Machine.PUSHMT);
        _instance.add(Machine.ADDR, 0, 0);
        _instance.add(Machine.SWAP); // value must be on top

        // Stack :: regA address -> MSP
        _instance.add(Machine.STORE);
    }

    /**
     * Loads value stored in Register A (ADDR 0 0)
     */
    public void emitCodeLoadRegA() {
        _instance.add(Machine.ADDR, 0, 0);
        _instance.add(Machine.LOAD);
    }
    
    
    public void emitCodeRoutineCall(int calleeLL, RoutineSymbol routineSym, ASTList<Expn> argList){
        // Saves address of activation record to RegA
        _instance.emitCodePushMTtoRegA();

        // Emits codes for the four fields of callee's activation record.
        _instance.add(Machine.PUSH, Machine.UNDEFINED); // return value
        _instance.add(Machine.ADDR, calleeLL, 0); // dynamic link
        _instance.add(Machine.PUSH, Machine.UNDEFINED); // return address
        // Start counting number of instructions
        short retAddrLoc = _instance.startCountingInstruction();
        _instance.add(Machine.ADDR, routineSym.getLexicLevel() - 1, 0); // static link

        if (argList != null) {
            // Emits codes for the arguments
            argList.doCodeGen(_instance);
        }

        // Emits code to branch to the callee.
        _instance.add(Machine.PUSH, routineSym.getBaseAddr());
        _instance.add(Machine.BR);

        // Replaces the return address with the calculated value.
        short numInstructions = _instance.stopCountingInstruction();
        _instance.replace(retAddrLoc, numInstructions + retAddrLoc + 1);

        // After routine call returns:
        // Stack :: return-value -> dynamic link

        // Emits code to update the rest of the display.
        // display of current lexic-level display[$curL] has been already
        // set the procedure returned.
        // We now need to update the rest of the display display[0 to $curL -1]
        // Same as RoutineBody we follow static links to update the display
        int L = calleeLL;
        while (L > 0) {
            _instance.add(Machine.ADDR, L, 3); // static link
            _instance.add(Machine.LOAD);       // Loads the value of the static link.
            _instance.add(Machine.SETD, L - 1);
            L--;
        }

        // Stack :: return-value -> dynamic link

        // Dynamic link is now on top of the stack, update display 
        _instance.add(Machine.SETD, calleeLL);

        // At this point return-value is now on top of the stack
        // if routineSym is of a procedure (has no return type, pop it)
        if (routineSym.getType() == null) {
            _instance.add(Machine.POP);
        }

        // Stack :: ...
    }

    /**
     * Have to call this function when finished writing the program
     * to the memory.
     */
    public void finishedWriting() {

        // instructionCounter should be empty at this point.
        if (instructionCounter.size() > 0) {
            throw new IllegalStateException("Number of calls to MachineWriter.startCountingInstruction " +
                    "and MachineWriter.stopCountingInstruction do not match");
        }

        // Sets all the registers
        Machine.setPC((short) 0);
        Machine.setMSP((short) (nextAddr));
        Machine.setMLP((short) (Machine.memorySize - 1));

        // Do other finalization tasks here

    }

}
