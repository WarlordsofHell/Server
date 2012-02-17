package server.model.players.packets;

import server.model.players.Client;
import server.model.players.PacketType;
import server.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 * Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		
		if (!c.getItems().playerHasItem(itemId,1))
			return;

		switch (itemId) {
			case 11694:
				c.getItems().deleteItem(itemId,1);
				c.getItems().addItem(11690,1);
				c.getItems().addItem(11702,1);
				c.sendMessage("You dismantle the godsword blade from the hilt.");
			break;
			case 11696:
				c.getItems().deleteItem(itemId,1);
				c.getItems().addItem(11690,1);
				c.getItems().addItem(11704,1);
				c.sendMessage("You dismantle the godsword blade from the hilt.");
			break;
			case 11698:
				c.getItems().deleteItem(itemId,1);
				c.getItems().addItem(11690,1);
				c.getItems().addItem(11706,1);
				c.sendMessage("You dismantle the godsword blade from the hilt.");
			break;
			case 11700:
				c.getItems().deleteItem(itemId,1);
				c.getItems().addItem(11690,1);
				c.getItems().addItem(11708,1);
				c.sendMessage("You dismantle the godsword blade from the hilt.");
			break;
		default:
			if (c.playerRights == 3)
				Misc.println(c.playerName+ " - Item3rdOption: "+itemId);
			break;
		}

	}

}
