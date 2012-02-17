package server.world;

import server.model.players.Client;
import server.util.Misc;
import server.Server;
import server.Connection;
import server.Config;

import java.util.ArrayList;
import server.model.players.PlayerHandler;

/**
 * @author Sanity, PJNoMore
 */

public class ClanChatHandler {

	public ClanChatHandler() {
	
	}
	
	public Clan[] clans = new Clan[100];
	
	
	public void handleClanChat(Client c, String name) {
		c.savedClan = name;
		if (c.clanId != -1) {
			c.sendMessage("You are already in a clan!");
			return;
		}
		for (int j = 0; j < clans.length; j++) {
			if (clans[j] != null) {
				if (clans[j].name.equalsIgnoreCase(name)) {
					if(checkPass(c, j)){
						addToClan(c.playerId, j);
						return;
					} else {
						c.sendMessage("Incorrect password!");
						return;
					}
				}
			}
		}
		makeClan(c, name);
	}
	
	
	public void makeClan(Client c, String name) {
		
		String pass = null;
		boolean loot = false;
		
		if (openClan() >= 0) {
			if (validName(name)) {
				c.clanId = openClan();
				createClan(c.playerName, name, pass, loot, false);
				addToClan(c.playerId, c.clanId);
				Server.pJClans.saveClan(c.playerName, name, pass, loot, false);
			} else {
				c.sendMessage("A clan with this name already exists.");
			}
		} else {
			c.sendMessage("Your clan chat request could not be completed.");
		}
	}
	
