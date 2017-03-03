package compiler488.ast;

public class LOC {
	
	private java_cup.runtime.Symbol symbol;
	
	public LOC(java_cup.runtime.Symbol symbol) {
		// Init in CUP like 
		//		new LOC((java_cup.runtime.Symbol)CUP$Parser$stack.peek())
		this.symbol = symbol;
		
//		this.print(); // COMMENT THIS OUT AFTER!!!
	}
	
	public LOC() {

	}
	
	public int getLineNumber() {
		return (this.symbol == null) ? -1:this.symbol.left + 1;
	}
	
	public int getColumnNumber() {
		return (this.symbol == null) ? -1:this.symbol.right;
	}
	
	public String getLine() {
		if (this.symbol == null || this.symbol.value == null)
		    return "";
		else
			return symbol.value.toString();
	}
	
	public String toString() {
		if(this.symbol == null) {
			return "-> LOC NOT SET";
		}
		
		return "-> Line:" + this.getLineNumber() + " | Column:" + this.getColumnNumber() + " | Line:\n\t" + this.getLine();
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	
}
