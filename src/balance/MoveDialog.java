package balance;

import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class MoveDialog extends JDialog implements HierarchyListener {
	private static final long serialVersionUID = 996893340528209319L;
	private JLabel label = new JLabel("Computer player is calculating its next move.");	
	private volatile Player player;
	private volatile Move move;
	private volatile Board boardState;
	private volatile Square yourAlignment;
	private volatile Square otherAlignment;	
	
	public MoveDialog(JFrame frame) {
		super(frame, "Waiting on Move", true);
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(label);
		Container contentPane = getContentPane();
		contentPane.add( panel);
		pack();
		setResizable(false);
		setLocationRelativeTo(frame); //centers Balance window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);		
		   // Hierarchy listener is a way to detect visibility changes.
        addHierarchyListener(this);       
	}
	
	//Show dialog box while computing move
	//Think of setVisible() as a blocking call, since this is a modal dialog
	public void show(final Player player, final Board boardState, final Square yourAlignment, final Square otherAlignment) {
		this.player = player;
		this.boardState = boardState;
		this.yourAlignment = yourAlignment;
		this.otherAlignment = otherAlignment;		
		setVisible(true);	
	}
	
	public Move getMove() {
		return move;
	}
	
	@Override
    public void hierarchyChanged(HierarchyEvent e) {
        if (isVisible()) {
        	label.setText(player.getName() + " (" + player.getType() + ") is calculating their next move.");
        	pack();                	
        	SwingWorker<Move,Object> worker = new SwingWorker<Move,Object>() {
    			@Override
    			public Move doInBackground() throws Exception {
    				return player.makeMove(boardState, yourAlignment, otherAlignment);
    			}
    			
    			 @Override
    	       protected void done() {
    	           try {
    	        	   move = get();    	        	  
    	           } catch (Exception ignore) {
    	           }
    	           setVisible(false);
    	       }
    		};		
    		worker.execute();
        }
    }
}
