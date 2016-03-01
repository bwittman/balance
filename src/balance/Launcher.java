package balance;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Launcher extends JFrame implements ActionListener {
	private static final long serialVersionUID = 6234049734851858113L;

	JRadioButton player1City = new JRadioButton("City");
	JRadioButton player1Tree = new JRadioButton("Tree");
	JRadioButton player1Desert = new JRadioButton("Desert");
	JRadioButton player2City = new JRadioButton("City");
	JRadioButton player2Tree = new JRadioButton("Tree");
	JRadioButton player2Desert = new JRadioButton("Desert");

	JComboBox<String> player1Select = new JComboBox<String>( new String[] { "Human", "Computer", "Network"});
	JTextField player1Name = new JTextField(20);	
	JComboBox<String> player2Select = new JComboBox<String>( new String[] { "Human", "Computer", "Network"});
	JTextField player2Name = new JTextField(20);
	JButton launchGame = new JButton("Launch Game");
	
	private Player player1 = new HumanPlayer("Player 1");
	private Player player2 = new HumanPlayer("Player 2");

	private static int ALIGNMENT_WIDTH = 200;
	private static int ALIGNMENT_HEIGHT = 125;
	private static int PLAYER_WIDTH = 200;	
	private static int PLAYER_HEIGHT = 25;

	public Launcher() {
		super("Launcher");

		add(makeDisplay(1, player1City, player1Tree, player1Desert, player1Select, player1Name), BorderLayout.WEST);
		add(makeDisplay(2, player2City, player2Tree, player2Desert, player2Select, player2Name), BorderLayout.EAST);

		player1City.setSelected(true);
		player2Tree.setSelected(true);
		player1Select.setSelectedIndex(0);
		player2Select.setSelectedIndex(0);

		launchGame.addActionListener(this);

		JPanel panel = new JPanel();
		panel.add(launchGame);		
		add(panel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationRelativeTo(null); //centers Launcher window
		setVisible(true);				
	}

	private JPanel makeDisplay(int player, JRadioButton city, JRadioButton tree, JRadioButton desert, JComboBox<String> select, JTextField name) {
		JPanel display = new JPanel();		
		display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
		display.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Player " + player), BorderFactory.createEmptyBorder(5, 5, 5, 5))));
		
		//add Name
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(PLAYER_WIDTH, PLAYER_HEIGHT));
		panel.setMaximumSize(new Dimension(PLAYER_WIDTH, PLAYER_HEIGHT));
		panel.setPreferredSize(new Dimension(PLAYER_WIDTH, PLAYER_HEIGHT));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new JLabel("Name"));
		panel.add(Box.createHorizontalStrut(5));
		name.setText("Player " + player);
		panel.add(name);
		panel.setAlignmentX(LEFT_ALIGNMENT);
		display.add(panel);
		
		display.add(Box.createVerticalStrut(5));
		
		//add Human/Computer/Network combo box
		select.addActionListener(this);
		select.setAlignmentX(LEFT_ALIGNMENT);
		display.add(select);
		
		display.add(Box.createVerticalStrut(5));
		
		//add Alignment panel
		panel = new JPanel();
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

		panel.add(Box.createVerticalGlue());
		panel.add(city);
		panel.add(tree);
		panel.add(desert);
		panel.add(Box.createVerticalGlue());
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
		else if( source == player1Select ) {
			if( player1Select.getSelectedItem().equals("Human")) {
				player1Name.setEditable(true);				
			}
			else if( player1Select.getSelectedItem().equals("Computer")) {
				player1Name.setEditable(false);
				player1 = loadComputerPlayer();
				if( player1 == null )
					player1Select.setSelectedItem("Human");
				else
					player1Name.setText(player1.getName());
			}
			else if( player1Select.getSelectedItem().equals("Network")) {
				player1Name.setEditable(false);
				NetworkDialog dialog = new NetworkDialog(this, 1);
				player1 = dialog.getPlayer();
				if( player1 == null ) {
					player1Select.setSelectedItem("Human");
					JOptionPane.showMessageDialog(this, "Network connection problem!", "Network Problem", JOptionPane.ERROR_MESSAGE);
				}
				else
					player1Name.setText(player1.getName());
			}
				
		}
		else if( source == player2Select ) {
			if( player2Select.getSelectedItem().equals("Human")) {
				player2Name.setEditable(true);				
			}
			else if( player2Select.getSelectedItem().equals("Computer")) {
				player2Name.setEditable(false);
				player2 = loadComputerPlayer();
				if( player2 == null )
					player2Select.setSelectedItem("Human");
				else
					player2Name.setText(player2.getName());
			}
			else if( player2Select.getSelectedItem().equals("Network")) {
				player2Name.setEditable(false);
				NetworkDialog dialog = new NetworkDialog(this, 2);
				player2 = dialog.getPlayer();
				if( player2 == null ) {
					player2Select.setSelectedItem("Human");
					JOptionPane.showMessageDialog(this, "Network connection problem!", "Network Problem", JOptionPane.ERROR_MESSAGE);
				}
				else
					player2Name.setText(player2.getName());
			}				
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
			
			if( player1Select.getSelectedItem().equals("Human") )
				player1 = new HumanPlayer(fixName(player1Name.getText(), 1));
			
			if( player2Select.getSelectedItem().equals("Human") )
				player2 = new HumanPlayer(fixName(player2Name.getText(), 2));

			new Game( player1, player2, alignment1, alignment2 );
			dispose();
		}
	}
	
	private static String fixName(String name, int number) {
		name = name.trim();
		if( name.isEmpty() )
			return "Player " + number;
		else
			return name;
	}

	public static class ComputerClassLoader extends URLClassLoader {		

		public ComputerClassLoader(URL[] urls) {
			super(urls);			
		}

		public Class<?> loadURL(URL url) {	
			try {		        	
				URLConnection connection = url.openConnection();		            
				InputStream input = connection.getInputStream();
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				int data = input.read();

				while(data != -1){
					buffer.write(data);
					data = input.read();
				}

				input.close();
				byte[] classData = buffer.toByteArray();	     
				return defineClass(null, classData, 0, classData.length);
			} 
			catch (Exception e) {}

			return null;
		}	
	}

	private Player loadComputerPlayer() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "Class files (*.class)", "class");
		chooser.setFileFilter(filter);

		if( chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) {			
			File file = chooser.getSelectedFile();
			try{				
				URL url = file.toURI().toURL();				
				ComputerClassLoader loader = new ComputerClassLoader(new URL[] { url } );	
				Class<?> loadedClass = loader.loadURL( url );
				loader.close();
				return (Player)loadedClass.newInstance(); //if null, null pointer exception
			}
			//Usually bad form, but *any* exception should get us to return null
			catch(Exception e) {
				JOptionPane.showMessageDialog(this, "Failed to load computer player class!", "Load Failure", JOptionPane.ERROR_MESSAGE);				
			}
		}

		return null;
	}

}
