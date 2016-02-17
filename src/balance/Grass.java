package balance;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Grass implements Square {

	private static final String FILE = "grass.png";
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

}
