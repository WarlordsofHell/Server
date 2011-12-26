package server.model.players.content.teleporting;

import server.model.players.*;
import server.model.players.Client;
import server.*;
import server.util.*;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;

public class Teleporting {

		public  Client c;
	public Teleporting(Client Client) {
		this.c = Client;
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
	
	public int teleportX, teleportY, teleportH;
	
	TeleportLocation(int teleportX, int teleportY, int teleportH) {
		this.teleportX = teleportX;
		this.teleportY = teleportY;
		this.teleportHeight = teleportH;
	}
}
	/**
	 * Handles the Teleport Tablets
	 */
	public static enum TeleportTablets {
		Varrock_Teleport(6873, 1, 1, 1, 1),
		Ardougne_Teleport(6873, 1, 1, 1, 1),
		House_Teleport(6873, 1, 1, 1, 1),
		Camelot_Teleport(6873, 1, 1, 1, 1),
		Lumbridge_Teleprt(6873, 1, 1, 1, 1);
		
	public int itemId, teleportX, teleportY, teleportH, magicXP;
		public int teleportX() {
			return c.teleX;
		}
		public int teleportY() {
			return c.teleY;
		}
		public int teleportH() {
			return c.getPA().newHeight;
		}
	
	TeleportTablets(int itemId, int teleportX, int teleportY, int teleportH, int magicXP) {
		this.itemId = itemId;
		this.teleportX = teleportX;
		this.teleportY = teleportY;
		this.teleportHeight = teleportH;
		this.magicXP = magicXP;
	}
}


	/**
	 * Handles the Buttons for Teleports
	 */
	public void handleButtonClick(int buttonId) {
			switch (buttonId) {
				case 58156:
					//startTeleport(c, TeleportLocation.toString(VARROCK), TeleportLocation.teleportX, TeleportLocation.teleportY, TeleportLocation.teleportH, "modern");
				break;
			}
		}
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
		if(c.getPA().teleporting || c.getPA().teleportDelay != -1){
			return false;
		}
		if(c.getPA().teleporting) {
			return false;
		}
		if (c.playerIndex > 0 || c.npcIndex > 0) {
			c.getCombat().resetPlayerAttack();
		}
		c.getPA().teleporting = true;
		c.getPA().removeAllWindows();
		c.npcIndex = 0;
		c.playerIndex = 0;
		c.faceUpdate(0);
		return true;
	}

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
		if(c.getPA().ancientstele){
			c.getPA().teleportDelay = 4;
			c.startAnimation(1979);
			c.gfx0(392);
		} else if(!c.getPA().ancientstele) {
			c.getPA().teleportDelay = 3;
			c.startAnimation(714);
			c.gfx100(111);
		}
	}


	public static void startTeleport(final Client c, int teleX,int teleY,int height,String type){
		if(System.currentTimeMillis() - c.lastAction > 4000){
			if(!proceedTeleporting(c)) {
				return;
			}
			c.lastAction = System.currentTimeMillis();
			c.teleToX = teleX + Misc.random(1);
			c.teleToY = teleY - Misc.random(1);
			c.getPA().newHeight = height;
			if(type == "ancient") {
				c.getPA().ancientstele = true;
			} else {
				c.getPA().ancientstele = false;
			}
			teleport(c);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.getPA().teleportToX = c.teleToX;
					c.getPA().teleportToY = c.teleToY;
					c.heightLevel = c.getPA().newHeight;
					c.getPA().teleporting = false;
					c.getPA().teleportDelay = -1;
					stopPlayerEmote(c);
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, c.getPA().teleportDelay);
		}
	}
}