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
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;

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
	
	JPanel computerPanel1 = new JPanel();
	JPanel computerPanel2 = new JPanel();

	private Player player1 = null;
	private Player player2 = null;

	private static int ALIGNMENT_WIDTH = 200;
	private static int ALIGNMENT_HEIGHT = 125;
	private static int PLAYER_WIDTH = 200;	
	private static int PLAYER_HEIGHT = 150;

	public Launcher() {
		super("Launcher");

		add(makeDisplay(1, player1City, player1Tree, player1Desert, player1Human, player1Computer, player1ComputerName, computerPanel1), BorderLayout.WEST);
		add(makeDisplay(2, player2City, player2Tree, player2Desert, player2Human, player2Computer, player2ComputerName, computerPanel2), BorderLayout.EAST);

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
		setLocationRelativeTo(null); //centers Launcher window
		setVisible(true);				
	}

	private JPanel makeDisplay(int player, JRadioButton city, JRadioButton tree, JRadioButton desert, JRadioButton human, JRadioButton computer, JLabel name, JPanel computerPanel) {
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
		
		computerPanel.setBorder(BorderFactory.createTitledBorder("Name"));
		computerPanel.add(name);
		computerPanel.setVisible(false);
		computerPanel.setAlignmentX(LEFT_ALIGNMENT);
		panel.add(computerPanel);		
		
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
		else if( source == player1Human )
			computerPanel1.setVisible(false);		
		else if( source == player1Computer ) {
			player1 = loadComputerPlayer();
			if( player1 == null )
				player1Human.setSelected(true);
			else {
				player1ComputerName.setText(player1.getName());
				computerPanel1.setVisible(true);
			}
		}
		else if( source == player2Human )
			computerPanel2.setVisible(false);
		else if( source == player2Computer ) {
			player2 = loadComputerPlayer();
			if( player2 == null )
				player2Human.setSelected(true);
			else {
				player2ComputerName.setText(player2.getName());
				computerPanel2.setVisible(true);
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

			new Game( player1, player2, alignment1, alignment2 );
			dispose();
		}
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
		chooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File file) {				
				return file.isDirectory() || file.getName().endsWith(".class");
			}

			@Override
			public String getDescription() {				
				return "Class files (*.class)";
			}});		

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
