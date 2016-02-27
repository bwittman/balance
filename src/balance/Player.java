package balance;

public interface Player {	
	Move makeMove(Board board, Game.Selected yourAlignment, Game.Selected otherAlignment );
	String getName();
}
