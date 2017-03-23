package compiler488.codegen;

import compiler488.runtime.ExecutionException;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public final class MachineWriter {

    private static MachineWriter _instance;

    // Next available free memory address on the machine to write to.
    private short nextAddr = 0;
    private boolean isCountingInstructions;
    private short instructionCounter;

    private MachineWriter() {}

    public static MachineWriter getInstance() {
        if (_instance == null) {
            _instance = new MachineWriter();
        }
        return _instance;
    }

    /**
     *
     * @return Returns the address of last instruction written to memory.
     */
    public short startCountingInstruction() {
        if (isCountingInstructions) {
            throw new IllegalStateException("Already called startCountingInstructions");
        }
        isCountingInstructions = true;
        instructionCounter = 0;
        return (short) (nextAddr - 1);
    }

    public short stopCountingInstruction() {
        if (!isCountingInstructions) {
            throw new IllegalStateException("Instruction counting has not beed started");
        }
        isCountingInstructions = false;
        return instructionCounter;
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
        return nextAddr;
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
                if (isCountingInstructions) {
                    instructionCounter++;
                }
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
     * Have to call this function when finished writing the program
     * to the memory.
     */
    public void finishedWriting() {

        // Sets all the registers
        Machine.setPC((short) 0);
        Machine.setMSP((short) (nextAddr));
        Machine.setMLP((short) (Machine.memorySize - 1));

        // Do other finalization tasks here
    }

}
