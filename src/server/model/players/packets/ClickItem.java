package server.model.players.packets;

import server.model.players.Client;
import server.Server;
import server.model.players.content.teleporting.*;
import server.model.players.PacketType;
import server.model.players.skills.Herblore;


/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int junk = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordBigEndian();
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			int a = itemId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().fillPouch(pouch);
			return;
		}
			c.getWoodcutting().HandleItemClick(itemId);	
                        
		if(Herblore.isHerb(c, itemId))
			Herblore.cleanTheHerb(c, itemId);
		if (c.getFood().isFood(itemId))
			c.getFood().eat(itemId,itemSlot);
		//ScriptManager.callFunc("itemClick_"+itemId, c, itemId, itemSlot);
		if (c.getPotions().isPotion(itemId))
			c.getPotions().handlePotion(itemId,itemSlot);
		if (c.getPrayer().isBone(itemId)) {
			c.getPrayer().handleBones(itemId, false);
		}
			if (itemId == 8007) {
				TeleportingTab.teleportTab(c,3216,3424,0,itemId,itemSlot);
			} else if (itemId == 8008) {
				TeleportingTab.teleportTab(c,3221,3217,0,itemId,itemSlot); 
			} else if (itemId == 8009) {
				TeleportingTab.teleportTab(c,2964,3380,0,itemId,itemSlot); 
			} else if (itemId == 8010) {
				TeleportingTab.teleportTab(c,2756,3479,0,itemId,itemSlot); 
			} else if (itemId == 8011) {
				TeleportingTab.teleportTab(c,2661,3306,0,itemId,itemSlot);
			} else if (itemId == 8012) {
				TeleportingTab.teleportTab(c,2549,3113,0,itemId,itemSlot); 
			}
				if (itemId > 15085 && itemId < 15102){
			c.useDice(itemId, false);
		}
		if (itemId == 15084)
		{//dice bag
			c.diceID = itemId;
			c.getDH().sendDialogues(106, 0);
		}
		if (itemId == 952) {
			if(c.inArea(3553, 3301, 3561, 3294)) {
				c.teleTimer = 3;
				c.newLocation = 1;
			} else if(c.inArea(3550, 3287, 3557, 3278)) {
				c.teleTimer = 3;
				c.newLocation = 2;
			} else if(c.inArea(3561, 3292, 3568, 3285)) {
				c.teleTimer = 3;
				c.newLocation = 3;
			} else if(c.inArea(3570, 3302, 3579, 3293)) {
				c.teleTimer = 3;
				c.newLocation = 4;
			} else if(c.inArea(3571, 3285, 3582, 3278)) {
				c.teleTimer = 3;
				c.newLocation = 5;
			} else if(c.inArea(3562, 3279, 3569, 3273)) {
				c.teleTimer = 3;
				c.newLocation = 6;
			} else {
				Server.digHandler.startDigging(c);
			}
			
		}
	}

}
