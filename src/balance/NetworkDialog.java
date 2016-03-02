package balance;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class NetworkDialog extends JDialog implements ActionListener, WindowListener {
	private static final long serialVersionUID = -6942340314284143360L;	
	private JComboBox<String> connectionType = new JComboBox<String>( new String[]{ "Client", "Server"} );	
	private JTextField portField = new JTextField();
	private JTextField ipField = new JTextField();
	private JLabel ipLabel = new JLabel("IP Address");
	private JButton connectButton = new JButton("Connect");
	private JButton cancelButton = new JButton("Cancel");
	private NetworkPlayer player = null;
	private PlayerData data;
	
	private boolean canceled = false;
	
	private static int LABEL_WIDTH = 100;
	private static int LABEL_HEIGHT = 25;
	private static int FIELD_WIDTH = 150;
	private static int FIELD_HEIGHT = 25;
	
	public NetworkDialog(JFrame frame, PlayerData data) {
		super(frame, "Network Connection", true);
		this.data = data;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		connectionType.setSelectedIndex(0);
		connectionType.addActionListener(this);
		panel.add(connectionType);
		
		panel.add(Box.createVerticalStrut(5));		
		panel.add(makeField(new JLabel("Port"), portField));
		
		panel.add(Box.createVerticalStrut(5));
		panel.add(makeField(ipLabel, ipField));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(panel, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		connectButton.addActionListener(this);
		panel.add(connectButton);
		cancelButton.addActionListener(this);
		panel.add(cancelButton);
		
		contentPane.add(panel, BorderLayout.SOUTH);		
		
		pack();
		setResizable(false);
		setLocationRelativeTo(frame); //centers Balance window
		//setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
		setVisible(true);
	}
	
	private static JPanel makeField(JLabel label, JTextField field ) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));		
		
		label.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		label.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		label.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		panel.add(label);		
		
		field.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
		field.setMaximumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
		field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
		panel.add(field);
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if( source == connectionType ) {
			if( connectionType.getSelectedItem().equals("Client") ) {				
				ipField.setEnabled(true);
				ipLabel.setEnabled(true);
				connectButton.setText("Connect");
			}
			else { //Server
				ipField.setEnabled(false);
				ipLabel.setEnabled(false);
				ipField.setText("");
				connectButton.setText("Listen");
			}				
		}
		else if( source == connectButton ) {
			
			final boolean client = connectionType.getSelectedItem().equals("Client");  
			String port = portField.getText().trim();
			if( !isValidPort(port) ) {
				if( port.isEmpty() )
					port = "(none)";
				JOptionPane.showMessageDialog(this, "Invalid port number: " + port, "Invalid Port", JOptionPane.ERROR_MESSAGE);
				return;
			}			
			
			String ip = ipField.getText().trim();
			if( client && !isValidIp(ip) ) {
				if( ip.isEmpty() )
					ip = "(none)";
				JOptionPane.showMessageDialog(this, "Invalid IP address: " + ip, "Invalid IP", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if( client ) {
				connectButton.setText("Connecting...");
				connectButton.setEnabled(false);
				ipField.setEditable(false);
			}
			else {
				connectButton.setText("Listening...");
				connectButton.setEnabled(false);
			}
			
			portField.setEditable(false);
			
			SwingWorker<NetworkPlayer,Object> worker = new SwingWorker<NetworkPlayer,Object>() {
    			@Override
    			public NetworkPlayer doInBackground() throws Exception {
    				if( client )
    					return new NetworkPlayer(data, Integer.parseInt(portField.getText()), ipField.getText() );
    				else
    					return new NetworkPlayer(data, Integer.parseInt(portField.getText()) );
    			}
    			
    			 @Override
    	       protected void done() {
    	           try {
    	        	   player = get();    	        	   
    	           } catch (Exception ignore) {    	        	   
    	        	   player = null;	    	        	   
    	           }    	           
    	           dispose();
    	       }
    		};		
    		worker.execute();			
		}
		else if( source == cancelButton ) {
			canceled = true;
			dispose();		
		}
	}
	
	private static boolean isValidIp(String ip) {		
		return Pattern.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$", ip);
	}

	private static boolean isValidPort(String port) {
		try {
			int value = Integer.parseInt(port);
			if( value >= 1024 && value <= 65535 )
				return true;
		}
		catch(NumberFormatException e)
		{}		

		return false;
	}

	public NetworkPlayer getPlayer() {
		return player;
	}
	
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		canceled = true;
		dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
