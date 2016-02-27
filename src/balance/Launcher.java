package balance;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Launcher extends JFrame implements ActionListener {
	private static final long serialVersionUID = 6234049734851858113L;
	
	JRadioButton player1City = new JRadioButton("City");
	JRadioButton player1Tree = new JRadioButton("Tree");
	JRadioButton player1Desert = new JRadioButton("Desert");
	JRadioButton player2City = new JRadioButton("City");
	JRadioButton player2Tree = new JRadioButton("Tree");
	JRadioButton player2Desert = new JRadioButton("Desert");
	
	JRadioButton player1Human = new JRadioButton("Human");
	JRadioButton player1Computer = new JRadioButton("Computer");
	JLabel player1ComputerName = new JLabel("");
	JRadioButton player2Human = new JRadioButton("Human");
	JRadioButton player2Computer = new JRadioButton("Computer");
	JLabel player2ComputerName = new JLabel("");
	JButton launchGame = new JButton("Launch Game");
	
	private Player player1 = null;
	private Player player2 = null;
	
	private static int ALIGNMENT_WIDTH = 150;
	private static int ALIGNMENT_HEIGHT = 125;
	private static int PLAYER_WIDTH = 150;
	private static int PLAYER_HEIGHT = 125;
	
	public Launcher() {
		super("Launcher");
				
		add(makeDisplay(1, player1City, player1Tree, player1Desert, player1Human, player1Computer, player1ComputerName), BorderLayout.WEST);
		add(makeDisplay(2, player2City, player2Tree, player2Desert, player2Human, player2Computer, player2ComputerName), BorderLayout.EAST);
		
		player1City.setSelected(true);
		player2Tree.setSelected(true);
		player1Human.setSelected(true);
		player2Human.setSelected(true);
		
		launchGame.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.add(launchGame);		
		add(panel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	private JPanel makeDisplay(int player, JRadioButton city, JRadioButton tree, JRadioButton desert, JRadioButton human, JRadioButton computer, JLabel name) {
		JPanel display = new JPanel();		
		display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
		display.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Player " + player), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(ALIGNMENT_WIDTH, ALIGNMENT_HEIGHT));
		panel.setMaximumSize(new Dimension(ALIGNMENT_WIDTH, ALIGNMENT_HEIGHT));
		panel.setPreferredSize(new Dimension(ALIGNMENT_WIDTH, ALIGNMENT_HEIGHT));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Alignment"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		ButtonGroup group = new ButtonGroup();
		group.add(city);
		group.add(tree);
		group.add(desert);
		
		city.addActionListener(this);
		tree.addActionListener(this);
		desert.addActionListener(this);
		
		panel.add(city);
		panel.add(tree);
		panel.add(desert);
		display.add(panel);
		
		panel = new JPanel();
		panel.setMinimumSize(new Dimension(PLAYER_WIDTH, PLAYER_HEIGHT));
		panel.setMaximumSize(new Dimension(PLAYER_WIDTH, PLAYER_HEIGHT));
		panel.setPreferredSize(new Dimension(PLAYER_WIDTH, PLAYER_HEIGHT));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Player"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		group = new ButtonGroup();
		group.add(human);
		group.add(computer);		
		
		human.addActionListener(this);
		computer.addActionListener(this);				
		
		panel.add(human);
		panel.add(computer);
		panel.add(name);
		display.add(panel);
		
		return display;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if( source == player1City ) {
			if( player2City.isSelected() )
				player2Tree.setSelected(true);
		}
		else if( source == player1Tree ) {
			if( player2Tree.isSelected() )
				player2Desert.setSelected(true);
		}
		else if( source == player1Desert ) {
			if( player2Desert.isSelected() )
				player2City.setSelected(true);
		}
		else if( source == player2City ) {
			if( player1City.isSelected() )
				player1Tree.setSelected(true);
		}
		else if( source == player2Tree ) {
			if( player1Tree.isSelected() )
				player1Desert.setSelected(true);
		}
		else if( source == player2Desert ) {
			if( player1Desert.isSelected() )
				player1City.setSelected(true);
		}
		else if( source == player1Computer ) {
			player1 = loadComputerPlayer();
			if( player1 == null )
				player1Human.setSelected(true);
			else
				player1ComputerName.setText(player1.getName());			
		}
		else if( source == player2Computer ) {
			player2 = loadComputerPlayer();
			if( player2 == null )
				player2Human.setSelected(true);
			else
				player2ComputerName.setText(player1.getName());
		}
		else if( source == launchGame ) {
			String alignment1;
			String alignment2;
			
			if( player1City.isSelected() )
				alignment1 = "City";
			else if( player1Tree.isSelected() )
				alignment1 = "Tree";
			else
				alignment1 = "Desert";
			
			if( player2City.isSelected() )
				alignment2 = "City";
			else if( player2Tree.isSelected() )
				alignment2 = "Tree";
			else
				alignment2 = "Desert";
		
			new Game( player1, player2, alignment1, alignment2 );
			dispose();
		}
	}
	
	private Player loadComputerPlayer() {
		return null;
	}

}
