package computer;

import balance.*;

public class Random implements Player {
	
	java.util.Random random = new java.util.Random();

	@Override
	public Move makeMove(Board board, Square yourAlignment, Square otherAlignment ) {
		Square type;
		
		switch( random.nextInt(3) ) {
		case 0: type = new City(); break;
		case 1: type = new Tree(); break;
		default: 
			switch( random.nextInt(4) ) {
			case 0: type = new Fire(Fire.NORTH); break;
			case 1: type = new Fire(Fire.SOUTH); break;
			case 2: type = new Fire(Fire.EAST); break;
			default: type = new Fire(Fire.WEST); break;
			}
		}		
		
		int row;
		int column;
		
		do {			
			row = random.nextInt(board.getRows());
			column = random.nextInt(board.getColumns());
		} while( !type.canMoveOn(board.get(row, column)) );
		
		return new Move( type, row, column );
	}

	@Override
	public String getName() {
		return "Random";
	}
}
