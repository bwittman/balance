package balance;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class NetworkPlayer implements Player {
	
	private Socket socket;
	private ObjectOutputStream out; 
	private ObjectInputStream in; 
	
	//Server version
	public NetworkPlayer( int number, int port ) throws IOException {
		ServerSocket serverSocket = new ServerSocket( port );	
		socket = serverSocket.accept();
		serverSocket.close();
		in = new ObjectInputStream(socket.getInputStream()) ;
		out = new ObjectOutputStream(socket.getOutputStream() );
		
		int other = in.readInt();		
		out.writeInt(number);	
		out.flush();		
		
		//Player 1 connects to Player 2 or vice versa
		//Player 1 connecting to Player 1 makes no sense
		if( other == number ) {			
			JOptionPane.showMessageDialog(null, "Networked players are numbered inconsistently!", "Inconsistent Players", JOptionPane.ERROR_MESSAGE);
			throw new IOException();
		}
			
	}
		
	//Client version
	public NetworkPlayer( int number, int port, String address ) throws UnknownHostException, IOException {
		socket = new Socket( address, port );
		out = new ObjectOutputStream(socket.getOutputStream() );
		in = new ObjectInputStream(socket.getInputStream());
		
		out.writeInt(number);
		out.flush();
		int other = in.readInt();		
		
		//Player 1 connects to Player 2 or vice versa
		//Player 1 connecting to Player 1 makes no sense
		if( other == number ) {
			JOptionPane.showMessageDialog(null, "Networked players are numbered inconsistently!", "Inconsistent Players", JOptionPane.ERROR_MESSAGE);
			throw new IOException();
		}			
	}
	
	public void sendMove(Move move) throws IOException {		
		out.writeObject( move );
		out.flush();
	}

	@Override
	public Move makeMove(Board board, Square yourAlignment,
			Square otherAlignment) {
		Move move = null;
		try {			
			move = (Move) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			move = null;
		}
		return move;
	}

	@Override
	public String getName() {
		return "Network Player";
	}

	@Override
	public Type getType() {		
		return Type.NETWORK;
	}
	
	public void close() {
		if( out != null )
			try {
				out.close();
			} catch (IOException e) {}
		
		if( in != null )
			try {
				in.close();
			} catch (IOException e) {}		
		
		if( socket != null )
			try {
				socket.close();
			} catch (IOException e) {}
	}
}
