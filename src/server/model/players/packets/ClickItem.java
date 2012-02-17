package server.model.players.packets;

import server.Config;
import server.Connection;
import server.Server;
import server.model.players.Client;
import server.model.players.PacketType;
import server.model.players.PlayerHandler;
import server.model.players.Player;
import server.util.Misc;
import server.world.WorldMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import server.event.EventManager;
import server.event.Event;
import server.event.EventContainer;
import server.model.players.*;
import server.model.players.PacketType;


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
		
		   if (itemId == 6199) {
                int mysteryReward = Misc.random(5); // Coded by Sonic chao
                if (mysteryReward == 1) {
		c.getItems().addItem(995, 500000);
		c.getItems().addItem(15273, 500);
                c.getItems().deleteItem(6199, 1);
                c.sendMessage("Thanks for Donating!");
		}
		else if (mysteryReward == 2) {
		c.getItems().addItem(4083, 1);
                c.getItems().deleteItem(6199, 1);
                c.sendMessage("Thanks for Donating!");
		}
		else if (mysteryReward == 3) {
		c.getItems().addItem(6585, 1);
                c.getItems().deleteItem(6199, 1);
                c.sendMessage("Thanks for Donating!");
		}
		else if (mysteryReward == 4) {
		c.getItems().addItem(1038, 1);
                c.getItems().deleteItem(6199, 1);
                c.sendMessage("Thanks for Donating!");
		}
		else if (mysteryReward == 5) {
		c.getItems().addItem(1055, 1);
                c.getItems().deleteItem(6199, 1);
                c.sendMessage("Thanks for Donating!");
		}
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
		if (c.getHerblore().isUnidHerb(itemId))
			c.getHerblore().handleHerbClick(itemId);
		if (c.getFood().isFood(itemId))
			c.getFood().eat(itemId,itemSlot);
		//ScriptManager.callFunc("itemClick_"+itemId, c, itemId, itemSlot);
		if (c.getPotions().isPotion(itemId))
			c.getPotions().handlePotion(itemId,itemSlot);
		if (c.getPrayer().isBone(itemId))
			c.getPrayer().buryBone(itemId, itemSlot);

 	if (itemId > 15085 && itemId < 15102){
			c.getDicing().useDice(itemId, false);
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
			}
		}
	}

}
