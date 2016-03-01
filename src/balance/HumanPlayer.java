package balance;

public class HumanPlayer implements Player {
	
	String name;
	
	public HumanPlayer(String name) {
		this.name = name;
	}

	@Override
	public Move makeMove(Board board, Square yourAlignment,
			Square otherAlignment) {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {		
		return Type.HUMAN;
	}

}
