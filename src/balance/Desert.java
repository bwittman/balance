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
	public void update(JButton button)
	{
		button.setIcon(IMAGE);		
	}

}
