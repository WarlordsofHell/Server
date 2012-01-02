package server.model.players.packets;

import server.Config;
import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 * Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int itemId1 = c.getInStream().readSignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		

		switch (itemId) {
		case 995:
			int cashAmount = c.getItems().getItemAmount(995);
			if (c.inWild()) {
				c.sendMessage("You cannot do this in the wilderness");
				c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
				return;
			}
			if(c.MoneyCash == 2147483647) {
				c.sendMessage("Your pouch is full!");
				return;
			}
			if ((c.MoneyCash + cashAmount) <= Config.MAXITEM_AMOUNT && (c.MoneyCash + cashAmount) > -1) {
				if(cashAmount == 1) {
					c.sendMessage("You add 1 coin to your pouch.");
				} else  {
					c.sendMessage("You add "+cashAmount+" coins to your pouch.");
				}
				c.MoneyCash += cashAmount;
				c.getItems().deleteItem(995, cashAmount);
				if(c.MoneyCash > 99999 && c.MoneyCash <= 999999) {
					c.getPA().sendFrame126(""+c.MoneyCash/1000+"K", 8134);
					} else if(c.MoneyCash > 999999 && c.MoneyCash <= 2147483647) {
						c.getPA().sendFrame126(""+c.MoneyCash/1000000+"M", 8134);
					} else {
							c.getPA().sendFrame126(""+c.MoneyCash+"", 8134); 
						}
					c.getPA().sendFrame126(""+c.MoneyCash+"", 8135); 
					return;
			}
			int Joker = c.MoneyCash-2147483647-cashAmount;
			int DisIs = c.MoneyCash+cashAmount-2147483647;
			int cash = c.MoneyCash;
			if((c.MoneyCash + cashAmount) < 2147483647) {
				cash += cashAmount;
				c.getItems().deleteItem(995, cashAmount);
				c.getItems().addItem(995, c.MoneyCash+cashAmount-2147483647);
				cash = c.MoneyCash;
				if(DisIs == 1) {
					c.sendMessage("You add 1 coin to your pouch.");
				} else  {
					c.sendMessage("You add "+Joker+" coins to your pouch.");
				}
			c.MoneyCash = 2147483647;
			if(c.MoneyCash > 99999 && c.MoneyCash <= 999999) {
				c.getPA().sendFrame126(""+c.MoneyCash/1000+"K", 8134); 
				} else if(c.MoneyCash > 999999 && c.MoneyCash <= 2147483647) {
					c.getPA().sendFrame126(""+c.MoneyCash/1000000+"M", 8134); 
				} else {
						c.getPA().sendFrame126(""+c.MoneyCash+"", 8134);
					}
				c.getPA().sendFrame126(""+c.MoneyCash+"", 8135);
			return;
			}
			break;
		case 1712:
			c.getPA().handleGlory(itemId);
			break;
			
		default:
			if (c.playerRights == 3)
				Misc.println(c.playerName+ " - Item3rdOption: "+itemId+" : "+itemId11+" : "+itemId1);
			break;
		}

	}

}
