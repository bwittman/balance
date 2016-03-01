package computer;

import balance.Board;
import balance.City;
import balance.Fire;
import balance.Move;
import balance.Player;
import balance.Square;
import balance.Tree;

/**
 * Class is used to test delay functionality when a computer opponent is
 * taking a while to make a move.
 * @author Barry Wittman
 *
 */
public class RandomWithDelay implements Player {
	
	java.util.Random random = new java.util.Random();

	/**
	 * Makes a random legal move after waiting between .5 and 2 seconds.
	 * @param board
	 * @param yourAlignment
	 * @param otherAlignment
	 * @return
	 */
	@Override
	public Move makeMove(Board board, Square yourAlignment, Square otherAlignment ) {
		Square type;
		
		try {
			Thread.sleep(random.nextInt(1500) + 500);
		} 
		catch (InterruptedException e) {			
		}
		
		
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
		return "Random with Delay";
	}
	
	@Override
	public Type getType() {
		return Type.COMPUTER;
	}
}
