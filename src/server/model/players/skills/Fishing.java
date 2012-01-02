package server.model.players.skills;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.util.Misc;

	/**
	 * Class Fishing
	 * Handles:	Fishing
	 * @author:	2012/Kyle
	 * START:	22:07 23/12/2010
	 * FINISH:	22:28 23/12/2010
	 */

public class Fishing extends SkillHandler {

	private final static int[][] data = {
		{1, 1, 303, -1, 317, 10, 621, 321, 15, 30},		//SHRIMP + ANCHOVIES
		{2, 5, 309, 313, 327, 20, 622, 345, 10, 30},	//SARDINE + HERRING
		{3, 16, 305, -1, 353, 20, 620, -1, -1, -1},		//MACKEREL
		{4, 20, 309, -1, 335, 50, 622, 331, 30, 70},	//TROUT
		{5, 23, 305, -1, 341, 45, 619, 363, 46, 100},	//BASS + COD
		{6, 25, 309, 313, 349, 60, 622, -1, -1, -1},		//PIKE
		{7, 35, 311, -1, 359, 80, 618, 371, 50, 100},	//TUNA + SWORDIE
		{8, 40, 301, -1, 377, 90, 619, -1, -1, -1},		//LOBSTER
		{9, 62, 305, -1, 7944, 120, 620, -1, -1, -1},		//MONKFISH
		{10, 76, 311, -1, 383, 110, 618, -1, -1, -1},		//SHARK
		{11, 79, 305, -1, 395, 38, 619, -1, -1, -1},		//SEA TURTLE
		{12, 81, 305, -1, 389, 46, 621, -1, -1, -1},		//MANTA RAY
	};

