package compiler488.ast.type;

/**
 * Created by Amir on 3/22/2017.
 */
public class ArrayType extends Type {

    private Type elementType;

    public ArrayType(Type elementType) {
        this.elementType = elementType;
    }

    public Type getElementType() {
        return elementType;
    }

}
