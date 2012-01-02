package server.model.minigames;

import server.model.players.Client;
import server.Server;

/**
 * @author Sanity
 */

public class CorperalBeast {
	
	public CorperalBeast() {
		
	}
	
	public final int GAME_TIMER = 70; //5 minutes
	public final int WAIT_TIMER = 7;
	
	public static int gameTimer = -1;
	public int waitTimer = 15;
	public int properTimer = 0;
	
	public void process() {
		setInterface();
		if (properTimer > 0) {
			properTimer--;
			return;
		} else {
			properTimer = 4;
		}
		if (waitTimer > 0)
			waitTimer--;
		else if (waitTimer == 0)
			startGame();
		if (gameTimer > 0) {
			gameTimer--;
			if (CorpisDead) {
				endGame(true);
			}
		} else if (gameTimer == 0)
			endGame(false);
	}
	
	public void startGame() {
		if (playersInCorpWait() > 2) {
			gameTimer = GAME_TIMER;
			waitTimer = -1;
			//spawn npcs
			spawnNpcs();	
			//move players into game
			for (int j = 0; j < Server.playerHandler.players.length; j++) {
				if (Server.playerHandler.players[j] != null) {
					if (Server.playerHandler.players[j].inPcBoat()) {
						movePlayer(j);
					}			
				}		
			}
		} else {
			waitTimer = WAIT_TIMER;
			for (int j = 0; j < Server.playerHandler.players.length; j++) {
				if (Server.playerHandler.players[j] != null) {
					if (Server.playerHandler.players[j].inPcBoat()) {
						Client c = (Client)Server.playerHandler.players[j];
						c.sendMessage("There need to be at least 3 players to start a game of pest control.");
					}			
				}		
			}
		}
	}

	public void setInterface() {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				if (Server.playerHandler.players[j].inCorpWait()) {
					Client c = (Client) Server.playerHandler.players[j];
					c.getPA().sendFrame126("Next Departure: "+waitTimer+"", 21006);
					c.getPA().sendFrame126("Players Ready: "+playersInBoat()+"", 21007);
					c.getPA().sendFrame126("(Need 3 to 25 players)", 21008);
					c.getPA().sendFrame126("FXP: "+c.pcPoints+"", 21009);
				}
 				if (Server.playerHandler.players[j].inCorpArea()) {
					Client c = (Client) Server.playerHandler.players[j];
					c.getPA().sendFrame126("Time remaining: "+gameTimer+"", 21111);
						}
					}
				}
			}
		}
	}
	
	public int playersInCorpWait() {
		int count = 0;
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				if (Server.playerHandler.players[j].inCorpWait()) {
						count++;
				}
			}
		}
		return count;
	}
	
	public void endGame(boolean won) {
		gameTimer = -1;
		waitTimer = WAIT_TIMER;
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				if (Server.playerHandler.players[j].inPcGame()) {
					Client c = (Client)Server.playerHandler.players[j];
					c.getPA().movePlayer(2657, 2639, 0);
					if (won) {
						c.sendMessage("You have killed the Corperal Beast.");
						c.pcPoints += 4;
						c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
						c.playerLevel[5] = c.getLevelForXP(c.playerXP[5]);
						c.specAmount = 10;
						c.getItems().addItem(995, c.combatLevel * 50);
						c.getPA().refreshSkill(3);
						c.getPA().refreshSkill(5);
					} else if (won) {
						c.sendMessage("The void knights notice your lack of zeal.");
					} else {
						c.sendMessage("You have failed to kill the Corperal Beast");
					}
					c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
					c.getCombat().resetPrayers();
				}			
			}		
		}

		for (int j = 0; j < Server.npcHandler.npcs.length; j++) {
			if (Server.npcHandler.npcs[j] != null) {
				if (Server.npcHandler.npcs[j].npcType >= 6142 && Server.npcHandler.npcs[j].npcType <= 6145)
					Server.npcHandler.npcs[j] = null;
			}			
		}
	}
	
	public boolean CorpisDead() {
		int count = 0;
		for (int j = 0; j < Server.npcHandler.npcs.length; j++) {
			if (Server.npcHandler.npcs[j] != null) {
				if (Server.npcHandler.npcs[j].npcType = 8133)
					if (Server.npcHandler.npcs[j].needRespawn)
						count++;		
			}			
		}
		return count >= 4;	
	}
	
	public void movePlayer(int index) {
		Client c = (Client)Server.playerHandler.players[index];
		if (c.combatLevel < 110) {
			c.sendMessage("You must be at least combat level 110 to enter this boat.");
			return;
		}
		c.getPA().movePlayer(2658,2611,0);
	}
	private final int[][] WAVES = {{5213, 5213, 5230, 5230}, // Wave 1
									{5213, 5213, 5230, 5230, 5213, 5213, 5230, 5230}, // Wave 2
									{5214, 5214, 5231, 5231, 5213, 5213, 5230, 5230}, // Wave 3
									{5214, 5214, 5231, 5231, 5214, 5214, 5231, 5231}, // Wave 5
									{5215, 5215, 5232, 5232, 5214, 5214, 5231, 5231}, // Wave 4
									{5215, 5215, 5232, 5232, 5215, 5215, 5232, 5232}, // Wave 5
									{8133}, // Wave 25 - FINAL WAVE
									};
									
	private final int[][] COORDS = {{3160, 9758}, {3176, 9758}, {3169, 9755}, {3168, 9761}, 
									{3161, 9758}, {3175, 9758}, {3169, 9756}, {3168, 9760}, 
									{3164, 9761}, {3164, 9755}, {3172, 9761}, {3172, 9755}};
	public void spawnWave(Client c) {
		if (c != null && c.inCorpArea) {
			c.getCombat().resetPrayers();
			if (c.barbWave >= WAVES.length) {
				endGame(c, true);
				c.barbWave = 0;
				return;				
			}
			if (c.barbWave < 0)
				return;
			int npcAmount = WAVES[c.barbWave].length;
			for (int j = 0; j < npcAmount; j++) {
				int npc = WAVES[c.barbWave][j];
				int X = COORDS[j][0];
				int Y = COORDS[j][1];
				int H = c.heightLevel;
				int hp = getHp(npc);
				int max = getMax(npc);
				int atk = getAtk(npc);
				int def = getDef(npc);
				Server.npcHandler.spawnNpc(c, npc, X, Y, H, 0, hp, max, atk, def, true, npc == 5247);
				if (npc == 5247)
					spawnHelpers(c, COORDS);
				c.getPA().stillGfx(86, X, Y, c.heightLevel, 0);
			}
			if (c.barbLeader <= 0) 
				Server.npcHandler.spawnNpc3(c, 751, 3168, 9758, c.heightLevel, 0, 0, 0, 0, 0, false, false);
			NPC n = Server.npcHandler.npcs[c.barbLeader];
			n.requestAnimation(6728, 0);
			n.gfx0(1176);
			n.forceChat(n.barbRandom(c, Misc.random(5)));
			c.barbsToKill = npcAmount;
			c.barbsKilled = 0;
		}
	}
	public void spawnNpcs() {
		Server.npcHandler.spawnNpc2(6142,2628,2591,0,0,200,0,0,100);
		Server.npcHandler.spawnNpc2(6143,2680,2588,0,0,200,0,0,100);
		Server.npcHandler.spawnNpc2(6144,2669,2570,0,0,200,0,0,100);
		Server.npcHandler.spawnNpc2(6145,2645,2569,0,0,200,0,0,100);
	}


}