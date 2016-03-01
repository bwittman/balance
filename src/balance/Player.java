package balance;

public interface Player {	
	enum Type {
		HUMAN("Human"),
		COMPUTER("Computer"),
		NETWORK("Network");
		
		private String name;
		private Type(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	
	Move makeMove(Board board, Square yourAlignment, Square otherAlignment );
	String getName();
	Type getType();
}
