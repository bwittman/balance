package balance;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Grass implements Square {
	private static final long serialVersionUID = -6690144932033888753L;
	private static final String FILE = "grass.png";
	private static final ImageIcon IMAGE = new ImageIcon(FILE);
	
	@Override
	public Type getType() {
		return Type.GRASS;
	}
	
	@Override
	public void update(JButton button) {
		button.setIcon(IMAGE);		
	}

	@Override
	public boolean canMoveOn(Square other) {		
		return false; //Grass is never a legal move
	}
	
	@Override
	public String toString() {
		return "Grass";
	}
	
	public String presentVerb() {
		return "";
	}
	
	public String pastVerb() {
		return "";
	}

}
