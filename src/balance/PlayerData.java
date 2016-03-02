package balance;

import java.io.Serializable;

public class PlayerData implements Serializable {
	private static final long serialVersionUID = -2937281075035334513L;
	private int networkPlayer;
	public int getNetworkPlayer() {
		return networkPlayer;
	}

	public int getOtherPlayer() {
		return otherPlayer;
	}

	public String getNetworkAlignment() {
		return networkAlignment;
	}

	public String getOtherAlignment() {
		return otherAlignment;
	}
	
	public String getOtherName() {
		return otherName;
	}

	private int otherPlayer;
	private String networkAlignment;
	private String otherAlignment;
	private String otherName;
	
	public PlayerData(int networkPlayer, int otherPlayer, String networkAlignment, String otherAlignment, String otherName) {
		this.networkPlayer = networkPlayer;
		this.otherPlayer = otherPlayer;
		this.networkAlignment = networkAlignment;
		this.otherAlignment = otherAlignment;
		this.otherName = otherName;
	}
}
