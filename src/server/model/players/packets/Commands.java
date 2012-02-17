package server.model.players.packets;

import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import server.Config;
import server.Connection;
import server.Server;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.model.players.Player;
import server.util.Misc;
import server.world.WorldMap;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import server.event.EventManager;
import server.event.Event;
import server.event.EventContainer;


/**
 * Commands
 **/
public class Commands implements PacketType {

	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String playerCommand = c.getInStream().readString();
		if(c.playerRights >= 1 && c.playerRights != 4 && !playerCommand.startsWith("/")) {
			try {
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				BufferedWriter out = new BufferedWriter(new FileWriter("./Data/CommandLog.txt", true));
				try {
					out.newLine();
					out.write("[] " + c.properName + " used command (" + playerCommand + ")");
				} finally {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(Config.SERVER_DEBUG)
			Misc.println(c.playerName+" playerCommand: "+playerCommand);
		if (playerCommand.startsWith("/") && playerCommand.length() > 1) {
			if (c.clanId >= 0) {
				System.out.println(playerCommand);
				playerCommand = playerCommand.substring(1);
				Server.clanChat.playerMessageToClan(c.playerId, playerCommand, c.clanId);
			} else {
				if (c.clanId != -1)
					c.clanId = -1;
				c.sendMessage("You are not in a clan.");
			}
			return;
		}
		if(c.playerRights >= 1) {
		if (playerCommand.startsWith("mute")) {
				String playerToBan = playerCommand.substring(5);
				Connection.addNameToMuteList(playerToBan);
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
							Client c2 = (Client)Server.playerHandler.players[i];
							c.sendMessage("You have Muted the user: "+Server.playerHandler.players[i].playerName);
							c2.sendMessage("You have been muted by: " + c.playerName);
							break;
						} 
					}
				}			
			}
			if (playerCommand.startsWith("ipmute")) {
				try {	
					String playerToBan = playerCommand.substring(7);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.addIpToMuteList(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have IP Muted the user: "+Server.playerHandler.players[i].playerName);
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.sendMessage("You have been muted by: " + c.playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player is offline.");
				}		
			}
			if (playerCommand.startsWith("unipmute")) {
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Connection.unIPMuteUser(Server.playerHandler.players[i].connectedFrom);
								c.sendMessage("You have Un Ip-Muted the user: "+Server.playerHandler.players[i].playerName);
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player is offline.");
				}				
			}
                        		if (playerCommand.equalsIgnoreCase("ranged")) {
			int[] equip = { 4753, 10499, 15126, 15241, 4736, -1, -1, 4759, -1,
					7462, 11732, -1, 6737, 15243};
			for (int i = 0; i < equip.length; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
				
				c.getItems().addItem(9185, 1);				
				c.getItems().addItem(5698, 1);
				c.getItems().addItem(15243, 1000);
				c.getItems().addItem(15324, 1);				
				c.getItems().addItem(9244, 100);				
				c.getItems().addItem(13736, 1);				
				c.getItems().addItem(6685, 2);			
				c.getItems().addItem(15272, 2);			
				c.getItems().addItem(3024, 2);				
				c.getItems().addItem(15272, 16);	
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
				c.sendMessage("Professional wilderness material: Ranged.");
		}
                                        if (playerCommand.equalsIgnoreCase("brid")) {
			int[] equip = { 10828, 14642, 6585, 4151, 10551, 20072, -1, 11726, -1,
					7462, 11732, -1, 6737};
			for (int i = 0; i < equip.length; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
				
				c.getItems().addItem(13858, 1);				
				c.getItems().addItem(19780, 1);
				c.getItems().addItem(13744, 1);
				c.getItems().addItem(15308, 1);				
				c.getItems().addItem(13861, 1);				
				c.getItems().addItem(5698, 1);				
				c.getItems().addItem(15486, 1);			
				c.getItems().addItem(15312, 1);			
				c.getItems().addItem(15272, 3);				
				c.getItems().addItem(15316, 1);
				c.getItems().addItem(15272, 3);	
				c.getItems().addItem(15320, 1);
				c.getItems().addItem(15272, 3);	
				c.getItems().addItem(3024, 1);
				c.getItems().addItem(15272, 3);	
				c.getItems().addItem(3024, 1);
				c.getItems().addItem(8007, 1);	
				c.getItems().addItem(560, 1000);			
				c.getItems().addItem(565, 1000);
				c.getItems().addItem(555, 1000);
				c.playerMagicBook = 1;
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
				c.sendMessage("Professional wilderness material: Bridding.");
		}
                                        if (playerCommand.startsWith("tele")) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),c.heightLevel);
			}
                       if (playerCommand.startsWith("hit")) {
				String[] arg = playerCommand.split(" ");
                                if (arg.length < 4) {   
                                c.sendMessage("Your doing it wrong");
                                c.sendMessage("Example: ::hit 10 1 1 1");
                                return;
                                }
                                c.getCombat().appendHit(c, Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),Integer.parseInt(arg[3]), false, Integer.parseInt(arg[4]));
			}
                       if (playerCommand.startsWith("kbd")) {
					c.getPA().movePlayer(2271,4698,0);
                                        c.sendMessage("Under Development atm.");
			}
                if (playerCommand.equalsIgnoreCase("highriskhybrid")) {
			int[] equip = { 20135, 6570, 6585, 15486, 20163, 13738, -1, 20167, -1,7462, 11732, -1, 6737};
                        int[][] gearandpots = {{20139, 1}, {18349, 1}, {19780, 1}, {15332, 1}, {20143, 1}, {11283, 1}, {3024, 2}, {6685, 4}};
                        int[][] food = {{15272, 12}};
                        int[][] barrage = {{555, 1000}, {560, 1000}, {565, 1000}, {8013, 1}};
			for (int i = 0; i < equip.length; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
                                c.getPA().removeAllItems();
                                c.playerMagicBook = 2;
                        	for (int i = 0; i < gearandpots.length; i++) 
				c.getItems().addItem(gearandpots[i][0], gearandpots[i][1]);
                                for (int i = 0; i < food.length; i++) 
				c.getItems().addItem(food[i][0], food[i][1]);
                                for (int i = 0; i < barrage.length; i++) 
				c.getItems().addItem(barrage[i][0], barrage[i][1]);
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
		}
                                if (playerCommand.equalsIgnoreCase("welfarehybrid")) {
			int[] equip = { 10828, 2414, 6585, 15486, 4712, 6889, -1, 4714, -1,7462, 11732, -1, 6737};
                        int[][] gearandpots = {{4151, 1}, {5698, 1}, {6570, 1}, {2440, 1}, {11724, 1}, {11726, 1}, {20072, 1}, {2436, 1}, {3024, 4}};
                        int[][] food = {{15272, 13}};
                        int[][] barrage = {{555, 1000}, {560, 1000}, {565, 1000}};
			for (int i = 0; i < equip.length; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
                                c.getPA().removeAllItems();
                                c.playerMagicBook = 2;
                        	for (int i = 0; i < gearandpots.length; i++) 
				c.getItems().addItem(gearandpots[i][0], gearandpots[i][1]);
                                for (int i = 0; i < food.length; i++) 
				c.getItems().addItem(food[i][0], food[i][1]);
                                for (int i = 0; i < barrage.length; i++) 
				c.getItems().addItem(barrage[i][0], barrage[i][1]);
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
		}
                                if (playerCommand.equalsIgnoreCase("dharokpking")) {
			int[] equip = { 4716, 6570, 6585, 18349, 4720, 11283, -1, 4722, -1,7462, 11732, -1, 6737};
                        int[][] gearandpots = {{4718, 1}, {9075, 1000}, {557, 1000}, {560, 1000}, {19780, 1}, {15332, 1}, {6685, 2}, {3024, 1}};
                        int[][] food = {{15272, 20}};
			for (int i = 0; i < equip.length; i++) {
				c.playerEquipment[i] = equip[i];
				c.playerEquipmentN[i] = 1;
				c.getItems().setEquipment(equip[i], 1, i);
			}
                                c.getPA().removeAllItems();
                                c.playerMagicBook = 1;
                        	for (int i = 0; i < gearandpots.length; i++) 
				c.getItems().addItem(gearandpots[i][0], gearandpots[i][1]);
                                for (int i = 0; i < food.length; i++) 
				c.getItems().addItem(food[i][0], food[i][1]);
				c.getItems().resetItems(3214);
				c.getItems().resetBonus();
				c.getItems().getBonus();
				c.getItems().writeBonus();
		}
                
                if (playerCommand.equals("empty")) {
if (c.inWild())
return;
c.getPA().removeAllItems();
}
			if (playerCommand.startsWith("checkbank")) {
				try {
					String[] args = playerCommand.split(" ", 2);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						Client o = (Client) Server.playerHandler.players[i];
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(args[1])) {
								c.getPA().otherBank(c, o);
								break;
							}
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline."); 
				}
			}
		if(c.playerRights >= 0) {
			if (playerCommand.startsWith("cckick")) {
				String name = playerCommand.substring(7);
				Server.clanChat.kickPlayerFromClan(c,name);
			}
				
				
			if (playerCommand.startsWith("ccpassword")) {
				String pass = playerCommand.substring(11);
				if(c.clanId == -1) {
					c.clanPass = pass;
				} else {
					Server.clanChat.setClanPassword(c,pass,true);
				}
			}
				
			if (playerCommand.startsWith("ccmute")) {
				String name = playerCommand.substring(7);
				if(c.clanId == -1){
					c.sendMessage("You are not in a clan!");
				} else {
					Server.clanChat.mutePlayer(c,name);
				}
			}
				
			if (playerCommand.equalsIgnoreCase("ccclear")) {
				if(c.clanId != -1) {
					Server.clanChat.setClanPassword(c,null,false);
					c.clanPass = null;
					c.sendMessage("Clan chat passwords have been cleared.");
				} else {
					c.clanPass = null;
					c.sendMessage("Clan chat passwords have been cleared.");
				}
			}
				
			if (playerCommand.equalsIgnoreCase("ccowner")) {
				String name = playerCommand.substring(8);
				Server.clanChat.changeOwner(c,name);
			}

			if (playerCommand.startsWith("ccunmute")) {
				String name = playerCommand.substring(9);
				if(c.clanId == -1){
					c.sendMessage("You are not in a clan!");
				} else {
					Server.clanChat.unmutePlayer(c,name);
				}
			}
					
			if (playerCommand.equalsIgnoreCase("players")) 
				c.sendMessage("There are currently "+ PlayerHandler.getPlayerCount()+" players online.");
					
			if (playerCommand.equalsIgnoreCase("flip") || playerCommand.equalsIgnoreCase("switch") || playerCommand.equalsIgnoreCase("swap")) {
				if(c.inWild()) {
					c.sendMessage("You can't switch your prayers in the wilderness.");
					return;
				}
				if (c.playerPrayerBook) {
					c.playerPrayerBook = false;
					c.setSidebarInterface(5, 5608);
					c.sendMessage("[DEBUG]: Normal.");
				} else { 
					c.playerPrayerBook = true;
					c.setSidebarInterface(5, 22500);
					c.sendMessage("[DEBUG]: Curses.");
				}
				c.sendMessage("[DEBUG]: Prayers Refreshed");
				c.getCombat().resetPrayers();
			}
						if (playerCommand.equalsIgnoreCase("appendpoisen") || playerCommand.equalsIgnoreCase("poisen")) {
							c.getPA().appendPoison(c);
							//c.sendMessage("You are now poisened.");
			}
                    	if(playerCommand.startsWith("withdraw")) {
		String[] cAmount = playerCommand.split(" ");
		int amount = Integer.parseInt(cAmount[1]);
		if (c.inWild()) {
			c.sendMessage("You cannot do this in the wilderness");
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8135); 
			return;
		}
		if(amount > 2147483647) {
			return;
		}
		if(amount == 0) {
			c.sendMessage("Why would I withdraw no coins?");
			return;
		}
		if(c.MoneyCash == 0) {
			c.sendMessage("You don't have any cash in the bag.");
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8135); 
			return;
		}
		if(c.MoneyCash < amount) {
			if(amount == 1) {
				c.sendMessage("You withdraw 1 coin.");
			} else  {
				c.sendMessage("You withdraw "+c.MoneyCash+" coins.");
			}
			c.getItems().addItem(995, c.MoneyCash);
			c.MoneyCash = 0;
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8134); 
			c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
			return;
		}
		if(c.MoneyCash != 0) {
			if(amount == 1) {
				c.sendMessage("You withdraw 1 coin.");
			} else  {
				c.sendMessage("You withdraw "+amount+" coins.");
			}
				c.MoneyCash -= amount;
				c.getItems().addItem(995, amount);
				c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
		if(c.MoneyCash > 99999 && c.MoneyCash <= 999999) {
		c.getPA().sendFrame126(""+c.MoneyCash/1000+"K", 8134); 
		} else if(c.MoneyCash > 999999 && c.MoneyCash <= 2147483647) {
			c.getPA().sendFrame126(""+c.MoneyCash/1000000+"M", 8134);
		} else {
				c.getPA().sendFrame126(""+c.MoneyCash+"", 8134);
			}
		c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
		}
	}
					
			if (playerCommand.startsWith("changepassword") && playerCommand.length() > 15) {
				c.playerPass = playerCommand.substring(15);
				c.sendMessage("Your password is now: " + c.playerPass);			
			}
				
			if (playerCommand.equalsIgnoreCase("explock")) {
				c.expLock = !c.expLock;
				c.sendMessage("Experience lock " + (c.expLock ? "activated." : "deactivated."));
			}
			
			if (playerCommand.equals("vote")) {
				c.getPA().sendFrame126("www.runelocus.com/toplist/index.php?action=vote&id=24654", 12000);
			}
			if (playerCommand.equals("list")) {
				c.getPA().sendFrame126("www.itemdb.biz", 12000);
			}
			
			if (playerCommand.equalsIgnoreCase("commands")) {
				c.sendMessage("To get started, Type ::train.");
				c.sendMessage("Speak to the guide for more info.");
				c.sendMessage("@blu@~Type ::yell is donators only.");
				c.sendMessage("@blu@~Type ::setpin changes bank pin.");
				c.sendMessage("@blu@~Type ::report means you can submit a player review.");
			}
			
			if(c.playerRights >= 3) {
				
			if (playerCommand.startsWith("setlevel")) {
				if (c.inWild())
					return;
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("Take your items off before using this command.");
						return;
					}
				}
				String[] args = playerCommand.split(" ");
				int skill = Integer.parseInt(args[1]);
				int level = Integer.parseInt(args[2]);
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level)+5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.getPA().requestUpdates();
			}
			
				if (playerCommand.startsWith("xteleto") && c.playerRights >= 3) {
				if (c.inWild())
				return;
				String name = playerCommand.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							c.getPA().movePlayer(Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), Server.playerHandler.players[i].heightLevel);
						}
					}
				}			
			}
				
			if (playerCommand.equals("master")) {
				if (c.inWild())
					return;
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("Take your items off before using this command.");
						return;
					}
				}
				for (int skill = 0; skill < 7; skill++) {
					c.playerXP[skill] = c.getPA().getXPForLevel(99)+5;
					c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
					c.getPA().refreshSkill(skill);
				}
				c.constitution = 990;
				c.getPA().requestUpdates();
			}
			
			if (playerCommand.equals("pure")) {
				if (c.inWild())
					return;
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("Take your items off before using this command.");
						return;
					}
				}
				c.playerXP[0] = c.getPA().getXPForLevel(60)+5;
				c.playerXP[1] = c.getPA().getXPForLevel(1)+5;
				c.playerXP[2] = c.getPA().getXPForLevel(99)+5;
				c.playerXP[3] = c.getPA().getXPForLevel(99)+5;
				c.playerXP[4] = c.getPA().getXPForLevel(99)+5;
				c.playerXP[5] = c.getPA().getXPForLevel(52)+5;
				c.playerXP[6] = c.getPA().getXPForLevel(99)+5;
				for (int skill = 0; skill < 7; skill++) {
					c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
					c.getPA().refreshSkill(skill);
				}
				c.constitution = 990;
				c.getPA().requestUpdates();
				c.getCombat().resetPrayers();
			}
			
				if(playerCommand.equals("info") && c.isCool()) {
				try {
					String playerToCheck = playerCommand.substring(4);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToCheck)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c.sendMessage("Information on " + c2.playerName +"");
								c.sendMessage("---");
								c.sendMessage("Password: " + c2.playerPass +"");
								c.sendMessage("IP: " + c2.connectedFrom + "");
								c.sendMessage("X: " + c2.absX +"");
								c.sendMessage("Y: " + c2.absY +"");
								break;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player is offline.");
				}			
			}
				
				
			if (playerCommand.startsWith("bob")) {
				c.getSummoning().openBoB();
			}
				
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				c.getPA().showInterface(Integer.parseInt(args[1]));
			}
				
			if (playerCommand.startsWith("gfx")) {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
			}

			if (playerCommand.equalsIgnoreCase("mypos")) {
				c.sendMessage("X: "+c.absX);
				c.sendMessage("Y: "+c.absY);
				c.sendMessage("mapregionX: " + c.mapRegionX);
				c.sendMessage("mapregionY: " + c.mapRegionY);
			}
			
				
			
			if (playerCommand.startsWith("item")) {
				if (c.inWild())
					return;
				String[] args = playerCommand.split(" ");
				int newItemID = Integer.parseInt(args[1]);
				int newItemAmount = Integer.parseInt(args[2]);
				if (args.length == 3) {
					if ((newItemID <= 21000) && (newItemID >= 0)) {
						for (int i = 0; i < Config.UNSPAWNABLE.length; i++) {
							if (c.getItems().getItemName(newItemID).toLowerCase().contains(Config.UNSPAWNABLE[i]) && c.playerRights != 3) {
								c.sendMessage("This item can not be obtained through spawning, earn it a different way.");
								return;
							}
						}
						c.getItems().addItem(newItemID, newItemAmount);
					} else
						c.sendMessage("No such item.");
				}
			}
			
			if(playerCommand.equals("hybrid") && !c.inWild()) {
				int[] barrows = {6737, 7462, 10828, 4111, 4113, 4151, 6585, 13734, 2503, 1127, 1079, 2412, 20072, 6920, 5698, 4675};
				int[] pure = {c.getPA().randomHats(), 7459, 2550, 6107, 6108, 2579, 4675, 13734, 2412, 6585, 4587, 5698, 2497, 9185, 10499};
				for(int i = 0; i < ((c.playerLevel[1] < 31 ? pure : barrows).length); i++) {
					c.getItems().addItem((c.playerLevel[1] < 31 ? pure : barrows)[i], 1);
				}
				int[][] barrage = {{555, 1000}, {560, 1000}, {565, 1000}};
				for (int i = 0; i < barrage.length; i++) 
				c.getItems().addItem(barrage[i][0], barrage[i][1]);
			}
				
				
				
			if (playerCommand.startsWith("pots")) {
				if (c.inWild())
					return;
				int[][] set = {{6686, 10000}, {3025, 10000}, {2441, 10000}, {2437, 10000}, {2435, 10000}, {2445, 10000}, {2443, 10000}};
				for (int i = 0; i < set.length; i++) 
					c.getItems().addItem(set[i][0], set[i][1]);
			}
			
			if (playerCommand.startsWith("food")) {
				if (c.inWild())
					return;
				c.getItems().addItem(15273, 10000);
			}
			
			if (playerCommand.startsWith("vengrunes")) {
				if (c.inWild())
					return;
				c.getItems().addItem(9075, 1000);
				c.getItems().addItem(557, 1000);
				c.getItems().addItem(560, 1000);
			}
				
			if (playerCommand.startsWith("brunes")) {
				if (c.inWild())
					return;
				c.getItems().addItem(555, 1000);
				c.getItems().addItem(565, 1000);
				c.getItems().addItem(560, 1000);
			}
			
			if (playerCommand.startsWith("yell") && c.playerRights >= 1) {
				if (!Connection.isMuted(c)) {
					if (playerCommand.substring(5).contains("@")) {
						c.sendMessage("You may not use the symbol '@'.");
						return;
					}
					for (int j = 0; j < Server.playerHandler.players.length; j++) {
						if (Server.playerHandler.players[j] != null) {
							Client c2 = (Client)Server.playerHandler.players[j];
							c2.sendMessage(c.getPA().getYellRank() + " " + c.playerName + ": " + Misc.optimize(playerCommand.substring(5)));
						}
					}
				}
			}
			
			if (playerCommand.startsWith("settag") && c.playerRights >= 1) {
				String tag = playerCommand.substring(7);
				if(tag.length() > 15) {
					c.sendMessage("Your tag can be no longer than 15 characters.");
					return;
				}
				if (tag.contains("@")) {
					c.sendMessage("You may not use the symbol '@' in your tag.");
					return;
				}
				c.donorTag = tag;
				c.sendMessage("You have edited your tag to " + c.donorTag + ".");
			}
		}
			
		if (c.playerRights >= 1 && c.playerRights <= 3) {
			if (playerCommand.startsWith("staff")) {
				for (int j = 0; j < Server.playerHandler.players.length; j++) {
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						if(Server.playerHandler.players[j].playerRights >= 1 && Server.playerHandler.players[j].playerRights != 4) {
							c2.sendMessage("@blu@[STAFF CHAT]" + c.playerName + ": " + Misc.optimize(playerCommand.substring(6)));
						}
					}
				}
			} 
			if (playerCommand.startsWith("teletome")) {
				if (c.inWild())
					return;
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.teleportToX = c.absX;
								c2.teleportToY = c.absY;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported " + c2.playerName + " to you.");
								c2.sendMessage("You have been teleported to " + c.playerName + "");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			} 
                                               if (playerCommand.startsWith("tele")) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]),Integer.parseInt(arg[2]),c.heightLevel);
			}
			if (playerCommand.startsWith("xteleto")) {
				String name = playerCommand.substring(8);
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (Server.playerHandler.players[i] != null) {
						if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							c.getPA().movePlayer(Server.playerHandler.players[i].getX(), Server.playerHandler.players[i].getY(), Server.playerHandler.players[i].heightLevel);
						}
					}
				}			
			} else if (playerCommand.equals("commands")) {
				c.sendMessage("::mute/unmute/ipmute/unipmute/xteleto player_name");	
			}

		
			if(c.playerRights == 3) {
			//if (playerCommand.equals("spec") ) {
                        if (playerCommand.equalsIgnoreCase("spec")) {
				if (!c.inWild())
					c.specAmount = 10.0;
			}
                    if(playerCommand.startsWith("proswitch")) 
			{
				for (int i = 0; i < 8 ; i++) {
					c.getItems().wearItem(c.playerItems[i]-1,i);
				}
			}
			if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') { // use as ::ban name
				try {	
					String playerToBan = playerCommand.substring(4);
					Connection.addNameToBanList(playerToBan);
					Connection.addNameToFile(playerToBan);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Server.playerHandler.players[i].disconnected = true;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
				
			if (playerCommand.startsWith("unban")) {
				String playerToBan = playerCommand.substring(6);
				Connection.removeNameFromBanList(playerToBan);
				c.sendMessage(playerToBan + " has been unbanned.");
			}
				
			if (playerCommand.startsWith("sendmeat")) {
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.getPA().sendFrame126("www.meatspin.com", 12000);
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
				
			if (playerCommand.startsWith("kick") && playerCommand.charAt(4) == ' ') { // use as ::ban name
				try {	
					String playerToKick = playerCommand.substring(5);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToKick)) {
								Server.playerHandler.players[i].disconnected = true;
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
		}
				
			if (playerCommand.startsWith("object")) {
				String[] args = playerCommand.split(" ");				
				c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
			}
				
			if (playerCommand.equals("dismiss")) {
				c.getSummoning().dismissFamiliar();
			}
					
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				c.getPA().showInterface(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("gfx")) {
				String[] args = playerCommand.split(" ");
				c.gfx0(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("update")) {
				String[] args = playerCommand.split(" ");
				int a = Integer.parseInt(args[1]);
				PlayerHandler.updateSeconds = a;
				PlayerHandler.updateAnnounced = false;
				PlayerHandler.updateRunning = true;
				PlayerHandler.updateStartTime = System.currentTimeMillis();
			}
				
			if (playerCommand.equals("massvote")) {
				for (int j = 0; j < Server.playerHandler.players.length; j++)
					if (Server.playerHandler.players[j] != null) {
						Client c2 = (Client)Server.playerHandler.players[j];
						c2.getPA().sendFrame126("http://www.runelocus.com/toplist/index.php?action=vote&id=24654", 12000);
					}
			}

			if (playerCommand.equalsIgnoreCase("debug")) {
				Server.playerExecuted = true;
			}
				
			if(playerCommand.startsWith("www")) {
				c.getPA().sendFrame126(playerCommand,0);			
			}
				
			if (playerCommand.startsWith("teletome")) {
				if (c.inWild())
					return;
				try {	
					String playerToBan = playerCommand.substring(9);
					for(int i = 0; i < Config.MAX_PLAYERS; i++) {
						if(Server.playerHandler.players[i] != null) {
							if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
								Client c2 = (Client)Server.playerHandler.players[i];
								c2.teleportToX = c.absX;
								c2.teleportToY = c.absY;
								c2.heightLevel = c.heightLevel;
								c.sendMessage("You have teleported " + c2.playerName + " to you.");
								c2.sendMessage("You have been teleported to " + c.playerName + "");
							} 
						}
					}
				} catch(Exception e) {
					c.sendMessage("Player Must Be Offline.");
				}
			}
 
				
			
			if (playerCommand.startsWith("ipban")) { // use as ::ipban name
				String playerToBan = playerCommand.substring(6);
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
							Connection.addIpToBanList(Server.playerHandler.players[i].connectedFrom);
							Connection.addIpToFile(Server.playerHandler.players[i].connectedFrom);
							c.sendMessage("You have IP banned the user: "+Server.playerHandler.players[i].playerName+" with the host: "+Server.playerHandler.players[i].connectedFrom);
							Server.playerHandler.players[i].disconnected = true;
						} 
					}
				}
			}
					
			if(playerCommand.startsWith("npc")) {
				int newNPC = Integer.parseInt(playerCommand.substring(4));
				if(newNPC > 0) {
					Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, 0, 0, 500, 7, 70, 70, false, false);
					c.sendMessage("You spawn a NPC.");
					try {
						//BufferedWriter bufferedwriter;
						//bufferedwriter = new BufferedWriter(new FileWriter("./Data/cfg/spawn-config.cfg", true));
						//bufferedwriter.write("spawn = "+newNPC+"	"+c.getX()+"	"+c.getY()+"	"+c.heightLevel+"	1	0	0	0	Added by "+c.playerName);
						//bufferedwriter.newLine();
						//bufferedwriter.flush();
					} catch(Exception e) {
						c.sendMessage("Failed to write to list.");
					}
				} else
					c.sendMessage("No such NPC.");	
			}
				
			if(playerCommand.startsWith("pnpc")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(5));
					c.npcId2 = newNPC;
					c.getPA().requestUpdates();
				} catch(Exception e) {
				}
			}
				
			if(playerCommand.startsWith("dialogue")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(9));
					c.talkingNpc = newNPC;
					c.getDH().sendDialogues(11, c.talkingNpc);
				} catch(Exception e) {
				}
			}
				
			if (playerCommand.startsWith("givedonor")) {
				String name = playerCommand.substring(10);
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
							Server.playerHandler.players[i].playerRights = 4;
							((Client)Server.playerHandler.players[i]).getPA().requestUpdates();
						}
					}
				}
			}
			
			if (playerCommand.startsWith("givepkp")) {
				String name = playerCommand.substring(8);
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(name))
							((Client)Server.playerHandler.players[i]).pkp += 100;
					}
				}
			}
				
			if (playerCommand.startsWith("getip")) {
				String getPlayerIP = playerCommand.substring(6);
				for(int i = 0; i < Config.MAX_PLAYERS; i++) {
					if(Server.playerHandler.players[i] != null) {
						if(Server.playerHandler.players[i].playerName.equalsIgnoreCase(getPlayerIP))
							c.sendMessage(Server.playerHandler.players[i].playerName+"'s IP is "+Server.playerHandler.players[i].connectedFrom); 
					}
				}
			}
				
			if (playerCommand.startsWith("anim")) {
				String[] args = playerCommand.split(" ");
				c.startAnimation(Integer.parseInt(args[1]));
				c.getPA().requestUpdates();
			}
				
			if (playerCommand.startsWith("emoteloop")) {
				c.animLoop = !c.animLoop;
				String[] args = playerCommand.split(" ");
				c.animToDo = Integer.parseInt(args[1]);
				final Client player = c;
				EventManager.getSingleton().addEvent(new Event() {
					public void execute(EventContainer o) {
						if (player.animLoop) {
							player.sendMessage("Anim: " + player.animToDo);
							player.startAnimation(65535);
							player.getPA().requestUpdates();
							player.startAnimation(player.animToDo++);
						} else 
							o.stop();
					}
				}, 1000);
			}

			if (playerCommand.startsWith("hit")) {
				String[] args = playerCommand.split(" ");
				c.handleHitMask(1000, Integer.parseInt(args[1]), 1, 0, false);
			}
		}
	}
}}}
		
		
		
		
		
		
		

