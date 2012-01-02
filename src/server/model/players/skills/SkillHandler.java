package server.model.players.skills;

import server.model.players.Client;
import server.Config;
import server.util.Misc;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.model.players.content.randomevent.*;

public class SkillHandler {

	public static final int AGILITY_XP = Config.AGILITY_EXPERIENCE;
	public static final int PRAYER_XP = Config.PRAYER_EXPERIENCE;
	public static final int MINING_XP = Config.MINING_EXPERIENCE;
	public static final int COOKING_XP = Config.COOKING_EXPERIENCE;
	public static final int RUNECRAFTING_XP = Config.RUNECRAFTING_EXPERIENCE;
	public static final int WOODCUTTING_XP = Config.WOODCUTTING_EXPERIENCE;
	public static final int THIEVING_XP = Config.THIEVING_EXPERIENCE;
	public static final int HERBLORE_XP = Config.HERBLORE_EXPERIENCE;
	public static final int FISHING_XP = Config.FISHING_EXPERIENCE;
	public static final int FLETCHING_XP = Config.FLETCHING_EXPERIENCE;
	public static final int FIREMAKING_XP = Config.FIREMAKING_EXPERIENCE;
	public static final int SMITHING_XP = Config.SMITHING_EXPERIENCE;

	public static final boolean view190 = true;

	private static boolean startRandomEvent;

	public static String getLine(Client c) {
		return c.below459 ? ("\\n\\n\\n\\n") : ("\\n\\n\\n\\n\\n");
	}

	public static boolean noInventorySpace(Client c, String skill) {
		if (c.getItems().freeSlots() == 0) {
			c.sendMessage("You haven't got enough inventory space to continue "+skill+"!");
			c.getDH().sendStatement("You haven't got enough inventory space to continue "+skill+"!");
			return false;
		}
		return true;
	}

	public static void resetPlayerSkillVariables(Client c) {
		if(c.playerIsFishing) {
			for(int i = 0; i < 11; i++) {
				c.fishingProp[i] = -1;
			}
		} else if(c.playerIsCooking) {
			for(int i2 = 0; i2 < 6; i2++) {
				c.cookingProp[i2] = -1;
			}
		/*} else if(c.playerIsWoodcutting) {
			Woodcutting.resetWoodcutting(c);*/
		} else if(c.playerIsMining) {
			for(int i = 0; i < 2; i++) {
				c.miningProp[i] = -1;
			}
		} else if(c.playerIsFletching) {
			for(int i = 0; i < 9; i++) {
				c.fletchingProp[i] = -1;
			}
		}
	}

	public static boolean hasRequiredLevel(final Client c, int id, int lvlReq, String skill, String event) {
		if(c.playerLevel[id] < lvlReq) {
			c.sendMessage("You dont't have a high enough "+skill+" level to "+event+"");
			c.sendMessage("You at least need the "+skill+" level of "+ lvlReq +".");
			c.getDH().sendStatement("You haven't got high enough "+skill+" level to "+event+"!");
			return false;
		}
		if(!startRandomEvent) {
			startRandomEvent = true;
   			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					EventHandler.getRandomEvent(c);
					startRandomEvent = false;
					container.stop();
				}
				@Override
				public void stop() {
	
				}
			}, 1 + Misc.random(30000));
		}
		return true;
	}

	public static void deleteTime(Client c) {
		c.doAmount--;
	}
}