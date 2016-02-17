package balance;
import java.awt.BorderLayout;
import java.awt.Color;
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
	public static final int ROWS = 15;
	public static final int COLUMNS = 15;
	
	private Square[][] board = new Square[ROWS][COLUMNS]; 
	private Square[][] swap = new Square[ROWS][COLUMNS];
	private JButton[][] buttons = new JButton[ROWS][COLUMNS];
	private JLabel playerTurn = new JLabel("Player 1 Turn");
	private JLabel citySelected = new JLabel("City Selected");
	private JLabel treeSelected = new JLabel("");
	private JLabel fireSelected = new JLabel("");
	private JButton fireButton = new JButton();
	private JLabel grass = new JLabel("Grass: 100%");
	private JLabel desert = new JLabel("Desert: 0%");
	private JLabel tree = new JLabel("Tree: 0%");
	private JLabel city = new JLabel("City: 0%");
	private JLabel message = new JLabel();
	
	enum Selected {
		CITY,
		TREE,
		FIRE
	}
	
	private Selected move = Selected.CITY;
	private boolean fireNorthSouth = true;

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
		display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
			
		
		JPanel panel = new JPanel();
		panel.add(playerTurn);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMinimumSize(new Dimension(200, 25));
		panel.setMaximumSize(new Dimension(200, 25));
		panel.setPreferredSize(new Dimension(200, 25));
		display.add(panel);
		
		display.add(Box.createVerticalStrut(10));
		
		display.add(createSelector( citySelected, new JButton(), new City(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectCity();				
			}}));
		
		display.add(createSelector( treeSelected, new JButton(), new Tree(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectTree();				
			}}));
		
		display.add(createSelector( fireSelected, fireButton, new Fire( Fire.NORTH | Fire.SOUTH), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectFire();				
			}}));
		
		display.add(Box.createVerticalStrut(10));
		
		display.add(grass);
		display.add(desert);
		display.add(tree);
		display.add(city);
		
		display.add(Box.createVerticalGlue());
		
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.setBorder(BorderFactory.createTitledBorder("Message"));
		messagePanel.add(message, BorderLayout.NORTH);		
		messagePanel.setMinimumSize(new Dimension(200, 200));
		messagePanel.setMaximumSize(new Dimension(200, 200));
		messagePanel.setPreferredSize(new Dimension(200, 200));
		messagePanel.setAlignmentX(LEFT_ALIGNMENT);
		display.add(messagePanel);
		
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
		if( move == Selected.FIRE)
			fireNorthSouth = !fireNorthSouth;
		else {
			move = Selected.FIRE;
			citySelected.setText("");
			treeSelected.setText("");
			fireSelected.setText("Fire Selected");
		}
		
		if( fireNorthSouth )
			new Fire(Fire.NORTH | Fire.SOUTH).update(fireButton);
		else
			new Fire(Fire.EAST | Fire.WEST).update(fireButton);		
	}	
	
	public void clickButton( int row, int column ) {
		if( move == Selected.CITY )
			board[row][column] = new City();
		else if( move == Selected.TREE )
			board[row][column] = new Tree();
		else if( fireNorthSouth )
			board[row][column] = new Fire(Fire.NORTH | Fire.SOUTH);
		else
			board[row][column] = new Fire(Fire.EAST | Fire.WEST);
		
		board[row][column].update(buttons[row][column]);
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
