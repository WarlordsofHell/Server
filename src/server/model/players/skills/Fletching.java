package server.model.players.skills;

import java.util.HashMap;
import java.util.Map;

import server.model.players.Client;

public class Fletching {

	Client c;
	
	public boolean fletching;

	public Fletching(Client c) {
		this.c = c;
	}

	private enum Arrows {
		HEADLESS(52, 314, 53, 15, 1),
		BRONZE(53, 39, 882, 40, 1),
		IRON(53, 40, 884, 58, 15),
		STEEL(53, 41, 886, 95, 30),
		MITHRIL(53, 42, 888, 132, 45),
		ADAMANT(53, 43, 890, 170, 60),
		RUNE(53, 44, 892, 207, 75);

		public int item1, item2, outcome, xp, levelReq;
		private Arrows(int item1, int item2, int outcome, int xp, int levelReq) {
			this.item1 = item1;
			this.item2 = item2;
			this.outcome = outcome;
			this.xp = xp;
			this.levelReq = levelReq;
		}
		public int getItem1() {
			return item1;
		}

		public int getItem2() {
			return item2;
		}

		public int getOutcome() {
			return outcome;
		}

		public int getXp() {
			return xp;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public static final Map<Integer, Arrows> arr = new HashMap<Integer, Arrows>();

		public static Arrows forId(int id) {
			return arr.get(id);
		}

		static {
			for (Arrows ar : Arrows.values()) {
				arr.put(ar.getItem2(), ar);
			}
		}
	}

	public int getPrimary(int item1, int item2) {
		return item1 == 52 || item1 == 53 ? item2 : item1;
	}

	public void makeArrows(int item1, int item2) {
		System.out.println(getPrimary(item1, item2));
		Arrows arr = Arrows.forId(getPrimary(item1, item2));
		if (arr == null) {
			return;
		}
		if (c.playerLevel[c.playerFletching] >= arr.getLevelReq()) {
			if (c.getItems().getItemCount(arr.getItem1()) >= 15 && c.getItems().getItemCount(arr.getItem2()) >= 15) {
				c.getItems().deleteItem(arr.getItem1(), c.getItems().getItemSlot(arr.getItem1()), 15); 
				c.getItems().deleteItem(arr.getItem2(), c.getItems().getItemSlot(arr.getItem2()), 15);
				c.getItems().addItem(arr.getOutcome(), 15);
				c.getPA().addSkillXP(arr.getXp(), c.playerFletching);
			} else {
				c.sendMessage("You must have at least 15 of each supply to make arrows!");
			}
		} else {
			c.sendMessage("You need a fletching level of "+arr.getLevelReq()+" to fletch this.");
		}
	}
	private enum Fletch {

		ARROWSHAFTS(1511, 52, 5, 15),

		SHORTBOW(1511, 841, 5, 5),
		LONGBOW(1511, 839, 10, 10),

		OAKSBOW(1521, 843, 17, 20),
		OAKLBOW(1521, 845, 25, 25),

		WILLOWSBOW(1519, 849, 34, 35),
		WILLOWLBOW(1519, 847, 42, 40),

		MAPLESBOW(1517, 853, 50, 50),
		MAPLELBOW(1517, 851, 59, 55),

		YEWSBOW(1515, 857, 68, 65),
		YEWLBOW(1515, 855, 75, 70),

		MAGICSBOW(1513, 861, 84, 80),
		MAGICLBOW(1513, 859, 92, 87);

		public int logID, unstrungBow, xp, levelReq;

		private Fletch(int logID, int unstrungBow, int xp, int levelReq) {
			this.logID = logID;
			this.unstrungBow = unstrungBow;
			this.xp = xp;
			this.levelReq = levelReq;
		}

		public int getLogID() {
			return logID;
		}

		public int getBowID() {
			return unstrungBow;
		}

		public int getXp() {
			return xp;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public static final Map<Integer, Fletch> fle = new HashMap<Integer, Fletch>();

		public static Fletch forId(int id) {
			return fle.get(id);
		}

		static {
			for (Fletch fl : Fletch.values()) {
				fle.put(fl.getBowID(), fl);
			}
		}
	}

	public void handleLog(int item1, int item2) {
		openFletching((item1 == 946) ? item2 : item1);
	}
	
	private void resetFletching() {
		fletching = false;
		c.log = -1;
	}

	public void handleFletchingClick(int abutton) {
		switch (abutton) {
		case 34185:
			switch (c.log) {
			case 1511: //Normal log
				fletchBow(841);
				break;
			case 1521: //Oak log
				fletchBow(843);
				break;
			case 1519: //Willow log
				fletchBow(849);
				break;
			case 1517: //Maple log
				fletchBow(853);
				break;
			case 1515: //Yew log
				fletchBow(857);
				break;
			case 1513: //Magic logs
				fletchBow(861);
				break;
			}
			break;
		case 34189:
			switch (c.log) {
			case 1511: //Normal log
				fletchBow(839);
				break;
			case 1521: //Oak log
				fletchBow(845);
				break;
			case 1519: //Willow log
				fletchBow(847);
				break;
			case 1517: //Maple log
				fletchBow(851);
				break;
			case 1515: //Yew log
				fletchBow(855);
				break;
			case 1513: //Magic logs
				fletchBow(859);
				break;
			}
			break;
		case 34193: //Arrow shafts
			fletchBow(52);
			break;
		}
	}

	public void fletchBow(int id) {
		Fletch fle = Fletch.forId(id);
		if (fle == null) {
			return;
		}
		if (id == 52) {
			int[] logArray = {1511, 1521, 1519, 1517, 1515, 1513};
			for (int i = 0; i < logArray.length; i++)
				if (c.getItems().playerHasItem(logArray[i])) {
					c.getItems().deleteItem(logArray[i], c.getItems().getItemSlot(logArray[i]), 1);
					c.getItems().addItem(fle.getBowID(), 15);
					c.getPA().addSkillXP(fle.getXp(), c.playerFletching);
					return;
				}
		} else {
			if (c.getItems().playerHasItem(fle.getLogID())) {
				if (c.playerLevel[c.playerFletching] >= fle.getLevelReq()) {
					c.getItems().deleteItem(fle.getLogID(), c.getItems().getItemSlot(fle.getLogID()), 1);
					c.getItems().addItem(fle.getBowID(), 1);
					c.getPA().addSkillXP(fle.getXp(), c.playerFletching);
					c.startAnimation(1248);
				} else {
					c.sendMessage("You need a fletching level of at least" +fle.getLevelReq()+" to cut this log.");
				}
			} 
		}
		resetFletching();
		c.getPA().removeAllWindows();
	}
	int[][] ifItems = {
			{1511, 839, 841},
			{1521, 845, 843},
			{1519, 847, 849},
			{1517, 851, 853},
			{1515, 855, 857},
			{1513, 859, 861}
	};

	public void openFletching(int item) {
		for (int i = 0; i < ifItems.length; i++) {
			if (ifItems[i][0] == item) {
				c.getPA().sendFrame164(8880);
				c.getPA().sendFrame126("What would you like to make?", 8879);
				c.getPA().sendFrame246(8884, 250, ifItems[i][1]); // middle
				c.getPA().sendFrame246(8883, 250, ifItems[i][2]); // left picture
				c.getPA().sendFrame246(8885, 250, 52); // right pic
				c.getPA().sendFrame126("Shortbow", 8889);
				c.getPA().sendFrame126("Longbow", 8893);
				c.getPA().sendFrame126("Arrow Shafts", 8897);
			}
		}
		c.log = item;
		fletching = true;
	}
}