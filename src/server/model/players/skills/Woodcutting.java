package server.model.players.skills;

import server.model.players.*;
import server.Server;
import server.Config;
import server.util.Misc;

/**
* @Recoded by Xaero
*/

public class Woodcutting {
	
	Client c;
	
	private final int VALID_AXE[] = {1351,1349,1353,1361,1355,1357,1359,6739, 13661};
	private final int[] AXE_REQS = {1,1,6,6,21,31,41,61, 61};
	public final int EMOTES[] = {879,877,875,873,871,869,867,2846, 10251};
	private int logType;
	private int exp;
	private int levelReq;
	private int axeType;
	private int treeType;
	private int counts = 0;
	private int logsLeft;
	private boolean allowLogs = false;
	private boolean firstEmote = false;
	
	private final int maxLogsInOneTree = 25;
	private final int treeRespawnTimer = 5;//seconds
	
	public Woodcutting(Client c) {
		this.c = c;
	}
		public void GetBirdNest() {
	Server.itemHandler.createGroundItem(c, 5070 + Misc.random(4), c.getX(), c.getY(), 1, c.getId());//Makes any item with the ID of 5070 to 5074 appear on the ground.
	c.sendMessage("A bird's nest falls out of the tree!");//Sends Message telling you that a birds nest has fallen out of the tree.
}

	public int randomBirdNests() {
		return birdNestRewards[(int)(Math.random()*birdNestRewards.length)];
	}
	
	public void HandleItemClick(int itemId) {
			if (itemId >= 5070 && itemId <= 5074){
			c.getItems().deleteItem(itemId, c.getItems().getItemSlot(itemId), 1);
			c.getItems().addItem(5075, 1);
			c.getItems().addItem(randomBirdNests(), 10);
		}
	}
	public static int birdNestRewards[] = {1636,5297,995,5298,5299,5296,5295,1644,1642,995,7498};//Random Rewards for bird nests
	public void startWoodcutting(int logType, int levelReq, int exp) {
		if (c.getItems().freeSlots() > 0) {
			if (goodAxe() > 0) {
				if (c.playerLevel[c.playerWoodcutting] >= levelReq) {
					this.logType = logType;
					this.exp = exp;
					this.levelReq = levelReq;
					this.axeType = goodAxe();
					this.treeType = c.objectId;
					this.logsLeft = 1 + Misc.random(maxLogsInOneTree - 1);
					c.wcTimer = getWcTimer();
                    c.cuttingTree = true;
					this.firstEmote = true;
					c.wcEmoteDelay = System.currentTimeMillis();
					c.sendMessage("You swing your axe at the tree.");
					c.turnPlayerTo(c.objectX, c.objectY);
				} else {
					c.getPA().resetVariables();
					c.startAnimation(65535);
					c.sendMessage("You need a woodcutting level of " + levelReq + " to cut this tree.");
				}		
			} else {
				c.startAnimation(65535);
				c.sendMessage("You don't have an axe which you have the Woodcutting level to use.");
				c.getPA().resetVariables();
			}
		} else { 
			c.getDH().sendStatement("Not enough space in your inventory."); 
			c.turnPlayerTo(c.objectX, c.objectY);
			resetWoodcut();
		}	
	}
	
	public void resetWoodcut() {
		this.logType = -1;
		this.exp = -1;
		this.levelReq = -1;
		this.axeType = -1;
		c.wcTimer = -1;	
		c.cuttingTree = false;
		this.firstEmote = false;
		this.allowLogs = false;
		this.counts = 0;
	}
	
	public void cutWood() {
		if(this.allowLogs){
			if (c.getItems().addItem(logType,1)) {
				this.logsLeft--;
				c.sendMessage("You get some " + getTreeName(treeType) + " logs.");
				if(Misc.random(100) == 0) {//if random number lands on 100, starting at 0 do:
				GetBirdNest();
				}
				if(Misc.random(100) == 5) {//if random number lands on 100, starting at 0 do:
				c.getRandomEvent().TreeSpirit(c);
				}
				if (c.playerEquipment[c.playerFeet] == 10933) {
					c.getPA().addSkillXP(exp * Config.WOODCUTTING_EXPERIENCE * 2, c.playerWoodcutting);
				}
				if (c.playerEquipment[c.playerChest] == 10939) { //Lumberjack Chest
					c.getPA().addSkillXP(exp * Config.WOODCUTTING_EXPERIENCE * 2, c.playerWoodcutting);
				}
				if (c.playerEquipment[c.playerLegs] == 10940) { //Lumberjack Legs
					c.getPA().addSkillXP(exp * Config.WOODCUTTING_EXPERIENCE * 2, c.playerWoodcutting);
				}
				if (c.playerEquipment[c.playerHat] == 10941) {
					c.getPA().addSkillXP(exp * Config.WOODCUTTING_EXPERIENCE * 2, c.playerWoodcutting);
				} else {
				c.getPA().addSkillXP(exp * Config.WOODCUTTING_EXPERIENCE, c.playerWoodcutting);
				}
				c.getPA().refreshSkill(c.playerWoodcutting);
				c.wcTimer = getWcTimer();
				if(this.logsLeft == 0){
					spawnStump();
				}
			} else {
				c.getPA().resetVariables();
				resetWoodcut();
			}
		}
	}

