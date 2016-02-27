package balance;

public class Board {
	
	private Square[][] state;
	
	public Board(Square[][] state) {
		this.state = state;
	}
	
	public void setState(Square[][] state) {
		this.state = state;
	}
	
	public int getRows() {
		return state.length;
	}
	
	public int getColumns() {
		return state[0].length;
	}
	
	public Square get( int row, int column ) {
		return state[row][column];
	}
}
