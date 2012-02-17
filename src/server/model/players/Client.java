package server.model.players;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;

import org.apache.mina.common.IoSession;

import server.Config;
import server.Server;
import server.model.items.ItemAssistant;
import server.model.shops.ShopAssistant;
import server.net.HostList;
import server.net.Packet;
import server.net.StaticPacketBuilder;
import server.event.CycleEventHandler;
import server.util.Misc;
import server.util.Stream;
import server.model.players.skills.*;
import server.model.players.combat.CombatAssistant;
import server.event.EventManager;
import server.event.Event;
import server.event.EventContainer;
import server.model.minigames.Gambling.Dicing;
import server.model.players.PlayerSave;
import server.model.npcs.NPC;

public class Client extends Player {

	public boolean ramboModeInitiated = false;

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private IoSession session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private TradeAndDuel tradeAndDuel = new TradeAndDuel(this);
	private Dicing dicing = new Dicing(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private Curses curses = new Curses(this);
	private Summoning summoning = new Summoning(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private PlayerKilling playerKilling = new PlayerKilling(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private Potions potions = new Potions(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);
	/**
	 * Skill instances
	 */
	private Slayer slayer = new Slayer(this);
	private Runecrafting runecrafting = new Runecrafting(this);
	private Woodcutting woodcutting = new Woodcutting(this);
	private Mining mine = new Mining(this);
	private Agility agility = new Agility(this);
	private Cooking cooking = new Cooking(this);
	private Fishing fish = new Fishing(this);
	private Crafting crafting = new Crafting(this);
	private Smithing smith = new Smithing(this);
	private Prayer prayer = new Prayer(this);
	private Fletching fletching = new Fletching(this);
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Farming farming = new Farming(this);
	private Thieving thieving = new Thieving(this);
	private Firemaking firemaking = new Firemaking(this);
	private Herblore herblore = new Herblore(this);

	/**
	* Integers, booleans, ect..
	**/
	
	//Summoning
	public boolean callFamiliar;
	//End of summoning
	
	private int somejunk;
	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;		
	public int returnCode = 2; 
	private Future<?> currentTask;
	
	public int animToDo;
	public boolean animLoop;
	
	public Client(IoSession s, int _playerId) {
		super(_playerId);
		this.session = s;
		synchronized(this) {
			outStream = new Stream(new byte[Config.BUFFER_SIZE]);
			outStream.currentOffset = 0;
		}
		inStream = new Stream(new byte[Config.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Config.BUFFER_SIZE];
	}
	
	public int[] closestTileToEntity(Client opponent) {
		int otherX = opponent.absX;
		int otherY = opponent.absY;
		if(otherY > getY() && otherX == getX()) {
			return new int[] {otherX, otherY - 1};
		} else if(otherY < getY() && otherX == getX()) {
			return new int[] {otherX, otherY + 1};
		} else if(otherX > getX() && otherY == getY()) {
			return new int[] {otherX - 1, otherY};
		} else if(otherX < getX() && otherY == getY()) {
			return new int[] {otherX + 1, otherY};
		} else if(otherX < getX() && otherY < getY()) {
			return new int[] {otherX + 1, otherY + 1};
		} else if(otherX > getX() && otherY > getY()) {
			return new int[] {otherX - 1, otherY - 1};
		} else if(otherX < getX() && otherY > getY()) {
			return new int[] {otherX + 1, otherY - 1};
		} else if(otherX > getX() && otherY < getY()) {
			return new int[] {otherX - 1, otherY + 1};
		} else {
			return new int[] {otherX, otherY};
		}
	}
	
	public void flushOutStream() {	
		if(disconnected || outStream.currentOffset == 0) return;
		synchronized(this) {	
			StaticPacketBuilder out = new StaticPacketBuilder().setBare(true);
			byte[] temp = new byte[outStream.currentOffset]; 
			System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
			out.addBytes(temp);
			session.write(out.toPacket());
			outStream.currentOffset = 0;
		}
    }
	
	public void jadSpawn() {
		getDH().sendDialogues(33, 2618);
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer c) {
				Server.fightCaves.spawnNextWave((Client)Server.playerHandler.players[playerId]);
				c.stop();
			}
		}, 10000);
	}
	
	public void sendClan(String name, String message, String clan, int rights) {
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}
	
	public static final int PACKET_SIZES[] = {
		0, 0, 0, 1, -1, 0, 0, 0, 0, 0, //0
		0, 0, 0, 0, 8, 0, 6, 2, 2, 0,  //10
		0, 2, 0, 6, 0, 12, 0, 0, 0, 0, //20
		0, 0, 0, 0, 0, 8, 4, 0, 0, 2,  //30
		2, 6, 0, 6, 0, -1, 0, 0, 0, 0, //40
		0, 0, 0, 12, 0, 0, 0, 8, 8, 12, //50
		8, 8, 0, 0, 0, 0, 0, 0, 0, 0,  //60
		6, 0, 2, 2, 8, 6, 0, -1, 0, 6, //70
		0, 0, 0, 0, 0, 1, 4, 6, 0, 0,  //80
		0, 0, 0, 0, 0, 3, 0, 0, -1, 0, //90
		0, 13, 0, -1, 0, 0, 0, 0, 0, 0,//100
		0, 0, 0, 0, 0, 0, 0, 6, 0, 0,  //110
		1, 0, 6, 0, 0, 0, -1, 0, 2, 6, //120
		0, 4, 6, 8, 0, 6, 0, 0, 0, 2,  //130
		0, 0, 0, 0, 0, 6, 0, 0, 0, 0,  //140
		0, 0, 1, 2, 0, 2, 6, 0, 0, 0,  //150
		0, 0, 0, 0, -1, -1, 0, 0, 0, 0,//160
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  //170
		0, 8, 0, 3, 0, 2, 0, 0, 8, 1,  //180
		0, 0, 12, 0, 0, 0, 0, 0, 0, 0, //190
		2, 0, 0, 0, 0, 0, 0, 0, 4, 0,  //200
		4, 0, 0, 0, 7, 8, 0, 0, 10, 0, //210
		0, 0, 0, 0, 0, 0, -1, 0, 6, 0, //220
		1, 0, 0, 0, 6, 0, 6, 8, 1, 0,  //230
		0, 4, 0, 0, 0, 0, -1, 0, -1, 4,//240
		0, 0, 6, 6, 0, 0, 0            //250
	};

	public void destruct() {
		if(session == null) 
			return;
                		CycleEventHandler.getSingleton().stopEvents(this);
		//PlayerSaving.getSingleton().requestSave(playerId);
		getPA().removeFromCW();
		if (inPits)
			Server.fightPits.removePlayerFromPits(playerId);
		if (clanId >= 0)
			Server.clanChat.leaveClan(playerId, clanId);
		Misc.println("[DEREGISTERED]: "+playerName+"");
		HostList.getHostList().remove(session);
		disconnected = true;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		super.destruct();
	}
	
	public void addToHp(int toAdd) {
		if (constitution + toAdd >= maxConstitution)
			toAdd = maxConstitution - maxConstitution;
		constitution += toAdd;
	}
	
	public void removeFromPrayer(int toRemove) {
		if (toRemove > playerLevel[5]) 
			toRemove = playerLevel[5];
		playerLevel[5] -= toRemove;
		getPA().refreshSkill(5);
	}
	
	
	public void sendMessage(String s) {
		synchronized (this) {
			if(getOutStream() != null) {
				outStream.createFrameVarSize(253);
				outStream.writeString(s);
				outStream.endFrameVarSize();
			}
		}
	}

	public void setSidebarInterface(int menuId, int form) {
		synchronized (this) {
			if(getOutStream() != null) {
				outStream.createFrame(71);
				outStream.writeWord(form);
				outStream.writeByteA(menuId);
			}
		}
	}	
	
	public void initialize() {
            if(MoneyCash > 99999 && MoneyCash <= 999999) {
		getPA().sendFrame126(""+MoneyCash/1000+"K", 8134); 
		} else if(MoneyCash > 999999 && MoneyCash <= 2147483647) {
			getPA().sendFrame126(""+MoneyCash/1000000+"M", 8134);
		} else {
			getPA().sendFrame126(""+MoneyCash+"", 8134);
		}
		getPA().sendFrame126(""+MoneyCash+"", 8135);
		synchronized (this) {
			outStream.createFrame(249);
			outStream.writeByteA(1);		// 1 for members, zero for free
			outStream.writeWordBigEndianA(playerId);
			for (int j = 0; j < Server.playerHandler.players.length; j++) {
				if (j == playerId)
					continue;
				if (Server.playerHandler.players[j] != null) {
					if (Server.playerHandler.players[j].playerName.equalsIgnoreCase(playerName))
						disconnected = true;
				}
			}
			for (int i = 0; i < 25; i++) {
				getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
				getPA().refreshSkill(i);
			}
			for(int p = 0; p < PRAYER.length; p++) { // reset prayer glows 
				prayerActive[p] = false;
				getPA().sendFrame36(PRAYER_GLOW[p], 0);	
			}
			//getPA().sendCrashFrame();
			getPA().handleWeaponStyle();
			getPA().handleLoginText();
			accountFlagged = getPA().checkForFlags();
			getPA().sendFrame36(108, 0);//resets autocast button
			getPA().sendFrame36(172, 1);
			getPA().sendFrame107(); // reset screen
			getPA().setChatOptions(0, 0, 0); // reset private messaging options
			setSidebarInterface(1, 3917);
			setSidebarInterface(2, 638);
			setSidebarInterface(3, 3213);
			setSidebarInterface(4, 1644);
			getPA().PrayerTab();
			//setSidebarInterface(5, 22500); // Curses - 22500  5608
			setSidebarInterface(6, getPA().spellBook[playerMagicBook]);
			setSidebarInterface(7, 18128);//far left tab		
			setSidebarInterface(8, 5065);
			setSidebarInterface(9, 5715);
			setSidebarInterface(10, 2449); //2449
			setSidebarInterface(11, 904); // wrench tab
			setSidebarInterface(12, 147); // run tab
			setSidebarInterface(13, 25605); //music tab 6299 for lowdetail. 962 for highdetail
			setSidebarInterface(14, 17000); //logout
			setSidebarInterface(15, 173); //blank
			setSidebarInterface(16, 25605); //blank
			setSidebarInterface(0, 2423);
			correctCoordinates();
			getPA().resetAutocast();
			getCombat().resetPrayers();
			if (specAmount > 10)
				specAmount = 10;
			sendMessage("Welcome to "+Config.SERVER_NAME);
			sendMessage("Donations are @gre@Online.");
			getPA().showOption(4, 0,"Follow", 4);
			getPA().showOption(5, 0,"Trade With", 3);
			getItems().resetItems(3214);
			getItems().sendWeapon(playerEquipment[playerWeapon], getItems().getItemName(playerEquipment[playerWeapon]));
			getItems().resetBonus();
			getItems().getBonus();
			getItems().writeBonus();
			getItems().setEquipment(playerEquipment[playerHat],1,playerHat);
			getItems().setEquipment(playerEquipment[playerCape],1,playerCape);
			getItems().setEquipment(playerEquipment[playerAmulet],1,playerAmulet);
			getItems().setEquipment(playerEquipment[playerArrows],playerEquipmentN[playerArrows],playerArrows);
			getItems().setEquipment(playerEquipment[playerChest],1,playerChest);
			getItems().setEquipment(playerEquipment[playerShield],1,playerShield);
			getItems().setEquipment(playerEquipment[playerLegs],1,playerLegs);
			getItems().setEquipment(playerEquipment[playerHands],1,playerHands);
			getItems().setEquipment(playerEquipment[playerFeet],1,playerFeet);
			getItems().setEquipment(playerEquipment[playerRing],1,playerRing);
			getItems().setEquipment(playerEquipment[playerWeapon],playerEquipmentN[playerWeapon],playerWeapon);
			getCombat().getPlayerAnimIndex(getItems().getItemName(playerEquipment[playerWeapon]).toLowerCase());
			getPA().logIntoPM();
			getItems().addSpecialBar(playerEquipment[playerWeapon]);
			saveTimer = 100;
			saveCharacter = true;
			Misc.println("[REGISTERED]: "+playerName+"");
			if (playerName.toLowerCase().contains("Slayrz")) {//testing purposes havn't finished debugging the file server -brandyn
			//Downloads maps to players cache on login region loading also needs to be fixed
			getPA().requestFileServer();
			}
			handler.updatePlayer(this, outStream);
			handler.updateNPC(this, outStream);
			flushOutStream();
			getPA().clearClanChat();
			getPA().resetFollow();
			getPA().sendFrame36(172, autoRet);
			getPA().sendFrame36(173, isRunning2 ? 1 : 0);
			for (int i = 0; i < 25; i++) {
				getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
				getPA().refreshSkill(i);
			}
			if(savedClan.length() > 0 && savedClan != null)
				Server.clanChat.handleClanChat(this, savedClan);
		}
	}

	public void update() {
		synchronized (this) {
			handler.updatePlayer(this, outStream);
			handler.updateNPC(this, outStream);
			flushOutStream();
		}
	}
	
	public void logout() {
		synchronized (this) {
			if(System.currentTimeMillis() - logoutDelay > 10000) {
                            		CycleEventHandler.getSingleton().stopEvents(this);
				outStream.createFrame(109);
				properLogout = true;
			} else {
				sendMessage("You must wait a few seconds from being out of combat to logout.");
			}
		}
	}

	public int diceTimer = 0;
	
	public int packetSize = 0, packetType = -1;
	
	public void process() {
		getPA().sendFrame126("Combat Level: "+combatLevel, 19000);
		getPA().sendFrame126("" + constitution, 19001);
		curses().handleProcess();
		if (wcTimer > 0 && woodcut[0] > 0) {
			wcTimer--;
		} else if (wcTimer == 0 && woodcut[0] > 0) {
			getWoodcutting().cutWood();
		} else if (miningTimer > 0 && mining[0] > 0) {
			miningTimer--;
		} else if (miningTimer == 0 && mining[0] > 0) {
			getMining().mineOre();
		} else  if (smeltTimer > 0 && smeltType > 0) {
			smeltTimer--;
		} else if (smeltTimer == 0 && smeltType > 0) {
			getSmithing().smelt(smeltType);
		} else if (fishing && fishTimer > 0) {
			fishTimer--;
		} else if (fishing && fishTimer == 0) {
			getFishing().catchFish();
		}
		specRestoreTimer--;
		getDicing().FetchDice();
	maxHp = getLevelForXP(playerXP[3]);
  //if(torvaEquipped(playerHat))
   //constitution += 60;
  //if(torvaEquipped(playerChest))
   //constitution += 200;
  //if(torvaEquipped(playerLegs))
  // constitution += 100;
		if(playerLevel[3] > (maxHp * 1.2))
			playerLevel[3] = (int) (maxHp * 1.2);
		getPA().sendFrame126(""+maxHp, 4017);
		if (clawDelay > 0) 
			clawDelay--;
		if (clawDelay == 0) {
			if (clawTarg != 0) {
				int soak = ((Client)Server.playerHandler.players[clawTarg]).getCombat().damageSoaked(hit3, "Melee");
				hit3 -= soak;
				int soak2 = ((Client)Server.playerHandler.players[clawTarg]).getCombat().damageSoaked(hit4, "Melee");
				hit4 -= soak;
				getCombat().appendHit((Client)Server.playerHandler.players[clawTarg], hit3, 0, 0, true, soak);
				getCombat().appendHit((Client)Server.playerHandler.players[clawTarg], hit4, 0, 0, true, soak2);
			}
			clawDelay = -1;
		}
		
	
		if(System.currentTimeMillis() - specDelay > Config.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if(specAmount < 10) {
				specAmount += .5;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}
		if(getAwaitingUpdate())
			getItems().resetItems(3214);
		
		if(followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}
		
		getCombat().handlePrayerDrain();
		
		if(System.currentTimeMillis() - singleCombatDelay >  3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackBy2 = 0;
		}
		if (System.currentTimeMillis() - restoreStatsDelay2 > 6000) {
			if (!inWild()) {
			restoreStatsDelay2 = System.currentTimeMillis();
			if (constitution < maxConstitution) 
				constitution += 1;
			}
		}
		if(System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++)  {
				if (playerLevel[level] < (level == 3 ? maxHp : getLevelForXP(playerXP[level]))) {
					if(level != 5) { // prayer doesn't restore
						playerLevel[level] += 1;
						getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
						getPA().refreshSkill(level);
					}
                                       				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					if (level == 0 || level == 1 || level == 2 || level == 4 || level == 6) {
						if (hasOverloadBoost)
							continue;
					}
				} else if (playerLevel[level] > (level == 3 ? maxHp : getLevelForXP(playerXP[level]))) {
					playerLevel[level] -= 1;
					getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
					getPA().refreshSkill(level);
				}
			}
		}
		
		if (barbLeader > 0 && inBarbDef) {
			NPC n = Server.npcHandler.npcs[barbLeader];
			if (n != null) {
				n.facePlayer(playerId);
				if (Misc.random(50) == 0) {
					n.requestAnimation(6728, 0);
					n.forceChat(n.barbRandom(this, Misc.random(5)));
				}
			}
		}
			
		
		if(inWild() || playerName.toLowerCase().equals("Banter")) {
			int modY = absY > 6400 ?  absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			getPA().walkableInterface(197);
			if(Config.SINGLE_AND_MULTI_ZONES) {
				if(inMulti()) {
					getPA().sendFrame126("@yel@Level: "+wildLevel, 199);
				} else {
					getPA().sendFrame126("@yel@Level: "+wildLevel, 199);
				}
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: "+wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
		} else if (inDuelArena()) {
			getPA().walkableInterface(201);
			if(duelStatus == 5) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
		} else if(inBarrows()){
			getPA().sendFrame99(2);
			getPA().sendFrame126("Kill Count: "+barrowsKillCount, 4536);
			getPA().walkableInterface(4535);
		} else if (inCwGame || inPits) {
			getPA().showOption(3, 0, "Attack", 1);	
		} else if (getPA().inPitsWait()) {
			getPA().showOption(3, 0, "Null", 1);
		}else if (!inCwWait) {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		} else if (inBarbDef) {
			getPA().sendFrame126("Total damage dealt: @gre@"+barbDamage, 25503);
			getPA().sendFrame126("Barbarian Defense", 25502);
			getPA().sendFrame126("Wave @gre@"+(barbWave+1), 25504);
			getPA().walkableInterface(25500);
		}
		
		if(!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}
		
		if(hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}

		if(skullTimer > 0) {
			skullTimer--;
			if(skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}	
		}
		
		if(isDead && respawnTimer == -6) {
			getPA().applyDead();
		}
		
		if(respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if(respawnTimer == 12) {
			respawnTimer--;
			startAnimation(0x900);
			poisonDamage = -1;
		}	
		
		if(respawnTimer > -6) {
			respawnTimer--;
		}
		if(freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (Server.playerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, Server.playerHandler.players[frozenBy].absX, Server.playerHandler.players[frozenBy].absY, 12)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}
		
		if(hitDelay > 0) {
			hitDelay--;
		}
		
		if(teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if(teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getPA().changeLocation();
				}
				if(teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if(teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					if (teleGfx != 342)
						gfx100(teleGfx);
					else 
						gfx0(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}	
		
		if (teleBlockLength > 0 && !inWild())
			teleBlockLength = 0;

		if(hitDelay == 1) {
			if(oldNpcIndex > 0) {
				getCombat().delayedHit(oldNpcIndex);
			}
			if(oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(oldPlayerIndex);				
			}		
		}
		
		if(attackTimer > 0) {
			attackTimer--;
		}
		
		if(attackTimer == 1){
			if(npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if(playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}
		
		if(timeOutCounter > Config.TIMEOUT) {
			disconnected = true;
		}
		
		timeOutCounter++;
	
	}
	
	public boolean torvaEquipped(int slot) {
		if(slot == playerHat) {
			if(playerEquipment[slot] == 20135 || playerEquipment[slot] == 20147 || playerEquipment[slot] == 20159)
				return true;
		} else if(slot == playerChest) {
			if(playerEquipment[slot] == 20163 || playerEquipment[slot] == 20151 || playerEquipment[slot] == 20139)
				return true;
		} else if(slot == playerLegs) {
			if(playerEquipment[slot] == 20167 || playerEquipment[slot] == 20155 || playerEquipment[slot] == 20143)
				return true;
		}
		return false;
	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}
	
	public synchronized Stream getInStream() {
		return inStream;
	}
	
	public synchronized int getPacketType() {
		return packetType;
	}
	
	public synchronized int getPacketSize() {
		return packetSize;
	}
	
	public synchronized Stream getOutStream() {
		return outStream;
	}
	
	public ItemAssistant getItems() {
		return itemAssistant;
	}
		
	public PlayerAssistant getPA() {
		return playerAssistant;
	}
	
	public DialogueHandler getDH() {
		return dialogueHandler;
	}
	
	public ShopAssistant getShops() {
		return shopAssistant;
	}
	
	public TradeAndDuel getTradeAndDuel() {
		return tradeAndDuel;
	}
	public Dicing getDicing() {
		return dicing;
	}
	
	public CombatAssistant getCombat() {
		return combatAssistant;
	}
	
	public Curses curses() {
		return curses;
	}
	
	public Summoning getSummoning() {
		return summoning;
	}
	
	public ActionHandler getActions() {
		return actionHandler;
	}
  
	public PlayerKilling getKill() {
		return playerKilling;
	}
	
	public IoSession getSession() {
		return session;
	}
	
	public Potions getPotions() {
		return potions;
	}
	
	public PotionMixing getPotMixing() {
		return potionMixing;
	}
	
	public Food getFood() {
		return food;
	}
	
	/**
	 * Skill Constructors
	 */
	public Slayer getSlayer() {
		return slayer;
	}
	
	public Runecrafting getRunecrafting() {
		return runecrafting;
	}
	
	public Woodcutting getWoodcutting() {
		return woodcutting;
	}
	
	public Mining getMining() {
		return mine;
	}
	
	public Cooking getCooking() {
		return cooking;
	}
	
	public Agility getAgility() {
		return agility;
	}
	
	public Fishing getFishing() {
		return fish;
	}
	
	public Crafting getCrafting() {
		return crafting;
	}
	
	public Smithing getSmithing() {
		return smith;
	}
	
	public Farming getFarming() {
		return farming;
	}
	
	public Thieving getThieving() {
		return thieving;
	}
	
	public Herblore getHerblore() {
		return herblore;
	}
	
	public Firemaking getFiremaking() {
		return firemaking;
	}
	
	public SmithingInterface getSmithingInt() {
		return smithInt;
	}
	
	public Prayer getPrayer() { 
		return prayer;
	}
	
	public Fletching getFletching() { 
		return fletching;
	}
	
	/**
	 * End of Skill Constructors
	 */
	
	public void queueMessage(Packet arg1) {
		synchronized(queuedPackets) {
				queuedPackets.add(arg1);
		}
	}
	
	public synchronized boolean processQueuedPackets() {
		Packet p = null;
		synchronized(queuedPackets) {
			p = queuedPackets.poll();
		}
		if(p == null) {
			return false;
		}
		inStream.currentOffset = 0;
		packetType = p.getId();
		packetSize = p.getLength();
		inStream.buffer = p.getData();
		if(packetType > 0) {
			PacketHandler.processPacket(this, packetType, packetSize);
		}
		timeOutCounter = 0;
		return true;
	}
	
	public synchronized boolean processPacket(Packet p) {
		synchronized (this) {
			if(p == null) {
				return false;
			}
			inStream.currentOffset = 0;
			packetType = p.getId();
			packetSize = p.getLength();
			inStream.buffer = p.getData();
			if(packetType > 0) {
				PacketHandler.processPacket(this, packetType, packetSize);
			}
			timeOutCounter = 0;
			return true;
		}
	}
	
	
	public void correctCoordinates() {
		if (inPcGame()) {
			getPA().movePlayer(2657, 2639, 0);
		}
		if (inFightCaves()) {
			getPA().movePlayer(absX, absY, playerId * 4);
			sendMessage("Your wave will start in 10 seconds.");
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer c) {
					Server.fightCaves.spawnNextWave((Client)Server.playerHandler.players[playerId]);
					c.stop();
				}
				}, 10000);
		
		}
	
	}
	
}
