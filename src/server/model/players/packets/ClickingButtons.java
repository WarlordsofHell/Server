package server.model.players.packets;

import server.Config;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.items.GameItem;
import server.model.players.Client;
import server.model.players.SkillMenu;
import server.model.players.PacketType;
import server.util.Misc;

/**
 * Clicking most buttons
 **/
public class ClickingButtons implements PacketType {

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);
		//int actionButtonId = c.getInStream().readShort();
		if (c.isDead)
			return;
		c.getPA().switchCombatType(actionButtonId);
		c.curses().curseButtons(actionButtonId);
		if(c.playerRights == 3)	
			c.sendMessage("@blu@Actionbutton: " + actionButtonId + " Fight mode: " + c.fightMode + " Dialogue action: " + c.dialogueAction);
			int[] spellIds = {4128,4130,4132,4134,4136,4139,4142,4145,4148,4151,4153,4157,4159,4161,4164,4165,4129,4133,4137,6006,6007,6026,6036,6046,6056,
			4147,6003,47005,4166,4167,4168,48157,50193,50187,50101,50061,50163,50211,50119,50081,50151,50199,50111,50071,50175,50223,50129,50091};
			for(int i = 0; i < spellIds.length; i++) {
				if(actionButtonId == spellIds[i]) {
					c.autocasting = (c.autocastId != i) ? true : false;
					if (!c.autocasting) {
						c.getPA().resetAutocast();
					} else {
						c.autocastId = i;
					}
				}
			}
		switch (actionButtonId){
			
			/**
			* Summoning Action Buttons 
			* @author Banter
			**/
			/*
			//Handling Spec
			case 66122:
			case 66117:
			case 66119:
				c.handleSumSpec();
			break;
			
			//Calling Familiar
			case 66126:
				if (c.hasFollower > 0) {
					c.callFamiliar = true;
					c.sendMessage("Your familiar has been called.");
					}
				if (c.hasFollower <= 0) {
					c.sendMessage("You don't have a familiar to call!");
					}
			break;
			
			//Dismiss Familiar
			case 66127:
			break;
			
			/**
			* End of Summoning
			**/

			case 150:
				c.autoRet = (c.autoRet == 0) ? 1 : 0;
			break;
			case 82020:
				for(int i = 0; i < c.playerItems.length; i++) {
					c.getItems().bankItem(c.playerItems[i], i,c.playerItemsN[i]);
				}
                        break;
                            					case 82024:
c.getItems().bankEquipment();
				break;

		case 82016:
			c.takeAsNote = c.takeAsNote ? false : true;
		break;
			case 9190: // 1st of 5
                            if (c.dialogueAction == 106) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15086, 1);
						c.sendMessage("You get a six-sided die out of the dice bag.");
					}
					c.getPA().closeAllWindows();
				} else if (c.dialogueAction == 107) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15092, 1);
						c.sendMessage("You get a ten-sided die out of the dice bag.");
					}
					c.getPA().closeAllWindows();
				}
				if (c.getPA().dialogueAction(9)) {
				c.getPA().movePlayer(2670, 3712, 0);
					c.getPA().closeAllWindows();
				} else if (c.getPA().dialogueAction(8)) {
					c.getPA().movePlayer(3369, 3268, 0);
					c.getPA().closeAllWindows();
				}
				break;
			case 9191: // 2nd of 5
                            if (c.dialogueAction == 106) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15088, 1);
						c.sendMessage("You get two six-sided dice out of the dice bag.");
					}
					c.getPA().closeAllWindows();
				} else if (c.dialogueAction == 107) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15094, 1);
						c.sendMessage("You get a twelve-sided die out of the dice bag.");
					}
					c.getPA().closeAllWindows();
				}
				if (c.getPA().dialogueAction(9)) {
					c.getPA().movePlayer(3555, 9943, 0);
					c.getPA().closeAllWindows();
				} else if (c.getPA().dialogueAction(12)) {
					c.getPA().movePlayer(1, 1, 0);
					c.getPA().closeAllWindows();
				}
				break;
			case 9192: // 3rd of 5
                            if (c.dialogueAction == 106) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15100, 1);
						c.sendMessage("You get a four-sided die out of the dice bag.");
					}
					c.getPA().closeAllWindows();
				} else if (c.dialogueAction == 107) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15096, 1);
						c.sendMessage("You get a twenty-sided die out of the dice bag.");
				}
					c.getPA().closeAllWindows();
				}
				if (c.getPA().dialogueAction(9)) {
					c.getPA().movePlayer(3114, 5528, 0);
					c.getPA().closeAllWindows();
				}
				break;
			case 9193: // 4th of 5
                            if (c.dialogueAction == 106) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15090, 1);
						c.sendMessage("You get an eight-sided die out of the dice bag.");
					}
					c.getPA().closeAllWindows();
				} else if (c.dialogueAction == 107) {
					if(c.getItems().playerHasItem(c.diceID, 1)) {
						c.getItems().deleteItem(c.diceID, c.getItems().getItemSlot(c.diceID), 1);	
						c.getItems().addItem(15098, 1);
						c.sendMessage("You get the percentile dice out of the dice bag.");
				}
					c.getPA().closeAllWindows();
				}
				if (c.getPA().dialogueAction(9)) {
					//c.getPA().movePlayer(2522, 4642, 0);
					c.sendMessage("Coming soon.");
					c.getPA().closeAllWindows();
				}
				break;
			case 9194: // 5th of 5
                            if (c.dialogueAction == 106) {
				c.getDH().sendDialogues(107, 0);
				break;
				} else if (c.dialogueAction == 107) {
				c.getDH().sendDialogues(106, 0);
				break;
				}if (c.dialogueAction == 106) {
				c.getDH().sendDialogues(107, 0);
				break;
				} else if (c.dialogueAction == 107) {
				c.getDH().sendDialogues(106, 0);
				break;
				}
				if (c.getPA().dialogueAction(9)) {
					c.getDH().sendDialogues(32, 3792);
				}
				break;
			
			case 71074:
				if (c.clanId >= 0) {
					if (Server.clanChat.isOwner(c)) {
						Server.clanChat.sendLootShareMessage(c.clanId, "Lootshare has been toggled to " + (!Server.clanChat.clans[c.clanId].lootshare ? "on" : "off") + " by the clan leader.");
						Server.clanChat.clans[c.clanId].lootshare = !Server.clanChat.clans[c.clanId].lootshare;
					} else
						c.sendMessage("Only the owner of the clan has the power to do that.");
				}	
			break;
			case 34185: case 34184: case 34183: case 34182: case 34189: case 34188: case 34187: case 34186: case 34193: case 34192: case 34191: case 34190:
				if (c.craftingLeather)
					c.getCrafting().handleCraftingClick(actionButtonId);
				if (c.getFletching().fletching)
					c.getFletching().handleFletchingClick(actionButtonId);
			break;
			
			case 15147:
				if (c.smeltInterface) {
					c.smeltType = 2349;
					c.smeltAmount = 1;
					c.getSmithing().startSmelting(c.smeltType);
				}
			break;
			
			case 15151:
				if (c.smeltInterface) {
					c.smeltType = 2351;
					c.smeltAmount = 1;
					c.getSmithing().startSmelting(c.smeltType);
				}
			break;
			
			
			case 15159:
				if (c.smeltInterface) {
					c.smeltType = 2353;
					c.smeltAmount = 1;
					c.getSmithing().startSmelting(c.smeltType);
				}
			break;
			
			
			case 29017:
				if (c.smeltInterface) {
					c.smeltType = 2359;
					c.smeltAmount = 1;
					c.getSmithing().startSmelting(c.smeltType);
				}
			break;
			
			case 29022:
				if (c.smeltInterface) {
					c.smeltType = 2361;
					c.smeltAmount = 1;
					c.getSmithing().startSmelting(c.smeltType);
				}
			break;
			
			case 29026:
				if (c.smeltInterface) {
					c.smeltType = 2363;
					c.smeltAmount = 1;
					c.getSmithing().startSmelting(c.smeltType);
				}
			break;
			case 58253:
			//c.getPA().showInterface(15106);
			c.getItems().writeBonus();
			break;
			
			case 59004:
			c.getPA().removeAllWindows();
			break;
			
			case 70212:
				if (c.clanId > -1)
					Server.clanChat.leaveClan(c.playerId, c.clanId);
				else
					c.sendMessage("You are not in a clan.");
			break;
			case 62137:
				if (c.clanId >= 0) {
					c.sendMessage("You are already in a clan.");
					break;
				}
				if (c.getOutStream() != null) {
					c.getOutStream().createFrame(187);
					c.flushOutStream();
				}	
			break;
			
			case 9178: 
				if (c.usingGlory)
					c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, "glory");
				else if(c.getPA().dialogueAction(0)) 
					c.getShops().openShop(3);
				else if (c.getPA().dialogueAction(1)) 
					c.getPA().specialTeleport(3370, 3699, 0, 2);
				else if (c.getPA().dialogueAction(4)) 
					//c.getPA().startTeleport(3565,3308, 0, "modern");
					c.getDH().sendDialogues(27, 3792);
				else if (c.getPA().dialogueAction(2)) 
					c.getShops().openShop(6);
				else if (c.getPA().dialogueAction(12)) 
					c.getPA().startTeleport(1, 1, 0, "modern");//Tormented Demons
				else if (c.getPA().dialogueAction(5)) 
					c.getPA().startTeleport(2662, 2650, 0, "modern");
				else if (c.getPA().dialogueAction(7)) 
					c.getPA().startTeleport(3565, 3308, 0, "modern");
				else if (c.getPA().dialogueAction(6)) {
					c.setSidebarInterface(6, 1151);
					c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
					c.autocastId = -1;
					c.getPA().resetAutocast();
					c.playerMagicBook = 0;
					c.getPA().removeAllWindows();
				}
			break;
			
			case 9179:
				if (c.usingGlory)
					c.getPA().startTeleport(Config.AL_KHARID_X, Config.AL_KHARID_Y, 0, "glory");
				else if(c.getPA().dialogueAction(0)) 
					c.getDH().sendDialogues(24,8725);
				else if (c.getPA().dialogueAction(1)) 
					c.getPA().specialTeleport(2983, 3598, 0, 2);
				else if (c.getPA().dialogueAction(2)) 
					c.getShops().openShop(5);
				else if (c.getPA().dialogueAction(4)) 
					c.getDH().sendDialogues(29, 3792);
				else if (c.getPA().dialogueAction(12)) 
					c.getPA().startTeleport(2309, 5239, 0, "modern");
				else if (c.getPA().dialogueAction(7)) 
					c.getPA().startTeleport(2438, 5171, 0, "modern");
				else if (c.getPA().dialogueAction(5)) 
					c.getPA().startTeleport(3366, 3266, 0, "modern");
				else if (c.getPA().dialogueAction(6)) {
					c.setSidebarInterface(6, 12855);
					c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
					c.autocastId = -1;
					c.playerMagicBook = 1;
					c.getPA().removeAllWindows();
				}
			break;
			
			case 9180:
				if (c.usingGlory)
					c.getPA().startTeleport(Config.KARAMJA_X, Config.KARAMJA_Y, 0, "glory");
				else if(c.getPA().dialogueAction(0)) 
					c.getDH().sendDialogues(25,8725);
				else if (c.getPA().dialogueAction(1))
					c.getPA().specialTeleport(2980, 3857, 0, 2);
				else if (c.getPA().dialogueAction(2)) 
					c.getShops().openShop(8);
				else if (c.getPA().dialogueAction(4)) 
					c.getDH().sendDialogues(30, 3792);
					//c.getPA().startTeleport(2441, 5171, 0, "modern");
				else if (c.getPA().dialogueAction(12)) 
					c.getPA().startTeleport(1, 1, 0, "modern");//Chaos Tunnels
				else if (c.getPA().dialogueAction(5)) 
					c.getPA().startTeleport(1, 1, 0, "modern");
				else if (c.getPA().dialogueAction(7)) 
					c.getPA().movePlayer(2533, 3569, 0);
				else if (c.getPA().dialogueAction(6)) {
					c.setSidebarInterface(6, 29999);
					c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
					c.autocastId = -1;
					c.getPA().resetAutocast();
					c.playerMagicBook = 2;
					c.getPA().removeAllWindows();
				}
			break;
			
			case 9181:
				if (c.usingGlory)
					c.getPA().startTeleport(Config.MAGEBANK_X, Config.MAGEBANK_Y, 0, "glory");
				else if(c.getPA().dialogueAction(0)) 
					c.getPA().removeAllWindows();
				else if (c.getPA().dialogueAction(1)) 
					c.getPA().specialTeleport(Config.MAGEBANK_X, Config.MAGEBANK_Y, 0, 2);
				else if (c.getPA().dialogueAction(2)) 
					c.getShops().openShop(7);
				else if (c.getPA().dialogueAction(4))
					c.getDH().sendDialogues(31, 3792);				
					//c.getDH().sendDialogues(23, 3792);
				else if (c.getPA().dialogueAction(12)) 
					c.getDH().sendDialogues(29, 0);
				else if (c.getPA().dialogueAction(5)) 
					c.getPA().startTeleport(1, 1, 0, "modern");
				else if (c.getPA().dialogueAction(7)) 
					c.getDH().sendDialogues(28, 0);
				else if (c.getPA().dialogueAction(6)) 
					c.getPA().removeAllWindows();
			break;
			
			case 1093:
			case 1094:
			case 1097:
				if (c.autocastId > 0) {
					c.getPA().resetAutocast();
				} else {
					if (c.playerMagicBook == 1) {
						if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 15486)
							c.setSidebarInterface(0, 1689);
						else
							c.sendMessage("You can't autocast ancients without an ancient staff.");
					} else if (c.playerMagicBook == 0) {
						if (c.playerEquipment[c.playerWeapon] == 4170) {
							c.setSidebarInterface(0, 12050);
						} else {
							c.setSidebarInterface(0, 1829);
						}	
					}
						
				}		
			break;
			
			case 9157:
				if(c.dialogueAction == 13) {
					int r = 4;
					//int r = Misc.random(3);
					switch(r) {
						case 0:
							c.getPA().movePlayer(3534, 9677, 0);
							break;
						
						case 1:
							c.getPA().movePlayer(3534, 9712, 0);
							break;
						
						case 2:
							c.getPA().movePlayer(3568, 9712, 0);
							break;
						
						case 3:
							c.getPA().movePlayer(3568, 9677, 0);
							break;
						case 4:
							c.getPA().movePlayer(3551, 9694, 0);
							break;
					}
					c.getPA().removeAllWindows();
				} else if(c.getPA().dialogueAction(0)) 
					//Reserved	*c.getShops().openShop(3);
					c.sendMessage("Debug");
				else {
				c.dialogueAction = 0;
				c.getPA().removeAllWindows();
				}
				break;
			
			case 9158:
				if(c.getPA().dialogueAction(0))
					c.getShops().openShop(4);
				else {
				c.dialogueAction = 0;
				c.getPA().removeAllWindows();
				}
				break;
			
			/**Specials**/
			case 29188:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E C I A L  A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 29038:
			c.specBarId = 7486;
			if (c.playerEquipment[c.playerWeapon] == 4153 || c.playerEquipment[c.playerWeapon] == 7668) 
				c.getCombat().handleGmaulPlayer();
			else 
				c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 29063:
			if(c.getCombat().checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
				c.gfx0(246);
				c.forcedChat("Raarrrrrgggggghhhhhhh!");
				c.startAnimation(1056);
				c.playerLevel[2] = c.getLevelForXP(c.playerXP[2]) + (c.getLevelForXP(c.playerXP[2]) * 15 / 100);
				c.getPA().refreshSkill(2);
				c.getItems().updateSpecialBar();
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;
			
			case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			case 30108: // Claws
			c.specBarId = 7812;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;
			
			/**Dueling**/			
			case 26065: // no forfeit
			case 26040:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(0);
			break;
			
			case 26066: // no movement
			case 26048:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(1);
			break;
			
			case 26069: // no range
			case 26042:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(2);
			break;
			
			case 26070: // no melee
			case 26043:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(3);
			break;				
			
			case 26071: // no mage
			case 26041:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(4);
			break;
				
			case 26072: // no drinks
			case 26045:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(5);
			break;
			
			case 26073: // no food
			case 26046:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(6);
			break;
			
			case 26074: // no prayer
			case 26047:	
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(7);
			break;
			
			case 26076: // obsticals
			case 26075:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(8);
			break;
			
			case 2158: // fun weapons
			case 2157:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(9);
			break;
			
			case 30136: // sp attack
			case 30137:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(10);
			break;	

			case 53245: //no helm
			c.duelSlot = 0;
			c.getTradeAndDuel().selectRule(11);
			break;
			
			case 53246: // no cape
			c.duelSlot = 1;
			c.getTradeAndDuel().selectRule(12);
			break;
			
			case 53247: // no ammy
			c.duelSlot = 2;
			c.getTradeAndDuel().selectRule(13);
			break;
			
			case 53249: // no weapon.
			c.duelSlot = 3;
			c.getTradeAndDuel().selectRule(14);
			break;
			
			case 53250: // no body
			c.duelSlot = 4;
			c.getTradeAndDuel().selectRule(15);
			break;
			
			case 53251: // no shield
			c.duelSlot = 5;
			c.getTradeAndDuel().selectRule(16);
			break;
			
			case 53252: // no legs
			c.duelSlot = 7;
			c.getTradeAndDuel().selectRule(17);
			break;
			
			case 53255: // no gloves
			c.duelSlot = 9;
			c.getTradeAndDuel().selectRule(18);
			break;
			
			case 53254: // no boots
			c.duelSlot = 10;
			c.getTradeAndDuel().selectRule(19);
			break;
			
			case 53253: // no rings
			c.duelSlot = 12;
			c.getTradeAndDuel().selectRule(20);
			break;
			
			case 53248: // no arrows
			c.duelSlot = 13;
			c.getTradeAndDuel().selectRule(21);
			break;
			
			
			case 26018:	
			Client o = (Client) Server.playerHandler.players[c.duelingWith];
			if(o == null) {
				c.getTradeAndDuel().declineDuel();
				return;
			}
			
			if(c.duelRule[2] && c.duelRule[3] && c.duelRule[4]) {
				c.sendMessage("You won't be able to attack the player with the rules you have set.");
				break;
			}
			c.duelStatus = 2;
			if(c.duelStatus == 2) {
				c.getPA().sendFrame126("Waiting for other player...", 6684);
				o.getPA().sendFrame126("Other player has accepted.", 6684);
			}
			if(o.duelStatus == 2) {
				o.getPA().sendFrame126("Waiting for other player...", 6684);
				c.getPA().sendFrame126("Other player has accepted.", 6684);
			}
			
			if(c.duelStatus == 2 && o.duelStatus == 2) {
				c.canOffer = false;
				o.canOffer = false;
				c.duelStatus = 3;
				o.duelStatus = 3;
				c.getTradeAndDuel().confirmDuel();
				o.getTradeAndDuel().confirmDuel();
			}
			break;
			
			case 25120:
			if(c.duelStatus == 5) {
				break;
			}
			Client o1 = (Client) Server.playerHandler.players[c.duelingWith];
			if(o1 == null) {
				c.getTradeAndDuel().declineDuel();
				return;
			}

			c.duelStatus = 4;
			if(o1.duelStatus == 4 && c.duelStatus == 4) {				
				c.getTradeAndDuel().startDuel();
				o1.getTradeAndDuel().startDuel();
				o1.duelCount = 4;
				c.duelCount = 4;
                                				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(System.currentTimeMillis() - c.duelDelay > 800 && c.duelCount > 0) {
							if(c.duelCount != 1) {
								c.forcedChat(""+(--c.duelCount));
								c.duelDelay = System.currentTimeMillis();
							} else {
								c.damageTaken = new int[Config.MAX_PLAYERS];
								c.forcedChat("FIGHT!");
								c.duelCount = 0;
							}
						}
						if (c.duelCount == 0) {
							container.stop();
						}
					}
					@Override
					public void stop() {
					}
				}, 1);
				c.duelDelay = System.currentTimeMillis();
				o1.duelDelay = System.currentTimeMillis();
			} else {
				c.getPA().sendFrame126("Waiting for other player...", 6571);
				o1.getPA().sendFrame126("Other player has accepted", 6571);
			}
			break;
	
			
			case 4169: // god spell charge
			c.usingMagic = true;
			if(!c.getCombat().checkMagicReqs(48)) {
				break;
			}
				
			if(System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay	= System.currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(c.MAGIC_SPELLS[48][3]);
			c.startAnimation(c.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
	        break;
			
			
			case 28164: // item kept on death 
			break;
			
			case 152:
			c.isRunning2 = !c.isRunning2;
			int frame = c.isRunning2 == true ? 1 : 0;
			c.getPA().sendFrame36(173, frame);
			break;
			
			case 9154:
			c.logout();
			break;
			
			case 21010:
			c.takeAsNote = true;
			break;

			case 21011:
			c.takeAsNote = false;
			break;
			
			
			case 4171:
			case 50056: 
			case 117048: // Home teleport
			//if (c.underAttackBy > 0) {
			//c.sendMessage("You cannot not use this teleport in combat.");
			//return;
			//} else {
			String type = c.playerMagicBook != 1 ? "modern" : "ancient";
			c.getPA().startTeleport(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0, type);	
			//}
			break;
			
			case 50235:
			case 4140: // Varrock
			c.teleAction = 1;
			break;
			
			case 4143:
			case 50245: 
			c.teleAction = 2;
			break;
			
			case 50253:
			case 4146:
			c.teleAction = 3;
			break;
			

			case 51005:
			case 4150:
			c.teleAction = 4;
			break;			
			
			case 51013:
			case 6004:
			c.teleAction = 5;
			break; 
			
			
			case 51023:
			case 6005:
			break; 
			
			
			case 51031:
			case 29031:
			break; 		

			case 72038:
			case 51039:
			break;
			
			/**Prayers**/
			case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;	
			case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			break;	
			case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			break;	
			case 70080: // range
			c.getCombat().activatePrayer(3);
			break;
			case 70082: // mage
			c.getCombat().activatePrayer(4);
			break;
			case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			break;
			case 21237: // super human
			c.getCombat().activatePrayer(6);
			break;
			case 21238:	// improved reflexes
			c.getCombat().activatePrayer(7);
			break;
			case 21239: //hawk eye
			c.getCombat().activatePrayer(8);
			break;
			case 21240:
			c.getCombat().activatePrayer(9);
			break;
			case 21241: // protect Item
			c.getCombat().activatePrayer(10);
			break;			
			case 70084: // 26 range
			c.getCombat().activatePrayer(11);
			break;
			case 70086: // 27 mage
			c.getCombat().activatePrayer(12);
			break;	
			case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			break;
			case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			break;
			case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			break;	
			case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			break;					
			case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			break;
			case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			break;
			case 70088: // 44 range
			c.getCombat().activatePrayer(19);
			break;	
			case 70090: // 45 mystic
			c.getCombat().activatePrayer(20);
			break;				
			case 2171: // retrui
			c.getCombat().activatePrayer(21);
			break;					
			case 2172: // redem
			c.getCombat().activatePrayer(22);
			break;					
			case 2173: // smite
			c.getCombat().activatePrayer(23);
			break;
			case 70092: // chiv
			c.getCombat().activatePrayer(24);
			break;
			case 70094: // piety
			c.getCombat().activatePrayer(25);
			break;
			
			case 13092:
			Client ot = (Client) Server.playerHandler.players[c.tradeWith];
			if(ot == null) {
				c.getTradeAndDuel().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			c.getPA().sendFrame126("Waiting for other player...", 3431);
			ot.getPA().sendFrame126("Other player has accepted", 3431);	
			c.goodTrade= true;
			ot.goodTrade= true;
			
			for (GameItem item : c.getTradeAndDuel().offeredItems) {
				if (item.id > 0) {
					if(ot.getItems().freeSlots() < c.getTradeAndDuel().offeredItems.size()) {					
						c.sendMessage(ot.playerName +" only has "+ot.getItems().freeSlots()+" free slots, please remove "+(c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots())+" items.");
						ot.sendMessage(c.playerName +" has to remove "+(c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots())+" items or you could offer them "+(c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots())+" items.");
						c.goodTrade= false;
						ot.goodTrade= false;
						c.getPA().sendFrame126("Not enough inventory space...", 3431);
						ot.getPA().sendFrame126("Not enough inventory space...", 3431);
							break;
					} else {
						c.getPA().sendFrame126("Waiting for other player...", 3431);				
						ot.getPA().sendFrame126("Other player has accepted", 3431);
						c.goodTrade= true;
						ot.goodTrade= true;
						}
					}	
				}	
				if (c.inTrade && !c.tradeConfirmed && ot.goodTrade && c.goodTrade) {
					c.tradeConfirmed = true;
					if(ot.tradeConfirmed) {
						c.getTradeAndDuel().confirmScreen();
						ot.getTradeAndDuel().confirmScreen();
						break;
					}
							  
				}

		
			break;
					
			case 13218:
			c.tradeAccepted = true;
			Client ot1 = (Client) Server.playerHandler.players[c.tradeWith];
				if (ot1 == null) {
					c.getTradeAndDuel().declineTrade();
					c.sendMessage("Trade declined as the other player has disconnected.");
					break;
				}
				
				if (c.inTrade && c.tradeConfirmed && ot1.tradeConfirmed && !c.tradeConfirmed2) {
					c.tradeConfirmed2 = true;
					if(ot1.tradeConfirmed2) {	
						c.acceptedTrade = true;
						ot1.acceptedTrade = true;
						c.getTradeAndDuel().giveItems();
						ot1.getTradeAndDuel().giveItems();
						break;
					}
				ot1.getPA().sendFrame126("Other player has accepted.", 3535);
				c.getPA().sendFrame126("Waiting for other player...", 3535);
				}
				
			break;		
			/* Rules Interface Buttons */
			case 125011: //Click agree
				if(!c.ruleAgreeButton) {
					c.ruleAgreeButton = true;
					c.getPA().sendFrame36(701, 1);
				} else {
					c.ruleAgreeButton = false;
					c.getPA().sendFrame36(701, 0);
				}
				break;
			case 125003://Accept
				if(c.ruleAgreeButton) {
					c.getPA().showInterface(3559);
					c.newPlayer = false;
				} else if(!c.ruleAgreeButton) {
					c.sendMessage("You need to click on you agree before you can continue on.");
				}
				break;
			case 125006://Decline
				c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
				break;
			/* End Rules Interface Buttons */
			/* Player Options */
			case 74176:
				if(!c.mouseButton) {
					c.mouseButton = true;
					c.getPA().sendFrame36(500, 1);
					c.getPA().sendFrame36(170,1);
				} else if(c.mouseButton) {
					c.mouseButton = false;
					c.getPA().sendFrame36(500, 0);
					c.getPA().sendFrame36(170,0);					
				}
				break;
			case 74184:
				if(!c.splitChat) {
					c.splitChat = true;
					c.getPA().sendFrame36(502, 1);
					c.getPA().sendFrame36(287, 1);
				} else {
					c.splitChat = false;
					c.getPA().sendFrame36(502, 0);
					c.getPA().sendFrame36(287, 0);
				}
				break;
			case 74180:
				if(!c.chatEffects) {
					c.chatEffects = true;
					c.getPA().sendFrame36(501, 1);
					c.getPA().sendFrame36(171, 0);
				} else {
					c.chatEffects = false;
					c.getPA().sendFrame36(501, 0);
					c.getPA().sendFrame36(171, 1);
				}
				break;
			case 74188:
				if(!c.acceptAid) {
					c.acceptAid = true;
					c.getPA().sendFrame36(503, 1);
					c.getPA().sendFrame36(427, 1);
				} else {
					c.acceptAid = false;
					c.getPA().sendFrame36(503, 0);
					c.getPA().sendFrame36(427, 0);
				}
				break;
			case 74192:
				if(!c.isRunning2) {
					c.isRunning2 = true;
					c.getPA().sendFrame36(504, 1);
					c.getPA().sendFrame36(173, 1);
				} else {
					c.isRunning2 = false;
					c.getPA().sendFrame36(504, 0);
					c.getPA().sendFrame36(173, 0);
				}
				break;
			case 74201://brightness1
				c.getPA().sendFrame36(505, 1);
				c.getPA().sendFrame36(506, 0);
				c.getPA().sendFrame36(507, 0);
				c.getPA().sendFrame36(508, 0);
				c.getPA().sendFrame36(166, 1);
				break;
			case 74203://brightness2
				c.getPA().sendFrame36(505, 0);
				c.getPA().sendFrame36(506, 1);
				c.getPA().sendFrame36(507, 0);
				c.getPA().sendFrame36(508, 0);
				c.getPA().sendFrame36(166,2);
				break;

			case 74204://brightness3
				c.getPA().sendFrame36(505, 0);
				c.getPA().sendFrame36(506, 0);
				c.getPA().sendFrame36(507, 1);
				c.getPA().sendFrame36(508, 0);
				c.getPA().sendFrame36(166,3);
				break;

			case 74205://brightness4
				c.getPA().sendFrame36(505, 0);
				c.getPA().sendFrame36(506, 0);
				c.getPA().sendFrame36(507, 0);
				c.getPA().sendFrame36(508, 1);
				c.getPA().sendFrame36(166,4);
				break;
			case 74206://area1
				c.getPA().sendFrame36(509, 1);
				c.getPA().sendFrame36(510, 0);
				c.getPA().sendFrame36(511, 0);
				c.getPA().sendFrame36(512, 0);
				break;
			case 74207://area2
				c.getPA().sendFrame36(509, 0);
				c.getPA().sendFrame36(510, 1);
				c.getPA().sendFrame36(511, 0);
				c.getPA().sendFrame36(512, 0);
				break;
			case 74208://area3
				c.getPA().sendFrame36(509, 0);
				c.getPA().sendFrame36(510, 0);
				c.getPA().sendFrame36(511, 1);
				c.getPA().sendFrame36(512, 0);
				break;
			case 74209://area4
				c.getPA().sendFrame36(509, 0);
				c.getPA().sendFrame36(510, 0);
				c.getPA().sendFrame36(511, 0);
				c.getPA().sendFrame36(512, 1);
				break;
			case 168:
                c.startAnimation(855);
            break;
            case 169:
                c.startAnimation(856);
            break;
            case 162:
                c.startAnimation(857);
            break;
            case 164:
                c.startAnimation(858);
            break;
            case 165:
                c.startAnimation(859);
            break;
            case 161:
                c.startAnimation(860);
            break;
            case 170:
                c.startAnimation(861);
            break;
            case 171:
                c.startAnimation(862);
            break;
            case 163:
                c.startAnimation(863);
            break;
            case 167:
                c.startAnimation(864);
            break;
            case 172:
                c.startAnimation(865);
            break;
            case 166:
                c.startAnimation(866);
            break;
            case 52050:
                c.startAnimation(2105);
            break;
            case 52051:
                c.startAnimation(2106);
            break;
            case 52052:
                c.startAnimation(2107);
            break;
            case 52053:
                c.startAnimation(2108);
            break;
            case 52054:
                c.startAnimation(2109);
            break;
            case 52055:
                c.startAnimation(2110);
            break;
            case 52056:
                c.startAnimation(2111);
            break;
            case 52057:
                c.startAnimation(2112);
            break;
            case 52058:
                c.startAnimation(2113);
            break;
            case 43092:
                c.startAnimation(0x558);
            break;
            case 2155:
                c.startAnimation(0x46B);
            break;
            case 25103:
                c.startAnimation(0x46A);
            break;
            case 25106:
                c.startAnimation(0x469);
            break;
            case 2154:
                c.startAnimation(0x468);
            break;
            case 52071:
                c.startAnimation(0x84F);
            break;
            case 52072:
                c.startAnimation(0x850);
            break;
            case 59062:
                c.startAnimation(2836);
            break;
            case 72032:
                c.startAnimation(3544);
            break;
            case 72033:
                c.startAnimation(3543);
            break;
            case 72254:
                c.startAnimation(6111);
            break;
			case 118098: // Vengeance
				c.getPA().vengMe();
			break;			
			
			case 47130:
				c.forcedText = "I must slay another " + c.taskAmount + " " + Server.npcHandler.getNpcListName(c.slayerTask) + ".";
				c.forcedChatUpdateRequired = true;
				c.updateRequired = true;
			break;
			
			case 24017:
				c.getPA().resetAutocast();
				c.getItems().sendWeapon(c.playerEquipment[c.playerWeapon], c.getItems().getItemName(c.playerEquipment[c.playerWeapon]));
			break;
			
			case 55095:
				c.getPA().destroyItem(c.destroyItem);
			case 55096:
				c.getPA().closeAllWindows();
			break;
			case 154:
				handleSkillCape(c);
			break;
		}
		if (c.isAutoButton(actionButtonId))
			c.assignAutocast(actionButtonId);
	}
	
	public void handleSkillCape(Client c) { 
		if (c.playerEquipment[c.playerCape] == 9747 || c.playerEquipment[c.playerCape] == 9748) {
			c.startAnimation(4959);
			c.gfx0(823);
		} else if (c.playerEquipment[c.playerCape] == 9750 || c.playerEquipment[c.playerCape] == 9751) {
			c.startAnimation(4981);
			c.gfx0(828);
		} else if (c.playerEquipment[c.playerCape] == 9753 || c.playerEquipment[c.playerCape] == 9754) {
			c.startAnimation(4961);
			c.gfx0(824);
		} else if (c.playerEquipment[c.playerCape] == 9756 || c.playerEquipment[c.playerCape] == 9757) {
			c.startAnimation(4973);
			c.gfx0(832);
		} else if (c.playerEquipment[c.playerCape] == 9759 || c.playerEquipment[c.playerCape] == 9760) {
			c.startAnimation(4979);
			c.gfx0(829);
		} else if (c.playerEquipment[c.playerCape] == 9762 || c.playerEquipment[c.playerCape] == 9763) {
			c.startAnimation(4939);
			c.gfx0(813);
		} else if (c.playerEquipment[c.playerCape] == 9765 || c.playerEquipment[c.playerCape] == 9766) {
			c.startAnimation(4947);
			c.gfx0(817);
		} else if (c.playerEquipment[c.playerCape] == 9768 || c.playerEquipment[c.playerCape] == 9769) {
			c.startAnimation(4971);
			c.gfx0(833);
		} else if (c.playerEquipment[c.playerCape] == 9771 || c.playerEquipment[c.playerCape] == 9772) {
			c.startAnimation(4977);
			c.gfx0(830);
		} else if (c.playerEquipment[c.playerCape] == 9774 || c.playerEquipment[c.playerCape] == 9775) {
			c.startAnimation(4969);
			c.gfx0(835);
		} else if (c.playerEquipment[c.playerCape] == 9777 || c.playerEquipment[c.playerCape] == 9778) {
			c.startAnimation(4965);
			c.gfx0(826);
		} else if (c.playerEquipment[c.playerCape] == 9780 || c.playerEquipment[c.playerCape] == 9781) {
			c.startAnimation(4949);
			c.gfx0(818);
		} else if (c.playerEquipment[c.playerCape] == 9783 || c.playerEquipment[c.playerCape] == 9784) {
			c.startAnimation(4937);
			c.gfx0(812);
		} else if (c.playerEquipment[c.playerCape] == 9786 || c.playerEquipment[c.playerCape] == 9787) {
			c.startAnimation(4967);
			c.gfx0(827);
		} else if (c.playerEquipment[c.playerCape] == 9789 || c.playerEquipment[c.playerCape] == 9790) {
			c.startAnimation(4953);
			c.gfx0(820);
		} else if (c.playerEquipment[c.playerCape] == 9792 || c.playerEquipment[c.playerCape] == 9793) {
			c.startAnimation(4941);
			c.gfx0(814);
		} else if (c.playerEquipment[c.playerCape] == 9795 || c.playerEquipment[c.playerCape] == 9796) {
			c.startAnimation(4943);
			c.gfx0(815);
		} else if (c.playerEquipment[c.playerCape] == 9798 || c.playerEquipment[c.playerCape] == 9799) {
			c.startAnimation(4951);
			c.gfx0(819);
		} else if (c.playerEquipment[c.playerCape] == 9801 || c.playerEquipment[c.playerCape] == 9802) {
			c.startAnimation(4955);
			c.gfx0(821);
		} else if (c.playerEquipment[c.playerCape] == 9804 || c.playerEquipment[c.playerCape] == 9805) {
			c.startAnimation(4975);
			c.gfx0(831);
		} else if (c.playerEquipment[c.playerCape] == 9807 || c.playerEquipment[c.playerCape] == 9808) {
			c.startAnimation(4957);
			c.gfx0(822);
		} else if (c.playerEquipment[c.playerCape] == 9810 || c.playerEquipment[c.playerCape] == 9811) {
			c.startAnimation(4963);
			c.gfx0(825);
		} else if (c.playerEquipment[c.playerCape] == 9813) {
			c.startAnimation(4945);
			c.gfx0(816);
		} else if (c.playerEquipment[c.playerCape] == 9948 || c.playerEquipment[c.playerCape] == 9949) {
			c.startAnimation(5158);
			c.gfx0(907);
		} else {
			c.sendMessage("You need a skillcape to perform this emote.");
		}
	}
}
