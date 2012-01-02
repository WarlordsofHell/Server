package server.model.players.content.randomevent;

import server.Server;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.util.Misc;

public class SandwichLady extends EventHandler {

	private static String[] refreshments = {
		"pie", "kebab", "chocolate", "baguette",
		"triangle sandwich", "square sandwich", "piece of bread",
	};

	private static void clearupEvent(Client c) {
		c.getPA().removeAllWindows();
		c.getRefreshment = -1;
		NPCHandler.npcs[c.npcSlot].absX = 0;
		NPCHandler.npcs[c.npcSlot].absY = 0;
		NPCHandler.npcs[c.npcSlot].HP = 0;
	}

	public static void spawnSandwichLady(final Client c) {
		Server.npcHandler.spawnNpc(c, 3117, c.absX + 1, c.absY+ Misc.random(1), c.heightLevel, 0, 0, 0, 0, 0, false, false);
	}

	public static String getRefreshment(Client c) {
		if(c.getRefreshment < 0) {
			c.getRefreshment = Misc.random(refreshments.length-1);
		}
		return refreshments[c.getRefreshment];
	}

	public static void showInterface(Client c) {
		c.sendMessage("The sandwich lady told you that you can have a "+refreshments[c.getRefreshment]+".");
		c.getPA().showInterface(16135);
	}

	public static void pickingRefreshment(Client c, int button) {
		int[][] data = {
			{63013, 2323}, {63014, 4608}, {63015, 1973}, {63009, 6961},
			{63010, 6962}, {63011, 6965}, {63012, 2309},
		};
		for(int i = 0; i < data.length; i++) {
			if(button == data[i][0]) {
				if(c.getRefreshment == i) {
					c.getItems().addItem(data[i][1], 1);
				} else {
					c.sendMessage("You've picked the wrong one!");
					failEvent(c);
				}
				clearupEvent(c);
			}
		}
	}
}