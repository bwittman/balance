package balance;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class Tree implements Square {

	private static final String FILE = "tree.png";
	private static final ImageIcon IMAGE = new ImageIcon(FILE);
	
	@Override
	public Type getType() {
		return Type.TREE;
	}
	
	@Override
	public void update(JButton button) {
		button.setIcon(IMAGE);		
	}

	@Override
	public boolean canMoveOn(Square other) {		
		return other instanceof Grass || other instanceof Desert || other instanceof City;
	}
	
	@Override
	public String toString() {
		return "Tree";
	}
	
	public String presentVerb() {
		return "plant";
	}
	
	public String pastVerb() {
		return "planted";
	}

}
