package server.model.players.packets;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.items.Item;
import server.model.players.Client;
import server.model.players.PacketType;


/**
 * Magic on floor items
 **/
public class MagicOnFloorItems implements PacketType {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		final int itemY = c.getInStream().readSignedWordBigEndian();
		int itemId = c.getInStream().readUnsignedWord();
		final int itemX = c.getInStream().readSignedWordBigEndian();
		int spellId = c.getInStream().readUnsignedWordA();

		if(!Server.itemHandler.itemExists(itemId, itemX, itemY)) {
			c.stopMovement();
			return;
		}
		c.usingMagic = true;
		if(!c.getCombat().checkMagicReqs(51)) {
			c.stopMovement();
			return;
		}
				/*
		 * Telegrab spell
		 */
		if ((((c.getItems().freeSlots() >= 1) || c.getItems().playerHasItem(itemId, 1)) && Item.itemStackable[itemId]) || ((c.getItems().freeSlots() > 0) && !Item.itemStackable[itemId])) {
			if(c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
				c.walkingToItem = true;
				int offY = (c.getX() - itemX) * -1;
				int offX = (c.getY() - itemY) * -1;
				c.teleGrabX = itemX;
				c.teleGrabY = itemY;
				c.teleGrabItem = itemId;
				c.turnPlayerTo(itemX, itemY);
				c.teleGrabDelay = System.currentTimeMillis();
				c.startAnimation(c.MAGIC_SPELLS[51][2]);
				c.gfx100(c.MAGIC_SPELLS[51][3]);
				c.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72);
				c.getPA().createPlayersProjectile(c.getX(), c.getY(), offX, offY, 50, 70, c.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
				c.getPA().addSkillXP(c.MAGIC_SPELLS[51][7], 6);
				c.getPA().refreshSkill(6);
				c.stopMovement();
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(!c.walkingToItem)
							container.stop();
						if(System.currentTimeMillis() - c.teleGrabDelay > 1550 && c.usingMagic) {
							if(Server.itemHandler.itemExists(c.teleGrabItem, c.teleGrabX, c.teleGrabY) && c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
								Server.itemHandler.removeGroundItem(c, c.teleGrabItem, c.teleGrabX, c.teleGrabY, true);
								c.usingMagic = false;
								container.stop();
							}
						}
					}
					@Override
					public void stop() {
						c.walkingToItem = false;
					}
				}, 1);
			}
		} else {
			c.sendMessage("You don't have enough space in your inventory.");
			c.stopMovement();
		}
		if(c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
			int offY = (c.getX() - itemX) * -1;
			int offX = (c.getY() - itemY) * -1;
			c.teleGrabX = itemX;
			c.teleGrabY = itemY;
			c.teleGrabItem = itemId;
			c.turnPlayerTo(itemX, itemY);
			c.teleGrabDelay = System.currentTimeMillis();
			c.startAnimation(c.MAGIC_SPELLS[51][2]);
			c.gfx100(c.MAGIC_SPELLS[51][3]);
			c.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72);
			c.getPA().createPlayersProjectile(c.getX(), c.getY(), offX, offY, 50, 70, c.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
			c.getPA().addSkillXP(c.MAGIC_SPELLS[51][7], 6);
			c.getPA().refreshSkill(6);
			c.stopMovement();
		}
	}

}
