package server.model.players.content.teleporting;

import server.model.players.*;
import server.*;
import server.util.*;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;

public class Teleporting extends Requirements {

	public static void startTeleport(final Client c, int teleX,int teleY,int height,String type){
		if(System.currentTimeMillis() - c.lastAction > 4000){
			if(!proceedTeleporting(c)) {
				return;
			}
			c.lastAction = System.currentTimeMillis();
			c.getPA().teleToX = teleX + Misc.random(1);
			c.getPA().teleToY = teleY - Misc.random(1);
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
					c.teleportToX = c.getPA().teleToX;
					c.teleportToY = c.getPA().teleToY;
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