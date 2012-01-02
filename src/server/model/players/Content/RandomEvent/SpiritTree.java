package server.model.players.content.randomevent;

import server.model.players.*;
import server.util.Misc;
import server.*;

public class SpiritTree  {

	private static int[][] spiritTree = {
		{3, 	10, 	438, 	19, 	1},
		{11, 	20, 	439, 	40, 	1},
		{21, 	40, 	440, 	80, 	3},
		{61, 	90, 	441, 	105, 	4},
		{91, 	110, 	442, 	120, 	5},
		{111, 	138, 	443, 	150, 	7},
	};

	public static void spawnSpiritTree(Client c) {
		for(int i = 0; i < spiritTree.length; i++) {
			if(c.combatLevel >= spiritTree[i][0] && c.combatLevel <= spiritTree[i][1]) {
				Server.npcHandler.spawnNpc(c, spiritTree[i][2], c.absX + Misc.random(1), c.absY+ Misc.random(1), c.heightLevel, 0, spiritTree[i][3], spiritTree[i][4], spiritTree[i][4] * 10, spiritTree[i][4] * 10, true, false);
			}
		}
	}
}