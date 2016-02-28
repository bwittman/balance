package balance;
import javax.swing.JButton;

public interface Square {
	
	enum Type {
		CITY,
		DESERT,
		FIRE,
		TREE,
		GRASS
	}
	
	Type getType();
	void update(JButton button);
	boolean canMoveOn(Square other);
	String presentVerb();
	String pastVerb();
}
