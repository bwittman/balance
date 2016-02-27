package balance;

public class FireStarter implements Player {

	@Override
	public Move makeMove(Board board, Game.Selected yourAlignment, Game.Selected otherAlignment ) {
		int bestRow = 0;
		int bestRowCount = 0;
		int bestRowStart = 0;
		for( int i = 0; i < board.getRows(); ++i )			
			for( int j = 0; j < board.getColumns(); ++j ) 
				for( int k = j; k < board.getColumns() && (board.get(i, k) instanceof Tree || board.get(i, k) instanceof Grass ); ++k )
					if( k - j + 1 > bestRowCount ) {
						bestRowCount = k - j + 1;						
						bestRow = i;
						bestRowStart = j;						
					}
		
		int bestColumn = 0;
		int bestColumnCount = 0;
		int bestColumnStart = 0;
		for( int i = 0; i < board.getColumns(); ++i )			
			for( int j = 0; j < board.getRows(); ++j ) 
				for( int k = j; k < board.getRows() && (board.get(k, i) instanceof Tree || board.get(k, i) instanceof Grass ); ++k )
					if( k - j + 1 > bestColumnCount ) {
						bestColumnCount = k - j + 1;						
						bestColumn = i;
						bestColumnStart = j;						
					}
		
		if( bestColumnCount > bestRowCount )
			return new Move(new Fire(Fire.SOUTH), bestColumnStart, bestColumn );
		else
			return new Move(new Fire(Fire.EAST), bestRow, bestRowStart);
	}

	@Override
	public String getName() {
		return "Fire Starter";
	}
}
