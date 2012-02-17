package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;
/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int Xamount = c.getInStream().readDWord();
		if (Xamount == 0)
			Xamount = 1;
					if(c.getGamble().betting) {
			c.getGamble().playerBet = Xamount;
			c.getGamble().blackJack(c);
		}
		switch(c.xInterfaceId) {
                    			case 7423:
			if(c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			if(!c.getItems().playerHasItem(c.xRemoveId, Xamount)) {
				Xamount = c.getItems().getItemAmount(c.xRemoveId);
			}
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot, Xamount);
			c.getItems().resetItems(7423);
			break;
			case 5064:
			if(c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot] , c.xRemoveSlot, Xamount);
			break;
			case 5382:
			if(c.inTrade) {
				c.getTradeAndDuel().declineTrade(true);
			}
			c.getItems().fromBank(c.bankItems[c.xRemoveSlot] , c.xRemoveSlot, Xamount);
			break;
				
			case 3322:
			if(c.duelStatus <= 0) {
            	c.getTradeAndDuel().tradeItem(c.xRemoveId, c.xRemoveSlot, Xamount);
            } else {				
				c.getTradeAndDuel().stakeItem(c.xRemoveId, c.xRemoveSlot, Xamount);
			}  
			break;
                           /*case 3322:
			if(!c.getItems().playerHasItem(c.xRemoveId, Xamount)) {
				Xamount = c.getItems().getItemAmount(c.xRemoveId);
			}
            		c.getTradeAndDuel().tradeItem(c.xRemoveId, c.xRemoveSlot, Xamount);
			break;*/
				
case 3415: 
			if(c.duelStatus <= 0) { 
            	c.getTradeAndDuel().fromTrade(c.xRemoveId, c.xRemoveSlot, Xamount);
			} 
			break;
				
			case 6669:
			c.getTradeAndDuel().fromDuel(c.xRemoveId, c.xRemoveSlot, Xamount);
			break;			
		}
	}
}