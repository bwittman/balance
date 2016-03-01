package balance;
import java.io.Serializable;

import javax.swing.JButton;

public interface Square extends Serializable {
	
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
