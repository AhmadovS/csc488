package compiler488.ast.expn;


/**
 * The common features of binary expressions.
 */
public abstract class BinaryExpn extends Expn {
    private Expn left, right;	/* Left and right operands of the binary operator. */
    private String opSymbol;	/* Name of the operator. */
    
    public BinaryExpn(String opSymbol, Expn left, Expn right) {
    	this.opSymbol = opSymbol;
    	
    	this.left = left;
    	this.left.setParent(this);
    	
    	this.right = right;
    	this.right.setParent(this);
    }

    /** Returns a string that represents the binary expression. */
    @Override
	public String toString () {
    	return ("(" + left + ")" + opSymbol + "(" + right + ")");
    }

	public Expn getLeft() {
		return left;
	}

	public void setLeft(Expn left) {
		this.left = left;
	}

	public String getOpSymbol() {
		return opSymbol;
	}

	public void setOpSymbol(String opSymbol) {
		this.opSymbol = opSymbol;
	}

	public Expn getRight() {
		return right;
	}

	public void setRight(Expn right) {
		this.right = right;
	}
}
