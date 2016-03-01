package balance;

import java.io.Serializable;

public class Move implements Serializable {
	private static final long serialVersionUID = -5270901492699547173L;
	private final boolean alignment;
	private final int row;
	private final int column;
	private final Square type;
	
	public Move( Square square ) {
		alignment = true;
		type = square;
		row = column = 0;
	}
	
	public Move( Square square, int row, int column ) {
		alignment = false;
		type = square;
		this.row = row;
		this.column = column;
	}
	
	public Square getType() {
		return type;
	}
	
	public boolean isAlignment() {
		return alignment;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
}
