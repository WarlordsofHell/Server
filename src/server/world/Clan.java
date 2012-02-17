package server.world;

import server.model.players.Client;

/**
 * @author Sanity
 */

public class Clan {

	public Clan(String owner, String name, String password, boolean lootshare, boolean hasPassword) {
		this.owner = owner;
		this.name = name;
		this.password = password;
		this.lootshare = lootshare;
		this.hasPassword = hasPassword;
	}
		
	public int[] members = new int [50];
	public int[] mutedMembers = new int [10];
	public String name;
	public String owner;
	public String password;
	public boolean lootshare;
	public boolean hasPassword;
}