	public void mutePlayer(Client c, String name) {
		if (!isOwner(c)) {
			c.sendMessage("You do not have the power to do that!");
			return;
		}
		if (clans[c.clanId] != null) {
			for (int j = 0; j < clans[c.clanId].mutedMembers.length; j++) {
				for(int i = 0; j < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
						Client c2 = (Client)Server.playerHandler.players[i];
						if (!isInClan(c2)) {
							c.sendMessage(c2.playerName+" is not in your clan!");
							return;
						}
						if (clans[c.clanId].mutedMembers[j] <= 0) {
							clans[c.clanId].mutedMembers[j] = i;
							c2.sendMessage("You have been muted in: " + clans[c.clanId].name);
						}
					} else {
						c.sendMessage("This person is not online!");
					}
				}
			}			
		}
	}
	
	public void unmutePlayer(Client c, String name){
		if (!isOwner(c)) {
			c.sendMessage("You do not have the power to do that!");
			return;
		}
		if (clans[c.clanId] != null) {
			for (int j = 0; j < clans[c.clanId].mutedMembers.length; j++) {
				for(int i = 0; j < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
						Client c2 = (Client)Server.playerHandler.players[i];
						if (!isInClan(c2)) {
							c.sendMessage(c2.playerName+" is not in your clan!");
							return;
						}
						if (clans[c.clanId].mutedMembers[j] == i) {
							clans[c.clanId].mutedMembers[j] = -1;
							c2.sendMessage("You have been unmuted in: " + clans[c.clanId].name);
						}
					} else {
						c.sendMessage("This person is not online!");
					}
				}
			}			
		}
	}
	
	public boolean isOwner(Client c) {
		if (clans[c.clanId].owner.equalsIgnoreCase(c.playerName)) {
			return true;
		}
		if (c.playerRights >= 2) {
			return true;
		}
		return false;
	}
	
	public boolean isInClan(Client c) {
		for(int i = 0; i < clans.length; i++) {
			if(clans[i] != null){
				for(int j = 0; i < clans[i].members.length; j++) {
					if(clans[i].members[j] == c.playerId){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isClanMuted(Client c) {
		if (c.playerRights >= 2) {
			return false;
		}
		for(int i = 0; i < clans[c.clanId].mutedMembers[i]; i++) {
			if(clans[c.clanId].members[i] == c.playerId) {
				return true;
			}
		}
		return false;
	}
	
	public void createClan(String owner, String name, String pass, boolean lootshare, boolean hasPass) {
		if (openClan() >= 0) {
			if (validName(name)) {
				clans[openClan()] = new Clan (owner,name,pass,lootshare,hasPass);
			}
		}
	}
	
	public void setClanPassword(Client c, String pass, boolean hasPass) {
		if(isOwner(c)){
			clans[c.clanId].password = pass;
			clans[c.clanId].hasPassword = hasPass;
			c.sendMessage("Your new clan chat password is: "+pass);
		} else {
			c.sendMessage("You do not have the rights to change this clan's password.");
		}
	}
	
	
	
	public void changeOwner(Client c, String name) {
		if (c.clanId == -1) {
			c.sendMessage("You are not in a clan!");
			return;
		}
		if (c.playerRights >= 2) {
			clans[c.clanId].owner = name;
			updateClanChat(c.clanId);
		}
	}
	
	public void kickPlayerFromClan(Client c, String name) {
		if (!isOwner(c)) {
			c.sendMessage("You do not have the power to kick players from this clan chat!");
			return;
		}
		if (c.playerName.equalsIgnoreCase(name)){
			c.sendMessage("You may not kick yourself from a clan chat!");
			return;
		}
		if (c.clanId < 0) {
			c.sendMessage("You are not in a clan.");
			return;		
		}
		
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (Server.playerHandler.players[i] != null) {
				if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
					Client c2 = (Client)Server.playerHandler.players[i];
					if(c2.playerRights >= 2){
						c.sendMessage("You may @red@NOT@bla@ kick an admin from you clan!");
						c2.sendMessage(c.playerName+" has tried to kick you from his/her clan.");
						return;
					}
					c2.clanId = -1;
					c2.sendMessage("You have been kicked from "+clans[c.clanId].name+" by "+c.playerName);
					c2.getPA().clearClanChat();
					c.sendMessage("You have kicked "+c2.playerName+" from your clan.");
					for (int j = 0; j < clans[c.clanId].members.length; j++) {
						if (clans[c.clanId].members[j] == i) {
							clans[c.clanId].members[j] = -1;
						}
					}
				}
			}
		}
		updateClanChat(c.clanId);
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
	
	public boolean checkPass(Client c, int clanId){
			if(!clans[clanId].hasPassword){
				return true;
			}
			if(c.playerRights >= 2) {
				return true;
			}
			if(clans[clanId].owner.equalsIgnoreCase(c.playerName)) {
				return true;
			}
			if (clans[clanId].password.equalsIgnoreCase(c.clanPass)) {
				return true;
			}
		return false;
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
					}
				}			
			}	
	}

	
	
	public void leaveClan (int playerId, int clanId) {
		Client c = (Client)PlayerHandler.players[playerId];
		if(clanId < 0) {
			c.sendMessage("You are not in a clan!");
			return;
			}
		if(clans[clanId] !=null) {
			if(PlayerHandler.players[playerId].playerName.equalsIgnoreCase(clans[clanId].owner)) {
				messageToClan("The owner has left the Clan Chat.", clanId);
				c.getPA().clearClanChat();
				}
			for (int j = 0; j < clans[clanId].members.length; j++)
				if(clans[clanId].members[j] == playerId)
				   clans[clanId].members[j] = -1;
				  
			if(PlayerHandler.players[playerId] != null) {
				PlayerHandler.players[playerId].clanId = -1;
				c.sendMessage("You have left the clan.");
				c.getPA().clearClanChat();
			}
			updateClanChat(clanId);
		} else {
			PlayerHandler.players[playerId].clanId = -1;
			c.sendMessage("You are not in a clan.");
			}
		}

	/*public void leaveClan(int playerId, int clanId) {
		if (clanId < 0) {
			Client c = (Client)Server.playerHandler.players[playerId];
			c.sendMessage("You are not in a clan.");
			return;		
		}
		if (clans[clanId] != null) {
			if (Server.playerHandler.players[playerId] != null) {
				Client c = (Client)Server.playerHandler.players[playerId];
				Server.playerHandler.players[playerId].clanId = -1;
				c.sendMessage("You have left the clan.");
				c.getPA().clearClanChat();
				for (int j = 0; j < clans[clanId].members.length; j++) {
					if (clans[clanId].members[j] == playerId) {
						clans[clanId].members[j] = -1;
					}
				}
				for (int j = 0; j < clans[clanId].members.length; j++) {
					Client c2 = (Client)Server.playerHandler.players[clans[clanId].members[j]];
					c2.sendMessage(c.playerName+" has left the clan.");
				}
			}
			updateClanChat(clanId);
		} else {
			Client c = (Client)Server.playerHandler.players[playerId];
			Server.playerHandler.players[playerId].clanId = -1;
			c.sendMessage("You are not in a clan.");
		}
	}*/
	
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
		clans[clanId].password = null;
		clans[clanId].hasPassword = false;
	}
	
	public void messageToClan(String message, int clanId) {
		if (clanId < 0)
			return;
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] < 0)
				continue;
			if (Server.playerHandler.players[clans[clanId].members[j]] != null) {
				Client c = (Client)Server.playerHandler.players[clans[clanId].members[j]];
				c.sendMessage("@bla@" + message);
			}
		}	
	}
	
	public void playerMessageToClan(int playerId, String message, int clanId) {
		if (clanId < 0)
			return;
		if (Connection.isMuted((Client)Server.playerHandler.players[playerId])) {
			Client c = (Client)Server.playerHandler.players[playerId];
			c.sendMessage("You are muted and are not permitted to speak!");
			return;
		}
		if (isClanMuted((Client)Server.playerHandler.players[playerId])) {
			Client c = (Client)Server.playerHandler.players[playerId];
			c.sendMessage("You are muted in this clan and are not permitted to speak!");
			return;
		}
		for (int j = 0; j < clans[clanId].members.length; j++) {
			if (clans[clanId].members[j] <= 0)
				continue;
			if (Server.playerHandler.players[clans[clanId].members[j]] != null) {
				Client c = (Client)Server.playerHandler.players[clans[clanId].members[j]];
				c.sendClan(Server.playerHandler.players[playerId].playerName, message, clans[clanId].name, Server.playerHandler.players[playerId].playerRights);
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