package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;
import server.util.*;


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
                
                                                if(c.wearId == 7927) {
		c.resetWalkingQueue();
for (int i = 0; i < 14; i++) {
	c.setSidebarInterface(i, 6014);
}
c.isMorphed = true;
c.sendMessage("As you put on the ring you turn into an egg!");
c.npcId2 = 3689 + Misc.random(5);
c.isNpc = true;
c.updateRequired = true;
c.appearanceUpdateRequired = true;
}
	
		c.getItems().wearItem(c.wearId, c.wearSlot);
		if (torvaChanged && c.playerLevel[3] > c.calculateMaxLifePoints()) {
			c.playerLevel[3] = c.calculateMaxLifePoints();
			c.getPA().refreshSkill(3);
		}
                
	}

}