	public void spawnStump(){
		int stump = getStump();
		int stumpx = c.objectX;
		int stumpy = c.objectY;
		for(int i = 0; i < Server.playerHandler.players.length; i++){
			Client all = (Client)Server.playerHandler.players[i];
			if(all != null){
				all.getPA().object(stump, stumpx, stumpy, 0, 10);
			}
		}
		int slot = getFreeRespawnSlot();
		Server.trees[slot] = new TreeRespawn(slot);
		Server.trees[slot].respawnDelay = System.currentTimeMillis();
		Server.trees[slot].respawnTimer = treeRespawnTimer;
		Server.trees[slot].respawnRequired = true;
		Server.trees[slot].respawnTree = c.objectId;
		Server.trees[slot].respawnX = c.objectX;
		Server.trees[slot].respawnY = c.objectY;
		Server.trees[slot].stumpId = stump;
		Server.trees[slot].stumpX = stumpx;
		Server.trees[slot].stumpY = stumpy;
		Server.treesToRespawn.add(slot);
		Server.treesToRespawn2 = Server.convertIntegers(Server.treesToRespawn);
		for(int i = 0; i < Server.playerHandler.players.length; i++){
			Client all = (Client)Server.playerHandler.players[i];
			if(all != null){
				if(all.objectX == Server.trees[slot].respawnX && all.objectY == Server.trees[slot].respawnY){
				all.getPA().resetVariables();
				all.getWoodcutting().resetWoodcut();
				}
			}
		}
		c.startAnimation(65535);
	}

	public int getFreeRespawnSlot(){
		for(int i = 0; i < Server.trees.length; i++){
			if(Server.trees[i] == null){
				return i;
			}
		}
		return -1;
	}

	public int getStump(){
		switch(c.objectId){
			case 1276:
			case 1278://trees
				return 1342;
			case 1281: //oak
				return 1355;
			case 1308: //willow
				return 7399;
			case 1307: //maple
				return 7400;
			case 1309: //yew
				return 7402;
			case 1306: //magic
				return 7401;
		}
		return 6212;
	}

	public int goodAxe() {
		for (int j = VALID_AXE.length - 1; j >= 0; j--) {
			if (c.playerEquipment[c.playerWeapon] == VALID_AXE[j]) {
				if (c.playerLevel[c.playerWoodcutting] >= AXE_REQS[j])
					return VALID_AXE[j];
			}		
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			for (int j = VALID_AXE.length - 1; j >= 0; j--) {
				if (c.playerItems[i] == VALID_AXE[j] + 1) {
					if (c.playerLevel[c.playerWoodcutting] >= AXE_REQS[j])
						return VALID_AXE[j];
				}
			}		
		}
		return - 1;
	}
	
	public int getWcTimer() {
		int aType = getAxeSlot(this.axeType) + 1;
		int tType = getTreeRate(this.treeType);
		int lvlRate = (c.playerLevel[c.playerWoodcutting] / 10) / 2;
		int time = Misc.random(13 - aType - lvlRate + tType);
		return time;
	}

	public void wcProcesses(){
		startEmote();
		if(c.cuttingTree && this.firstEmote == false){
			if(System.currentTimeMillis() - c.wcEmoteDelay < 2000){
				return;
			}
			if(c.isMoving == false){
				c.startAnimation(this.EMOTES[getAxeSlot(this.axeType)]);
				c.turnPlayerTo(c.objectX, c.objectY);
				c.wcEmoteDelay = System.currentTimeMillis();
				if(this.allowLogs == false){
					this.allowLogs = true;
				}
			}
		}
	}


	public void startEmote(){
		if(c.cuttingTree && this.firstEmote == true){
				if(c.isMoving == false && this.counts == 0){
					this.counts++;
					return;
				}
				if(c.isMoving){
					return;
				}
				c.startAnimation(EMOTES[getAxeSlot(this.axeType)]);
				c.turnPlayerTo(c.objectX, c.objectY);
				this.counts = 0;
				c.wcEmoteDelay = System.currentTimeMillis();
				this.firstEmote = false;
		}
	}

	public int getAxeSlot(int type){
		for(int a = 0; a < VALID_AXE.length; a++){
			if(VALID_AXE[a] == type){
				return a;
			}
		}
		return -1;
	}


	public int getTreeRate(int type){
		switch(type){
			case 1276:
			case 1278://trees
				return 1;
			case 1281: //oak
				return 2;
			case 1308: //willow
				return 3;
			case 1307: //maple
				return 4;
			case 1309: //yew
				return 5;
			case 1306: //magic
				return 6;
		}
		return 0;
	}

	public String getTreeName(int type){
		switch(type){
			case 1276:
			case 1278://trees
				return "tree";
			case 1281: //oak
				return "oak";
			case 1308: //willow
				return "willow";
			case 1307: //maple
				return "maple";
			case 1309: //yew
				return "yew";
			case 1306: //magic
				return "magic";
		}
		return "";
	}

}