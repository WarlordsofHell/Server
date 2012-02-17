package server.model.items;

import server.Config;
import server.Server;
import server.model.players.Client;
import server.util.Misc;

public class ItemAssistant {

	private Client c;
	
	public ItemAssistant(Client client) {
		this.c = client;
	}
		
	/**
	Items
	**/
	
	public int[][] brokenBarrows = {{4708,4860},{4710,4866},{4712,4872},{4714,4878},{4716,4884},
	{4720,4896},{4718,4890},{4720,4896},{4722,4902},{4732,4932},{4734,4938},{4736,4944},{4738,4950},
	{4724,4908},{4726,4914},{4728,4920},{4730,4926},{4745,4956},{4747,4926},{4749,4968},{4751,4794},
	{4753,4980},{4755,4986},{4757,4992},{4759,4998}};
	
	public void resetItems(int WriteFrame) {
		if(c.getAwaitingUpdate())
			c.setAwaitingUpdate(false);
		if(c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			c.getOutStream().writeWord(c.playerItems.length);
			for (int i = 0; i < c.playerItems.length; i++) {
				if(c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255); 		
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else
					c.getOutStream().writeByte(c.playerItemsN[i]);
					c.getOutStream().writeWordBigEndianA(c.playerItems[i]); 
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	
	public int getItemCount(int itemID) {
		int count = 0;	
		for (int j = 0; j < c.playerItems.length; j++) {
			if (c.playerItems[j] == itemID + 1)
				count += c.playerItemsN[j];	
		}
		return count;
	}
	
	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < c.playerBonus.length; i++) {
			send = (c.playerBonus[i] >= 0) ? BONUS_NAMES[i]+": +"+c.playerBonus[i] : BONUS_NAMES[i]+": -"+java.lang.Math.abs(c.playerBonus[i]);
			if (i == 10)
				offset = 1;
			c.getPA().sendFrame126(send, (1675+i+offset));
		}
		String[] soakingNames = {"Melee", "Magic", "Ranged"};
		for (int i = 0; i < c.soakingBonus.length; i++) {
			String toSend = "Absorb " + soakingNames[i] + ": +" + (int)(c.soakingBonus[i] * 100) + "%";
			c.getPA().sendFrame126(toSend, 19149 + i);
		}
	}
	
	public int getTotalCount(int itemID) {
		int count = 0;	
		for (int j = 0; j < c.playerItems.length; j++) {
			if (Item.itemIsNote[itemID+1]) {
				if (itemID+2 == c.playerItems[j])
					count += c.playerItemsN[j];
			} 
			if (!Item.itemIsNote[itemID+1]) {
				if (itemID+1 == c.playerItems[j])
					count += c.playerItemsN[j];
			}
		}
		for (int j = 0; j < c.bankItems.length; j++) {
			if (c.bankItems[j] == itemID + 1)
				count += c.bankItemsN[j];	
		}
		return count;
	}
	
	public void sendItemsKept() {
		if(c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6963);
			c.getOutStream().writeWord(c.itemKeptId.length);
			for (int i = 0; i < c.itemKeptId.length; i++) {
				if(c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255); 
					c.getOutStream().writeDWord_v2(1);
				} else {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(c.itemKeptId[i] > 0 ? c.itemKeptId[i] + 1 : 0);
				}
			}
			c.getOutStream().endFrameVarSizeWord();   
			c.flushOutStream();
		}
    }
	
		/**
	 * Banks our equipment for us
	 */
	public void bankEquipment() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > 0 && c.playerEquipmentN[i] > 0)
				c.getItems().addItemToBank(c.playerEquipment[i], c.playerEquipmentN[i]);
			c.getItems().replaceEquipment(i, -1);
		}
	}
	
	/**
	 * Adds an item to the bank without checking if the player has it in there inventory
	 * @param itemId the id of the item were banking
	 * @param amount amount of items to bank
	 */
	public void addItemToBank(int itemId, int amount) {
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0 || c.bankItems[i] == itemId + 1
					&& c.bankItemsN[i] + amount < Integer.MAX_VALUE) {
				c.bankItems[i] = itemId + 1;
				c.bankItemsN[i] += amount;
				resetBank();
				return;
			}
		}
	}
	/**
	 * Replaces an equipment item with the specified replaceItem and updates player
	 * @param slot equipment id slot
	 * @param replaceItem the new item
	 */
	public void replaceEquipment(int slot, int replaceItem) {
		if (c.playerEquipment[slot] > 0) {
			c.playerEquipment[slot] = replaceItem;
			if (replaceItem <= 0) {
				c.playerEquipmentN[slot] = 0;
				c.updateRequired = true;
				c.getPA().requestUpdates();
				c.setAppearanceUpdateRequired(true);
			}
			c.getItems().updateSlot(slot);
			resetBonus();
			getBonus();
			writeBonus();
		}
	}
	/**
	* Item kept on death
	**/
	
	public void keepItem(int keepItem, boolean deleteItem) { 	
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for(int i = 0; i < c.playerItems.length; i++) {
			if(c.playerItems[i]-1 > 0) {
				int inventoryItemValue = c.getShops().getItemShopValue(c.playerItems[i] - 1);
				if(inventoryItemValue > value && !c.invSlot[i]) {
					value = inventoryItemValue;
					item = c.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;			
				}
			}
		}
		for(int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			if(c.playerEquipment[i1] > 0) {
				int equipmentItemValue = c.getShops().getItemShopValue(c.playerEquipment[i1]);
				if(equipmentItemValue > value && !c.equipSlot[i1]) {
					value = equipmentItemValue;
					item = c.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;			
				}
			}
		}	
		if(itemInInventory) {
			c.invSlot[slotId] = true;
			if(deleteItem)			
				deleteItem(c.playerItems[slotId]-1, getItemSlot(c.playerItems[slotId]-1), 1);
		} else {
			c.equipSlot[slotId] = true;
			if(deleteItem)
				deleteEquipment(item, slotId);	
		}
		c.itemKeptId[keepItem] = item;	
	}
		
	/**
	* Reset items kept on death
	**/
	
	public void resetKeepItems() {
		for(int i = 0; i < c.itemKeptId.length; i++)
			c.itemKeptId[i] = -1;
		for(int i1 = 0; i1 < c.invSlot.length; i1++)
			c.invSlot[i1] = false;
		for(int i2 = 0; i2 < c.equipSlot.length; i2++)
			c.equipSlot[i2] = false;	
	}
	
	/**
	* delete all items
	**/
	
	public void deleteAllItems() {	
		for(int i1 = 0; i1 < c.playerEquipment.length; i1++)
			deleteEquipment(c.playerEquipment[i1], i1);
		for(int i = 0; i < c.playerItems.length; i++)
			deleteItem(c.playerItems[i]-1, getItemSlot(c.playerItems[i]-1), c.playerItemsN[i]);
	}
	public void handleSpecialPickup(int itemId) {
	}
	
	
	/**
	* Drop all items for your killer
	**/
	
	public void dropAllItems() {
		Client o = (Client) Server.playerHandler.players[c.killerId];
		for(int i = 0; i < c.playerItems.length; i++) {
			if(o != null) {
				if (!tradeable(c.playerItems[i] - 1)) {
					Server.itemHandler.createGroundItem(o, c.playerItems[i] -1, c.getX(), c.getY(), c.playerItemsN[i], c.killerId);
				} else {
					if (specialCase(c.playerItems[i] - 1))
						Server.itemHandler.createGroundItem(o, 995, c.getX(), c.getY(), getUntradePrice(c.playerItems[i]-1), c.killerId);
					Server.itemHandler.createGroundItem(c, c.playerItems[i] -1, c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerItems[i] -1, c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
			}
		} 
		for(int e = 0; e < c.playerEquipment.length; e++) {
			if(o != null) {
				if (!tradeable(c.playerEquipment[e])) {
					Server.itemHandler.createGroundItem(o, c.playerEquipment[e], c.getX(), c.getY(), c.playerEquipmentN[e], c.killerId);
				} else {
					if (specialCase(c.playerEquipment[e]))
						Server.itemHandler.createGroundItem(o, 995, c.getX(), c.getY(), getUntradePrice(c.playerEquipment[e]), c.killerId);
					Server.itemHandler.createGroundItem(c, c.playerEquipment[e], c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e], c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
			}
		}
		Server.itemHandler.createGroundItem(o, 526, c.getX(), c.getY(), 1, c.killerId);
	if (Misc.random(20) == 1)//chance is 1/20
		if(c.inWild()) {
				o.sendMessage("You have received a PvP drop!");
				Server.itemHandler.createGroundItem(o, c.getPA().randomPvPDrop(), c.getX(), c.getY(), 1, c.killerId);
		}	
	}
	
	public int getUntradePrice(int item) {
		switch (item) {
			case 2518:
			case 2524:
			case 2526:
				return 100000;
			case 2520:
			case 2522:
				return 150000;
		}
		return 0;
	}
	
	public boolean specialCase(int itemId) {
		switch (itemId) {
			case 2518:
			case 2520:
			case 2522:
			case 2524:
			case 2526:
			return true;		
		}
		return false;
	}
	
	public boolean tradeable(int itemId) {
		for (int j = 0; j < Config.ITEM_TRADEABLE.length; j++) {
			if (itemId == Config.ITEM_TRADEABLE[j])
				return true;
		}
		return false;
	}
	
	/**
	*Add Item
	**/
	public boolean addItem(int item, int amount) {
		if (amount < 1)
			amount = 1;
		if(item <= 0)
			return false;
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item]) || ((freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == item + 1 && Item.itemStackable[item] && c.playerItems[i] > 0) {
					c.playerItems[i] = (item + 1);
					if (c.playerItemsN[i] + amount < Config.MAXITEM_AMOUNT && c.playerItemsN[i] + amount > -1)
						c.playerItemsN[i] += amount;
					else
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					if(c.getOutStream() != null && c != null ) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else
							c.getOutStream().writeByte(c.playerItemsN[i]);
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if (amount < Config.MAXITEM_AMOUNT && amount > -1) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getItems().addItem(item, amount - 1);
							return true;
						}
					} else
						c.playerItemsN[i] = Config.MAXITEM_AMOUNT;
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
	}
	

	
	/**
	*Bonuses
	**/
	public final String[] BONUS_NAMES = {
		"Stab", "Slash", "Crush", "Magic", "Ranged", "Stab", "Slash",
		"Crush", "Magic", "Range", "Strength", "Prayer"
	};
	
	public void resetBonus() {
		for (int i = 0; i < c.playerBonus.length; i++) {
			c.playerBonus[i] = 0;
		}
		for (int i = 0; i < c.soakingBonus.length; i++) {
			c.soakingBonus[i] = 0;
		}
	}
	public int[] getBonus(int weaponId) {
  int[] bonuses = new int[13];
  for (int i = 0; i < c.playerEquipment.length; i++) {
   if (c.playerEquipment[i] > -1) {
    for (int j = 0; j < Config.ITEM_LIMIT; j++) {
     if (Server.itemHandler.ItemList[j] != null){
      if (Server.itemHandler.ItemList[j].itemId == c.playerEquipment[i]) {
       if(i == c.playerWeapon)
        j = weaponId;
       for (int k = 0; k < c.playerBonus.length; k++)
        bonuses[k] += Server.itemHandler.ItemList[j].Bonuses[k];
       break;
      }
     }
    }
   }
  }
  return bonuses;
 }
	
	public void getBonus() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > -1) {
				for (int j = 0; j < Config.ITEM_LIMIT; j++) {
					if (Server.itemHandler.ItemList[j] != null){
						if (Server.itemHandler.ItemList[j].itemId == c.playerEquipment[i]) {
							for (int k = 0; k < c.playerBonus.length; k++)
								c.playerBonus[k] += Server.itemHandler.ItemList[j].Bonuses[k];
							break;
						}
					}
				}
			}
		}
		getSoakingBonus();
	}
	
	public void getSoakingBonus() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > -1) {
				for (int j = 0; j < Config.ITEM_LIMIT; j++) {
					if (Server.itemHandler.ItemList[j] != null) {
						if (Server.itemHandler.ItemList[j].itemId == c.playerEquipment[i]) {
							for (int k = 0; k < c.soakingBonus.length; k++)
								c.soakingBonus[k] += Server.itemHandler.ItemList[j].soakingInfo[k];
							break;
						}
					}
				}
			}
		}
	}
	
	
	/**
	*Wear Item
	**/

	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		//c.sendMessage(WeaponName2);
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855); //punch, kick, block
			c.getPA().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")) {
			c.setSidebarInterface(0, 12290); //flick, lash, deflect
			c.getPA().sendFrame246(12291, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10")|| WeaponName.endsWith("full") || WeaponName.startsWith("seercull") || WeaponName.contains("cannon")) {
			c.setSidebarInterface(0, 1764); //accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1767);
		} else if (WeaponName.startsWith("Staff") || WeaponName.endsWith("staff") || WeaponName.endsWith("wand")) {
			c.setSidebarInterface(0, 6103); //spike, impale, smash, block
			c.getPA().sendFrame246(6104, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 6132);
		} else if (WeaponName2.startsWith("dart") || WeaponName2.startsWith("knife") || WeaponName2.startsWith("javelin") || WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			c.setSidebarInterface(0, 4446); //accurate, rapid, longrange
			c.getPA().sendFrame246(4447, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger") || WeaponName2.contains("sword") && !WeaponName2.contains("godsword") && !WeaponName2.contains("lime") && !WeaponName2.contains("Vesta's")) {
			c.setSidebarInterface(0, 2276); //stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2279);
		} else if (WeaponName2.startsWith("pickaxe")) {
			c.setSidebarInterface(0, 5570); //spike, impale, smash, block
			c.getPA().sendFrame246(5571, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.contains("axe")) {
			c.setSidebarInterface(0, 1698); //chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1701);
		} else if (WeaponName2.contains("claws")) {
			c.setSidebarInterface(0, 7762);
			c.getPA().sendFrame246(7763, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 7765);
		} else if (WeaponName2.startsWith("halberd")) {
			c.setSidebarInterface(0, 8460); //jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("Scythe")) {
			c.setSidebarInterface(0, 8460); //jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.contains("spear")) {
			c.setSidebarInterface(0, 4679); //lunge, swipe, pound, block
			c.getPA().sendFrame246(4680, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")){
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame246(3797, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 3799);

		} else if (WeaponName2.contains("maul") || WeaponName2.contains("hammer")) {
			c.setSidebarInterface(0, 425); //war hamer equip.
			c.getPA().sendFrame246(426, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 428);		
		} else {
			c.setSidebarInterface(0, 2423); //chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2426);
		}
		
	}
	

	/**
	*Weapon Requirements
	**/
	
	public void getRequirements(String itemName, int itemId) {
		c.attackLevelReq = c.defenceLevelReq = c.strengthLevelReq = c.rangeLevelReq = c.prayerLevelReq = c.magicLevelReq = 0;
		if(itemName.contains("mystic") || itemName.contains("nchanted")) {
			if(itemName.contains("staff")) {
				c.magicLevelReq = 20;
				c.attackLevelReq = 40;
			} else {
				c.magicLevelReq = 20;
				c.defenceLevelReq = 20;
			}
		}
		if (itemName.contains("infinity")) {
			c.magicLevelReq = 50;
			c.defenceLevelReq = 25;		
		}
		if (itemName.contains("rune c'bow")) {
			c.rangeLevelReq = 61;
		}
		if(itemName.contains("splitbark")) {
			c.magicLevelReq = 40;
			c.defenceLevelReq = 40;
		}
		if(itemName.contains("Green")) {
			if(itemName.contains("hide")) {
				c.rangeLevelReq = 40;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if(itemName.contains("Blue")) {
			if(itemName.contains("hide")) {
				c.rangeLevelReq = 50;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if(itemName.contains("Red")) {
			if(itemName.contains("hide")) {
				c.rangeLevelReq = 60;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if(itemName.contains("Black")) {
			if(itemName.contains("hide")) {
				c.rangeLevelReq = 70;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if(itemName.contains("bronze")) {
			if(!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if(itemName.contains("iron")) {
			if(!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}	
			return;
		}
		if(itemName.contains("steel")) {	
			if(!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 5;
			}
			return;
		}
		/*if(itemName.contains("black")) {
			if(!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe") && !itemName.contains("vamb") && !itemName.contains("chap")) {
				c.attackLevelReq = c.defenceLevelReq = 10;
			}
			return;
		}*/
		if(itemName.contains("mithril")) {
			if(!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 20;
			}
			return;
		}
		if(itemName.contains("adamant")) {
			if(!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 30;
			}
			return;
		}
		if(itemName.contains("rune")) {
			if(!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe") && !itemName.contains("'bow")) {
				c.attackLevelReq = c.defenceLevelReq = 40;
			}
			return;
		}
		if(itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				c.attackLevelReq = c.defenceLevelReq = 60;
			return;
			}
		}
		if(itemName.contains("crystal")) {
			if(itemName.contains("shield")) {	
				c.defenceLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
			}
			return;
		}
		if(itemName.contains("ahrim")) {
			if(itemName.contains("staff")) {
				c.attackLevelReq = 70;
			} else {
				c.defenceLevelReq = 70;
			}
			c.magicLevelReq = 70;
		}
		if(itemName.contains("dagon")) {
				c.magicLevelReq = 40;
				c.defenceLevelReq = 20;
		}
		if(itemName.contains("stream")) {
				c.magicLevelReq = 70;
		}
		if(itemName.contains("initiate")) {
				c.defenceLevelReq = 20;
		}
		if(itemName.contains("chaotic")) {
			if(itemName.contains("shield")) {
				c.defenceLevelReq = 80;				
			} else {
				c.attackLevelReq = 80;
			}
		}
		if(itemName.contains("vesta") && itemName.contains("corrupt")) {
			if(itemName.contains("longsword") || itemName.contains("spear")) {
				c.attackLevelReq = 20;				
			} else {
				c.defenceLevelReq = 20;
			}
		}
		if(itemName.contains("vesta") && !itemName.contains("corrupt")) {
			if(itemName.contains("longsword") || itemName.contains("spear")) {
				c.attackLevelReq = 78;				
			} else {
				c.defenceLevelReq = 78;
			}
		}
		if(itemName.contains("statius") && itemName.contains("corrupt")) {
			if(itemName.contains("warhammer")) {
				c.attackLevelReq = 20;				
			} else {
				c.defenceLevelReq = 20;
			}
		}
		if(itemName.contains("statius") && !itemName.contains("corrupt")) {
			if(itemName.contains("warhammer")) {
				c.attackLevelReq = 78;				
			} else {
				c.defenceLevelReq = 78;
			}
		}
		if(itemName.contains("zuriel") && itemName.contains("corrupt")) {
			if(itemName.contains("staff")) {
				c.attackLevelReq = 20;				
			} else {
				c.defenceLevelReq = 20;
			}
			c.magicLevelReq = 20;
		}
		if(itemName.contains("zuriel") && !itemName.contains("corrupt")) {
			if(itemName.contains("staff")) {
				c.attackLevelReq = 78;				
			} else {
				c.defenceLevelReq = 78;
			}
			c.magicLevelReq = 78;
		}
		if(itemName.contains("morrigan") && itemName.contains("corrupt")) {
			if(itemName.contains("javelin")) {
				c.rangeLevelReq = 20;				
			} else {
				c.rangeLevelReq = 20;
				c.defenceLevelReq = 20;
			}
		}
		if(itemName.contains("morrigan") && !itemName.contains("corrupt")) {
			if(itemName.contains("javelin")) {
				c.rangeLevelReq = 78;				
			} else {
				c.rangeLevelReq = 78;
				c.defenceLevelReq = 78;
			}
		}
		if(itemName.contains("karil")) {
			if(itemName.contains("crossbow")) {
				c.rangeLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if(itemName.contains("elite")) {
			c.defenceLevelReq = 40;
		}
		
		if(itemName.contains("torva")) {
			c.defenceLevelReq = 80;
		}
		
		if(itemName.contains("pernix")) {
			c.defenceLevelReq = 80;
			c.rangeLevelReq = 80;
		}
		
		if(itemName.contains("virtus")) {
			c.defenceLevelReq = 80;
			c.magicLevelReq = 80;
		}
		if(itemName.contains("godsword")) {
			c.attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			c.defenceLevelReq = 60;
		}
		if (itemName.contains("Initiate")) {
			c.defenceLevelReq = 20;
		}
		if(itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag")) {

			if(itemName.contains("hammers")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if(itemName.contains("axe")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if(itemName.contains("warspear")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if(itemName.contains("flail")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else {
				c.defenceLevelReq = 70;
			}
		}
			
		switch(itemId) {
			case 8839:
			case 8840:
			case 8842:
			case 11663:
			case 11664:
			case 11665:
			c.attackLevelReq = 42;
			c.rangeLevelReq = 42;
			c.strengthLevelReq = 42;
			c.magicLevelReq = 42;
			c.defenceLevelReq = 42;
			return;
			case 5698:
			c.attackLevelReq = 60;
			return;
			case 18335:
			c.magicLevelReq = 70;//WHAT THE FUCK WHY AM I STILL ABLE TO WIELD WITH 1 MAGE
			return;
			case 11283:
			c.defenceLevelReq = 75;
			return;
			case 4675:
			c.attackLevelReq = 50;
			c.magicLevelReq = 50;
			return;
			case 15486:
			c.attackLevelReq = 75;
			c.magicLevelReq = 75;
			return;
			case 11730:
			case 11716:
			c.attackLevelReq = 70;
			return;
			case 13742:
			case 13740:
			c.defenceLevelReq = 75;
			c.prayerLevelReq = 75;
			return;
			case 13734:
			c.defenceLevelReq = 40;
			c.prayerLevelReq = 55;
			return;
			case 13736:
			c.defenceLevelReq = 70;
			c.prayerLevelReq = 60;
			return;
			case 13744:
			case 13738:
			c.defenceLevelReq = 75;
			c.magicLevelReq = 65;
			c.prayerLevelReq = 70;
			return;
			case 10548:
			case 10551:
			case 2501:
			case 2499:
			case 1135:
			c.defenceLevelReq = 40;
			return;
			case 11235:
			case 6522:
			c.rangeLevelReq = 60;
			break;
			case 6524:
			c.defenceLevelReq = 60;
			break;
			case 11284:
			c.defenceLevelReq = 75;
			return;
			case 6889:
			case 6914:
			c.magicLevelReq = 60;
			break;
			case 861:
			c.rangeLevelReq = 50;
			break;
			case 10828:
			c.defenceLevelReq = 55;
			break;
			case 11724:
			case 11726:
			case 11728:
				c.defenceLevelReq = 65;
			break;
			case 3751:
			case 3749:
			case 3755:
			case 3753:
			c.defenceLevelReq = 45;
			break;
			case 2497:
			c.rangeLevelReq = 70;
			break;
			case 2412:
			case 2413:
			case 2414:
			c.magicLevelReq = 60;
			break;
			case 9185:
			c.rangeLevelReq = 61;
			break;
			case 2503:
			c.defenceLevelReq = 40;
			c.rangeLevelReq = 70;
			break;
			
			case 7462:
			case 7461:
			c.defenceLevelReq = 40;
			break;
			case 8846:
			c.defenceLevelReq = 5;
			break;
			case 8847:
			c.defenceLevelReq = 10;
			break;
			case 8848:
			c.defenceLevelReq = 20;
			break;
			case 8849:
			c.defenceLevelReq = 30;
			break;
			case 8850:
			c.defenceLevelReq = 40;
			break;
			
			
			case 7460:
			c.defenceLevelReq = 40;
			break;
			
			
			case 837:
			c.rangeLevelReq = 61;
			break;
			
			case 4151: // if you don't want to use names 			case 4151:
			case 15445:
			case 16022:
			case 15444:
			case 15443:
			case 15442:
			case 15441:			c.attackLevelReq = 70;
			return;
			
			case 6724: // seercull
			c.rangeLevelReq = 60; // idk if that is correct
			return;
			case 4153:
			c.attackLevelReq = 50;
			c.strengthLevelReq = 50;
			return;
		}
	}
	
	/**
	*two handed weapon check
	**/
	public boolean is2handed(String itemName, int itemId) {
		if(itemName.contains("ahrim") || itemName.contains("karil") || itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if(itemName.contains("longbow") || itemName.contains("shortbow") || itemName.contains("ark bow")) {
			return true;
		}
		if(itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword") || itemName.contains("aradomin sword") || itemName.contains("2h") || itemName.contains("spear") || itemName.contains("maul") || itemName.contains("anchor")) { 
			return true;
		}
		switch(itemId) {
			case 6724: // seercull
			case 11730:
			case 4153:
			case 6528:
			case 14484:
			case 15241:
			return true;
		}
		return false;
	}
	
	/**
	* Weapons special bar, adds the spec bars to weapons that require them
	* and removes the spec bars from weapons which don't require them
	**/
	
	public void addSpecialBar(int weapon) {
		switch(weapon) {
			case 14484: // Dragon claws
			c.getPA().sendFrame171(0, 7800);
			specialAmount(weapon, c.specAmount, 7812);
			break;
			case 15441: // whip
			case 15442: // whip
			case 15443: // whip
			case 15444: // whip
			case 15486: // whip
			case 4151: // whip
			case 16022: // lime
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;
			
			case 859: // Magic bows
			case 861:
			case 11235: // Dark bow
			case 15241: // Hand cannon
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;
			
			case 4587: // dscimmy
			case 10887:
			case 11694:
			case 11698:
			case 11700:
			case 11730:
			case 11696:
			case 13899:
						case 19780: // korasi
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.specAmount, 7611);
			break;
			
			case 3204: // d hally
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.specAmount, 8505);
			break;
			
			case 1377: // d battleaxe
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.specAmount, 7511);
			break;
			
			case 4153: // gmaul
			case 13902:
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;
			
			case 1249: //dspear
			case 13905:
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.specAmount, 7686);
			break;
		
			
			case 1215:// dragon dagger
			case 1231:
			case 5680:
			case 5698:
			case 1305: // dragon long
			c.getPA().sendFrame171(0, 7574); 
			specialAmount(weapon, c.specAmount, 7586);
			break;
			
			case 1434: // dragon mace
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.specAmount, 7636);
			break;
			
			default:
			c.getPA().sendFrame171(1, 7624); // mace interface
			c.getPA().sendFrame171(1, 7474); // hammer, gmaul
			c.getPA().sendFrame171(1, 7499); // axe
			c.getPA().sendFrame171(1, 7549);  // bow interface
			c.getPA().sendFrame171(1, 7574); // sword interface
			c.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most swords
			c.getPA().sendFrame171(1, 8493);
			c.getPA().sendFrame171(1, 12323); // whip interface
			break;		
		}
	}
	
	/**
	* Specials bar filling amount
	**/
	
	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
        c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
        c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));	
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}
	
	/**
	* Special attack text and what to highlight or blackout
	**/
	
	public void updateSpecialBar() {
		if(c.usingSpecial) {
			c.getPA().sendFrame126(
			""+(c.specAmount >= 2 ?  "@yel@S P" : "@bla@S P")
			+""+(c.specAmount >= 3 ?  "@yel@ E" : "@bla@ E") 
			+""+(c.specAmount >= 4 ?  "@yel@ C I" : "@bla@ C I")
			+""+(c.specAmount >= 5 ?  "@yel@ A L" : "@bla@ A L") 
			+""+(c.specAmount >= 6 ?  "@yel@  A" : "@bla@  A") 
			+""+(c.specAmount >= 7 ?  "@yel@ T T" : "@bla@ T T") 
			+""+(c.specAmount >= 8 ?  "@yel@ A" : "@bla@ A") 
			+""+(c.specAmount >= 9 ?  "@yel@ C" : "@bla@ C") 
			+""+(c.specAmount >= 10 ?  "@yel@ K" : "@bla@ K") , c.specBarId);
		} else {
			c.getPA().sendFrame126("@bla@S P E C I A L  A T T A C K", c.specBarId);
		}
	}
	
	/**
	*Wear Item
	**/
	public int targetSlot(String item) {
		int targetSlot = -1;
		for (int i = 0; i < HATS.length; i++) {
			if (item.contains(HATS[i]))
				targetSlot = 0;
		}
		for (int i = 0; i < CAPES.length; i++) {
			if (item.contains(CAPES[i]))
				targetSlot = 1;
		}
		for (int i = 0; i < AMULETS.length; i++) {
			if (item.contains(AMULETS[i]))
				targetSlot = 2;
		}
		for (int i = 0; i < WEAPONS.length; i++) {
			if (item.contains(WEAPONS[i]))
				targetSlot = 3;
		}
		for (int i = 0; i < BODY.length; i++) {
			if (item.contains(BODY[i]))
				targetSlot = 4;
		}
		for (int i = 0; i < SHIELDS.length; i++) {
			if (item.contains(SHIELDS[i]))
				targetSlot = 5;
		}
		for (int i = 0; i < LEGS.length; i++) {
			if (item.contains(LEGS[i]))
				targetSlot = 7;
		}
		for (int i = 0; i < GLOVES.length; i++) {
			if (item.contains(GLOVES[i]))
				targetSlot = 9;
		}
		for (int i = 0; i < BOOTS.length; i++) {
			if (item.contains(BOOTS[i]))
				targetSlot = 10;
		}
		for (int i = 0; i < RINGS.length; i++) {
			if (item.contains(RINGS[i]))
				targetSlot = 12;
		}
		for (int i = 0; i < ARROWS.length; i++) {
			if (item.contains(ARROWS[i]))
				targetSlot = 13;
		}
		return targetSlot;
	}
	
	public static String[] HATS = {"sallet", "hat", "helm", "mask", "coif", "hood", "headdress", "cowl"};
	public static String[] CAPES = {"cape", "accumulator", "attractor", "cloak", "alerter"};
	public static String[] AMULETS = {"amulet", "necklace"};
	public static String[] WEAPONS = {"flail", "hand", "mace", "dart", "knife", "javelin", "scythe", "claws", "bow", "crossbow", "c' bow", "adze", "axe", "sword", "rapier", "scimitar", "spear", "dagger", 
									"staff", "wand", "blade", "whip", "silverlight", "darklight", "maul", "halberd", "anchor", "tzhaar-ket-om", "hammer", "hand cannon"};
	public static String[] BODY = {"hauberk", "torso", "body", "top", "platebody", "chainbody", "shirt", "chestplate", "guthix dragonhide", 
									"zamorak dragonhide", "saradomin dragonhide", "brassard"};
	public static String[] SHIELDS = {"shield", "book", "defender", "toktz-ket-xil"};
	public static String[] LEGS = {"cuisse", "chaps", "platelegs", "skirt", "tassets", "bottoms", "bottom", "legs", "cuise", "void knight robe"};
	public static String[] GLOVES = {"gloves", "gauntlets", "vambraces", "vamb"};
	public static String[] BOOTS = {"boots", "shoes"};
	public static String[] RINGS = {"ring"};
	public static String[] ARROWS = {"bolts", "arrow", "bolt rack", "handcannon shot"};
	
	public boolean wearItem(int wearID, int slot) {
		synchronized(c) {
			int targetSlot=0;
			boolean canWearItem = true;
			if(c.playerItems[slot] == (wearID+1)) {				
				getRequirements(getItemName(wearID).toLowerCase(), wearID);	
				targetSlot = targetSlot(getItemName(wearID).toLowerCase());


				if(c.duelRule[11] && targetSlot == 0) {
					c.sendMessage("Wearing hats has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[12] && targetSlot == 1) {
					c.sendMessage("Wearing capes has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[13]  && targetSlot == 2) {
					c.sendMessage("Wearing amulets has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[14]  && targetSlot == 3) {
					c.sendMessage("Wielding weapons has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[15]  && targetSlot == 4) {
					c.sendMessage("Wearing bodies has been disabled in this duel!");
					return false;
				}
				if((c.duelRule[16] && targetSlot == 5) || (c.duelRule[16] && is2handed(getItemName(wearID).toLowerCase(), wearID))) {
					c.sendMessage("Wearing shield has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[17]  && targetSlot == 7) {
					c.sendMessage("Wearing legs has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[18]  && targetSlot == 9) {
					c.sendMessage("Wearing gloves has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[19]  && targetSlot == 10) {
					c.sendMessage("Wearing boots has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[20]  && targetSlot == 12) {
					c.sendMessage("Wearing rings has been disabled in this duel!");
					return false;
				}
				if(c.duelRule[21]  && targetSlot == 13) {
					c.sendMessage("Wearing arrows has been disabled in this duel!");
					return false;
				}
				if(c.isDead) {//needs fixing along with drop item system
					c.sendMessage("You can't wield items when you are dead.");
					return false;
				} 
if (wearID > 15085 && wearID < 15102){
				if (c.clanId >= 0){
					c.getDicing().useDice(wearID, true);
				}else{
					c.sendMessage("You must be in a clan chat channel to do that.");
				}
					return false;
			}
				if(Config.itemRequirements) {
					if(targetSlot == 10 || targetSlot == 7 || targetSlot == 5 || targetSlot == 4 || targetSlot == 0 || targetSlot == 9 || targetSlot == 10) {
						if(c.defenceLevelReq > 0) {
							if(c.getPA().getLevelForXP(c.playerXP[1]) < c.defenceLevelReq) {
								c.sendMessage("You need a defence level of "+c.defenceLevelReq+" to wear this item.");
								canWearItem = false;
							}
						}
						if(c.rangeLevelReq > 0) {
							if(c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
								c.sendMessage("You need a range level of "+c.rangeLevelReq+" to wear this item.");
								canWearItem = false;
							}
						}
						if(c.magicLevelReq > 0) {
							if(c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
								c.sendMessage("You need a magic level of "+c.magicLevelReq+" to wear this item.");
								canWearItem = false;
							}
						}
					}
					if(targetSlot == 3) {
						if(c.attackLevelReq > 0) {
							if(c.getPA().getLevelForXP(c.playerXP[0]) < c.attackLevelReq) {
								c.sendMessage("You need an attack level of "+c.attackLevelReq+" to wield this weapon.");
								canWearItem = false;
							}
						}
						if(c.rangeLevelReq > 0) {
							if(c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
								c.sendMessage("You need a range level of "+c.rangeLevelReq+" to wield this weapon.");
								canWearItem = false;
							}
						}
						if(c.prayerLevelReq > 0) {
							if(c.getPA().getLevelForXP(c.playerXP[5]) < c.prayerLevelReq) {
								c.sendMessage("You need a prayer level of "+c.prayerLevelReq+" to wield this weapon.");
								canWearItem = false;
							}
						}
						if(c.magicLevelReq > 0) {
							if(c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
								c.sendMessage("You need a magic level of "+c.magicLevelReq+" to wield this weapon.");
								canWearItem = false;
							}
						}
					}
				}

				if(!canWearItem) {
					return false;
				}
				
				int wearAmount = c.playerItemsN[slot];
				if (wearAmount < 1) {
					return false;
				}

				if(slot >= 0 && wearID >= 0) {
					int toEquip = c.playerItems[slot];
					int toEquipN = c.playerItemsN[slot];
					int toRemove = c.playerEquipment[targetSlot]; 
					int toRemoveN = c.playerEquipmentN[targetSlot];
					if (toEquip == toRemove + 1 && Item.itemStackable[toRemove]) {
						deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
						c.playerEquipmentN[targetSlot] += toEquipN;
					} else if (targetSlot != 5 && targetSlot != 3) {
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					} else if (targetSlot == 5) {
						boolean wearing2h = is2handed(getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase(), c.playerEquipment[c.playerWeapon]);
						boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
						if (wearing2h) {
							toRemove = c.playerEquipment[c.playerWeapon];
							toRemoveN = c.playerEquipmentN[c.playerWeapon];
							c.playerEquipment[c.playerWeapon] = -1;
							c.playerEquipmentN[c.playerWeapon] = 0;
							updateSlot(c.playerWeapon);
						}
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;			
					} else if (targetSlot == 3) {
						boolean is2h = is2handed(getItemName(wearID).toLowerCase(), wearID);
						boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
						boolean wearingWeapon = c.playerEquipment[c.playerWeapon] > 0;
						if (is2h) {
							if (wearingShield && wearingWeapon) {
								if (freeSlots() > 0) {
									c.playerItems[slot] = toRemove + 1;
									c.playerItemsN[slot] = toRemoveN;
									c.playerEquipment[targetSlot] = toEquip - 1;
									c.playerEquipmentN[targetSlot] = toEquipN;
									removeItem(c.playerEquipment[c.playerShield], c.playerShield);
								} else {
									c.sendMessage("You do not have enough inventory space to do this.");
									return false;
								}						
							} else if (wearingShield && !wearingWeapon) {
								c.playerItems[slot] = c.playerEquipment[c.playerShield] + 1;
								c.playerItemsN[slot] = c.playerEquipmentN[c.playerShield];
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								c.playerEquipment[c.playerShield] = -1;
								c.playerEquipmentN[c.playerShield] = 0;
								updateSlot(c.playerShield);
							} else {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;	
							}
						} else {
							c.playerItems[slot] = toRemove + 1;
							c.playerItemsN[slot] = toRemoveN;
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;	
						}
					}
					c.getItems().resetItems(3214);
				}
				if(targetSlot == 3) {
					c.usingSpecial = false;
					addSpecialBar(wearID);
				}
				if(c.getOutStream() != null && c != null ) {
					c.getOutStream().createFrameVarSizeWord(34);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(targetSlot);
					c.getOutStream().writeWord(wearID+1);

					if (c.playerEquipmentN[targetSlot] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
					} else {
						c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
					}
					
					c.getOutStream().endFrameVarSizeWord();
					c.flushOutStream();
				}
				sendWeapon(c.playerEquipment[c.playerWeapon], getItemName(c.playerEquipment[c.playerWeapon]));
				resetBonus();
				getBonus();
				writeBonus();
				c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
				c.getPA().requestUpdates();
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	public void wearItem(int wearID, int wearAmount, int targetSlot) {	
		if(c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID+1);
			if (wearAmount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(wearAmount);
			} else
				c.getOutStream().writeByte(wearAmount);	
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipment[targetSlot]=wearID;
			c.playerEquipmentN[targetSlot]=wearAmount;
			c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
			c.getItems().resetBonus();
			c.getItems().getBonus();
			c.getItems().writeBonus();
			c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
			c.updateRequired = true; 
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	public void updateSlot(int slot) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null ) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(slot);
				c.getOutStream().writeWord(c.playerEquipment[slot] + 1);
				if (c.playerEquipmentN[slot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[slot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[slot]);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}			
		}
	
	}

	/**
	*Remove Item
	**/
	public void removeItem(int wearID, int slot) {
		synchronized(c) {
			if(c.getOutStream() != null && c != null) {
				if(c.playerEquipment[slot] > -1){
					if(addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
						c.playerEquipment[slot]=-1;
						c.playerEquipmentN[slot]=0;
						sendWeapon(c.playerEquipment[c.playerWeapon], getItemName(c.playerEquipment[c.playerWeapon]));
						resetBonus();
						getBonus();
						writeBonus();
						c.getCombat().getPlayerAnimIndex(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
						c.getOutStream().createFrame(34);
						c.getOutStream().writeWord(6);
						c.getOutStream().writeWord(1688);
						c.getOutStream().writeByte(slot);
						c.getOutStream().writeWord(0);
						c.getOutStream().writeByte(0);
						c.flushOutStream();
						c.updateRequired = true; 
						c.setAppearanceUpdateRequired(true);
					}
				}
			}
		}
	}
		
	/**
	*BANK
	*/
	
	public void rearrangeBank() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) { 
				totalItems ++;
				if (highestSlot <= i) {	
					highestSlot = i;
				}
			}  
		}
		
		for (int i = 0; i <= highestSlot; i++) {
			if (c.bankItems[i] == 0) {
				boolean stop = false;
			
			for (int k = i; k <= highestSlot; k++) {
				if (c.bankItems[k] != 0 && !stop) {
					int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							c.bankItems[j-spots] = c.bankItems[j];
							c.bankItemsN[j-spots] = c.bankItemsN[j];
							stop = true;
							c.bankItems[j] = 0; c.bankItemsN[j] = 0; 
						}
					}
				}					
			}
		}
		
	int totalItemsAfter = 0;
	for (int i = 0; i < Config.BANK_SIZE; i++) {
		if (c.bankItems[i] != 0) { 
		totalItemsAfter ++; 
		} 
	}
		
	if (totalItems != totalItemsAfter) 
		c.disconnected = true;
	}
	
	
	public void itemOnInterface(int id, int amount, int child) {
		synchronized(c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(child);
			c.getOutStream().writeWord(amount);
			if (amount > 254){
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().writeWordBigEndianA(id); 
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	
	public void sendFrame34(int item, int amount, int slot, int frame) {
		c.getOutStream().createFrameVarSizeWord(34);
		c.getOutStream().writeWord(frame);
		c.getOutStream().writeByte(slot);
		c.getOutStream().writeWord(item+1);
		c.getOutStream().writeByte(255);
		c.getOutStream().writeDWord(amount);
		c.getOutStream().endFrameVarSizeWord();
	}
	
	public void resetBank(){
		synchronized(c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5382); // bank
			c.getOutStream().writeWord(Config.BANK_SIZE);
			for (int i=0; i<Config.BANK_SIZE; i++){
				if (c.bankItemsN[i] > 254){
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.bankItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.bankItemsN[i]); 	
				}
				if (c.bankItemsN[i] < 1) {
					c.bankItems[i] = 0;
				}
				if (c.bankItems[i] > Config.ITEM_LIMIT || c.bankItems[i] < 0) {
					c.bankItems[i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.bankItems[i]); 
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	
	
	public void resetTempItems(){
		synchronized(c) {
			int itemCount = 0;
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] > -1) {
					itemCount=i;
				}
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5064);
			c.getOutStream().writeWord(itemCount+1); 
			for (int i = 0; i < itemCount+1; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255); 						
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				if (c.playerItems[i] > Config.ITEM_LIMIT || c.playerItems[i] < 0) {
					c.playerItems[i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.playerItems[i]); 
			}
			c.getOutStream().endFrameVarSizeWord();	
			c.flushOutStream();
		}
	}
	
	
	public boolean bankItem(int itemID, int fromSlot, int amount) {
		if (c.inTrade)
			return false;
		if (c.playerItemsN[fromSlot] <= 0){
			return false;
		}
		if (!Item.itemIsNote[c.playerItems[fromSlot]-1]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot]-1] || c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank=false;
			    for (int i=0; i< Config.BANK_SIZE; i++) {
						if (c.bankItems[i] == c.playerItems[fromSlot]) {
							if (c.playerItemsN[fromSlot]<amount)
									amount = c.playerItemsN[fromSlot];
							alreadyInBank = true;
							toBankSlot = i;
							i=Config.BANK_SIZE+1;
						}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
						for (int i=0; i<Config.BANK_SIZE; i++) {
							if (c.bankItems[i] <= 0) {
									toBankSlot = i;
									i=Config.BANK_SIZE+1;
							}
						}
						c.bankItems[toBankSlot] = c.playerItems[fromSlot];
						if (c.playerItemsN[fromSlot]<amount){
							amount = c.playerItemsN[fromSlot];
						}
						if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT && (c.bankItemsN[toBankSlot] + amount) > -1) {
							c.bankItemsN[toBankSlot] += amount;
						} else {
							c.sendMessage("Bank full!");
							return false;
						}
						deleteItem((c.playerItems[fromSlot]-1), fromSlot, amount);
						resetTempItems();
						resetBank();
						return true;
				}
				else if (alreadyInBank) {
						if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT && (c.bankItemsN[toBankSlot] + amount) > -1) {
							c.bankItemsN[toBankSlot] += amount;
						} else {
							c.sendMessage("Bank full!");
							return false;
						}
						deleteItem((c.playerItems[fromSlot]-1), fromSlot, amount);
						resetTempItems();
						resetBank();
						return true;
				} else {
						c.sendMessage("Bank full!");
						return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank=false;
			    for (int i=0; i<Config.BANK_SIZE; i++) {
						if (c.bankItems[i] == c.playerItems[fromSlot]) {
							alreadyInBank = true;
							toBankSlot = i;
							i=Config.BANK_SIZE+1;
						}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
			       	for (int i=0; i<Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
								toBankSlot = i;
								i=Config.BANK_SIZE+1;
						}
					}
						int firstPossibleSlot=0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i=firstPossibleSlot; i<c.playerItems.length; i++) {
									if ((c.playerItems[i]) == itemID) {
										firstPossibleSlot = i;
										itemExists = true;
										i=30;
									}
							}
							if (itemExists) {
									c.bankItems[toBankSlot] = c.playerItems[firstPossibleSlot];
									c.bankItemsN[toBankSlot] += 1;
									deleteItem((c.playerItems[firstPossibleSlot]-1), firstPossibleSlot, 1);
									amount--;
							} else {
									amount=0;
							}
						}
						resetTempItems();
						resetBank();
						return true;
				} else if (alreadyInBank) {
						int firstPossibleSlot=0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i=firstPossibleSlot; i<c.playerItems.length; i++) {
								if ((c.playerItems[i]) == itemID) {
									firstPossibleSlot = i;
									itemExists = true;
									i=30;
								}
							}
							if (itemExists) {
									c.bankItemsN[toBankSlot] += 1;
									deleteItem((c.playerItems[firstPossibleSlot]-1), firstPossibleSlot, 1);
									amount--;
							} else {
									amount=0;
							}
						}
						resetTempItems();
						resetBank();
						return true;
				} else {
						c.sendMessage("Bank full!");
						return false;
				}
			}
		}
		else if (Item.itemIsNote[c.playerItems[fromSlot]-1] && !Item.itemIsNote[c.playerItems[fromSlot]-2]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot]-1] || c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank=false;
			    for (int i=0; i<Config.BANK_SIZE; i++) {
						if (c.bankItems[i] == (c.playerItems[fromSlot]-1)) {
							if (c.playerItemsN[fromSlot]<amount)
									amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i=Config.BANK_SIZE+1;
						}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
			       	for (int i=0; i<Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
								toBankSlot = i;
								i=Config.BANK_SIZE+1;
						}
					}
					c.bankItems[toBankSlot] = (c.playerItems[fromSlot]-1);
					if (c.playerItemsN[fromSlot]<amount){
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT && (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot]-1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				}
				else if (alreadyInBank) {
					if ((c.bankItemsN[toBankSlot] + amount) <= Config.MAXITEM_AMOUNT && (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((c.playerItems[fromSlot]-1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
						c.sendMessage("Bank full!");
						return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank=false;
			    for (int i=0; i<Config.BANK_SIZE; i++) {
					if (c.bankItems[i] == (c.playerItems[fromSlot]-1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i=Config.BANK_SIZE+1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
			       	for (int i=0; i<Config.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0){
								toBankSlot = i;
								i=Config.BANK_SIZE+1;
						}
					}
						int firstPossibleSlot=0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i=firstPossibleSlot; i<c.playerItems.length; i++) {
								if ((c.playerItems[i]) == itemID) {
									firstPossibleSlot = i;
									itemExists = true;
									i=30;
								}
							}
							if (itemExists) {
									c.bankItems[toBankSlot] = (c.playerItems[firstPossibleSlot]-1);
									c.bankItemsN[toBankSlot] += 1;
									deleteItem((c.playerItems[firstPossibleSlot]-1), firstPossibleSlot, 1);
									amount--;
							} else {
									amount=0;
							}
						}
						resetTempItems();
						resetBank();
						return true;
				}
				else if (alreadyInBank) {
						int firstPossibleSlot=0;
						boolean itemExists = false;
						while (amount > 0) {
							itemExists = false;
							for (int i=firstPossibleSlot; i<c.playerItems.length; i++) {
								if ((c.playerItems[i]) == itemID) {
									firstPossibleSlot = i;
									itemExists = true;
									i=30;
								}
							}
							if (itemExists) {
									c.bankItemsN[toBankSlot] += 1;
									deleteItem((c.playerItems[firstPossibleSlot]-1), firstPossibleSlot, 1);
									amount--;
							} else {
									amount=0;
							}
						}
						resetTempItems();
						resetBank();
						return true;
				} else {
						c.sendMessage("Bank full!");
						return false;
				}
			}
		} else {
			c.sendMessage("Item not supported "+(c.playerItems[fromSlot]-1));
			return false;
		}
	}
	
	
	public int freeBankSlots(){
		int freeS=0;
        for (int i=0; i < Config.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}
	
	
	public void fromBank(int itemID, int fromSlot, int amount) {
		if (amount > 0) {
		  if (c.bankItems[fromSlot] > 0) {
			if (!c.takeAsNote) {
			  if (Item.itemStackable[c.bankItems[fromSlot]-1]) {
				if (c.bankItemsN[fromSlot] > amount) {
				  if (addItem((c.bankItems[fromSlot]-1), amount)) {
					c.bankItemsN[fromSlot] -= amount;
					resetBank();
					c.getItems().resetItems(5064);
				  }
				} else {
				  if (addItem((c.bankItems[fromSlot]-1), c.bankItemsN[fromSlot])) {
					c.bankItems[fromSlot] = 0;
					c.bankItemsN[fromSlot] = 0;
					resetBank();
					c.getItems().resetItems(5064);
				  }
				}
			  } else {
				while (amount > 0) {
				  if (c.bankItemsN[fromSlot] > 0) {
					if (addItem((c.bankItems[fromSlot]-1), 1)) {
					  c.bankItemsN[fromSlot] += -1;
					  amount--;
					} else {
					  amount = 0;
					}
				  } else {
					amount = 0;
				  }
				}
				resetBank();
				c.getItems().resetItems(5064);
			  }
			} else if (c.takeAsNote && Item.itemIsNote[c.bankItems[fromSlot]]) {
				if (c.bankItemsN[fromSlot] > amount) {
					if (addItem(c.bankItems[fromSlot], amount)) {
						c.bankItemsN[fromSlot] -= amount;
						resetBank();
						c.getItems().resetItems(5064);
					}
				} else {
					if (addItem(c.bankItems[fromSlot], c.bankItemsN[fromSlot])) {
						c.bankItems[fromSlot] = 0;
						c.bankItemsN[fromSlot] = 0;
						resetBank();
						c.getItems().resetItems(5064);
					}
				}
			} else {
			  c.sendMessage("This item can't be withdrawn as a note.");
			  if (Item.itemStackable[c.bankItems[fromSlot]-1]) {
				if (c.bankItemsN[fromSlot] > amount) {
				  if (addItem((c.bankItems[fromSlot]-1), amount)) {
					c.bankItemsN[fromSlot] -= amount;
					resetBank();
					c.getItems().resetItems(5064);
				  }
				} else {
				  if (addItem((c.bankItems[fromSlot]-1), c.bankItemsN[fromSlot])) {
					c.bankItems[fromSlot] = 0;
					c.bankItemsN[fromSlot] = 0;
					resetBank();
					c.getItems().resetItems(5064);
				  }
				}
			  } else {
				while (amount > 0) {
				  if (c.bankItemsN[fromSlot] > 0) {
					if (addItem((c.bankItems[fromSlot]-1), 1)) {
					  c.bankItemsN[fromSlot] += -1;
					  amount--;
					} else {
					  amount = 0;
					}
				  } else {
					amount = 0;
				  }
				}
				resetBank();
				c.getItems().resetItems(5064);
			  }
			}
		  }
		}
	}

  	public int itemAmount(int itemID){
		int tempAmount=0;
        for (int i=0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				tempAmount+=c.playerItemsN[i];
			}
		}
		return tempAmount;
	}
	
	public boolean isStackable(int itemID) {	
		return Item.itemStackable[itemID];
	}
	
	
	
	/**
	*Update Equip tab
	**/

	
	public void setEquipment(int wearID, int amount, int targetSlot) {
		synchronized(c) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID+1);
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(amount);
			} else {
				c.getOutStream().writeByte(amount);	
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipment[targetSlot]=wearID;
			c.playerEquipmentN[targetSlot]=amount;
			c.updateRequired = true; 
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	
	/**
	*Move Items
	**/
	
	public void moveItems(int from, int to, int moveWindow) {
		if (moveWindow == 3724) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];

			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
		}

		if (moveWindow == 34453 && from >= 0 && to >= 0 && from < Config.BANK_SIZE && to < Config.BANK_SIZE && to < Config.BANK_SIZE) {
			int tempI;
			int tempN;
			tempI = c.bankItems[from];
			tempN = c.bankItemsN[from];

			c.bankItems[from] = c.bankItems[to];
			c.bankItemsN[from] = c.bankItemsN[to];
			c.bankItems[to] = tempI;
			c.bankItemsN[to] = tempN;
		}

		if (moveWindow == 34453) {
			resetBank();
		}
		if (moveWindow == 18579) {
			int tempI;
			int tempN;
			tempI = c.playerItems[from];
			tempN = c.playerItemsN[from];

			c.playerItems[from] = c.playerItems[to];
			c.playerItemsN[from] = c.playerItemsN[to];
			c.playerItems[to] = tempI;
			c.playerItemsN[to] = tempN;
			resetItems(3214);
		}
			resetTempItems();
		if (moveWindow == 3724) {
			resetItems(3214);
		}

	}
	
	/**
	*delete Item
	**/
	
	public void deleteEquipment(int i, int j) {
		synchronized(c) {
			if(Server.playerHandler.players[c.playerId] == null) {
				return;
			}
			if(i < 0) {
				return;
			}
			
			c.playerEquipment[j] = -1;
			c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
			c.getOutStream().createFrame(34);
			c.getOutStream().writeWord(6);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(j);
			c.getOutStream().writeWord(0);
			c.getOutStream().writeByte(0);
			getBonus();
			if(j == c.playerWeapon) {
			 sendWeapon(-1, "Unarmed");
			}
			resetBonus();
			getBonus();
			writeBonus();
			c.updateRequired = true; 
			c.setAppearanceUpdateRequired(true);		
		}			
   	}
	
	public void deleteItem(int id, int amount) {
		if(id <= 0)
			return;
		for (int j = 0; j < c.playerItems.length; j++) {
			if (amount <= 0)
				break;
			if (c.playerItems[j] == id+1) {
				c.playerItems[j] = 0;
				c.playerItemsN[j] = 0;
				amount--;			
			}	
		}
		resetItems(3214);
	}
	
	public void deleteItem(int id, int slot, int amount) {
		if(id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id+1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}
	public void deleteItem2(int id, int amount)	{
		int am = amount;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (c.playerItems[i] == (id+1))	{
				if (c.playerItemsN[i] > amount)	{
					c.playerItemsN[i] -= amount;
					break;
				}
				else {
					c.playerItems[i] = 0;
					c.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}
	
	/**
	* Delete Arrows
	**/
	public void deleteArrow() {
		synchronized(c) {
			if (c.playerEquipment[c.playerCape] == 10499 && Misc.random(5) != 1 && c.playerEquipment[c.playerArrows] != 4740)
				return;
			if(c.playerEquipmentN[c.playerArrows] == 1) {
				c.getItems().deleteEquipment(c.playerEquipment[c.playerArrows], c.playerArrows);
			}
			if(c.playerEquipmentN[c.playerArrows] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.playerArrows);
				c.getOutStream().writeWord(c.playerEquipment[c.playerArrows]+1);
				if (c.playerEquipmentN[c.playerArrows] -1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[c.playerArrows] -1);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[c.playerArrows] -1); 
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipmentN[c.playerArrows] -= 1;
			}  
			c.updateRequired = true; 
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	public void deleteEquipment() {
		synchronized(c) {
			if(c.playerEquipmentN[c.playerWeapon] == 1) {
				c.getItems().deleteEquipment(c.playerEquipment[c.playerWeapon], c.playerWeapon);
			}
			if(c.playerEquipmentN[c.playerWeapon] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.playerWeapon);
				c.getOutStream().writeWord(c.playerEquipment[c.playerWeapon]+1);
				if (c.playerEquipmentN[c.playerWeapon] -1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[c.playerWeapon] -1);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[c.playerWeapon] -1); 
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipmentN[c.playerWeapon] -= 1;
			}  
			c.updateRequired = true; 
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	/**
	* Dropping Arrows
	**/
	
	
	public void dropArrowNpc() {
		if (c.playerEquipment[c.playerCape] == 10499)
			return;
		int enemyX = Server.npcHandler.npcs[c.oldNpcIndex].getX();
		int enemyY = Server.npcHandler.npcs[c.oldNpcIndex].getY();
		if(Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, amount+1, c.getId());
			}		
		}
	}	
	
	public void dropArrowPlayer() {
		int enemyX = Server.playerHandler.players[c.oldPlayerIndex].getX();
		int enemyY = Server.playerHandler.players[c.oldPlayerIndex].getY();
		if (c.playerEquipment[c.playerCape] == 10499)
			return;
		if(Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX, enemyY, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX, enemyY, amount+1, c.getId());
			}		
		}
	}
	
	
	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}
	
	public int freeSlots(){
		int freeS = 0;
        for (int i=0; i < c.playerItems.length; i++){
			if (c.playerItems[i] <= 0){
				freeS++;
			}
		}
		return freeS;
	}
	
	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}
	
	public String getItemName(int ItemID) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					return Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		return "Unarmed";
	}
	
	public int getItemId(String itemName) {
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName.equalsIgnoreCase(itemName)) {
					return Server.itemHandler.ItemList[i].itemId;
				}
			}
		}
		return -1;
	}
	
	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if((c.playerItems[i] - 1) == ItemID){
				return i;
			}
		}	
		return -1;
	}
	
	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if((c.playerItems[i] - 1) == ItemID) {
				itemCount += c.playerItemsN[i];
			}
		}
		return itemCount;
	}
	
	
	public boolean playerHasItem(int itemID, int amt, int slot) {
	    itemID++;
	    int found = 0;
		if (c.playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.playerItems.length; i++)  {
				if (c.playerItems[i] == itemID)  {
					if(c.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
            	}
        	}
			if(found >= amt) {
				return true;
			}
        	return false;
		}
		return false;
	}
	
	public boolean playerHasItem(int itemID) {
	    itemID++;
			for (int i = 0; i < c.playerItems.length; i++)  {
				if (c.playerItems[i] == itemID)
					return true;
        	}
		return false;
	}	
	
	
	public boolean playerHasItem(int itemID, int amt) {
	    itemID++;
	    int found = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
            if (c.playerItems[i] == itemID) {
		    	if(c.playerItemsN[i] >= amt){
					return true;
				} else{
			    	found++;
				}
            }
        }
			if(found >= amt) {
				return true;
			}
        	return false;
	}
	
	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					NotedName = Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		for (int i = 0; i < Config.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName == NotedName) {
					if (Server.itemHandler.ItemList[i].itemDescription.startsWith("Swap this note at any bank for a") == false) {
						NewID = Server.itemHandler.ItemList[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}
	
	
	/**
	*Drop Item
	**/
	
	public void createGroundItem(int itemID, int itemX, int itemY, int itemAmount) {
		synchronized(c) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(44);
			c.getOutStream().writeWordBigEndianA(itemID);
			c.getOutStream().writeWord(itemAmount);
			c.getOutStream().writeByte(0);	
			c.flushOutStream();
		}
	}
	
	/**
	*Pickup Item
	**/
	
	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		synchronized(c) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(156);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWord(itemID);
			c.flushOutStream();
		}
	}
	
	public boolean ownsCape() {
		if (c.getItems().playerHasItem(2412,1) || c.getItems().playerHasItem(2413,1) || c.getItems().playerHasItem(2414,1))
			return true;
		for (int j = 0; j < Config.BANK_SIZE; j++) {
			if (c.bankItems[j] == 2412 || c.bankItems[j] == 2413 || c.bankItems[j] == 2414)
				return true;		
		}
		if (c.playerEquipment[c.playerCape] == 2413 || c.playerEquipment[c.playerCape] == 2414 || c.playerEquipment[c.playerCape] == 2415)
			return true;
		return false;	
	}
	
	public boolean hasAllShards() { 
		return playerHasItem(11712,1) && playerHasItem(11712,1) && playerHasItem(11714,1);
	}
	
	public void makeBlade() {
		deleteItem(11710,1);
		deleteItem(11712,1);
		deleteItem(11714,1);
		addItem(11690,1);
		c.sendMessage("You combine the shards to make a blade.");
	}
	
	public void makeGodsword(int i) {
		int godsword = i - 8;
		if (playerHasItem(11690) && playerHasItem(i)) {
			deleteItem(11690,1);
			deleteItem(i,1);
			addItem(i - 8, 1);
			c.sendMessage("You combine the hilt and the blade to make a godsword.");
		}	
	}
	
	public boolean isHilt(int i) {
		return i >= 11702 && i <= 11708 && i%2 == 0;
	}
	

}