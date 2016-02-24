package balance;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private JLabel cityAlignment = new JLabel("");
	private JLabel treeAlignment = new JLabel("");
	private JLabel desertAlignment = new JLabel("");
	private JButton fireButton = new JButton();	
	private JLabel grass = new JLabel("100%");
	private JLabel desert = new JLabel("0%");
	private JLabel tree = new JLabel("0%");
	private JLabel city = new JLabel("0%");
	private JLabel message = new JLabel();
	
	private Selected player1Alignment;
	private Selected player2Alignment;
	
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
	
	// Initialized in initializeAlignments()
	private Selected move;
	private Selected player1Previous;
	private Selected player2Previous;
		
	private int fireDirection = Fire.NORTH;
	private boolean player1Turn = true;
	private boolean running = true;

	public static void main(String[] args) {		
		new Game();		
	}
	
	public Game() {
		super("Balance");
		
		initializeAlignments();
		
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
		panel.setLayout(new GridLayout(4,2));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Land"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		panel.add(new JLabel("Grass:"));
		panel.add(grass);
		panel.add(new JLabel("Desert:"));
		panel.add(desert);
		panel.add(new JLabel("Tree:"));
		panel.add(tree);
		panel.add(new JLabel("City:"));
		panel.add(city);
		panel.setAlignmentX(LEFT_ALIGNMENT);
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
	
	private void initializeAlignments() {
		String[] alignments = {"City", "Tree", "Fire"}; 
		
		String alignment1;		
		do {
			alignment1 = (String)JOptionPane.showInputDialog(this, "Player 1, what is your starting alignment?", "Player 1 Alignment", JOptionPane.PLAIN_MESSAGE, null, alignments, alignments[0]);
		} while( alignment1 == null );
		
		switch( alignment1 ) {
		case "City":
			alignments = new String[]{"Tree", "Fire"};
			cityAlignment.setText("Player 1");
			player1Alignment = Selected.CITY;
			break;
		case "Tree":
			alignments = new String[]{"City", "Fire"};
			treeAlignment.setText("Player 1");
			player1Alignment = Selected.TREE;
			break;
		case "Fire":
			alignments = new String[]{"City", "Tree"};
			desertAlignment.setText("Player 1");
			player1Alignment = Selected.FIRE;
			break;
		}
		
		String alignment2;		
		do {
			alignment2 = (String)JOptionPane.showInputDialog(this, "Player 2, what is your starting alignment?", "Player 2 Alignment", JOptionPane.PLAIN_MESSAGE, null, alignments, alignments[0]);
		} while( alignment2 == null );		
		
		switch( alignment2 ) {
		case "City":
			cityAlignment.setText("Player 2");
			player2Alignment = Selected.CITY;
			break;
		case "Tree":
			treeAlignment.setText("Player 2");
			player2Alignment = Selected.TREE;
			break;
		case "Fire":
			desertAlignment.setText("Player 2");
			player2Alignment = Selected.FIRE;
			break;
		}	

		// Initialize "previous" selections for the players

		player1Previous = player1Alignment;
		player2Previous = player2Alignment;
		
		select(player1Previous);
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
		if( running && player1Alignment != alignment && player2Alignment != alignment ) {
			
			String player = "Player " + (player1Turn ? 1 : 2);
			
			if( JOptionPane.showConfirmDialog(this, player + ", are you sure you want to change alignment to " + alignment + "?", "Change Alignment?", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION )
				return;
			
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
			
			// Select the newly aligned type for the convenience of the player
			select(alignment);
			
			updateBoard();
			
			addMessage(player + " aligned with " + alignment);
			
			endTurn();
		}
	}
	
	private void endTurn() {
		player1Turn = !player1Turn;

		// Store current selection for the corresponding player
		// Restore previous selection for the next player
		if( player1Turn ) {
			player2Previous = move;
			select(player1Previous);
			
			playerTurn.setText("Player 1 Turn");
		} else {
			player1Previous = move;
			select(player2Previous);
			
			playerTurn.setText("Player 2 Turn");
		}
		
		// Reset the fire button each turn
		fireDirection = Fire.NORTH;
		new Fire(fireDirection).update(fireButton);
	}
	
	private void updateBoard() {
		updateFire();
		updateTree();
		updateCity();
		
		for( int i = 0; i < ROWS; ++i )
			for( int j = 0; j < COLUMNS; ++j )
				board[i][j].update(buttons[i][j]);		
	}
	
	public void select(Selected type) {
		switch( type ) {
		case CITY: selectCity(); return;
		case TREE: selectTree(); return;
		case FIRE: selectFireDirection(Fire.NORTH); return;
		}
	}

	public void selectCity() {
		if( !running )
			return;	
		
		move = Selected.CITY;
		citySelected.setText("City Selected");
		treeSelected.setText("");
		fireSelected.setText("");
	}
	
	public void selectTree() {
		if( !running )
			return;	

		move = Selected.TREE;
		citySelected.setText("");
		treeSelected.setText("Tree Selected");
		fireSelected.setText("");
	}
	
	public void selectFireDirection(int direction) {
		if( !running )
			return;

		move = Selected.FIRE;
		citySelected.setText("");
		treeSelected.setText("");
		fireSelected.setText("Fire Selected");
		
		fireDirection = direction;
		new Fire(fireDirection).update(fireButton);
	}
	
	public void selectFire() {
		if( !running )
			return;
		
		if( move == Selected.FIRE) {
			switch( fireDirection ) {
			case Fire.NORTH: selectFireDirection(Fire.EAST); return;
			case Fire.SOUTH: selectFireDirection(Fire.WEST); return;
			case Fire.EAST: selectFireDirection(Fire.SOUTH); return;
			case Fire.WEST: selectFireDirection(Fire.NORTH); return;
			}
		}
		else {
			selectFireDirection(fireDirection);
		}
	}	
	
	public void clickButton( int row, int column ) {
		if( !running )
			return;
		
		String verb = "";
		
		if( move == Selected.CITY ) {
			
			if( canBuild(row, column) ) {
				board[row][column] = new City();
				verb = "built";
			}				
			else {
				addMessage("Cannot build city at (" + row + "," + column + ")" );
				return;
			}
		}
		else if( move == Selected.TREE ) {
			if( canPlant(row, column) ) {
				board[row][column] = new Tree();
				verb = "planted";
			}
			else {
				addMessage("Cannot plant tree at (" + row + "," + column + ")" );
				return;
			}
		}
		else if( move == Selected.FIRE  ) {
			if( canBurn(row, column) ) {				
				board[row][column] = new Fire(fireDirection);
				verb = "lit";
			}
			else {
				addMessage("Cannot light fire at (" + row + "," + column + ")" );
				return;
			}
		}
		
		updateBoard();
		
		addMessage("Player " + (player1Turn ? 1 : 2) + " " + verb + " " + move + " at (" + row + "," + column + ")" );

		endTurn();
		
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
					
					int limit = board[i][j] instanceof Desert ? 3 : 4; 
					
					if( neighboringCities >= limit ) {
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
		
		grass.setText(String.format("%.0f%%", grassPercent));
		desert.setText(String.format("%.0f%%", desertPercent));
		tree.setText(String.format("%.0f%%", treePercent));
		city.setText(String.format("%.0f%%", cityPercent));
		
		if( cityPercent > 50.0 )
			endGame( Selected.CITY );
		else if( treePercent > 50.0 )
			endGame( Selected.TREE );
		else if( desertPercent > 50.0 )
			endGame( Selected.FIRE );		
	}

	private void endGame( Selected selection ) {
		running = false;
		
		if( player1Alignment == selection ) {
			playerTurn.setText("Player 1 Wins!");
			JOptionPane.showMessageDialog(this, "Player 1 Wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		else if( player2Alignment == selection ) {
			playerTurn.setText("Player 2 Wins!");
			JOptionPane.showMessageDialog(this, "Player 2 Wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			playerTurn.setText("Tie!");
			JOptionPane.showMessageDialog(this, "Tie!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void addMessage(String text) {
		message.setText(text);
	}
}
