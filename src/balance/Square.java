package balance;
import javax.swing.JButton;

public interface Square {
	
	enum Type {
		CITY,
		DESERT,
		FIRE,
		TREE
	}
	
	Type getType();
	void update(JButton button);
}
