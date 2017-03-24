package compiler488.ast.type;

import compiler488.ast.AST;
import compiler488.codegen.MachineWriter;
import compiler488.symbol.SymbolTable;

/**
 * A placeholder for types.
 */
public abstract class Type extends AST {
    @Override
    public void checkSemantics(SymbolTable symbols) throws Exception {
        // Nothing to do here.
    }

    @Override
    public void doCodeGen(MachineWriter writer) {
        // Nothing to do here.
    }

    /**
     * All types have the same lexic-level as their parents.
     */
    @Override
    protected int calculateLexicLevel() {
        return getParent().getLexicLevel();
    }
}
