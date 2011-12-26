package server.world;

import server.model.players.Client;
import server.util.Misc;
import server.Server;

import java.util.ArrayList;

/**
 * @author Sanity
 */

public class ClanChatHandler {

	public ClanChatHandler() {
	
	}
	
	public Clan[] clans = new Clan[100];
	
	
	public void handleClanChat(Client c, String name) {
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] != null) {
				if (clans[j].name.equalsIgnoreCase(name)) {
					addToClan(c.playerId, j);
					return;
				}			
			}
		}
		makeClan(c, name);
	}
	
	
	public void makeClan(Client c, String name) {
		if (openClan() >= 0) {
			if (validName(name)) {
				c.clanId = openClan();
				clans[c.clanId] = new Clan (c,name);
				addToClan(c.playerId, c.clanId);
			} else {
				c.sendMessage("A clan with this name already exists.");
			}
		} else {
			c.sendMessage("Your clan chat request could not be completed.");
		}
	}
	
	public void updateClanChat(int clanId) {
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] <= 0)
				continue;
			if (Server.playerHandler.players[clans[clanId].members[j]] != null) {
				Client c = (Client)Server.playerHandler.players[clans[clanId].members[j]];
				c.getPA().sendFrame126("Talking in: " + clans[clanId].name, 18139);
				c.getPA().sendFrame126("Owner: " + clans[clanId].owner, 18140);
				int slotToFill = 18144;
				for (int i = 0; i < clans[clanId].members.length; i++) {
					if (clans[clanId].members[i] > 0){
						if (Server.playerHandler.players[clans[clanId].members[i]] != null) {
							c.getPA().sendFrame126(Server.playerHandler.players[clans[clanId].members[i]].playerName, slotToFill);
							slotToFill++;
						}	
					}
				}
				for (int k = slotToFill; k < 18244; k++)
					c.getPA().sendFrame126("", k);
			}		
		}
	}
	
	public int openClan() {	
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] == null || clans[j].owner == "")
				return j;
		}
		return -1;
	}
	
	public boolean validName(String name) { 
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] != null) {
				if (clans[j].name.equalsIgnoreCase(name))
					return false;
			}		
		}
		return true;
	}
	
	public void addToClan(int playerId, int clanId) {
		if (clans[clanId] != null) {
			for (int j = 0; j < clans[clanId].members.length; j++) {
				if (clans[clanId].members[j] <= 0) {
					clans[clanId].members[j] = playerId;
					Server.playerHandler.players[playerId].clanId = clanId;
					Client c = (Client)Server.playerHandler.players[playerId];
					//c.sendMessage("You have joined the clan chat: " + clans[clanId].name);
					messageToClan(Server.playerHandler.players[playerId].playerName + " has joined the channel.", clanId);
					updateClanChat(clanId);
					return;
		} else {
			Client c = (Client)Server.playerHandler.players[playerId];
			Server.playerHandler.players[playerId].clanId = clanId;
			c.sendMessage("You already joined a clan, Dont try to join twice!");
				return;
				}
			}			
		}	
	}
	
	public void leaveClan(int playerId, int clanId) {
		if (clanId < 0) {
			Client c = (Client)Server.playerHandler.players[playerId];
			c.sendMessage("You are not in a clan.");
			return;		
		}
		if (clans[clanId] != null) {	
			for (int j = 0; j < clans[clanId].members.length; j++) {
				if (clans[clanId].members[j] == playerId) {
					clans[clanId].members[j] = -1;
				}
			}
			if (Server.playerHandler.players[playerId] != null) {
				Client c = (Client)Server.playerHandler.players[playerId];
				Server.playerHandler.players[playerId].clanId = -1;
				c.sendMessage("You have left the clan.");
				c.getPA().clearClanChat();
			}
			updateClanChat(clanId);
		}
	}
	
	public void destructClan(int clanId) {
		if (clanId < 0)
			return;
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clanId < 0)
				continue;
			if (clans[clanId].members[j] <= 0)
				continue;
			if (Server.playerHandler.players[clans[clanId].members[j]] != null) {
				Client c = (Client)Server.playerHandler.players[clans[clanId].members[j]];
				c.clanId = -1;
				c.getPA().clearClanChat();
			}	
		}
		clans[clanId].members = new int[50];
		clans[clanId].owner = "";
		clans[clanId].name = "";
	}
	
	public void messageToClan(String message, int clanId) {
		if (clanId < 0)
			return;
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] < 0)
				continue;
			if (Server.playerHandler.players[clans[clanId].members[j]] != null) {
				Client c = (Client)Server.playerHandler.players[clans[clanId].members[j]];
				c.sendMessage("@red@" + message);
			}
		}	
	}
	
	public void playerMessageToClan(int playerId, String message, int clanId)throws NullPointerException{
	Client z = (Client) Server.playerHandler.players[playerId];
		if (clanId < 0)
			return;
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] <= 0)
				continue;
			if (Server.playerHandler.players[clans[clanId].members[j]] != null) {
				Client c = (Client)Server.playerHandler.players[clans[clanId].members[j]];
				//c.sendMessage("["+Server.playerHandler.players[playerId].playerName+"] - " + message");
				//sendClan(String name, String message, String clan, int rights)
				c.sendClan(Server.playerHandler.players[playerId].playerName, message, clans[clanId].name, Server.playerHandler.players[playerId].playerRights);
		} else {
				if (clanId > 0)
					clanId = -1;
				z.sendMessage("You are not in a clan.");
			}
		}	
	}
	
	public void sendLootShareMessage(int clanId, String message) {
		if (clanId >= 0) {
			for (int j = 0; j < clans[clanId].members.length; j++) {
				if (clans[clanId].members[j] <= 0)
					continue;
				if (Server.playerHandler.players[clans[clanId].members[j]] != null) {
					Client c = (Client)Server.playerHandler.players[clans[clanId].members[j]];
					c.sendClan("Lootshare", message, clans[clanId].name, 2);
				}
			}
		}
	}
	
	
	public void handleLootShare(Client c, int item, int amount) {
		sendLootShareMessage(c.clanId, c.playerName + " has received " + amount + "x " + server.model.items.Item.getItemName(item) + ".");	
	}
	
}