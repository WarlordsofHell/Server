package server.model.players.skills;

import server.model.players.Client;
import server.util.Misc;
import server.Config;

public class Cooking {
	
	Client c;
	
	public Cooking(Client c) {
		this.c = c;
	}
	
	private int[][] cookingItems = {{317,315,7954,1,30},{335,333,323,20,70},{331,329,323,30,90},{359,361,363,35,100},{377,379,381,40,120},{371,373,375,50,140},{7944,7946,7948,62,150},{383,385,387,80,210},{389,391,393,91,169}};
	
	public void itemOnObject(int id) {
		for (int j = 0; j < cookingItems.length; j++) {
			if (cookingItems[j][0] == id)
				cookFish(cookingItems[j][0],j);
		}
	}
	public void cookFish(int id, int slot) {
		for (int j = 0; j < 28; j++) {
			if (c.getItems().playerHasItem(id,1)) {
				if (c.playerLevel[c.playerCooking] >= cookingItems[slot][3]) {
					if (Misc.random(c.playerLevel[c.playerCooking] + 3 - cookingItems[slot][3]) == 1) {
						c.sendMessage("You accidently burn the fish.");
						c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
						c.getItems().addItem(cookingItems[slot][2], 1);
					} else {
						c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
						c.getItems().addItem(cookingItems[slot][1], 1);
						c.getPA().addSkillXP(cookingItems[slot][4] * Config.COOKING_EXPERIENCE, c.playerCooking);
					}
				} else {
					c.sendMessage("You need a cooking level of " + cookingItems[slot][3] + " to cook this fish.");
					break;
				}
			} else {
				break;
			}
		}
	}
	
	
}