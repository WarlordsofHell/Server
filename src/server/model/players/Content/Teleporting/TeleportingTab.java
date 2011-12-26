package server.model.players.content.teleporting;

import server.model.players.*;
import server.util.*;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;

public class TeleportingTab extends Requirements {

	public static void teleportTab(final Client c, int teleX, int teleY, int height, int itemId, int slot) {
		if(System.currentTimeMillis() - c.lastAction > 4000){
			if(!proceedTeleporting(c)) {
				return;
			}
			c.lastAction = System.currentTimeMillis();
			c.getPA().teleToX = teleX + Misc.random(1);
			c.getPA().teleToY = teleY - Misc.random(1);
			c.getPA().newHeight = height;
			c.startAnimation(4069);
			c.getItems().deleteItem(itemId, slot, 1);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.startAnimation(4071);
					c.gfx0(678);
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, 2);

			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.teleportToX = c.getPA().teleToX;
					c.teleportToY = c.getPA().teleToY;
					c.heightLevel = c.getPA().newHeight;
					c.getPA().teleporting = false;
					stopPlayerEmote(c);
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, 4);
		}
	}
}