package server.model.players;

import server.Config;
import server.Server;
import server.model.npcs.NPCHandler;
import server.model.npcs.NPC;
import java.util.Timer;
import java.util.TimerTask;
import server.event.EventContainer;
import server.event.EventManager;
import server.event.Event;
import server.util.Misc;
import server.world.map.VirtualWorld;
import server.model.players.Player;
import server.world.WorldMap;
import server.model.players.PathFinder;
import server.clip.region.Region;


public class PlayerAssistant{

	private Client c;
	public PlayerAssistant(Client Client) {
		this.c = Client;
	}
	
	public int CraftInt, Dcolor, FletchInt;
	
	/**
	 * MulitCombat icon
	 * @param i1 0 = off 1 = on
	 */
	public void multiWay(int i1) {
		synchronized(c) {
			c.outStream.createFrame(61);
			c.outStream.writeByte(i1);
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	public void clearClanChat() {
		c.clanId = -1;
		c.getPA().sendFrame126("Talking in: ", 18139);
		c.getPA().sendFrame126("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++)
			c.getPA().sendFrame126("", j);
	}
	public int backupItems[] = new int[Config.BANK_SIZE];
	public int backupItemsN[] = new int[Config.BANK_SIZE];

	public void otherBank(Client c, Client o) {
		if(o == c || o == null || c == null)
		{
		return;
		}

		for (int i = 0; i < o.bankItems.length; i++)
		{
			backupItems[i] = c.bankItems[i]; backupItemsN[i] = c.bankItemsN[i];
			c.bankItemsN[i] = o.bankItemsN[i]; c.bankItems[i] = o.bankItems[i];
		}
			openUpBank();

		for (int i = 0; i < o.bankItems.length; i++)
		{
		c.bankItemsN[i] = backupItemsN[i]; c.bankItems[i] = backupItems[i];
		}
	}
	
	public void resetAutocast() {
		c.autocastId = 0;
		c.autocasting = false;
		c.getPA().sendFrame36(108, 0);
		c.sendMessage(":resetautocast:");
	}
		public void handleObjectRegion(int objectId, int minX, int minY, int maxX, int maxY) {
		for (int i = minX; i < maxX+1; i++) {
			for (int j = minY; j < maxY+1; j++) {
				c.getPA().object(objectId, i, j, -1, 10);
			}
		}
	}
	        	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		c.getItems().resetItems(3214);
	}
	public boolean itemUsedInRegion(int minX, int maxX, int minY, int maxY) {
		return (c.objectX >= minX && c.objectX <= maxX) && (c.objectY >= minY && c.objectY <= maxY);
	}
	
	public void sendFrame126(String s, int id) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null ) {
				c.getOutStream().createFrameVarSizeWord(126);
				c.getOutStream().writeString(s);
				c.getOutStream().writeWordA(id);
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}
	}
	
	
	public void sendLink(String s) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null ) {
				c.getOutStream().createFrameVarSizeWord(187);
				c.getOutStream().writeString(s);
			}
		}	
	}
	
	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(134);
				c.getOutStream().writeByte(skillNum);
				c.getOutStream().writeDWord_v1(XP);
				c.getOutStream().writeByte(currentLevel);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame106(int sideIcon) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(106);
				c.getOutStream().writeByteC(sideIcon);
				c.flushOutStream();
				requestUpdates();
			}
		}
	}
	
	public void sendFrame107() {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(107);
				c.flushOutStream();
			}
		}
	}
	public void sendFrame36(int id, int state) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(36);
				c.getOutStream().writeWordBigEndian(id);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame185(int Frame) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(185);
				c.getOutStream().writeWordBigEndianA(Frame);
			}
		}
	}
	
	public void showInterface(int interfaceid) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(97);
				c.getOutStream().writeWord(interfaceid);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame248(int MainFrame, int SubFrame) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(248);
				c.getOutStream().writeWordA(MainFrame);
				c.getOutStream().writeWord(SubFrame);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(246);
				c.getOutStream().writeWordBigEndian(MainFrame);
				c.getOutStream().writeWord(SubFrame);
				c.getOutStream().writeWord(SubFrame2);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame171(int MainFrame, int SubFrame) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(171);
				c.getOutStream().writeByte(MainFrame);
				c.getOutStream().writeWord(SubFrame);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame200(int MainFrame, int SubFrame) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(200);
				c.getOutStream().writeWord(MainFrame);
				c.getOutStream().writeWord(SubFrame);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame70(int i, int o, int id) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(70);
				c.getOutStream().writeWord(i);
				c.getOutStream().writeWordBigEndian(o);
				c.getOutStream().writeWordBigEndian(id);
				c.flushOutStream();
			}
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(75);
				c.getOutStream().writeWordBigEndianA(MainFrame);
				c.getOutStream().writeWordBigEndianA(SubFrame);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame164(int Frame) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(164);
				c.getOutStream().writeWordBigEndian_dup(Frame);
				c.flushOutStream();
			}
		}
	}
	
	public void setPrivateMessaging(int i) { // friends and ignore list status
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
		        c.getOutStream().createFrame(221);
		        c.getOutStream().writeByte(i);
				c.flushOutStream();
			}
		}
    }
	
	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(206);
				c.getOutStream().writeByte(publicChat);	
				c.getOutStream().writeByte(privateChat);	
				c.getOutStream().writeByte(tradeBlock);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame87(int id, int state) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(87);
				c.getOutStream().writeWordBigEndian_dup(id);	
				c.getOutStream().writeDWord_v1(state);
				c.flushOutStream();
			}
		}
	}
	
	public void sendPM(long name, int rights, byte[] chatmessage, int messagesize) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSize(196);
				c.getOutStream().writeQWord(name);
				c.getOutStream().writeDWord(c.lastChatId++);
				c.getOutStream().writeByte(rights);
				c.getOutStream().writeBytes(chatmessage, messagesize, 0);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
				String chatmessagegot = Misc.textUnpack(chatmessage, messagesize);
				String target = Misc.longToPlayerName(name);
			}	
		}
	}
	
	public void createPlayerHints(int type, int id) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(254);
				c.getOutStream().writeByte(type);
				c.getOutStream().writeWord(id); 
				c.getOutStream().write3Byte(0);
				c.flushOutStream();
			}
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(254);
				c.getOutStream().writeByte(pos);
				c.getOutStream().writeWord(x);
				c.getOutStream().writeWord(y);
				c.getOutStream().writeByte(height);
				c.flushOutStream();
			}
		}
	}
	
	public void loadPM(long playerName, int world) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				if(world != 0) {
		            world += 9;
				} else if(!Config.WORLD_LIST_FIX) {
					world += 1;
				}	
				c.getOutStream().createFrame(50);
				c.getOutStream().writeQWord(playerName);
				c.getOutStream().writeByte(world);
				c.flushOutStream();
			}
		}
	}
	
	public void removeAllWindows() {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getPA().resetVariables();
				c.getOutStream().createFrame(219);
				c.flushOutStream();
			}
		}
	}
	
	public void closeAllWindows() {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(219);
				c.flushOutStream();
			}
		}
	}
	
	public void sendFrame34(int id, int slot, int column, int amount) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34); // init item to smith screen
				c.getOutStream().writeWord(column); // Column Across Smith Screen
				c.getOutStream().writeByte(4); // Total Rows?
				c.getOutStream().writeDWord(slot); // Row Down The Smith Screen
				c.getOutStream().writeWord(id+1); // item
				c.getOutStream().writeByte(amount); // how many there are?
				c.getOutStream().endFrameVarSizeWord();
			}
		}
	}	
	
	public void walkableInterface(int id) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(208);
		        c.getOutStream().writeWordBigEndian_dup(id);
				c.flushOutStream();
			}
		}
	}
	
	public int mapStatus = 0;
	public void sendFrame99(int state) { // used for disabling map
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				if(mapStatus != state) {
					mapStatus = state;
					c.getOutStream().createFrame(99);
			        c.getOutStream().writeByte(state);
					c.flushOutStream();
				}
			}
		}
	}
	
	/*public void sendCrashFrame() { // used for crashing cheat clients
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(123);
				c.flushOutStream();
			}
		}
	}*/
	
	/**
	* Reseting animations for everyone
	**/

	public void frame1() {
		synchronized(c) {
			for(int i = 0; i < Config.MAX_PLAYERS; i++) {
				if(Server.playerHandler.players[i] != null) {
					Client person = (Client)Server.playerHandler.players[i];
					if(person != null) {
						if(person.getOutStream() != null && !person.disconnected) {
							if(c.distanceToPoint(person.getX(), person.getY()) <= 25){	
								person.getOutStream().createFrame(1);
								person.flushOutStream();
								person.getPA().requestUpdates();
							}
						}
					}
				}
			}
		}
	}
	
	/**
	* Creating projectile
	**/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {      
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(85);
		        c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
		        c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
		        c.getOutStream().createFrame(117);
		        c.getOutStream().writeByte(angle);
		        c.getOutStream().writeByte(offY);
		        c.getOutStream().writeByte(offX);
		        c.getOutStream().writeWord(lockon);
		        c.getOutStream().writeWord(gfxMoving);
		        c.getOutStream().writeByte(startHeight);
		        c.getOutStream().writeByte(endHeight);
		        c.getOutStream().writeWord(time);
			    c.getOutStream().writeWord(speed);
				c.getOutStream().writeByte(16);
				c.getOutStream().writeByte(64);
				c.flushOutStream();
			}
		}
    }
	
	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time, int slope) {      
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(85);
		        c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
		        c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
		        c.getOutStream().createFrame(117);
		        c.getOutStream().writeByte(angle);
		        c.getOutStream().writeByte(offY);
		        c.getOutStream().writeByte(offX);
		        c.getOutStream().writeWord(lockon);
		        c.getOutStream().writeWord(gfxMoving);
		        c.getOutStream().writeByte(startHeight);
		        c.getOutStream().writeByte(endHeight);
		        c.getOutStream().writeWord(time);
			    c.getOutStream().writeWord(speed);
				c.getOutStream().writeByte(slope);
				c.getOutStream().writeByte(64);
				c.flushOutStream();
			}
		}
    }
	
	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time) {
		synchronized(c) {
			for(int i = 0; i < Config.MAX_PLAYERS; i++) {
				Player p = Server.playerHandler.players[i];
				if(p != null) {
					Client person = (Client)p;
					if(person != null) {
						if(person.getOutStream() != null) {
							if(person.distanceToPoint(x, y) <= 25){
								if (p.heightLevel == c.heightLevel)
									person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight, endHeight, lockon, time);
							}
						}
					}	
				}
			}
		}
	}
	
	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon, int time, int slope) {
		synchronized(c) {
			for(int i = 0; i < Config.MAX_PLAYERS; i++) {
				Player p = Server.playerHandler.players[i];
				if(p != null) {
					Client person = (Client)p;
					if(person != null) {
						if(person.getOutStream() != null) {
							if(person.distanceToPoint(x, y) <= 25){	
								person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight, endHeight, lockon, time, slope);	
							}
						}
					}	
				}
			}
		}
	}
	

	/**
	** GFX
	**/
	public void stillGfx(int id, int x, int y, int height, int time) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(85);
				c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
				c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
				c.getOutStream().createFrame(4);
				c.getOutStream().writeByte(0);
				c.getOutStream().writeWord(id);
				c.getOutStream().writeByte(height);
				c.getOutStream().writeWord(time);
				c.flushOutStream();
			}
		}
	}
	
	//creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		synchronized(c) {
			for(int i = 0; i < Config.MAX_PLAYERS; i++) {
				Player p = Server.playerHandler.players[i];
				if(p != null) {
					Client person = (Client)p;
					if(person != null) {
						if(person.getOutStream() != null) {
							if(person.distanceToPoint(x, y) <= 25){	
								person.getPA().stillGfx(id, x, y, height, time);
							}
						}
					}	
				}
			}
		}
	}
	
	/**
	* Objects, add and remove
	**/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(85);
				c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
				c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
				c.getOutStream().createFrame(101);
				c.getOutStream().writeByteC((objectType<<2) + (face&3));
				c.getOutStream().writeByte(0);
			
				if (objectId != -1) { // removing
					c.getOutStream().createFrame(151);
					c.getOutStream().writeByteS(0);
					c.getOutStream().writeWordBigEndian(objectId);
					c.getOutStream().writeByteS((objectType<<2) + (face&3));
				}
				c.flushOutStream();
			}	
		}
		Region.addObject(objectId, objectX, objectY, 0, objectType, face);
	}
	
	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		if (c.distanceToPoint(objectX, objectY) > 60)
			return;
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(85);
				c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
				c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
				c.getOutStream().createFrame(101);
				c.getOutStream().writeByteC((objectType<<2) + (face&3));
				c.getOutStream().writeByte(0);
			
				if (objectId != -1) { // removing
					c.getOutStream().createFrame(151);
					c.getOutStream().writeByteS(0);
					c.getOutStream().writeWordBigEndian(objectId);
					c.getOutStream().writeByteS((objectType<<2) + (face&3));
				}
				c.flushOutStream();
			}	
		}
