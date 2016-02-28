package balance;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class Desert implements Square {

	private static final String FILE = "desert.png";
	private static final ImageIcon IMAGE = new ImageIcon(FILE);
	
	@Override
	public Type getType() {
		return Type.DESERT;
	}
	
	@Override
	public void update(JButton button) {
		button.setIcon(IMAGE);		
	}

	@Override
	public boolean canMoveOn(Square other) {		
		return false; // Desert is never a legal move
	}
	
	@Override
	public String toString() {
		return "Desert";
	}
	
	public String presentVerb() {
		return "";
	}
	
	public String pastVerb() {
		return "";
	}

}
