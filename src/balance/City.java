package balance;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class City implements Square {
	
	private static final String FILE = "city.png";
	private static final ImageIcon IMAGE = new ImageIcon(FILE);
	
	@Override
	public Type getType() {
		return Type.CITY;
	}
	
	@Override
	public void update(JButton button)
	{
		button.setIcon(IMAGE);		
	}

	@Override
	public boolean canMoveOn(Square other) {		
		return other instanceof Grass || other instanceof Desert;
	}
	
	@Override
	public String toString() {
		return "City";
	}
	
	public String presentVerb() {
		return "build";
	}
	
	public String pastVerb() {
		return "built";
	}
}
