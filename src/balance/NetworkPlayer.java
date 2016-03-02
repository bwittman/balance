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
	private String name;
	
	//Server version
	public NetworkPlayer( PlayerData data, int port ) throws IOException, ClassNotFoundException {
		ServerSocket serverSocket = new ServerSocket( port );	
		socket = serverSocket.accept();
		serverSocket.close();
		in = new ObjectInputStream(socket.getInputStream()) ;
		out = new ObjectOutputStream(socket.getOutputStream() );
		
		PlayerData otherData = (PlayerData)in.readObject();		
		out.writeObject(data);	
		out.flush();		
		
		checkConsistency(data, otherData);		
		name = otherData.getOtherName();
	}
		
	//Client version
	public NetworkPlayer( PlayerData data, int port, String address ) throws UnknownHostException, IOException, ClassNotFoundException {
		socket = new Socket( address, port );
		out = new ObjectOutputStream(socket.getOutputStream() );
		in = new ObjectInputStream(socket.getInputStream());
		
		out.writeObject(data);
		out.flush();
		PlayerData otherData = (PlayerData)in.readObject();		
		
		checkConsistency(data, otherData);
		name = otherData.getOtherName();
	}
	
	private static void checkConsistency(PlayerData data, PlayerData otherData ) throws IOException {
		//Player 1 connects to Player 2 or vice versa
		//Player 1 connecting to Player 1 makes no sense
		if( data.getNetworkPlayer() != otherData.getOtherPlayer() ||
			data.getOtherPlayer() != otherData.getNetworkPlayer()) {
			JOptionPane.showMessageDialog(null, "Networked players are numbered inconsistently!", "Inconsistent Players", JOptionPane.ERROR_MESSAGE);
			throw new IOException();
		}
		
		if( !data.getNetworkAlignment().equals(otherData.getOtherAlignment()) ||
			!data.getOtherAlignment().equals(otherData.getNetworkAlignment()) ) {
			JOptionPane.showMessageDialog(null, "Networked players have inconsistent alignments!", "Inconsistent Alignments", JOptionPane.ERROR_MESSAGE);
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
		return name;
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
