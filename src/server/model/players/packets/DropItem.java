package server.model.players.packets;

import server.Config;
import server.Server;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.PlayerSave;

/**
 * Drop Item
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();
		if(c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}
		if(c.isDead) {
			c.sendMessage("You can't drop items when you are dead.");
			return;
		} 
		/*if(c.newPlayer()) {
			c.getTimeLeftForNP();
			return;
		} */
		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		boolean destroyable = false;
		for (int i : Config.DESTROYABLES) {
			if (i == itemId) {
				destroyable = true;
				break;
			}
		}
		if(c.playerItemsN[slot] != 0 && itemId != -1 && c.playerItems[slot] == itemId + 1) {
			if (destroyable) {
				c.getPA().destroyInterface(itemId);
				return;
			}
			if(droppable) {
				if (c.underAttackBy > 0) {
					if (c.getShops().getItemShopValue(itemId) > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.playerItemsN[slot], c.getId());
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				PlayerSave.saveGame(c);
			} else
				c.sendMessage("This item cannot be dropped.");
		}
		c.getPA().closeAllWindows();
	}
}