//		Region.addObject(objectId, objectX, objectY, 0, objectType, face);
	}
	

	/**
	* Show option, attack, trade, follow etc
	**/
	public String optionType = "null";
	public void showOption(int i, int l, String s, int a) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				if(!optionType.equalsIgnoreCase(s)) {
					optionType = s;
					c.getOutStream().createFrameVarSize(104);
					c.getOutStream().writeByteC(i);
					c.getOutStream().writeByteA(l);
					c.getOutStream().writeString(s);
					c.getOutStream().endFrameVarSize();
					c.flushOutStream();
				}
			}
		}
	}
	
	/**
	* Open bank
	**/
	public void openUpBank(){
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				c.getItems().resetItems(5064);
				c.getItems().rearrangeBank();
				c.getItems().resetBank();
				c.getItems().resetTempItems();
				c.getOutStream().createFrame(248);
				c.getOutStream().writeWordA(5292);
				c.getOutStream().writeWord(5063);
				c.flushOutStream();
			}
		}
	}
	
	/**
	* Private Messaging
	**/	
	public void logIntoPM() {
		setPrivateMessaging(2);
		for(int i1 = 0; i1 < Config.MAX_PLAYERS; i1++) {
			Player p = Server.playerHandler.players[i1];
			if(p != null && p.isActive) {
				Client o = (Client)p;
				if(o != null) {
					o.getPA().updatePM(c.playerId, 1);
				}
			}
		}
		boolean pmLoaded = false;

		for(int i = 0; i < c.friends.length; i++) {
			if(c.friends[i] != 0)  {
				for(int i2 = 1; i2 < Config.MAX_PLAYERS; i2++) {
					Player p = Server.playerHandler.players[i2];
					if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == c.friends[i])  {
						Client o = (Client)p;
						if(o != null) {
							if (c.playerRights >= 2 || p.privateChat == 0 || (p.privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(c.playerName)))) {
			 		 			loadPM(c.friends[i], 1);
			 		 			pmLoaded = true;
							}
							break;
						}
					}
				}
				if(!pmLoaded) {	
					loadPM(c.friends[i], 0);
				}
				pmLoaded = false;
			}
			for(int i1 = 1; i1 < Config.MAX_PLAYERS; i1++) {
				Player p = Server.playerHandler.players[i1];
    			if(p != null && p.isActive) {
					Client o = (Client)p;
					if(o != null) {
						o.getPA().updatePM(c.playerId, 1);
					}
				}
			}
		}
	}
	
	
	public void updatePM(int pID, int world) { // used for private chat updates
		Player p = Server.playerHandler.players[pID];
		if(p == null || p.playerName == null || p.playerName.equals("null")){
			return;
		}
		Client o = (Client)p;
		if(o == null) {
			return;
		}
        long l = Misc.playerNameToInt64(Server.playerHandler.players[pID].playerName);

        if (p.privateChat == 0) {
            for (int i = 0; i < c.friends.length; i++) {
                if (c.friends[i] != 0) {
                    if (l == c.friends[i]) {
                        loadPM(l, world);
                        return;
                    }
                }
            }
        } else if (p.privateChat == 1) {
            for (int i = 0; i < c.friends.length; i++) {
                if (c.friends[i] != 0) {
                    if (l == c.friends[i]) {
                        if (o.getPA().isInPM(Misc.playerNameToInt64(c.playerName))) {
                            loadPM(l, world);
                            return;
                        } else {
                            loadPM(l, 0);
                            return;
                        }
                    }
                }
            }
        } else if (p.privateChat == 2) {
            for (int i = 0; i < c.friends.length; i++) {
                if (c.friends[i] != 0) {
                    if (l == c.friends[i] && c.playerRights < 2) {
                        loadPM(l, 0);
                        return;
                    }
                }
            }
        }
    }
	
	public boolean isInPM(long l) {
        for (int i = 0; i < c.friends.length; i++) {
            if (c.friends[i] != 0) {
                if (l == c.friends[i]) {
                    return true;
                }
            }
        }
        return false;
    }
	
	
	/**
	 * Drink AntiPosion Potions
	 * @param itemId The itemId
	 * @param itemSlot The itemSlot
	 * @param newItemId The new item After Drinking
	 * @param healType The type of poison it heals
	 */
	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {
		c.attackTimer = c.getCombat().getAttackDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		if(c.duelRule[5]) {
			c.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if(!c.isDead && System.currentTimeMillis() - c.foodDelay > 2000) {
			if(c.getItems().playerHasItem(itemId, 1, itemSlot)) {
				c.sendMessage("You drink the "+c.getItems().getItemName(itemId).toLowerCase()+".");
				c.foodDelay = System.currentTimeMillis();
				// Actions
				if(healType == 1) {
					//Cures The Poison
				} else if(healType == 2) {
					//Cures The Poison + protects from getting poison again
				}
				c.startAnimation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}
	
	
	/**
	* Magic on items
	**/
	
	public void magicOnItems(int slot, int itemId, int spellId) {
		
		switch(spellId) {
			case 1162: // low alch
			if(System.currentTimeMillis() - c.alchDelay > 1000) {	
				if(c.getItems().playerHasItem(itemId, slot, 1)){
				if(!c.getCombat().checkMagicReqs(49)) {
					break;
				}
				if(itemId == 995) {
					c.sendMessage("You can't alch coins.");
					break;
				}
				if(c.getItems().playerHasItem(itemId, slot, 1)){
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, c.getShops().getItemShopValue(itemId)/3);
				c.startAnimation(c.MAGIC_SPELLS[49][2]);
				c.gfx100(c.MAGIC_SPELLS[49][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(c.MAGIC_SPELLS[49][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
				}
				}
			}
			break;
			
			case 1178: // high alch
			if(System.currentTimeMillis() - c.alchDelay > 2000) {	
				if(c.getItems().playerHasItem(itemId, slot, 1)){
				if(!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if(itemId == 995) {
					c.sendMessage("You can't alch coins.");
					break;
				}				
				if(c.getItems().playerHasItem(itemId, slot, 1)){
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, (int)(c.getShops().getItemShopValue(itemId)*.75));
				c.startAnimation(c.MAGIC_SPELLS[50][2]);
				c.gfx100(c.MAGIC_SPELLS[50][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(c.MAGIC_SPELLS[50][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
				}
				}
			}
			break;
			
			case -1:
//				c.getItems().removeItem(itemId);
			break;
		}
	}
	
	/**
	* Dying
	**/
	
	public void applyDead() {	
		c.respawnTimer = 12;
                /*c.isPoisoned = 0;
                c.poisonDamage = 0;*/
		c.isDead = false;
		if(c.duelStatus != 6) {
			c.killerId = findKiller();
			Client o = (Client) Server.playerHandler.players[c.killerId];
			if(o != null) {
				if (c.killerId != c.playerId) {
					o.sendMessage("You have defeated "+c.playerName+"!");
					if (!c.connectedFrom.equals(o.lastKilled) && !c.inMageBank()) {
						o.pkp++;
						o.sendMessage("@blu@You recieve a PK point for your kill. You now have " + o.pkp + " PK points.");
					} else if (!c.connectedFrom.equals(o.lastKilled) && c.inMageBank()) {
						o.pkp += 5;
						o.sendMessage("@blu@You recieve 5 PK points for your kill. You now have " + o.pkp + " PK points.");
					} else
						o.sendMessage("You have already killed " + c.playerName + " recently. You do not recieve a PK point.");
					o.lastKilled = c.connectedFrom;
				}
				o.playerKilled = c.playerId;
				if(o.duelStatus == 5) {
					o.duelStatus++;
				}
			}
		}
		c.faceUpdate(0);
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer b) {
				c.npcIndex = 0;
				c.playerIndex = 0;
				b.stop();
			}
		}, 2500);
		c.stopMovement();
		if(c.duelStatus <= 4 && !c.newPlayer()) {
			c.sendMessage("Oh dear you are dead!");
		} else if (c.duelStatus <= 4 && c.newPlayer()) {
				c.sendMessage("Oh dear you are dead, but don't worry you've kept your items because your are a new player.");
		} else if(c.duelStatus != 6) {
			c.sendMessage("You have lost the duel!");
		}
		resetDamageDone();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		c.lastVeng = 0;
		c.vengOn = false;
		resetFollowers();
		c.attackTimer = 10;
	}
	
	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[c.playerId] = 0;			
			}		
		}	
	}
	
	public void vengMe() {
		if (System.currentTimeMillis() - c.lastVeng > 30000) {
			if (c.getItems().playerHasItem(557,10) && c.getItems().playerHasItem(9075,4) && c.getItems().playerHasItem(560,2)) {
				c.vengOn = true;
				c.lastVeng = System.currentTimeMillis();
				c.startAnimation(4410);
				c.gfx100(726);
				c.getItems().deleteItem(557,c.getItems().getItemSlot(557),10);
				c.getItems().deleteItem(560,c.getItems().getItemSlot(560),2);
				c.getItems().deleteItem(9075,c.getItems().getItemSlot(9075),4);
			} else
				c.sendMessage("You do not have the required runes to cast this spell.");
		} else {
			c.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}
	
	public String spellBooks[] = {"Modern", "Ancient", "Lunar"};
	public int spellBook[] = {1151, 12855, 29999};
	
	public void switchSpellBook() {
		if (c.playerMagicBook < 2)
			c.playerMagicBook++;
		else 
			c.playerMagicBook = 0;
		c.sendMessage("You switch to " + spellBooks[c.playerMagicBook] + " magics.");
		c.setSidebarInterface(6, spellBook[c.playerMagicBook]);
		c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
		c.autocastId = -1;
		c.getPA().resetAutocast();
	}
	public void PrayerTab() {
		if (c.playerPrayerBook) {
		c.setSidebarInterface(5, 22500);
		c.sendMessage("[DEBUG]: Curses.");
		} else { 
		c.setSidebarInterface(5, 5608);
		c.sendMessage("[DEBUG]: Normal.");
		}
	}
	
	public void switchCombatType(int buttonId) {
		switch (buttonId) {
			case 22230: // Punch (unarmed)
				c.fightMode = c.ACCURATE;
			break;
			
			case 22229: // Kick (unarmed)
				c.fightMode = c.AGGRESSIVE;
			break;
			
			case 22228: // Block (unarmed)
				c.fightMode = c.BLOCK;
			break;
	                 
			case 9125: //Accurate
			case 6221: // range accurate
			case 48010: //flick (whip)
			case 21200: //spike (pickaxe)
			case 1080: //bash (staff)
			case 6168: //chop (axe)
			case 6236: //accurate (long bow)
			case 17102: //accurate (darts)
			case 8234: //stab (dagger)
			case 14128: //pound (Mace)
			case 18077: //Lunge (spear)
			case 18103: //Chop
			case 30088: //Chop (claws)
			case 3014: //Reap (Pickaxe)
			case 1177: //Pound (hammer)
			case 23249: //Bash (battlestaff)
			case 33020: //Jav
			c.fightMode = c.ACCURATE;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;
			
			case 9126: //Defensive
			case 48008: //deflect (whip)
			case 21201: //block (pickaxe)
			case 1078: //focus - block (staff)
			case 6169: //block (axe)
			case 33019: //fend (hally)
			case 18078: //block (spear)
			case 8235: //block (dagger)
			case 14219: //block (mace)
			case 18104: //block
			case 30089: //block (claws)
			case 3015: //block
			case 1175: //block (warhammer/hammer)
			case 23247: //block (battlestaff)
			case 33018: //fend (halberd)
			c.fightMode = c.DEFENSIVE;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;
			
			case 9127: // Controlled
			case 48009: //lash (whip)
			//case 33018: //jab (hally)
			case 6234: //longrange (long bow)
			case 6219: //longrange
			//case 18077: //lunge (spear)
			//case 18080: //swipe (spear)
			//case 18079: //pound (spear)
			case 17100: //longrange (darts)
			case 6170: // Smash (axe)
			case 14220: //Spike (mace)
			case 18080: //Swipe (spear)
			case 18079: //Pound (spear)
			c.fightMode = c.CONTROLLED;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;
			
			case 9128: // Aggressive
			case 6220: // Rapid
			case 21203: // Impale (pickaxe)
			case 21202: // Smash (pickaxe)
			case 1079: // Pound (staff)
			case 6171: // Hack (axe)
			//case 33020: // Swipe
			case 6235: // Rapid 
			case 17101: // Rapid
			case 8237: // Lunge
			case 8236: // Slash
			case 14221: //Pummel (mace)
			case 18106: //Slash
			case 18105: //Smash
			case 30091: //Slash (claws)
			case 30090: //Slash (claws)
			case 3017: //Chop (pickaxe)
			case 3016: //Jab (pickaxe)
			case 1176: //Pummel (hammer)
			case 23248: //Pound (battlestaff)
			//case 33019: //Swipe (halberd)
			c.fightMode = c.AGGRESSIVE;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;	
		}
	}
	
	public void resetTb() {
		c.teleBlockLength = 0;
		c.teleBlockDelay = 0;	
	}
	
	public void handleStatus(int i, int i2, int i3) {
		if (i == 2) {
			c.playerXP[i2] = c.getPA().getXPForLevel(i3)+5;
			c.playerLevel[i2] = c.getPA().getLevelForXP(c.playerXP[i2]);
		}
	}
	
	public void resetFollowers() {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				if (Server.playerHandler.players[j].followId == c.playerId) {
					Client c = (Client)Server.playerHandler.players[j];
					c.getPA().resetFollow();
				}			
			}		
		}	
	}
	
	public void giveLife() {
		c.isDead = false;
		c.faceUpdate(-1);
		c.freezeTimer = 0;
		if(c.duelStatus <= 4 && !c.getPA().inPitsWait()) { // if we are not in a duel we must be in wildy so remove items
			if (!c.inPits && !c.inFightCaves() && !c.inBarbDef) {
					c.getItems().resetKeepItems();
				if(!c.newPlayer()) {
					if(!c.isSkulled) {
						for (int i = 0; i < 3; i++)
							c.getItems().keepItem(i, true);
					}	
					if(c.prayerActive[10] && System.currentTimeMillis() - c.lastProtItem > 700 || c.curseActive[0] && System.currentTimeMillis() - c.lastProtItem > 700) {
						c.getItems().keepItem(3, true);
					}
					c.getItems().dropAllItems();
					c.getItems().deleteAllItems();
					if(!c.isSkulled) {
						for (int i1 = 0; i1 < 3; i1++) {
							if(c.itemKeptId[i1] > 0) {
								c.getItems().addItem(c.itemKeptId[i1], 1);
							}
						}
					}	
					if(c.prayerActive[10] || c.curseActive[0]) {
						if(c.itemKeptId[3] > 0) {
							c.getItems().addItem(c.itemKeptId[3], 1);
						}
					}
				}
				c.getItems().resetKeepItems();
			} else if (c.inPits) {
				Server.fightPits.removePlayerFromPits(c.playerId);
				c.pitsStatus = 1;
			}
		}
		c.getCombat().resetPrayers();
		c.constitution = c.maxConstitution;
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		if (c.pitsStatus == 1)
			movePlayer(2399, 5173, 0);
		else if (c.inBarbDef) 
			Server.barbDefence.endGame(c, false);
		else if(c.duelStatus <= 4) { // if we are not in a duel repawn to wildy
			movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			c.isSkulled = false;
			c.skullTimer = 0;
			c.attackedPlayers.clear();
		} else if (c.inFightCaves()) {
			c.getPA().resetTzhaar();
		} else { // we are in a duel, respawn outside of arena
			Client o = (Client) Server.playerHandler.players[c.duelingWith];
			if(o != null) {
				o.getPA().createPlayerHints(10, -1);
				if(o.duelStatus == 6) {
					o.getTradeAndDuel().duelVictory();
				}
			}
c.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
			o.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
			if(c.duelStatus != 6) { // if we have won but have died, don't reset the duel status.
				c.getTradeAndDuel().resetDuel();
			}
		}
		//PlayerSaving.getSingleton().requestSave(c.playerId);
		PlayerSave.saveGame(c);
		c.getCombat().resetPlayerAttack();
		resetAnimation();
		c.startAnimation(65535);
		frame1();
		resetTb();
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		c.damageTaken = new int[Config.MAX_PLAYERS];
		c.getPA().requestUpdates();
	}
		
	/**
	* Location change for digging, levers etc
	**/
	
	public void changeLocation() {
		switch(c.newLocation) {
			case 1:
			sendFrame99(2);
			movePlayer(3578,9706,-1);
			break;
			case 2:
			sendFrame99(2);
			movePlayer(3568,9683,-1);
			break;
			case 3:
			sendFrame99(2);
			movePlayer(3557,9703,-1);
			break;
			case 4:
			sendFrame99(2);
			movePlayer(3556,9718,-1);
			break;
			case 5:
			sendFrame99(2);
			movePlayer(3534,9704,-1);
			break;
			case 6:
			sendFrame99(2);
			movePlayer(3546,9684,-1);
			break;
		}
		c.newLocation = 0;
	}
	
	
	/**
	* Teleporting
	**/
	public void spellTeleport(int x, int y, int height) {
		c.getPA().startTeleport(x, y, height, c.playerMagicBook == 1 ? "ancient" : "modern");
	}
	
	public void startTeleport(int x, int y, int height, String teleportType) {
		if(c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if(c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level "+Config.NO_TELEPORT_WILD_LEVEL+" in the wilderness.");
			return;
		}
		if(System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if(!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerIndex > 0 || c.npcIndex > 0)
				c.getCombat().resetPlayerAttack();
			c.stopMovement();
			removeAllWindows();			
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			if(teleportType.equalsIgnoreCase("modern")) {
				c.startAnimation(8939);
				c.teleTimer = 9;
				c.gfx0(1576);
				c.teleEndGfx = 1577;
				c.teleEndAnimation = 8941;
			} 

			if(teleportType.equalsIgnoreCase("ancient")) {
				//c.startAnimation(1979);//this is old 1
				c.startAnimation(9599);//I made it execute with 65535 that fixes the invisible glitch
				c.teleGfx = 0;
				c.teleTimer = 11;
				c.teleEndAnimation = 65535;
				c.gfx0(1681);
			}
			if(teleportType.equalsIgnoreCase("tab")) {//could not be fucked to make a boolean because reg teleporting was interfering with tabs
				c.startAnimation(9597);
				c.teleEndAnimation = 11973;
				c.teleTimer = 8;
				c.gfx0(1680);
			}
			if(teleportType.equalsIgnoreCase("glory")) {
				c.startAnimation(9603);
				c.teleTimer = 13;
				c.gfx0(1684);
				
			}
			
		}
	}
	public void startTeleport2(int x, int y, int height) {
		if(c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if(System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if(!c.isDead && c.teleTimer == 0) {			
			c.stopMovement();
			removeAllWindows();			
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(8939);
			c.teleTimer = 9;
			c.gfx50(1576);
			c.teleEndGfx = 1577;
			c.teleEndAnimation = 8941;
		}
	}
	public void specialTeleport(int x, int y, int height, int npcId) {
		if(System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if(!c.isDead && c.teleTimer == 0) {			
			c.stopMovement();
			removeAllWindows();			
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(1816);
			c.teleTimer = 11;
			c.teleGfx = 342;
			c.teleEndAnimation = 65535;
			NPC n = Server.npcHandler.npcs[npcId];
			n.gfx0(343);
			n.requestAnimation(1818,0);
			n.facePlayer(c.playerId);
		}
	} 
		/*public void teleTab(String teleportType) {
		if(c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if(c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level "+Config.NO_TELEPORT_WILD_LEVEL+" in the wilderness.");
			return;
		}
		if(c.inFightCaves()) {
			c.sendMessage("You can't teleport Out of FightCaves.");
			return;
		}
		if(System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if(!c.isDead && c.teleTimer == 0 && c.respawnTimer == -6) {
			if (c.playerIndex > 0 || c.npcIndex > 0)
				c.getCombat().resetPlayerAttack();
			c.stopMovement();
			removeAllWindows();			
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = 0;
			c.startAnimation(9597);
			c.teleEndAnimation = 11973;
			c.teleTimer = 8;
			c.gfx0(1680);
			
			if(teleportType.equalsIgnoreCase("varrock")) {
				c.teleX = 3214;
				c.teleY = 3424;
				c.getItems().deleteItem(8007 ,c.getItems().getItemSlot(8007), 1);	
			} 
			if(teleportType.equalsIgnoreCase("home")) {
				c.teleX = 3087;
				c.teleY = 3500;
				c.getItems().deleteItem(8013 ,c.getItems().getItemSlot(8013), 1);	
			}
			if(teleportType.equalsIgnoreCase("lumbridge")) {
				c.teleX = 3222;
				c.teleY = 3222;
				c.getItems().deleteItem(8008 ,c.getItems().getItemSlot(8008), 1);	
			}
			if(teleportType.equalsIgnoreCase("falador")) {
				c.teleX = 3013;
				c.teleY = 3356;
				c.getItems().deleteItem(8009 ,c.getItems().getItemSlot(8009), 1);	
			}
			if(teleportType.equalsIgnoreCase("camelot")) {
				c.teleX = 2757;
				c.teleY = 3477;
				c.getItems().deleteItem(8010 ,c.getItems().getItemSlot(8010), 1);	
			}
			if(teleportType.equalsIgnoreCase("ardy")) {
				c.teleX = 2659;
				c.teleY = 3308;
				c.getItems().deleteItem(8011 ,c.getItems().getItemSlot(8011), 1);	
			}
			
		}
	}*/

	public void processTeleport() {
		c.teleportToX = c.teleX;
		c.teleportToY = c.teleY;
		c.heightLevel = c.teleHeight;
	if(c.teleEndAnimation > 0) {
			c.startAnimation(c.teleEndAnimation);
		}
	if(c.teleEndGfx > 0) {
			c.gfx0(c.teleEndGfx);
		}
	}
		
	public void movePlayer(int x, int y, int h) {
		c.resetWalkingQueue();
		c.teleportToX = x;
        c.teleportToY = y;
		c.heightLevel = h;
		requestUpdates();
	}
	
	/**
	* Following
	**/
	public void playerWalk(int x, int y) {
		PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1);
	}
	
	public boolean checkClip() {
		int x = 0, y = 0, x3 = 0, y3 = 0;
		if (c.playerIndex > 0) {
			Client o = (Client) PlayerHandler.players[c.playerIndex];
			if (o == null) {
				return false;
			}
			x = o.getX() - c.getX();
			y = o.getY() - c.getY();
			x3 = o.getX();
			y3 = o.getY();
		} else if (c.npcIndex > 0) {
			NPC n = NPCHandler.npcs[c.npcIndex];
			if (n == null) {
				return false;
			}
			x = n.getX() - c.getX();
			y = n.getY() - c.getY();
			x3 = n.getX();
			y3 = n.getY();
		} else {
			return false;
		}
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		for (int p = 0; p < 20; p++) {
			if (x == 0 && y == 0) {
				return true;
			}
			x1 = x > 0 ? 1 : x < 0 ? -1 : 0;
			y1 = y > 0 ? 1 : y < 0 ? -1 : 0;
			if (!Region.getClipping(c.getX() + x2 + x1, c.getY() + y2 + y1, c.heightLevel, x1, y1)) {
				//if ((c.rangableArea(c.getX(), c.getY()) || c.rangableArea(x3, y3)) && (c.mageFollow || c.usingRangeWeapon)) {
					//return true;
				//} else {
					return false;
				//}
			}
			if (x > 0) {
				x--;
				x2++;
			} else if (x < 0) {
				x++;
				x2--;
			}
			if (y > 0) {
				y--;
				y2++;
			} else if (y < 0) {
				y++;
				y2--;
			}
		}
		return true;
	}
	
	public void followPlayer() {
		//c.sendMessage("Checkclip: " + c.lineOfSight((Client) PlayerHandler.players[c.playerIndex]));
		//c.sendMessage("Checkclip: " + checkClip());
		if(Server.playerHandler.players[c.followId] == null || Server.playerHandler.players[c.followId].isDead) {
			c.followId = 0;
			return;
		}		
		if(c.freezeTimer > 0 || c.isDead || c.constitution <= 0)
			return;
		
		int otherX = Server.playerHandler.players[c.followId].getX();
		int otherY = Server.playerHandler.players[c.followId].getY();
		boolean withinDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1);
		boolean goodDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1);
		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 8);
		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 4);
		boolean sameSpot = c.absX == otherX && c.absY == otherY;
		if(!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId = 0;
			return;
		}
		if(c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1)) {
			if (otherX != c.getX() && otherY != c.getY()) {
				stopDiagonal(otherX, otherY);//This is what causes Clipping through walls needa rewrite this
				return;
			}
		}
		
		if((c.usingBow || c.mageFollow || (c.playerIndex > 0 && c.autocastId > 0)) && bowDistance && !sameSpot) {
			return;
		}

		if(c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if(c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}
		
		
		c.faceUpdate(c.followId+32768);
		int absX = c.getX();
		int absY = c.getY();
		if (otherX == c.absX && otherY == c.absY) {
			int r = Misc.random(3);
			switch (r) {
				case 0:
					walkTo(0,-1);
				break;
				case 1:
					walkTo(0,1);
				break;
				case 2:
					walkTo(1,0);
				break;
				case 3:
					walkTo(-1,0);
				break;			
			}		
		} else if(c.isRunning2 && !withinDistance) {
			if(otherY > c.getY() && otherX == c.getX()) {
				//walkTo(0, getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
				playerWalk(otherX, otherY - 1);
			} else if(otherY < c.getY() && otherX == c.getX()) {
				//walkTo(0, getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
				playerWalk(otherX, otherY + 1);
			} else if(otherX > c.getX() && otherY == c.getY()) {
				//walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1), 0);
				playerWalk(otherX - 1, otherY);
			} else if(otherX < c.getX() && otherY == c.getY()) {
				//walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), 0);
				playerWalk(otherX + 1, otherY);
			} else if(otherX < c.getX() && otherY < c.getY()) {
				//walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
				playerWalk(otherX + 1, otherY + 1);
			} else if(otherX > c.getX() && otherY > c.getY()) {
				//walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
				playerWalk(otherX - 1, otherY - 1);
			} else if(otherX < c.getX() && otherY > c.getY()) {
				//walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
				playerWalk(otherX + 1, otherY - 1);
			} else if(otherX > c.getX() && otherY < c.getY()) {
				//walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
				playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if(otherY > c.getY() && otherX == c.getX()) {
				//walkTo(0, getMove(c.getY(), otherY - 1));
				playerWalk(otherX, otherY - 1);
			} else if(otherY < c.getY() && otherX == c.getX()) {
				//walkTo(0, getMove(c.getY(), otherY + 1));
				playerWalk(otherX, otherY + 1);
			} else if(otherX > c.getX() && otherY == c.getY()) {
				//walkTo(getMove(c.getX(), otherX - 1), 0);
				playerWalk(otherX - 1, otherY);
			} else if(otherX < c.getX() && otherY == c.getY()) {
				//walkTo(getMove(c.getX(), otherX + 1), 0);
				playerWalk(otherX + 1, otherY);
			} else if(otherX < c.getX() && otherY < c.getY()) {
				//walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY + 1));
				playerWalk(otherX + 1, otherY + 1);
			} else if(otherX > c.getX() && otherY > c.getY()) {
				//walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY - 1));
				playerWalk(otherX - 1, otherY - 1);
			} else if(otherX < c.getX() && otherY > c.getY()) {
				//walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1));
				playerWalk(otherX + 1, otherY - 1);
			} else if(otherX > c.getX() && otherY < c.getY()) {
				//walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY + 1));
				playerWalk(otherX - 1, otherY + 1);
			} 
		}
		c.faceUpdate(c.followId+32768);
	}	
 
	
	public void followNpc() {
		if(Server.npcHandler.npcs[c.followId2] == null || Server.npcHandler.npcs[c.followId2].isDead) {
			c.followId2 = 0;
			return;
		}		
		if(c.freezeTimer > 0) {
			return;
		}
		if (c.isDead || c.constitution <= 0)
			return;
		
		int otherX = Server.npcHandler.npcs[c.followId2].getX();
		int otherY = Server.npcHandler.npcs[c.followId2].getY();
		boolean withinDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean goodDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1);
		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 8);
		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 4);
		boolean sameSpot = c.absX == otherX && c.absY == otherY;
		if(!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId2 = 0;
			return;
		}
		if(c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1)) {
			if (otherX != c.getX() && otherY != c.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}
		
		if((c.usingBow || c.mageFollow || (c.npcIndex > 0 /*&& c.autocastId > 0*/)) && bowDistance && !sameSpot) {
			return;
		}

		if(c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if(c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}
		
		c.faceUpdate(c.followId2);
		if (otherX == c.absX && otherY == c.absY) {
			int r = Misc.random(3);
			switch (r) {
				case 0:
					walkTo(0,-1);
				break;
				case 1:
					walkTo(0,1);
				break;
				case 2:
					walkTo(1,0);
				break;
				case 3:
					walkTo(-1,0);
				break;			
			}		
		} else if(c.isRunning2 && !withinDistance) {
			if(otherY > c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if(otherY < c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
			} else if(otherX > c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1), 0);
			} else if(otherX < c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), 0);
			} else if(otherX < c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY + 1) + getMove(c.getY(), otherY + 1));
			} else if(otherX > c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if(otherX < c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} else if(otherX > c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1) + getMove(c.getY(), otherY - 1));
			} 
		} else {
			if(otherY > c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY - 1));
			} else if(otherY < c.getY() && otherX == c.getX()) {
				walkTo(0, getMove(c.getY(), otherY + 1));
			} else if(otherX > c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1), 0);
			} else if(otherX < c.getX() && otherY == c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1), 0);
			} else if(otherX < c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY + 1));
			} else if(otherX > c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY - 1));
			} else if(otherX < c.getX() && otherY > c.getY()) {
				walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(), otherY - 1));
			} else if(otherX > c.getX() && otherY < c.getY()) {
				walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(), otherY + 1));
			} 
		}
		c.faceUpdate(c.followId2);
	}
	

	
	
	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else return j-i;
	}
	
	public void resetFollow() {
		c.followId = 0;
		c.followId2 = 0;
		c.mageFollow = false;
		c.outStream.createFrame(174);
		c.outStream.writeWord(0);
		c.outStream.writeByte(0);
		c.outStream.writeWord(1);
	}
	
	public void walkTo(int i, int j) {
		c.newWalkCmdSteps = 0;
        if(++c.newWalkCmdSteps > 50)
            c.newWalkCmdSteps = 0;
        int k = c.getX() + i;
        k -= c.mapRegionX * 8;
        c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
        int l = c.getY() + j;
        l -= c.mapRegionY * 8;

        for(int n = 0; n < c.newWalkCmdSteps; n++) {
            c.getNewWalkCmdX()[n] += k;
            c.getNewWalkCmdY()[n] += l;
        }
    }
	
	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
        if(++c.newWalkCmdSteps > 50)
            c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
        k -= c.mapRegionX * 8;
        c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
        int l = c.getY() + j;
        l -= c.mapRegionY * 8;

        for(int n = 0; n < c.newWalkCmdSteps; n++) {
            c.getNewWalkCmdX()[n] += k;
            c.getNewWalkCmdY()[n] += l;
        }
    }
	
	public void stopDiagonal(int otherX, int otherY) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - c.getY();
		/*if (!clipHor) {
			yMove = 0;
		} else if (!clipVer) {
			xMove = 0;	
		}*/
		
		int k = c.getX() + xMove;
        k -= c.mapRegionX * 8;
        c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
        int l = c.getY() + yMove;
        l -= c.mapRegionY * 8;
		
		for(int n = 0; n < c.newWalkCmdSteps; n++) {
            c.getNewWalkCmdX()[n] += k;
            c.getNewWalkCmdY()[n] += l;
        }
		
	}
	
		
	
	public void walkToCheck(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
        if(++c.newWalkCmdSteps > 50)
            c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
        k -= c.mapRegionX * 8;
        c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
        int l = c.getY() + j;
        l -= c.mapRegionY * 8;
		
		for(int n = 0; n < c.newWalkCmdSteps; n++) {
            c.getNewWalkCmdX()[n] += k;
            c.getNewWalkCmdY()[n] += l;
        }
	}
	

	public int getMove(int place1,int place2) {
		if (System.currentTimeMillis() - c.lastSpear < 4000)
			return 0;
		if ((place1 - place2) == 0) {
            return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
        return 0;
   	}
	
	public boolean fullVeracs() {
		return c.playerEquipment[c.playerHat] == 4753 && c.playerEquipment[c.playerChest] == 4757 && c.playerEquipment[c.playerLegs] == 4759 && c.playerEquipment[c.playerWeapon] == 4755;
	}
	public boolean fullGuthans() {
		return c.playerEquipment[c.playerHat] == 4724 && c.playerEquipment[c.playerChest] == 4728 && c.playerEquipment[c.playerLegs] == 4730 && c.playerEquipment[c.playerWeapon] == 4726;
	}
	
	/**
	* reseting animation
	**/
	public void resetAnimation() {
		c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		c.startAnimation(c.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}
	
	
	public void levelUp(int skill) {
		int totalLevel = 0;
		for (int i = 0; i < 22; i++) {
			totalLevel += (getLevelForXP(c.playerXP[i]));
		}
		sendFrame126("Level: " + totalLevel + 99 + 99, 13983);
		int[][] data = { { 0, 6248, 6249, 6247 }, // ATTACK
				{ 1, 6254, 6255, 6253 }, // DEFENCE
				{ 2, 6207, 6208, 6206 }, // STRENGTH
				{ 3, 6217, 6218, 6216 }, // HITPOINTS
				{ 4, 5453, 6114, 4443 }, // RANGED
				{ 5, 6243, 6244, 6242 }, // PRAYER
				{ 6, 6212, 6213, 6211 }, // MAGIC
				{ 7, 6227, 6228, 6226 }, // COOKING
				{ 8, 4273, 4274, 4272 }, // WOODCUTTING
				{ 9, 6232, 6233, 6231 }, // FLETCHING
				{ 10, 6259, 6260, 6258 }, // FISHING
				{ 11, 4283, 4284, 4282 }, // FIREMAKING
				{ 12, 6264, 6265, 6263 }, // CRAFTING
				{ 13, 6222, 6223, 6221 }, // SMITHING
				{ 14, 4417, 4438, 4416 }, // MINING
				{ 15, 6238, 6239, 6237 }, // HERBLORE
				{ 16, 4278, 4279, 4277 }, // AGILITY
				{ 17, 4263, 4264, 4261 }, // THIEVING
				{ 18, 12123, 12124, 12122 }, // SLAYER
				{ 19, -1, -1, -1 }, // FARMING
				{ 20, 4268, 4269, 4267 }, // RUNECRAFTING
		};
		String[] name = { "Attack", "Defence", "Strength", "Hitpoints",
				"Ranged", "Prayer", "Magic", "Cooking", "Woodcutting",
				"Fletching", "Fishing", "Firemaking", "Crafting", "Smithing",
				"Mining", "Herblore", "Agility", "Thieving", "Slayer",
				"Farming", "Runecrafting", };
		if (skill == data[skill][0]) {
			sendFrame126("@bla@Congratulations, you just advanced a "
					+ name[skill] + " level!", data[skill][1]);
			sendFrame126("@dbl@Your " + name[skill] + " level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", data[skill][2]);
			c.sendMessage("You've just advanced a " + name[skill]
					+ " level! You have reach level "
					+ getLevelForXP(c.playerXP[skill]) + ".");
			if (getLevelForXP(c.playerXP[skill]) == 99) {
				c.sendMessage("<col=129>Well done! You've achieved the highest possible level in this skill!");
			}
			sendFrame164(data[skill][3]);
		}
		sendFrame126("Combat level: " + c.combatLevel, 3983);
		c.dialogueAction = 0;
		c.nextChat = 0;
	}
    
	public int gfxId;
	public void handleLevelUpGfx() {
		gfxId = 1634;
		c.gfx0(gfxId);
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer s) {
				if (gfxId == 1637 || c.disconnected) {
					s.stop();
					return;
				}
				gfxId++;
				c.gfx0(gfxId);
			}
		}, 600);
	}
	
	public void refreshSkill(int i) {
		switch (i) {
			case 0:
			sendFrame126("" + c.playerLevel[0] + "", 4004);
			sendFrame126("" + getLevelForXP(c.playerXP[0]) + "", 4005);
			sendFrame126("" + c.playerXP[0] + "", 4044);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[0]) + 1) + "", 4045);
			break;
			
			case 1:
			sendFrame126("" + c.playerLevel[1] + "", 4008);
			sendFrame126("" + getLevelForXP(c.playerXP[1]) + "", 4009);
			sendFrame126("" + c.playerXP[1] + "", 4056);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[1]) + 1) + "", 4057);
			break;
			
			case 2:
			sendFrame126("" + c.playerLevel[2] + "", 4006);
			sendFrame126("" + getLevelForXP(c.playerXP[2]) + "", 4007);
			sendFrame126("" + c.playerXP[2] + "", 4050);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[2]) + 1) + "", 4051);
			break;
			
			case 3:
			sendFrame126("" + c.playerLevel[3] + "", 4016);
			sendFrame126("" + getLevelForXP(c.playerXP[3]) + "", 4017);
			sendFrame126("" + c.playerXP[3] + "", 4080);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[3])+1) + "", 4081);
			break;
			
			case 4:
			sendFrame126("" + c.playerLevel[4] + "", 4010);
			sendFrame126("" + getLevelForXP(c.playerXP[4]) + "", 4011);
			sendFrame126("" + c.playerXP[4] + "", 4062);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[4]) + 1) + "", 4063);
			break;
			
			case 5:
			sendFrame126("" + c.playerLevel[5] + "", 4012);
			sendFrame126("" + getLevelForXP(c.playerXP[5]) + "", 4013);
			sendFrame126("" + c.playerXP[5] + "", 4068);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[5]) + 1) + "", 4069);
			sendFrame126("" +c.playerLevel[5]+"/"+getLevelForXP(c.playerXP[5])+"", 687);//Prayer frame
			break;
			
			case 6:
			sendFrame126("" + c.playerLevel[6] + "", 4014);
			sendFrame126("" + getLevelForXP(c.playerXP[6]) + "", 4015);
			sendFrame126("" + c.playerXP[6] + "", 4074);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[6]) + 1) + "", 4075);
			break;
			
			case 7:
			sendFrame126("" + c.playerLevel[7] + "", 4034);
			sendFrame126("" + getLevelForXP(c.playerXP[7]) + "", 4035);
			sendFrame126("" + c.playerXP[7] + "", 4134);
			sendFrame126("" +getXPForLevel(getLevelForXP(c.playerXP[7]) + 1) + "", 4135);
			break;
			
			case 8:
			sendFrame126("" + c.playerLevel[8] + "", 4038);
			sendFrame126("" + getLevelForXP(c.playerXP[8]) + "", 4039);
			sendFrame126("" + c.playerXP[8] + "", 4146);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[8]) + 1) + "", 4147);
			break;
			
			case 9:
			sendFrame126("" + c.playerLevel[9] + "", 4026);
			sendFrame126("" + getLevelForXP(c.playerXP[9]) + "", 4027);
			sendFrame126("" + c.playerXP[9] + "", 4110);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[9]) + 1) + "", 4111);
			break;
			
			case 10:
			sendFrame126("" + c.playerLevel[10] + "", 4032);
			sendFrame126("" + getLevelForXP(c.playerXP[10]) + "", 4033);
			sendFrame126("" + c.playerXP[10] + "", 4128);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[10]) + 1) + "", 4129);
			break;
			
			case 11:
			sendFrame126("" + c.playerLevel[11] + "", 4036);
			sendFrame126("" + getLevelForXP(c.playerXP[11]) + "", 4037);
			sendFrame126("" + c.playerXP[11] + "", 4140);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[11]) + 1) + "", 4141);
			break;
			
			case 12:
			sendFrame126("" + c.playerLevel[12] + "", 4024);
			sendFrame126("" + getLevelForXP(c.playerXP[12]) + "", 4025);
			sendFrame126("" + c.playerXP[12] + "", 4104);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[12]) + 1) + "", 4105);
			break;
			
			case 13:
			sendFrame126("" + c.playerLevel[13] + "", 4030);
			sendFrame126("" + getLevelForXP(c.playerXP[13]) + "", 4031);
			sendFrame126("" + c.playerXP[13] + "", 4122);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[13]) + 1) + "", 4123);
			break;
			
			case 14:
			sendFrame126("" + c.playerLevel[14] + "", 4028);
			sendFrame126("" + getLevelForXP(c.playerXP[14]) + "", 4029);
			sendFrame126("" + c.playerXP[14] + "", 4116);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[14]) + 1)+ "", 4117);
			break;
			
			case 15:
			sendFrame126("" + c.playerLevel[15] + "", 4020);
			sendFrame126("" + getLevelForXP(c.playerXP[15]) + "", 4021);
			sendFrame126("" + c.playerXP[15] + "", 4092);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[15]) + 1) + "", 4093);
			break;
			
			case 16:
			sendFrame126("" + c.playerLevel[16] + "", 4018);
			sendFrame126("" + getLevelForXP(c.playerXP[16]) + "", 4019);
			sendFrame126("" + c.playerXP[16] + "", 4086);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[16]) + 1) + "", 4087);
			break;
			
			case 17:
			sendFrame126("" + c.playerLevel[17] + "", 4022);
			sendFrame126("" + getLevelForXP(c.playerXP[17]) + "", 4023);
			sendFrame126("" + c.playerXP[17] + "", 4098);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[17]) + 1) + "", 4099);
			break;
			
			case 18:
			sendFrame126("" + c.playerLevel[18] + "", 12166);
			sendFrame126("" + getLevelForXP(c.playerXP[18]) + "", 12167);
			sendFrame126("" + c.playerXP[18] + "", 12171);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[18]) + 1) + "", 12172);
			break;
			
			case 19:
			sendFrame126("" + c.playerLevel[19] + "", 13926);
			sendFrame126("" + getLevelForXP(c.playerXP[19]) + "", 13927);
			sendFrame126("" + c.playerXP[19] + "", 13921);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[19]) + 1) + "", 13922);
			break;
			
			case 20:
			sendFrame126("" + c.playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(c.playerXP[20]) + "", 4153);
			sendFrame126("" + c.playerXP[20] + "", 4157);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[20]) + 1) + "", 4158);
			break;
			
		case 21: // hunter
			sendFrame126("@yel@" + c.playerLevel[21] + "", 18166);
			sendFrame126("@yel@" + c.playerLevel[21] + "", 18170);
			break;
			
		case 22: //construction
			sendFrame126("@yel@" + c.playerLevel[22] + "", 18165);
			sendFrame126("@yel@" + c.playerLevel[22] + "", 18169);
			break;
		case 23: //Dungeoneering
			sendFrame126("@yel@" + c.playerLevel[23] + "", 18168);
			sendFrame126("@yel@" + c.playerLevel[23] + "", 18172);
			break;

		case 24: //summoning
			sendFrame126("@yel@" + c.playerLevel[24] + "", 18167);
			sendFrame126("@yel@" + c.playerLevel[24] + "", 18171);
			sendFrame126("" + c.playerXP[24] + "", 29803);
			break;
		}
	}
	
	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double)lvl + 300.0 * Math.pow(2.0, (double)lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int)Math.floor(points / 4);
		}
		return 0;
	}
	
	public String getYellRank() {
		switch (c.playerRights) {
			case 0:
				return "[Player]";
			case 1:
				return "[Moderator][" + c.donorTag + "]";
			case 2:
				return "[Administrator][" + c.donorTag + "]";
			case 3:
				return "[Developer][" + c.donorTag + "]";
			case 4:
				return "[@red@" + c.donorTag + "@bla@]";
		}
		return "";
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}
	
	public boolean addSkillXP(int amount, int skill){
		if (c.expLock) {
			return false;
		}
		if (amount+c.playerXP[skill] < 0 || c.playerXP[skill] > 200000000) {
			if(c.playerXP[skill] > 200000000) {
				c.playerXP[skill] = 200000000;
			}
			return false;
		}
		amount *= Config.SERVER_EXP_BONUS;
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		c.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			levelUp(skill);
			handleLevelUpGfx();
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}


	public void resetBarrows() {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.barrowsKillCount = 0;
		c.randomCoffin = Misc.random(3) + 1;
	}
	
	public static int Barrows[] = {4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759};
	public static int Runes[] = {4740,558,560,565};
	public static int Pots[] = {};
	public static int Hats[] = {656, 658, 660, 662, 664};
	public static int PvPDrop[] = {
		13896, 13884, 13890, 13902,//Statius's
		13899, 13887, 13893, 13905,//Vesta's
		13864, 13858, 13861, 13867,//Zuriel's
		13876, 13870, 13873, 13879, 13883,//Morrigan's
		13920, 13908, 13914, 13926,//Statius's Corrupt
		13911, 13917, 13923, 13929,//Vesta's Corrupt
		13938, 13932, 13935, 13941,//Zuriel's Corrupt
		3950, 13944, 13947, 13593,//Morrigan's Corrupt
	};
	
	public int randomBarrows() {
		return Barrows[(int)(Math.random()*Barrows.length)];
	}
	public int randomHats() {
		return Hats[(int)(Math.random()*Hats.length)];
	}
	public int randomPvPDrop() {
		return PvPDrop[(int)(Math.random()*PvPDrop.length)];
	}

	public int randomRunes() {
		return Runes[(int) (Math.random()*Runes.length)];
	}
	
	public int randomPots() {
		return Pots[(int) (Math.random()*Pots.length)];
	}
	/**
	 * Show an arrow icon on the selected player.
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		synchronized(c) {
			c.outStream.createFrame(254);
			c.outStream.writeByte(i);
	
			if (i == 1 || i == 10) {
				c.outStream.writeWord(j);
				c.outStream.writeWord(k);
				c.outStream.writeByte(l);
			} else {
				c.outStream.writeWord(k);
				c.outStream.writeWord(l);
				c.outStream.writeByte(j);
			}
		}
	}
	
	public int getNpcId(int id) {
		for(int i = 0; i < NPCHandler.maxNPCs; i++) {
			if(NPCHandler.npcs[i] != null) {
				if(Server.npcHandler.npcs[i].npcId == id) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public void removeObject(int x, int y) {
		object(-1, x, x, 10, 10);
	}
	
	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}
	
	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}
	
	
	public void handleGlory(int gloryId) {
		c.getDH().sendOption4("Edgeville", "Al Kharid", "Karamja", "Mage Bank");
		c.usingGlory = true;
	}
	
	public void resetVariables() {
		c.getFishing().resetFishing();
		c.getCrafting().resetCrafting();
		c.usingGlory = false;
		c.smeltInterface = false;
		c.smeltType = 0;
		c.smeltAmount = 0;
		c.woodcut[0] = c.woodcut[1] = c.woodcut[2] = 0;
		c.mining[0] = c.mining[1] = c.mining[2] = 0;
	}
	
	public boolean inPitsWait() {
		return c.getX() <= 2404 && c.getX() >= 2394 && c.getY() <= 5175 && c.getY() >= 5169;
	}
	
	
	public void removeFromCW() {
		if (c.castleWarsTeam == 1) {
			if (c.inCwWait) {
				Server.castleWars.saradominWait.remove(Server.castleWars.saradominWait.indexOf(c.playerId));
			} else {
				Server.castleWars.saradomin.remove(Server.castleWars.saradomin.indexOf(c.playerId));
			}
		} else if (c.castleWarsTeam == 2) {
			if (c.inCwWait) {
				Server.castleWars.zamorakWait.remove(Server.castleWars.zamorakWait.indexOf(c.playerId));
			} else {
				Server.castleWars.zamorak.remove(Server.castleWars.zamorak.indexOf(c.playerId));
			}		
		}
	}
	
	public int antiFire() {
		int toReturn = 0;
		if (c.antiFirePot)
			toReturn++;
		if (c.playerEquipment[c.playerShield] == 1540 || c.prayerActive[12] || c.playerEquipment[c.playerShield] == 11283)
			toReturn++;
		return toReturn;	
	}
	
	public boolean checkForFlags() {
		int[][] itemsToCheck = {{995,100000000},{35,5},{667,5},{2402,5},{746,5},{4151,150},{565,100000},{560,100000},{555,300000},{11235,10}};
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getItems().getTotalCount(itemsToCheck[j][0]))
				return true;		
		}
		return false;
	}
	
	public void addStarter() {
c.sendMessage("Type ::commands to begin.");
		//c.getPA().showInterface(3559);
		//c.canChangeAppearance = true;
		/*Items Recieved*/
		/*c.getItems().addItem(995,1000000);
		c.getItems().addItem(1731, 1);
		c.getItems().addItem(554,2000);
		c.getItems().addItem(555,2000);
		c.getItems().addItem(556,2000);
		c.getItems().addItem(558,6000);
		c.getItems().addItem(1381,1);
		c.getItems().addItem(1323,1);
		c.getItems().addItem(841,1);
		c.getItems().addItem(882,500);
		c.getItems().addItem(386,1000);*/
	}
	
	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.playerEquipment.length; j++) {
			if (c.playerEquipment[j] > 0)
				count++;		
		}
		return count;
	}
	/*this should apply to skillcapes to activate emotes + recoil ring to view recoil damages left*/
	public void useOperate(int itemId) {
		switch (itemId) {
			case 1712:
			case 1710:
			case 1708:
			case 1706:
			handleGlory(itemId);
			break;
			case 11283:
			case 11284:
			if (c.playerIndex > 0) {
				c.getCombat().handleDfs();				
			} else if (c.npcIndex > 0) {
				c.getCombat().handleDfsNPC();
			}
			break;	
		}
	}
	/*needs to be modified so that players cannot be speared into walls or objects*/
	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0)
			x = 1;
		else if (x < 0)
			x = -1;
		if (y > 0)
			y = 1;
		else if (y < 0)
			y = -1;
		moveCheck(x,y);
		c.lastSpear = System.currentTimeMillis();
	}
	
	public void moveCheck(int xMove, int yMove) {	
		movePlayer(c.absX + xMove, c.absY + yMove, c.heightLevel);
	}
		
		public void requestFileServer() {
			//Downloads maps to players cache on login
			for (int skill = 0; skill < 7; skill++) {
				c.playerXP[skill] = c.getPA().getXPForLevel(99)+5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
			}
			c.constitution = 990;
		c.getPA().requestUpdates();
	}
	
	public int findKiller() {
		int killer = c.playerId;
		int damage = 0;
		for (int j = 0; j < Config.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null)
				continue;
			if (j == c.playerId)
				continue;
			if (c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40) 
				|| c.goodDistance(c.absX, c.absY + 9400, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40)
				|| c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY + 9400, 40))
				if (c.damageTaken[j] > damage) {
					damage = c.damageTaken[j];
					killer = j;
				}
		}
		return killer;
	}
	
	public void resetTzhaar() {
		c.waveId = -1;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;	
		c.getPA().movePlayer(2438,5168,0);
	}
	public void enterCaves() {
		c.getPA().movePlayer(2413,5117, c.playerId * 4);
		c.waveId = 0;
		c.tzhaarToKill = -1;
		c.tzhaarKilled = -1;
		Server.fightCaves.spawnNextWave(c);
		c.jadSpawn();
	}
	public static void appendPoison(final Client c) {
		if (c.playerEquipment[c.playerShield] == 18340) {
			c.sendMessage("Your Anti-poison totem prevents you from being poisoned.");
			return;
		}
		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune) {
			c.sendMessage("You have been poisoned.");
			c.isPoisoned = 1;
        	c.updateRequired = true;
        	c.appearanceUpdateRequired = true;
			c.poisonDamage = 70 + Misc.random(7);
			c.lastPoison = 3;
			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					if (c.lastPoison == 0) {
                                                c.getCombat().appendHit(c, c.poisonDamage, 2, -1, false, 0);
						
                                                /*c.poisonMask = !c.getHitUpdateRequired() ? 1 : 2;
						c.handleHitMask(c.poisonDamage, 2, -1, 0, false);
						c.dealDamage(c.poisonDamage);*/
						c.getPA().refreshSkill(3);
						c.poisonDamage--;
					}
					c.lastPoison = c.lastPoison == 0 ? 18 : c.lastPoison - 1;
					if (c.poisonDamage == 0 && c.isDead == false) {
						c.sendMessage("The poison has worn off.");
						timer.cancel();
                                                c.updateRequired = true;
                                                c.appearanceUpdateRequired = true;
						c.isPoisoned = 0;
					}
					if (c.isDead == true) {
						c.poisonDamage = 0;
						c.isPoisoned = 0;
						timer.cancel();
						
					}
				}
			}, 0, 1000);
		}
	}
	
	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}	
		}
		return false;	
	}
	
	public void checkPouch(int i) {
		if (i < 0)
			return;
		c.sendMessage("This pouch has " + c.pouches[i] + " rune ess in it.");		
	}
	
	public void fillPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > c.getItems().getItemAmount(1436)) {
			toAdd = c.getItems().getItemAmount(1436);
		}
		if (toAdd > c.POUCH_SIZE[i] - c.pouches[i])
			toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > 0) {
			c.getItems().deleteItem(1436, toAdd);
			c.pouches[i] += toAdd;
		}		
	}
	
	public void emptyPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.pouches[i];
		if (toAdd > c.getItems().freeSlots()) {
			toAdd = c.getItems().freeSlots();
		}
		if (toAdd > 0) {
			c.getItems().addItem(1436, toAdd);
			c.pouches[i] -= toAdd;
		}		
	}
	/*this needs fixing because when barrows is fixed the items appear broken still unless you logout or wield an item*/
	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.playerItems[j]-1 == c.getItems().brokenBarrows[i][1]) {	
					c.playerItems[j] = c.getItems().brokenBarrows[i][0]+1;
				}		
			}
			if (breakOut)		
				break;
		}	
	}
	
	public void handleLoginText() {
		sendFrame126("Level 54 : Varrock", 13037);
		sendFrame126("A teleportation spell", 13038);
		sendFrame126("Level 60 : Lumbridge Teleport", 13047);
		sendFrame126("A teleportation spell", 13048);	
		sendFrame126("Level 66 : Canafis Teleport", 13055);
		sendFrame126("A teleportation spell", 13056);
		sendFrame126("Level 72 : Falador Teleport", 13063);	
		sendFrame126("A teleportation spell", 13064);	
		sendFrame126("Level 78 : Dareeyak Teleport", 13071);		
		sendFrame126("A teleportation spell", 13072);
		sendFrame126("Level 84 : Ice Plateau Teleport", 13081);	
		sendFrame126("A teleportation spell", 13082);	
		sendFrame126("Level 90 : Camelot Teleport", 13089);
		sendFrame126("A teleportation spell", 13090);		
		sendFrame126("Level 96 : Ardougne Teleport", 13097);
		sendFrame126("A teleportation spell", 13098);	
	}
	
	public void destroyInterface(int itemId) {
		String itemName = c.getItems().getItemName(itemId);

		String[][] info = {{ "Are you sure you want to drop this item?", "14174" },
				{ "Yes.", "14175" }, { "No.", "14176" }, { "", "14177" },
				{ "This item is valuable, you will not", "14182" }, { "get it back once lost.", "14183" },
				{ itemName, "14184" } };// make some kind of c.getItemInfo
		sendFrame34(itemId, 0, 14171, 1);
		for (int i = 0; i < info.length; i++)
			sendFrame126(info[i][0], Integer.parseInt(info[i][1]));
		c.destroyItem = itemId;
		sendFrame164(14170);
	}
	
	public void destroyItem(int itemId) {
		String itemName = c.getItems().getItemName(itemId);
		c.getItems().deleteItem(itemId, 1);
		c.sendMessage("Your " + itemName + " vanishes as you drop it on the ground.");
		c.destroyItem = 0;
	}
	
	public void handleWeaponStyle() {
		if (c.fightMode == 0) {
			c.getPA().sendFrame36(43, c.fightMode);
		} else if (c.fightMode == 1) {
			c.getPA().sendFrame36(43, 3);
		} else if (c.fightMode == 2) {
			c.getPA().sendFrame36(43, 1);
		} else if (c.fightMode == 3) {
			c.getPA().sendFrame36(43, 2);
		}
	}
	
	public boolean dialogueAction(int i) {
		return c.dialogueAction == i;
	}
		
}
