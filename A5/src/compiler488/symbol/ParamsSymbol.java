package compiler488.symbol;

import compiler488.ast.type.Type;

/**
 * Created by Amir on 3/1/2017.
 */
public class ParamsSymbol extends VariablesSymbol {

    public ParamsSymbol(String name, Type type, int lexicLevel) {
        super(name, type, lexicLevel);
    }

}
