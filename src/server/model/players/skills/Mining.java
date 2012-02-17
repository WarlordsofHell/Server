package server.model.players.skills;

import server.model.players.*;
import server.Config;
import server.util.Misc;

/**
* @Author Sanity : Highly Edited by Foreverrr
*/

public class Mining {
	
	Client c;
	
	private final int VALID_PICK[] = {1265,1267,1269,1273,1271,1275};
	private final int[] PICK_REQS = {1,1,6,6,21,31,41,61};
	private final int[] RANDOM_GEMS = {1623,1621,1619,1617,1631};
	private int 
		oreType,
		exp,
		levelReq,
		pickType,
		mineanim = 1;
	
	public Mining(Client c) {
		this.c = c;
	}
	
	public void startMining(int oreType, int levelReq, int exp) {
		c.turnPlayerTo(c.objectX, c.objectY);
		if (goodPick() > 0) {
			if (c.playerLevel[c.playerMining] >= levelReq) {
				for (int id : VALID_PICK) {
					if(id == c.playerEquipment[c.playerWeapon] && canminewithpick(c.playerEquipment[c.playerWeapon], c) || c.getItems().playerHasItem(id, 1) && canminewithpick(id, c)) {
						if (id == 1265) {
							mineanim = 625;
						}
						if (id == 1267) {
							mineanim = 626;
						}
						if (id == 1269) {
							mineanim = 627;
						}
						if (id == 1271) {
							mineanim = 628;
						}
						if (id == 1273) {
							mineanim = 629;
						}
						if (id == 1275) {
							mineanim = 624;
						}
					}
				}
				this.oreType = oreType;
				this.exp = exp;
				this.levelReq = levelReq;
				this.pickType = goodPick();
				c.sendMessage("You swing your pick at the rock.");
				c.miningTimer = getMiningTimer(oreType);
				c.startAnimation(mineanim);
			} else {
				resetMining();
				c.sendMessage("You need a mining level of " + levelReq + " to mine this rock.");
				c.startAnimation(65535);
			}		
		} else {
			resetMining();
			c.sendMessage("You need a pickaxe to mine this rock.");
			c.startAnimation(65535);
			c.getPA().resetVariables();
		}
	}
	
	public void mineOre() {
		if (c.getItems().addItem(oreType,1)) {
			c.startAnimation(mineanim);
			c.sendMessage("You manage to mine some ore.");
			c.getPA().addSkillXP(exp * Config.MINING_EXPERIENCE, c.playerMining);
			c.getPA().refreshSkill(c.playerMining);
			c.miningTimer = getMiningTimer(oreType);
			if (Misc.random(25) == 10) {
				c.getItems().addItem(RANDOM_GEMS[(int)(RANDOM_GEMS.length * Math.random())], 1);
				c.sendMessage("You find a gem!");
			}
		} else {
			c.getPA().resetVariables();
			c.startAnimation(65535);
		}
	}
	
	public void resetMining() {
		this.oreType = -1;
		this.exp = -1;
		this.levelReq = -1;
		this.pickType = -1;
	}
	
	public int goodPick() {
		for (int j = VALID_PICK.length - 1; j >= 0; j--) {
			if (c.playerEquipment[c.playerWeapon] == VALID_PICK[j]) {
				if (c.playerLevel[c.playerMining] >= PICK_REQS[j])
					return VALID_PICK[j];
			}		
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			for (int j = VALID_PICK.length - 1; j >= 0; j--) {
				if (c.playerItems[i] == VALID_PICK[j] + 1) {
					if (c.playerLevel[c.playerMining] >= PICK_REQS[j])
						return VALID_PICK[j];
				}
			}		
		}
		return - 1;
	}

	private boolean canminewithpick(int i, Client c) {
		switch (i) {
		case 1265:
		case 1267:
			if (c.playerLevel[14] >= 1)
				return true;
			break;
		case 1269:
			if (c.playerLevel[14] >= 6)
				return true;
			break;
		case 1273:
			if (c.playerLevel[14] >= 21)
				return true;
			break;
		case 1271:
			if (c.playerLevel[14] >= 31)
				return true;
			break;
		case 1275:
			if (c.playerLevel[14] >= 41)
				return true;
		break;
		default:
			return false;
			
		}
		return false;
	}
	
	public int getMiningTimer(int ore) {
		int time = Misc.random(5);
		if (ore == 451) {
			time += 4;
		}
		return time;
	}
	
}