package server.model.players.content.randomevent;

import server.model.players.skills.*;
import server.model.players.*;
import server.util.Misc;

public class EventHandler {

	public final static int SPAWN_EVENTS = 0;
	public final static int TELE_EVENTS = 0;

	public static void getRandomEvent(Client c) {
		if(c.playerIsWoodcutting) {
			SpiritTree.spawnSpiritTree(c);
		} else {
			if(Misc.random(1) == 0) {
				executeSpawnEvent(c);
			} else {
				executeTeleEvent(c);
			}
		}
	}

	private static void executeSpawnEvent(Client c) {
		int event = Misc.random(SPAWN_EVENTS);
		switch (event) {
			case 0: // EVIL CHICKEN
				EvilChicken.spawnChicken(c);
				break;
		}
	}

	private static void executeTeleEvent(Client c) {

		int event = Misc.random(TELE_EVENTS);
		c.lastX = c.absX;
		c.lastY = c.absY;
		SkillHandler.resetPlayerSkillVariables(c);
		c.gfx0(580);
		c.cantTeleport = true;

		switch (event) {
			case 0:	// FREAKY FORESTER
				FreakyForester.teleportToLocation(c);
				break;
			case 1: // DRILL DEMON
				DrillDemon.enterDrillEvent(c);
				break;
		}
	}

	private static int[][] failCoords = {
		{3333, 3333}, {3196, 3193}, {3084, 3549},
		{2974, 3346}, {2781, 3506}, {2810, 3508},
	};

	public static void failEvent(final Client c) {
		int loc = Misc.random(failCoords.length-1);
		c.teleportToX = failCoords[loc][0];
		c.teleportToY = failCoords[loc][1];
		c.sendMessage("You wake up in a strange location...");
	}

	public static void changeToSidebar(Client c, int[] bar) {
		for(int i = 0; i < 14; i++) {
			c.setSidebarInterface(i, -1);
		}
		c.setSidebarInterface(bar[0], bar[1]);
		c.getPA().changeToSidebar(bar[0]);
	}

	public static void changeBackSidebars(Client c) {
		//c.getPA().setSidebarInterfaces(c);
	}
}