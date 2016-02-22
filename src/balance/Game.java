package balance;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game extends JFrame {
	private static final long serialVersionUID = -8705064841297440045L;
	public static final int ROWS = 12;
	public static final int COLUMNS = 12;
	
	private Square[][] board = new Square[ROWS][COLUMNS]; 
	private Square[][] swap = new Square[ROWS][COLUMNS];
	private JButton[][] buttons = new JButton[ROWS][COLUMNS];
	private JLabel playerTurn = new JLabel("Player 1 Turn");
	private JLabel citySelected = new JLabel("City Selected");
	private JLabel treeSelected = new JLabel("");
	private JLabel fireSelected = new JLabel("");
	private JLabel cityAlignment = new JLabel("Player 1");
	private JLabel treeAlignment = new JLabel("Player 2");
	private JLabel desertAlignment = new JLabel("");
	private JButton fireButton = new JButton();
	private JLabel grass = new JLabel("Grass: 100%");
	private JLabel desert = new JLabel("Desert: 0%");
	private JLabel tree = new JLabel("Tree: 0%");
	private JLabel city = new JLabel("City: 0%");
	private JLabel message = new JLabel();
	
	private Selected player1Alignment = Selected.CITY;
	private Selected player2Alignment = Selected.TREE;
	
	enum Selected {
		CITY("City"),
		TREE("Tree"),
		FIRE("Fire");
		
		private String name;
		public String toString() {
			return name;
		}
		
		private Selected(String name) {
			this.name = name;
		}		
		
	}
	
	private Selected move = Selected.CITY;
	private int fireDirection = Fire.NORTH;
	private boolean player1Turn = true;

	public static void main(String[] args) {		
		new Game();		
	}
	
	public Game() {
		super("Balance");		
		
		JPanel tiles = new JPanel();	
		tiles.setLayout(new GridLayout(ROWS, COLUMNS));
		final Grass GRASS = new Grass();
	
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j ) {
				final int row = i;
				final int column = j;
				board[i][j] = GRASS;
				buttons[i][j] = new JButton();
				board[i][j].update(buttons[i][j]);
				buttons[i][j].setMargin(new Insets(0, 0, 0, 0));				
				buttons[i][j].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						clickButton( row, column );						
					}});				
				
				//button.setBorder(null);
				tiles.add(buttons[i][j]);
			}
		
		add(tiles, BorderLayout.CENTER);
		
		JPanel display = new JPanel();		
		display.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
			
		
		JPanel panel = new JPanel();
		panel.add(playerTurn);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMinimumSize(new Dimension(200, 25));
		panel.setMaximumSize(new Dimension(200, 25));
		panel.setPreferredSize(new Dimension(200, 25));
		display.add(panel);
		
		display.add(Box.createVerticalStrut(5));
		
		
		panel = new JPanel();
		panel.setMinimumSize(new Dimension(200, 210));
		panel.setMaximumSize(new Dimension(200, 210));
		panel.setPreferredSize(new Dimension(200, 210));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Next Move"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			
		
		panel.add(createSelector( citySelected, new JButton(), new City(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectCity();				
			}}));
		
		panel.add(createSelector( treeSelected, new JButton(), new Tree(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectTree();				
			}}));
		
		panel.add(createSelector( fireSelected, fireButton, new Fire( Fire.NORTH ), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectFire();				
			}}));
		
		display.add(panel);
		
		display.add(Box.createVerticalStrut(5));
		
		panel = new JPanel();
		panel.setMinimumSize(new Dimension(200, 100));
		panel.setMaximumSize(new Dimension(200, 100));
		panel.setPreferredSize(new Dimension(200, 100));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Land"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		panel.add(grass);
		panel.add(desert);
		panel.add(tree);
		panel.add(city);
		display.add(panel);
		
		display.add(Box.createVerticalStrut(5));
		
		panel = new JPanel();
		panel.setMinimumSize(new Dimension(200, 210));
		panel.setMaximumSize(new Dimension(200, 210));
		panel.setPreferredSize(new Dimension(200, 210));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Alignment"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		panel.add(createSelector( cityAlignment, new JButton(), new City(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateAlignment( Selected.CITY );				
			}}));
		
		panel.add(createSelector( treeAlignment, new JButton(), new Tree(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateAlignment( Selected.TREE );
			}}));
		
		panel.add(createSelector( desertAlignment, new JButton(), new Desert(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateAlignment( Selected.FIRE );
			}}));
		
		display.add(panel);
		
		display.add(Box.createVerticalGlue());
		
		panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Message"));
		message.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(message, BorderLayout.NORTH);		
		panel.setMinimumSize(new Dimension(200, 100));
		panel.setMaximumSize(new Dimension(200, 100));
		panel.setPreferredSize(new Dimension(200, 100));
		panel.setAlignmentX(LEFT_ALIGNMENT);
		display.add(panel);
		
		add(display, BorderLayout.EAST);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	private static JPanel createSelector( JLabel label, JButton button, Square square, ActionListener listener ){
		JPanel panel = new JPanel();		
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));		
		square.update(button);
		button.setMargin(new Insets(0, 0, 0, 0));		
		button.addActionListener(listener);
		panel.add(button, BorderLayout.WEST);
		panel.add(label, BorderLayout.EAST);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setMinimumSize(new Dimension(150 + button.getIcon().getIconWidth(), button.getIcon().getIconHeight() + 10));
		panel.setPreferredSize(new Dimension(150 + button.getIcon().getIconWidth(), button.getIcon().getIconHeight() + 10));
		panel.setAlignmentX(LEFT_ALIGNMENT);
		return panel;
	}
	
	public void updateAlignment( Selected alignment ) {
		if( player1Alignment != alignment && player2Alignment != alignment ) {
			
			String player = "Player " + (player1Turn ? 1 : 2);
			
			if( player1Turn ) {
				if( player1Alignment == Selected.CITY && alignment != Selected.CITY )
					cityAlignment.setText("");
				else if( player1Alignment == Selected.TREE && alignment != Selected.TREE )
					treeAlignment.setText("");
				else if( player1Alignment == Selected.FIRE && alignment != Selected.FIRE )
					desertAlignment.setText("");	
				
				player1Alignment = alignment;
			}
			else {
				if( player2Alignment == Selected.CITY && alignment != Selected.CITY )
					cityAlignment.setText("");
				else if( player2Alignment == Selected.TREE && alignment != Selected.TREE )
					treeAlignment.setText("");
				else if( player2Alignment == Selected.FIRE && alignment != Selected.FIRE )
					desertAlignment.setText("");
				
				player2Alignment = alignment;
			}
			
			switch( alignment ) {
			case CITY: cityAlignment.setText(player); break;
			case TREE: treeAlignment.setText(player); break;
			case FIRE: desertAlignment.setText(player); break;
			}				
				
			addMessage(player + " aligned with " + alignment);
			
			player1Turn = !player1Turn;
			
			if( player1Turn )
				playerTurn.setText("Player 1 Turn");
			else
				playerTurn.setText("Player 2 Turn");
		}
	}
	
	public void selectCity() {
		move = Selected.CITY;
		citySelected.setText("City Selected");
		treeSelected.setText("");
		fireSelected.setText("");
	}
	
	public void selectTree() {
		move = Selected.TREE;
		citySelected.setText("");
		treeSelected.setText("Tree Selected");
		fireSelected.setText("");
	}
	
	public void selectFire() {
		if( move == Selected.FIRE) {
			switch( fireDirection ) {
			case Fire.NORTH: fireDirection = Fire.EAST; break;
			case Fire.SOUTH: fireDirection = Fire.WEST; break;
			case Fire.EAST: fireDirection = Fire.SOUTH; break;
			case Fire.WEST: fireDirection = Fire.NORTH; break;
			}
		}
		else {
			move = Selected.FIRE;
			citySelected.setText("");
			treeSelected.setText("");
			fireSelected.setText("Fire Selected");
		}		
		
		new Fire(fireDirection).update(fireButton);
	}	
	
	public void clickButton( int row, int column ) {
		if( move == Selected.CITY ) {
			
			if( canBuild(row, column) )
				board[row][column] = new City();
			else {
				addMessage("Cannot build city at (" + row + "," + column + ")" );
				return;
			}
		}
		else if( move == Selected.TREE ) {
			if( canPlant(row, column) )
				board[row][column] = new Tree();
			else {
				addMessage("Cannot plant tree at (" + row + "," + column + ")" );
				return;
			}
		}
		else if( move == Selected.FIRE  ) {
			if( canBurn(row, column) )				
				board[row][column] = new Fire(fireDirection);			
			else {
				addMessage("Cannot light fire at (" + row + "," + column + ")" );
				return;
			}
		}
		
		updateFire();
		updateTree();
		updateCity();
		
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j )
				board[i][j].update(buttons[i][j]);
		
		
		
		addMessage("Player " + (player1Turn ? 1 : 2) + " played " + move + " at (" + row + "," + column + ")" );
		
		player1Turn = !player1Turn;
		
		if( player1Turn )
			playerTurn.setText("Player 1 Turn");
		else
			playerTurn.setText("Player 2 Turn");
		
		updateCounts();
	}
	
	private void updateCity() {
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j )
				swap[i][j] = board[i][j];
		
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j ) {
				if( canBuild(i,j) ) {
					int neighboringCities = 0;
					for( int row = i - 1; row <= i + 1; ++row )
						for( int column = j - 1; column <= j + 1; ++column )
							if( (row != i || column != j) && isLegal(row, column) && board[row][column] instanceof City ) {
								neighboringCities++;								
							}
					
					if( neighboringCities >= 4 ) {
						swap[i][j] = new City();
					}
				}				
			}
		
		Square[][] temp = board;
		board = swap;
		swap = temp;		
	}

	private void updateTree() {
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j )
				swap[i][j] = board[i][j];
		
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j ) {
				if( board[i][j] instanceof Grass || board[i][j] instanceof Desert ) {
					//fill empty spot between trees
					if( (isTree(i - 1, j) && isTree(i + 1, j)) ||
						(isTree(i, j - 1) && isTree(i, j + 1)) ||
						(isTree(i - 1, j - 1) && isTree(i + 1, j + 1)) ||
						(isTree(i + 1, j - 1) && isTree(i - 1, j + 1)))
						swap[i][j] = new Tree();
					//grow counterclockwise around cities 
					else if( (isTree(i - 1, j ) && ( isCity( i, j + 1) || isCity( i - 1, j + 1) ) ) ||  
					(isTree(i, j - 1) && ( isCity( i - 1, j) || isCity( i - 1, j - 1) ) ) ||
					(isTree(i + 1, j ) && ( isCity( i, j - 1) || isCity( i + 1, j - 1) ) ) ||
					(isTree(i, j + 1) && ( isCity( i + 1, j) || isCity( i + 1, j + 1) ) ))
						swap[i][j] = new Tree();					
				}				
			}
		
		Square[][] temp = board;
		board = swap;
		swap = temp;
	}

	private void updateFire() {
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j )
				swap[i][j] = board[i][j];
		
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j ) {
				if( board[i][j] instanceof Fire ) {
					Fire fire = (Fire) board[i][j];
					if( fire.isHeaded( Fire.NORTH ) && isLegal( i - 1, j ) && canBurn(i - 1, j) ) {
						addFire( i - 1, j, Fire.NORTH );						
						if( board[i - 1][j] instanceof Tree )
							addFire( i - 1, j, Fire.EAST );
					}
					if( fire.isHeaded( Fire.SOUTH ) && isLegal( i + 1, j ) && canBurn(i + 1, j) ) {
						addFire( i + 1, j, Fire.SOUTH );						
						if( board[i + 1][j] instanceof Tree )
							addFire( i + 1, j, Fire.WEST);
					}
					if( fire.isHeaded( Fire.EAST ) && isLegal( i, j + 1 ) && canBurn(i, j + 1) ) {
						addFire( i, j + 1, Fire.EAST );						
						if( board[i][j + 1] instanceof Tree )
							addFire( i, j + 1, Fire.SOUTH );
					}					
					if( fire.isHeaded( Fire.WEST ) && isLegal( i, j - 1 ) && canBurn(i, j - 1) ) {
						addFire( i, j - 1, Fire.WEST );						
						if( board[i][j - 1] instanceof Tree )
							addFire( i, j - 1, Fire.NORTH);
					}
					
					swap[i][j] = new Desert();
				}
				
			}
		
		Square[][] temp = board;
		board = swap;
		swap = temp;
	}
	
	private void addFire( int row, int column, int directions ) {
		if( swap[row][column] instanceof Fire ) {
			Fire fire = (Fire)swap[row][column];
			fire.addDirection(directions);
		}
		else
			swap[row][column] = new Fire(directions);
	}
	
	private static boolean isLegal(int row, int column) {
		return row >= 0 && column >= 0 && row < ROWS && column < COLUMNS;
	}
	
	private boolean isTree( int row, int column ) {
		return isLegal(row, column) && board[row][column] instanceof Tree;
	}
	
	private boolean isCity( int row, int column ) {
		return isLegal(row, column) && board[row][column] instanceof City;
	}
	
	
	private boolean canBurn( int row, int column ) {
		Square square = board[row][column];
		return square instanceof Grass || square instanceof Tree;
	}
	
	private boolean canBuild( int row, int column ) {
		Square square = board[row][column];
		return square instanceof Grass || square instanceof Desert;
	}
	
	private boolean canPlant( int row, int column ) {
		Square square = board[row][column];
		return square instanceof Grass || square instanceof Desert || square instanceof City;
	}

	private void updateCounts() {
		int grassCount = 0;
		int desertCount = 0;
		int treeCount = 0;
		int cityCount = 0;		
		
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j ) {
				Square square = board[i][j];
				if( square instanceof Grass )
					grassCount++;
				else if( square instanceof City )
					cityCount++;
				else if( square instanceof Tree )
					treeCount++;
				else //covers desert and fire
					desertCount++;				
			}
		
		double total = ROWS*COLUMNS;
		double grassPercent = 100 * grassCount / total;
		double cityPercent = 100 * cityCount / total;
		double treePercent = 100 * treeCount / total;
		double desertPercent = 100 * desertCount / total;
		
		grass.setText(String.format("Grass: %.0f%%", grassPercent));
		desert.setText(String.format("Desert: %.0f%%", desertPercent));
		tree.setText(String.format("Tree: %.0f%%", treePercent));
		city.setText(String.format("City: %.0f%%", cityPercent));
	}

	private void addMessage(String text) {
		message.setText(text);
	}
	
	
	
	
	
	private static Square makeRandomSquare( Random random ) {
		switch( random.nextInt(5) ) {
		case 0:	 return new City();			
		case 1:  return new Desert();
		case 2: {
			Fire fire = new Fire();			
			do
			{
				if( random.nextInt(3) == 0 )
					fire.addDirection(Fire.NORTH);
				if( random.nextInt(3) == 0 )
					fire.addDirection(Fire.SOUTH);
				if( random.nextInt(3) == 0 )
					fire.addDirection(Fire.EAST);
				if( random.nextInt(3) == 0 )
					fire.addDirection(Fire.WEST);				
				
			} while( fire.getDirections() == 0 );
			return fire;
		}
		case 3:  return new Grass();			
		default: return new Tree();
		}		
	}
}
