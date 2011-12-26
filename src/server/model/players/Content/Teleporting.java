package server.model.players.content.teleporting;

import server.model.players.*;
import server.*;
import server.util.*;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;

public class Teleporting {

	public static boolean proceedTeleporting(Client c) {
		if(System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		if(c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level "+Config.NO_TELEPORT_WILD_LEVEL+" in the wilderness.");
			return false;
		}
		if(c.isDead || c.respawnTimer != -6) {
			return false;
		}
		if(c.teleporting || c.teleportDelay != -1){
			return false;
		}
		if(c.teleporting) {
			return false;
		}
		if (c.playerIndex > 0 || c.npcIndex > 0) {
			c.getCombat().resetPlayerAttack();
		}
		c.teleporting = true;
		c.getPA().removeAllWindows();
		c.npcIndex = 0;
		c.playerIndex = 0;
		c.faceUpdate(0);
		return true;
	}
	/**
	 * Handles the Teleport Location
	 */
	public static enum TeleportLocation {
		VARROCK(3216,3424,0),
		LUMBRIDGE(3221,3217,0),
		FALADOR(2964,3380,0),
		CAMELOT(2756,3479,0),
		ARDOUGNE(2661,3306,0),
		WATCHTOWER(2549,3113,0),
		TROLLHELM(3285,3345,0),
		APE_ATOLL(2796,2799,1),
		PADDWWA(3098,9884,0),
		SENNTISTEN(3322,3337,0),
		KHARYRLL(3491,3471,0),
		LASSAR(3008,3477,0),
		DAREEJAK(2963,3696,0),
		CARRALLANGAR(3156,3666,0),
		ANNAKARL(3288,3886,0),
		GHORROCK(2976,3874,0);
	}
	private TeleportLocation(int teleportX, int teleportY, int teleportH) {
		this.teleportX = teleportX;
		this.teleportY = teleportY;
		this.teleportHeight = teleportH;
	}

	public int teleportX;
	public int teleportY;
	public int teleportH;

	}
	/**
	 * Handles the Teleport Tablets
	 */
	public static enum TeleportTablets {
		Varrock_Teleport(6873, 12093, 12435, 30, );
		Ardougne_Teleport(6873, 12093, 12435, 30);
		House_Teleport(6873, 12093, 12435, 30);
		Camelot_Teleport(6873, 12093, 12435, 30);
		Lumbridge_Teleprt(6873, 12093, 12435, 30);
	}

		private TeleportTablet(int itemId, int magicXP, int levelRequired, TeleportLocation teleportLocation) {
			this.itemId = itemId;
			this.magicXP = magicXP;
			this.levelRequired = levelRequired;
			this.teleportLocation = teleportLocation;
		}

		public int itemId;
		public int magicXP;
		public int levelRequired;
		public TeleportLocation teleportLocation;

		public void handleButtonClick(int buttonId) {
			switch (buttonId) {
				case 58156:
					c.getPA().startTeleport(2897, 3618, 4, "modern");
					break;
				default:
					return;
			}
		}
	//}
	public static void stopPlayerEmote(Client c){
		c.playerSEW = 819;
		c.playerSER = 824;
		c.getPA().requestUpdates();
		c.outStream.createFrame(1);
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(Server.playerHandler.players[i] != null) {
				Client person = (Client)Server.playerHandler.players[i];
				if(person != null) {
					Client cc = (Client)person;
					person.outStream.createFrame(1);
				}
			}
		}
	}

	public static void teleport(Client c) {
		if(c.ancientstele){
			c.teleportDelay = 4;
			c.startAnimation(1979);
			c.gfx0(392);
		} else if(!c.ancientstele) {
			c.teleportDelay = 3;
			c.startAnimation(714);
			c.gfx100(111);
		}
	}
}