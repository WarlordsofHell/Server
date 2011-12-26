package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;
import server.Config;
import java.io.*;
import server.util.*;
import server.model.npcs.*;
import server.*;
import server.model.items.*;


/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.wearId = c.getInStream().readUnsignedWord();
		c.wearSlot = c.getInStream().readUnsignedWordA();
		boolean torvaChanged = false;
		c.interfaceId = c.getInStream().readUnsignedWordA();
		
		int oldCombatTimer = c.attackTimer;
		if (c.playerIndex > 0 || c.npcIndex > 0)
			c.getCombat().resetPlayerAttack();
		if (c.wearId >= 5509 && c.wearId <= 5515) {
			int pouch = -1;
			int a = c.wearId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().emptyPouch(pouch);
			return;
		}
			//c.attackTimer = oldCombatTimer;
		if (c.wearSlot == 0 || c.wearSlot == 4 || c.wearSlot == 7) {
			if (c.playerEquipment[c.wearSlot] == 13362 || c.playerEquipment[c.wearSlot] == 13358 || c.playerEquipment[c.wearSlot] == 13360) //Torva
			if (c.playerEquipment[c.wearSlot] == 13355 || c.playerEquipment[c.wearSlot] == 13354 || c.playerEquipment[c.wearSlot] == 13352) //Pernix
			if (c.playerEquipment[c.wearSlot] == 13350 || c.playerEquipment[c.wearSlot] == 13348 || c.playerEquipment[c.wearSlot] == 13346) //Virtus
				torvaChanged = true;
		}
		c.getItems().wearItem(c.wearId, c.wearSlot);
		if (torvaChanged && c.playerLevel[3] > c.calculateMaxLifePoints()) {
			c.playerLevel[3] = c.calculateMaxLifePoints();
			c.getPA().refreshSkill(3);
		}
	}

}