	private static void attemptdata(final Client c, int npcId) {
		if(c.playerSkillProp[10][4] > 0) {
			c.playerSkilling[10] = false;
			return;
		}
		if (!noInventorySpace(c, "fishing")) {
			return;
		}
 		resetFishing(c);
		for(int i = 0; i < data.length; i++) {
			if(npcId == data[i][0]) {
				if (c.playerLevel[c.playerFishing] < data[i][1]) {
					c.sendMessage("You haven't got high enough fishing level to fish here!");
					c.sendMessage("You at list need the fishing level of "+ data[i][1] +".");
					c.getDH().sendStatement("You need the fishing level of "+ data[i][1] +" to fish here.");
					return;
				}
				if(data[i][3] > 0) {
					if (!c.getItems().playerHasItem(data[i][3])) {
						c.sendMessage("You haven't got any "+ c.getItems().getItemName(data[i][3]) +"!");
						c.sendMessage("You need "+ c.getItems().getItemName(data[i][3]) +" to fish here.");
						return;
					}
				}
				c.playerSkillProp[10][0] = data[i][6]; //	ANIM
				c.playerSkillProp[10][1] = data[i][4]; //	FISH
				c.playerSkillProp[10][2] = data[i][5]; //	XP
				c.playerSkillProp[10][3] = data[i][3]; //	BAIT
				c.playerSkillProp[10][4] = data[i][2]; //	EQUIP
				c.playerSkillProp[10][5] = data[i][7]; //	sFish
				c.playerSkillProp[10][6] = data[i][8]; //	sLvl
				c.playerSkillProp[10][7] = data[i][4]; //	FISH
				c.playerSkillProp[10][8] = data[i][9]; //	sXP
				c.playerSkillProp[10][9] = Misc.random(1) == 0 ? 7 : 5;
				c.playerSkillProp[10][10] = data[i][0]; //	INDEX
				if (!hasFishingEquipment(c, c.playerSkillProp[10][4])) {
					return;
				}

				c.sendMessage("You start fishing.");
				c.startAnimation(c.playerSkillProp[10][0]);
				c.stopPlayerSkill = true;

				if(c.playerSkilling[10]) {
					return;
				}

				c.playerSkilling[10] = true;

				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(c.playerSkillProp[10][5] > 0) {
							if(c.playerLevel[c.playerFishing] >= c.playerSkillProp[10][6]) {
								c.playerSkillProp[10][1] = c.playerSkillProp[10][Misc.random(1) == 0 ? 7 : 5];
							}
						}
						if(c.playerSkillProp[10][1] > 0) {
							c.sendMessage("You catch a "+ c.getItems().getItemName(c.playerSkillProp[10][1]) +".");
						}
						if(c.playerSkillProp[10][1] > 0) {
							c.getItems().addItem(c.playerSkillProp[10][1], 1);
							c.startAnimation(c.playerSkillProp[10][0]);
						}
						if(c.playerSkillProp[10][2] > 0) {
							c.getPA().addSkillXP(c.playerSkillProp[10][2] * FISHING_XP, c.playerFishing);
						}
						if(c.playerSkillProp[10][3] > 0) {
							c.getItems().deleteItem(c.playerSkillProp[10][3], c.getItems().getItemSlot(c.playerSkillProp[10][3]), 1);
							if (!c.getItems().playerHasItem(c.playerSkillProp[10][3])) {
								c.sendMessage("You haven't got any "+ c.getItems().getItemName(c.playerSkillProp[10][3]) +" left!");
								c.sendMessage("You need "+ c.getItems().getItemName(c.playerSkillProp[10][3]) +" to fish here.");
								container.stop();
							}
						}
						if (!hasFishingEquipment(c, c.playerSkillProp[10][4])) {
							container.stop();
						}
						if (!noInventorySpace(c, "fishing")) {
							container.stop();
						}
						if (Misc.random(15) == 0) {
							container.stop();
						}
						if(!c.stopPlayerSkill) {
							container.stop();
						}
						if(!c.playerSkilling[10]) {
							container.stop();
						}
					}
					@Override
					public void stop() {
						resetFishing(c);
					}
				}, getTimer(c, npcId) + 5 + playerFishingLevel(c));
			}
		}
	}

	private static boolean hasFishingEquipment(Client c, int equipment) {
		if (!c.getItems().playerHasItem(equipment)) {
			if(equipment == 311) {
				if(!c.getItems().playerHasItem(311) && !c.getItems().playerHasItem(10129) && c.playerEquipment[3] != 10129) {
					c.sendMessage("You need a "+ c.getItems().getItemName(equipment) +" to fish here.");
					return false;
				}
			} else {
				c.sendMessage("You need a "+ c.getItems().getItemName(equipment) +" to fish here.");
				return false;
			}
		}
		return true;
	}

	private static void resetFishing(Client c) {
		c.startAnimation(65535);
		c.getPA().removeAllWindows();
		c.playerSkilling[10] = false;
		for(int i = 0; i < 11; i++) {
			c.playerSkillProp[10][i] = -1;
		}
	}

	private static int playerFishingLevel(Client c) {
		return (10 - (int)Math.floor(c.playerLevel[c.playerFishing] / 10));
	}

	private static int getTimer(Client c, int npcId) {
		switch (npcId) {
			case 1: return 2;
			case 2:	return 3;
			case 3:	return 4;
			case 4: return 4;
			case 5: return 4;
			case 6: return 5;
			case 7: return 5;
			case 8: return 5;
			case 9: return 5;
			case 10: return 5;
			case 11: return 9;
			case 12: return 9;
			default: return -1;
		}
	}

	public static void fishingNPC(Client c, int i, int l) {
		switch (i) {
			case 1:
				switch (l) {
					case 319:
					case 329:
					case 323:
					case 325:
					case 326:
					case 327:
					case 330:
					case 332:
					case 316: //NET + BAIT
						Fishing.attemptdata(c, 1);
						break;
					case 334:
					case 313: //NET + HARPOON
						Fishing.attemptdata(c, 3);
						break;
					case 322: //NET + HARPOON
						Fishing.attemptdata(c, 5);
						break;

					case 309: //LURE
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 318:
					case 328:
					case 331:
						Fishing.attemptdata(c, 4);
						break;

					case 312:
					case 321:
					case 324: //CAGE + HARPOON
						Fishing.attemptdata(c, 8);
						break;
				}
				break;
			case 2:
				switch (l) {
					case 326:
					case 327:
					case 330:
					case 332:
					case 316: //BAIT + NET
						Fishing.attemptdata(c, 2);
						break;
					case 319:
					case 323:
					case 325: //BAIT + NET
						Fishing.attemptdata(c, 9);
						break;
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 318:
					case 328:
					case 329:
					case 331:
					case 309: //BAIT + LURE
						Fishing.attemptdata(c, 6);
						break;
					case 312:
					case 321:
					case 324://SWORDIES+TUNA-CAGE+HARPOON
						Fishing.attemptdata(c, 7);
						break;
					case 313:
					case 322:
					case 334:	//NET+HARPOON
						Fishing.attemptdata(c, 10);
						break;
				}
				break;
		}
	}

	public static boolean fishingNPC(Client c, int npc) {
		for(int i = 308; i < 335; i++) {
			if(npc == i) {
				return true;
			}
		}
		return false;
	}
}