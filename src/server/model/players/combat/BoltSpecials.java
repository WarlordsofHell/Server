package server.model.players.combat;

import server.model.players.*;

public class BoltSpecials {
	
	public static double boltMultiplier(Client c, int boltId) {
		handleBoltSpecial(c, boltId);
		switch (boltId) {
			case 9244:
			return 1.60;
		}
		return 1;
	}
	
	public static void handleBoltSpecial(Client c, int boltId) {
		switch (boltId) {
			case 9244:
				c.gfx(726, 100);
			break;
			default:
			break;
		}
	}
}