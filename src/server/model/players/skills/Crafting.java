package server.model.players.skills;

import java.util.HashMap;
import java.util.Map;

import server.model.players.Client;
import server.util.Misc;

public class Crafting {

	Client c;

	public Crafting(Client c) {
		this.c = c;
	}

	public enum LeatherCrafting {

		LEATHERVAMBS(1741, 1063, 11, 22, 1),
		LEATHERCHAPS(1741, 1095, 14, 25, 1),
		LEATHERBODY(1741, 1129, 18, 27, 1),

		GREENVAMBS(1745, 1065, 57, 62, 1),
		GREENCHAPS(1745, 1099, 60, 124, 2),
		GREENBODY(1745, 1135, 63, 186, 3),

		BLUEVAMBS(2505, 2487, 66, 70, 1),
		BLUECHAPS(2505, 2493, 68, 140, 2),
		BLUEBODY(2505, 2499, 71, 210, 3),

		REDVAMBS(2507, 2489, 73, 78, 1),
		REDCHAPS(2507, 2495, 75, 156, 2),
		REDBODY(2507, 2501, 77, 234, 3),

		BLACKVAMBS(2509, 2491, 79, 86, 1),
		BLACKCHAPS(2509, 2497, 82, 172, 2),
		BLACKBODY(2509, 2503, 84, 258, 3);

		private int leatherId, outcome, reqLevel, XP, reqAmt;
		private LeatherCrafting(int leatherId, int outcome, int reqLevel, int XP, int reqAmt) {
			this.leatherId = leatherId;
			this.outcome = outcome;
			this.reqLevel = reqLevel;
			this.XP = XP;
			this.reqAmt = reqAmt;	
		}

		public int getLeather() {
			return leatherId;
		}

		public int getOutcome() {
			return outcome;
		}

		public int getReqLevel() {
			return reqLevel;
		}

		public int getXP() {
			return XP;
		}

		public int getReqAmt() {
			return reqAmt;
		}

		private static final Map <Integer,LeatherCrafting> lea = new HashMap<Integer,LeatherCrafting>();

		public static LeatherCrafting forId(int id) {
			return lea.get(id);
		}

		static {
			for (LeatherCrafting l : LeatherCrafting.values()) {
				lea.put(l.getOutcome(), l);
			}
		}

	}

	int[][] leathers = {
			{1741, 1095, 1063, 1129},
			{1745, 1099, 1065, 1135},
			{2505, 2493, 2487, 2499},
			{2507, 2495, 2489, 2501},
			{2509, 2497, 2491, 2503}};

	public void openLeather(int hide) {
		for (int i = 0; i < leathers.length; i++) {
			if (leathers[i][0] == hide) {
				c.getPA().sendFrame164(8880); //leather
				c.getPA().sendFrame126("What would you like to make?", 8879);
				c.getPA().sendFrame246(8884, 250, leathers[i][1]); // middle
				c.getPA().sendFrame246(8883, 250, leathers[i][2]); // left picture
				c.getPA().sendFrame246(8885, 250, leathers[i][3]); // right pic
				c.getPA().sendFrame126("Vambs", 8889);
				c.getPA().sendFrame126("Chaps", 8893);
				c.getPA().sendFrame126("Body", 8897);
			}
		}
		c.craftingLeather = true;
		c.hideId = hide;
	}

	public void handleLeather(int item1, int item2) {
		openLeather((item1 == 1733) ? item2 : item1);
	}

	public void handleCraftingClick(int clickId) {
		switch (clickId) {
		case 34185: //Vambs
			switch (c.hideId) {
			case 1741:
				craftLeather(1063); //Leather vambs
				break;
			case 1745:
				craftLeather(1065); //Green d'hide vambs
				break;
			case 2505:
				craftLeather(2487); //Blue d'hide vambs
				break;
			case 2507:
				craftLeather(2489); //Red d'hide vambs
				break;
			case 2509:
				craftLeather(2491); //Black d'hide vambs
				break;
			}
			break;
		case 34189:
			switch (c.hideId) {
			case 1741:
				craftLeather(1095); //Leather chaps
				break;
			case 1745:
				craftLeather(1099); //Green d'hide chaps
				break;
			case 2505:
				craftLeather(2493); //Blue d'hide chaps
				break;
			case 2507:
				craftLeather(2495); //Red d'hide chaps
				break;
			case 2509:
				craftLeather(2497); //Black d'hide chaps
				break;
			}
			break;
		case 34193:
			switch (c.hideId) {
			case 1741:
				craftLeather(1129); //Leather body
				break;
			case 1745:
				craftLeather(1135); //Green d'hide body
				break;
			case 2505:
				craftLeather(2499); //Blue d'hide body
				break;
			case 2507:
				craftLeather(2501); //Red d'hide body
				break;
			case 2509:
				craftLeather(2503); //Black d'hide body
				break;
			}
			break;
		}
	}

