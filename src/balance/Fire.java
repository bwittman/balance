package balance;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Fire implements Square {
	private static final long serialVersionUID = -4850786399099981034L;
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 4;
	public static final int WEST = 8;
	
	private int directions = 0;	
	
	private static final String[] FILES = { "fire1.png", "fire2a.png", "fire2b.png", "fire3.png", "fire4.png" };
	private static final ImageIcon[] IMAGES;
	
	// initialize IMAGES
	static {	
		ImageIcon[] images = new ImageIcon[FILES.length];
		for( int i = 0; i < FILES.length; ++i )
			images[i] = new ImageIcon(FILES[i]);		
		IMAGES = images;
	}
	
	public Fire()
	{}
	
	public Fire(int directions) {
		this.directions = directions;
	}	
	
	public int getDirections() {
		return directions;
	}
	
	public boolean isHeaded( int direction ) {
		return (directions & direction) != 0; 
	}
	
	public void addDirection( int direction ) {
		directions |= direction; 
	}
	
	@Override
	public void update(JButton button)
	{
		int count = 0;
		if( isHeaded( NORTH ))
			count++;
		if( isHeaded( SOUTH ))
			count++;
		if( isHeaded( EAST ))
			count++;
		if( isHeaded( WEST ))
			count++;
		
		Icon image;
		if( count == 1 ) {
			image = IMAGES[0];			
			if( isHeaded( SOUTH ))
				image = new RotatedIcon( image, RotatedIcon.Rotate.UPSIDE_DOWN);
			else if( isHeaded( EAST ))
				image = new RotatedIcon( image, RotatedIcon.Rotate.DOWN);
			else if( isHeaded( WEST ))
				image = new RotatedIcon( image, RotatedIcon.Rotate.UP);
		}
		else if( count == 3 ) {
			image = IMAGES[3];
			if( !isHeaded( NORTH ))
				image = new RotatedIcon( image, RotatedIcon.Rotate.UPSIDE_DOWN);
			else if( !isHeaded( WEST ))
				image = new RotatedIcon( image, RotatedIcon.Rotate.DOWN);
			else if( !isHeaded( EAST ))
				image = new RotatedIcon( image, RotatedIcon.Rotate.UP);			
		}
		else if( count == 4 )
			image = IMAGES[4];
		else { //count == 2
			if( isHeaded( NORTH ) && isHeaded( SOUTH ) )
				image = IMAGES[1];
			else if( isHeaded( EAST ) && isHeaded( WEST ) ) {
				image = IMAGES[1];
				image = new RotatedIcon( image, RotatedIcon.Rotate.DOWN);
			}
			else {				
				image = IMAGES[2];
				if( isHeaded( EAST) && isHeaded( SOUTH ))
					image = new RotatedIcon( image, RotatedIcon.Rotate.DOWN);
				else if( isHeaded( SOUTH ) && isHeaded( WEST ) )
					image = new RotatedIcon( image, RotatedIcon.Rotate.UPSIDE_DOWN);
				else if( isHeaded( WEST ) && isHeaded( NORTH ) )
					image = new RotatedIcon( image, RotatedIcon.Rotate.UP);				
			}
		}
		button.setIcon(image);		
	}

	@Override
	public Type getType() {
		return Type.FIRE;
	}

	@Override
	public boolean canMoveOn(Square other) {		
		return other instanceof Tree || other instanceof Grass;
	}
	
	@Override
	public String toString() {
		return "Fire";
	}
	
	public String presentVerb() {
		return "light";
	}
	
	public String pastVerb() {
		return "lit";
	}

}
