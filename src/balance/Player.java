package balance;

public interface Player {	
	Move makeMove(Board board, Square yourAlignment, Square otherAlignment );
	String getName();
}