	public void craftLeather(int id) {
		LeatherCrafting lea = LeatherCrafting.forId(id);
		if (lea == null) {
			return;
		}
		if (c.playerLevel[c.playerCrafting] >= lea.getReqLevel()) {
			if (c.getItems().playerHasItem(lea.getLeather(), lea.getReqAmt())) {
				c.startAnimation(1249);
				c.getItems().deleteItem(lea.getLeather(), lea.getReqAmt());
				c.getItems().addItem(lea.getOutcome(), 1);
				c.getPA().addSkillXP(lea.getXP(), c.playerCrafting);
				resetCrafting();
			} else {
				c.sendMessage("You do not have enough items to craft this item.");
			}
		} else {
			c.sendMessage("You need a crafting level of "+lea.getReqLevel()+" to craft this item.");
		}
		c.getPA().removeAllWindows();
	}

	public void resetCrafting() {
		c.craftingLeather = false;
		c.hideId = -1;
	}

	public enum GemCrafting {

		OPAL(1625, 1609, 891, 1, 15),
		JADE(1627, 1611, 891, 13, 20),
		REDTOPAZ(1629, 1613, 892, 16, 25),
		SAPPHIRE(1623, 1607, 888, 1, 50),
		EMERALD(1621, 1605, 889, 27, 68),
		RUBY(1619, 1603, 887, 34, 85),
		DIAMOND(1631, 1601, 890, 43, 108),
		DRAGONSTONE(6571, 1615, 890, 55, 138),
		ONYX(6571, 6573, 2717, 67, 168);

		private int uncutID, cutID, animation, levelReq, XP;
		private GemCrafting(int uncutID, int cutID, int animation, int levelReq, int XP) {
			this.uncutID = uncutID;
			this.cutID = cutID;
			this.animation = animation;
			this.levelReq = levelReq;
			this.XP = XP;
		}

		public int getUncut() {
			return uncutID;
		}

		public int getCut() {
			return cutID;
		}

		public int getAnim() {
			return animation;
		}

		public int getReq() {
			return levelReq;
		}

		public int getXP() {
			return XP;
		}

		private static final Map <Integer,GemCrafting> gem = new HashMap<Integer,GemCrafting>();

		public static GemCrafting forId(int id) {
			return gem.get(id);
		}

		static {
			for (GemCrafting g : GemCrafting.values()) {
				gem.put(g.getUncut(), g);
			}
		}
	}

	public void handleChisel(int id1, int id2) {
		cutGem((id1 == 1755) ? id2 : id1);
	}

	private boolean isSemiprecious(int id) {
		int[] semipreciousGems = {1625, 1627, 1629};
		for (int i = 0; i < semipreciousGems.length; i++) {
			if (id == semipreciousGems[i]) {
				return true;
			}
		}
		return false;
	}

	public void cutGem(int id) {
		GemCrafting gem = GemCrafting.forId(id);
		if (gem == null) {
			return;
		}
		if (c.getItems().playerHasItem(gem.getUncut(), 1)) {
			if (c.playerLevel[c.playerCrafting] >= gem.getReq()) {
				c.getItems().deleteItem(gem.getUncut(), 1);
				if (isSemiprecious(gem.getUncut())) {
					if (Misc.random(100) == 37) {
						c.sendMessage("You accidently crush the gem!");
						c.getItems().addItem(1633, 1);
					}
				} else {
					c.getItems().addItem(gem.getCut(), 1);
					c.getPA().addSkillXP(gem.getXP(), c.playerCrafting);
				}
				c.startAnimation(gem.getAnim());
			} else {
				c.sendMessage("You need a crafting level of "+gem.getReq()+" to cut this gem.");
			}
		}
	}
}