package server.model.players.content.RandomEvent;

import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.util.Misc;

public class RandomEventHandler {
	private Client c;
	public RandomEventHandler(Client Client) {
		this.c = Client;
	}

	public final static int SPAWN_EVENTS = 0;
	public final static int TELE_EVENTS = 0;

	public static void startRandomEvent(final Client c) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (Misc.random(99) == 1) {
					c.getPA().removeAllWindows();
					c.getPA().resetAnimation();
							for (int i = 0; i < 20; i++) {
							c.getPA().refreshSkill(i);			
					getRandomEvent(c);
				}
			}
			}
			@Override
			public void stop() {
			}
		}, 1000);
	}

	public static void getRandomEvent(Client c) {
		{
			int i = 0;
			if (i == 1) {
				RandomEventHandler.executeSpawnEvent(c);
			} else {
				RandomEventHandler.executeTeleEvent(c);
			}
		}
	}

	private static void executeSpawnEvent(Client c) {
		int event = Misc.random(RandomEventHandler.SPAWN_EVENTS);
				for (int i = 0; i < 20; i++) {
			c.getPA().refreshSkill(i);
			
		switch (event) {
			case 0 :
				//spawnEvilChicken(c);
			break;
		}
	}
	}
public static enum TreeSpirit {
		SPIRIT_TREE_BEGINLOW(3, 10, 1265, 100, 1, 20, 1),
		SPIRIT_TREE_ENDLOW(11, 20, 1265, 100, 1, 20, 1),
		SPIRIT_TREE_BEGINMEDIUM(21, 40, 1265, 100, 1, 20, 1),
		SPIRIT_TREE_LOWMEDIUM(41, 60, 1265, 100, 1, 20, 1),
		SPIRIT_TREE_BEGINHIGH(61, 90, 1265, 100, 1, 20, 1),
		SPIRIT_TREE_ENDHIGH(91, 138, 1265, 100, 1, 20, 1);
	
	public int beginCombat, endCombat, npcId, getHp, getMax, getAtk, getDef;
	
	private TreeSpirit(int beginCombat, int endCombat, int npcId, int getHp, int getMax, int getAtk, int getDef) {
			this.beginCombat = beginCombat;
			this.endCombat = endCombat;
			this.npcId = npcId;
			this.getHp = getHp;
			this.getMax = getMax;
			this.getAtk = getAtk;
			this.getDef = getDef;

		}
	}

		public static void TreeSpirit(Client c) {
		for (TreeSpirit ts : TreeSpirit.values()) {
				if (c.combatLevel >= ts.beginCombat && c.combatLevel <= ts.endCombat) {
									int hp = ts.getHp;
                    int max = ts.getMax;
					int atk = ts.getAtk;
                    int def = ts.getDef;
					c.gfx0(179);
					Server.npcHandler.spawnNpc(c, ts.npcId, c.absX, c.absY, c.heightLevel, 0, hp, max, atk, def, true, true);
					//Server.npcHandler.spawnNpc(c, c.barrowsNpcs[c.randomCoffin][0], 3551, 9694-1, 0, 0, 120, 30, 200, 200, true, true);

					c.sendMessage("RANDOM EVENT - SPAWN - TREE SPIRIT");
				break;
			}
		}
	}

					

	private static void executeTeleEvent(Client c) {
		int event = Misc.random(RandomEventHandler.TELE_EVENTS);
		int lastX = c.absX;
		int lastY = c.absY;
		int lastH = c.heightLevel;
		for (int i = 0; i < 20; i++) {
		//c.playerLevel[i] = getLevelForXP(c.playerXP[i]);
		c.getPA().refreshSkill(i);
		c.gfx0(580);
		switch (event) {
			case 0 :
				//FreakyForester.teleportToLocation(c);
			break;
		}
	}
	}

	private static int[][] failCoords = {{3333, 3333}, {3196, 3193}, {3084, 3549}, {2974, 3346}, {2781, 3506}, {2810, 3508},};

	public static void failEvent(final Client c) {
		int loc = Misc.random2(failCoords.length);
		c.getPA().movePlayer(failCoords[loc][0], failCoords[loc][1], 0);
		c.sendMessage("You wake up in a strange location...");
	}

	public static void changeToSidebar(Client c, int[] bar) {
		for (int i = 0; i < 14; i++) {
			c.setSidebarInterface(i, bar[i]);
		}
		//c.getPA().changeToSidebar(3);
		//c.getItems().sendWeapon(c.playerEquipment[Config.WEAPON]);
	}

}