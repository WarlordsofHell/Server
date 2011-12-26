package server.model.players.content.teleporting;

import server.model.players.*;
import server.*;
import server.util.*;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;

public class Requirements {

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
}