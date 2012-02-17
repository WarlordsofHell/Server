package server.model.players;

import server.Config;
import server.Server;
import server.model.objects.Object;
import server.util.Misc;
import server.util.ScriptManager;
import server.model.items.Item;
import server.model.items.ItemAssistant;

public class ActionHandler {
	
	private Client c;
	
	public ActionHandler(Client Client) {
		this.c = Client;
	}
	
	
	public void firstClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		//c.sendMessage("Object type: " + objectType);
		handleDagganothObjects(objectType);
		switch(objectType) {
		case 10595:
			c.getPA().movePlayer(3056, 9562, 0);
		break;
		case 10596:
			c.getPA().movePlayer(3056, 9555, 0);
		break;
		case 10556:
		case 20226://new object real one :D
			Server.barbDefence.enter(c);
		break;
		case 8929: // Dagganoth entrance
			c.getPA().movePlayer(2442, 10146, 0);
		break;
		case 8966: // Dagganoth stairs
			c.getPA().movePlayer(2523, 3739, 0);
		break;
		case 1765:
			c.getPA().movePlayer(3067, 10256, 0);
		break;
		case 2882:
		case 2883:
			if (c.objectX == 3268) {
				if (c.absX < c.objectX) {
					c.getPA().walkTo(1,0);
				} else {
					c.getPA().walkTo(-1,0);
				}
			}
		break;
		case 272:
			c.getPA().movePlayer(c.absX, c.absY, 1);
		break;
		
		case 273:
			c.getPA().movePlayer(c.absX, c.absY, 0);
		break;
		case 245:
			c.getPA().movePlayer(c.absX, c.absY + 2, 2);
		break;
		case 246:
			c.getPA().movePlayer(c.absX, c.absY - 2, 1);
		break;
		case 1766:
			c.getPA().movePlayer(3016, 3849, 0);
		break;
		case 6552:
			//c.getPA().switchSpellBook();
			c.getDH().sendDialogues(26, 0);
		break;
		
		case 1816:
			c.getPA().startTeleport2(2271, 4680, 0);			
		break;
		case 1817:
			c.getPA().startTeleport(3067, 10253, 0, "modern");
		break;
		case 1814:
			//ardy lever
			c.getPA().startTeleport(3153, 3923, 0, "modern");
			c.sendMessage("Carefull you cant tele above 20 wilderness.");
		break;
		
		case 9356:
			c.getPA().enterCaves();//Fight caves fixed 100% due to kyle gus gus gus
			//c.sendMessage("Temporarily removed due to bugs.");
		break;
		case 1733:
			c.getPA().movePlayer(c.absX, c.absY + 6393, 0);
		break;
		case 117048:
			c.getPA().movePlayer(3087, 3500, 0);
		break;
		
		case 1734:
			c.getPA().movePlayer(c.absX, c.absY - 6396, 0);
		break;
		
		case 9357:
			c.getPA().resetTzhaar();
		break;
		
		case 8959:
			if (c.getX() == 2490 && (c.getY() == 10146 || c.getY() == 10148)) {
				if (c.getPA().checkForPlayer(2490, c.getY() == 10146 ? 10148 : 10146)) {
					new Object(6951, c.objectX, c.objectY, c.heightLevel, 1, 10, 8959, 15);	
				}			
			}
		break;
		
		case 2213:
		case 14367:
		case 11758:
		case 3193:
			c.getPA().openUpBank();
		break;
		
		case 2623:
			if (c.absX >= c.objectX)
				c.getPA().walkTo(-1,0);
			else
				c.getPA().walkTo(1,0);
		break;
		//pc boat
		case 14315:
			c.getPA().movePlayer(2661,2639,0);
		break;
		case 14314:
			c.getPA().movePlayer(2657,2639,0);
		break;
		
		case 1596:
		case 1597:
		if (c.getY() >= c.objectY)
			c.getPA().walkTo(0,-1);
		else
			c.getPA().walkTo(0,1);
		break;
		
		case 14235:
		case 14233:
			if (c.objectX == 2670)
				if (c.absX <= 2670)
					c.absX = 2671;
				else
					c.absX = 2670;
			if (c.objectX == 2643)
				if (c.absX >= 2643)
					c.absX = 2642;
				else
					c.absX = 2643;
			if (c.absX <= 2585)
				c.absY += 1;
			else c.absY -= 1;
			c.getPA().movePlayer(c.absX, c.absY, 0);
		break;
		
		case 14829: case 14830: case 14827: case 14828: case 14826: case 14831:
			//Server.objectHandler.startObelisk(objectType);
			Server.objectManager.startObelisk(objectType);
		break;
		
		case 9369:
			if (c.getY() > 5175)
				c.getPA().movePlayer(2399, 5175, 0);
			else
				c.getPA().movePlayer(2399, 5177, 0);
		break;
		
		case 9368:
			if (c.getY() < 5169) {
				Server.fightPits.removePlayerFromPits(c.playerId);
				c.getPA().movePlayer(2399, 5169, 0);
			}	
		break;
		case 4411:
		case 4415:
		case 4417:
		case 4418:
		case 4419:
		case 4420:
		case 4469:
		case 4470:
		case 4911:
		case 4912:
		case 1747:
		case 1757:
 			//Server.castleWars.handleObjects(c, objectType, obX, obY);
		break;
		
		
		
		case 2286:
		case 154:
		case 4058:
		case 2295:
		case 2285:
		case 2313:
		case 2312:
		case 2314:
			c.getAgility().handleGnomeCourse(objectType,obX,obY);
		break;
		
		//barrows
		//Chest
		case 10284:
			if(c.barrowsKillCount < 5) {
				c.sendMessage("You haven't killed all the brothers");
			}
			if(c.barrowsKillCount == 5 && c.barrowsNpcs[c.randomCoffin][1] == 1) {
				c.sendMessage("I have already summoned this npc.");
			}
			if(c.barrowsNpcs[c.randomCoffin][1] == 0 && c.barrowsKillCount >= 5) {
				Server.npcHandler.spawnNpc(c, c.barrowsNpcs[c.randomCoffin][0], 3551, 9694-1, 0, 0, 120, 30, 200, 200, true, true);
				c.barrowsNpcs[c.randomCoffin][1] = 1;
			}
			if((c.barrowsKillCount > 5 || c.barrowsNpcs[c.randomCoffin][1] == 2) && c.getItems().freeSlots() >= 2) {
				c.getPA().resetBarrows();
				//c.getItems().addItem(c.getPA().randomRunes(), Misc.random(150) + 100);
				//if (Misc.random(2) == 1)
					c.getItems().addItem(c.getPA().randomBarrows(), 1);
					c.getItems().addItem(c.getPA().randomBarrows(), 1);
				c.getPA().startTeleport(3564, 3288, 0, "modern");
			} else if(c.barrowsKillCount > 5 && c.getItems().freeSlots() <= 1) {
				c.sendMessage("You need at least 2 inventory slot opened.");
			}
			break;
		//doors
		case 6749:
			if(obX == 3562 && obY == 9678) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if(obX == 3558 && obY == 9677) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6730:
			if(obX == 3558 && obY == 9677) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if(obX == 3558 && obY == 9678) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;
		case 6727:
			if(obX == 3551 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6746:
			if(obX == 3552 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;
		case 6748:
			if(obX == 3545 && obY == 9678) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if(obX == 3541 && obY == 9677) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6729:
			if(obX == 3545 && obY == 9677){
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if(obX == 3541 && obY == 9678) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;
		case 6726:
			if(obX == 3534 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if(obX == 3535 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6745:
			if(obX == 3535 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if(obX == 3534 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;
		case 6743:
			if(obX == 3545 && obY == 9695) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if(obX == 3541 && obY == 9694) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;
		case 6724:
			if(obX == 3545 && obY == 9694) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if(obX == 3541 && obY == 9695) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break; 
		//end doors
		//coffins
		case 6707: // verac
			c.getPA().movePlayer(3556, 3298, 0);
			break;
			
		case 6823:
			if(server.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if(c.barrowsNpcs[0][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2030, c.getX(), c.getY()-1, -1, 0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6706: // torag 
			c.getPA().movePlayer(3553, 3283, 0);
			break;
			
		case 6772:
			if(server.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if(c.barrowsNpcs[1][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2029, c.getX()+1, c.getY(), -1, 0, 120, 20, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
			
		case 6705: // karil stairs
			c.getPA().movePlayer(3565, 3276, 0);
			break;
		case 6822:
			if(server.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if(c.barrowsNpcs[2][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2028, c.getX(), c.getY()-1, -1, 0, 90, 17, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 6704: // guthan stairs
			c.getPA().movePlayer(3578, 3284, 0);
			break;
		case 6773:
			if(server.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if(c.barrowsNpcs[3][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2027, c.getX(), c.getY()-1, -1, 0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 6703: // dharok stairs
			c.getPA().movePlayer(3574, 3298, 0);
			break;
		case 6771:
			if(server.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if(c.barrowsNpcs[4][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2026, c.getX(), c.getY()-1, -1, 0, 120, 45, 250, 250, true, true);
				c.barrowsNpcs[4][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 6702: // ahrim stairs
			c.getPA().movePlayer(3565, 3290, 0);
			break;
		case 6821:
			if(server.model.minigames.Barrows.selectCoffin(c, objectType)) {
				return;
			}
			if(c.barrowsNpcs[5][1] == 0) {
				Server.npcHandler.spawnNpc(c, 2025, c.getX(), c.getY()-1, -1, 0, 90, 19, 200, 200, true, true);
				c.barrowsNpcs[5][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 1276:
		case 1278://trees
			//c.sendMessage("You chop the tree.");
			/* Woodcutting object respawning etc. by lmtruck... making my own - look at this for example
			Objects stump = new Objects(1343, c.objectX, c.objectY, 0, -1, 10, 0);
			Server.objectHandler.addObject(stump);
			Server.objectHandler.placeObject(stump);
			Objects tree = new Objects(c.objectId, c.objectX, c.objectY, 0, -1, 10, 7);
			Server.objectHandler.addObject(tree);*/
			//c.treeId = objectType;
			c.woodcut[0] = 1511;
			c.woodcut[1] = 1;
			c.woodcut[2] = 25;
			c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2]);
		break;
		
		case 1281: //oak
			c.woodcut[0] = 1521;
			c.woodcut[1] = 15;
			c.woodcut[2] = 37;
			c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2]);
		break;
		
		case 1308: //willow
			c.woodcut[0] = 1519;
			c.woodcut[1] = 30;
			c.woodcut[2] = 68;
			c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2]);
		break;
		
		case 1307: //maple
			c.woodcut[0] = 1517;
			c.woodcut[1] = 45;
			c.woodcut[2] = 100;
			c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2]);
		break;
		
		case 1309: //yew
			c.woodcut[0] = 1515;
			c.woodcut[1] = 60;
			c.woodcut[2] = 175;
			c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2]);
		break;
		
		case 1306: //yew
			c.woodcut[0] = 1513;
			c.woodcut[1] = 75;
			c.woodcut[2] = 250;
			c.getWoodcutting().startWoodcutting(c.woodcut[0], c.woodcut[1], c.woodcut[2]);
		break;

		
		case 2090://copper
		case 2091:
			c.mining[0] = 436;
			c.mining[1] = 1;
			c.mining[2] = 18;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;
		
		case 2094://tin
			c.mining[0] = 438;
			c.mining[1] = 1;
			c.mining[2] = 18;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;		
		
		case 145856:
		case 2092:
		case 2093: //iron
			c.mining[0] = 440;
			c.mining[1] = 15;
			c.mining[2] = 35;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;
		
		case 14850:
		case 14851:
		case 14852:
		case 2096:
		case 2097: //coal
			c.mining[0] = 453;
			c.mining[1] = 30;
			c.mining[2] = 50;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;		
		
		case 2098:
		case 2099:
			c.mining[0] = 444;
			c.mining[1] = 40;
			c.mining[2] = 65;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;
		
		case 2102:
		case 2103:
		case 14853:
		case 14854:
		case 14855: //mith ore
			c.mining[0] = 447;
			c.mining[1] = 55;
			c.mining[2] = 80;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;
		
		case 2105:
		case 14862: //addy ore
			c.mining[0] = 449;
			c.mining[1] = 70;
			c.mining[2] = 95;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;
		
		case 14859:
		case 14860: //rune ore
			c.mining[0] = 451;
			c.mining[1] = 85;
			c.mining[2] = 125;
			c.getMining().startMining(c.mining[0], c.mining[1], c.mining[2]);
		break;
		
		case 8143:
			if (c.farm[0] > 0 && c.farm[1] > 0) {
				c.getFarming().pickHerb();
			}
		break;
	
			// DOORS
		case 1516:
		case 1519:
			if (c.objectY == 9698) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0,-1);
				else
					c.getPA().walkTo(0,1);
				break;
			}
		case 1530:
		case 1531:
		case 1533:
		case 1534:
		case 11712:
		case 11711:
		case 11707:
		case 11708:
		case 6725:
		case 3198:
		case 3197:
			Server.objectHandler.doorHandling(objectType, c.objectX, c.objectY, 0);	
			break;
		
		case 9319:
			if (c.heightLevel == 0)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			else if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 2);
		break;
		
		case 9320:
			if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 0);
			else if (c.heightLevel == 2)
				c.getPA().movePlayer(c.absX, c.absY, 1);
		break;
		
		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 0);
			}
		break;
		
		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
		break;
		
		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
		break;
		
		case 5126:
			if (c.absY == 3554)
				c.getPA().walkTo(0,1);
			else
				c.getPA().walkTo(0,-1);
		break;
		
		case 1755:
			if (c.objectX == 2884 && c.objectY == 9797)
				c.getPA().movePlayer(c.absX, c.absY - 6400, 0);	
		break;
		case 1759:
			if (c.objectX == 2884 && c.objectY == 3397)
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);				
		break;
		/*case 3203: //dueling forfeit
			if (c.duelCount > 0) {
				c.sendMessage("You may not forfeit yet.");
				break;
			}
			Client o = (Client) Server.playerHandler.players[c.duelingWith];				
			if(o == null) {
				c.getTradeAndDuel().resetDuel();
				c.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
				break;
			}
			if(c.duelRule[0]) {
				c.sendMessage("Forfeiting the duel has been disabled!");
				break;
			}
			if(o != null) {
				o.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
				c.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), Config.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);
				o.duelStatus = 6;
				o.getTradeAndDuel().duelVictory();
				c.getTradeAndDuel().resetDuel();
				c.getTradeAndDuel().resetDuelItems();
				o.sendMessage("The other player has forfeited the duel!");
				c.sendMessage("You forfeit the duel!");
				break;
			}
			
			break;*/
			
		case 409:
			if(c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;
		case 411:
			/*if(c.playerPrayerBook == 1) {
				c.playerPrayerBook = 0;
				c.setSidebarInterface(5, 5608);
			} else {
				c.setSidebarInterface(5, 22500);
				c.playerPrayerBook = 1;
			}
			c.getCombat().resetPrayers();*/
			//GOD FUCKING DAMN IT /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /WRIST /
			break;
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			}	
		break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Guthix blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			}
		break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			}
		break;
		case 2879:
			c.getPA().movePlayer(2538, 4716, 0);
		break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
		break;
		case 5960:
			c.getPA().startTeleport2(3090, 3956, 0);
			c.sendMessage("Carefull you cant tele above 20 wilderness");
		break;
		
		case 1815:
			c.getPA().startTeleport2(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
		break;
		
		case 9706:
			c.getPA().startTeleport2(3105, 3951, 0);
		break;
		case 9707:
			c.getPA().startTeleport2(3105, 3956, 0);
		break;
		
		case 5959:
			c.getPA().startTeleport2(2539, 4712, 0);
		break;
		
		case 2558:
			c.sendMessage("This door is locked.");	
		break;
		
		case 9294:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
			} else if (c.absX > c.objectX) {
				c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
			}
		break;
		
		case 9293:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else {
				c.getPA().movePlayer(2886, 9799, 0);
			}
		break;
		case 10529:
		case 10527:
			if (c.absY <= c.objectY)
				c.getPA().walkTo(0,1);
			else
				c.getPA().walkTo(0,-1);
		break;
		case 3044:
			c.getSmithing().sendSmelting();
		break;
		
		case 733:
		if (System.currentTimeMillis() - c.lastAction < 500)
				return;
		if (c.wearingSharpWeapon()) {
			c.startAnimation(c.getCombat().getWepAnim(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase()));
			if (Misc.random(1) == 1) {
				c.sendMessage("You slash the web.");
				c.lastAction = System.currentTimeMillis();
				if (c.objectX == 3158 && c.objectY == 3951) {
					new Object(734, c.objectX, c.objectY, c.heightLevel, 1, 10, 733, 800);
				} else {
					new Object(734, c.objectX, c.objectY, c.heightLevel, 0, 10, 733, 800);
				}
			} else {
				c.sendMessage("You fail to slash the webs.");
				c.lastAction = System.currentTimeMillis() + 2000;
			}
		} else {
			c.sendMessage("You need a sharp blade to cut the web.");
		}
		break;
		
		case 1568:
				//new Object(734, c.objectX, c.objectY, c.heightLevel, 1, 10, 733, 50);
				c.startAnimation(451);
				c.getPA().movePlayer(3097, 9868, 0);
		break;
		
		default:
			ScriptManager.callFunc("objectClick1_"+objectType, c, objectType, obX, obY);
			break;

		}
	}
	
	private boolean obj(int obX, int obY) {
		return c.objectX == obX && c.objectY == obY;
	}
	
	public void secondClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		//c.sendMessage("Object type: " + objectType);
		switch(objectType) {
			case 10177: // Dagganoth ladder 1st level
				if (obj(2546, 10143))
					c.getPA().movePlayer(1798, 4407, 3);
			break;
			case 11666:
			case 3044:
				c.getSmithing().sendSmelting();
			break;
			case 2213:
			case 14367:
			case 11758:
				c.getPA().openUpBank();
			break;
			case 6163:
				c.getThieving().stealFromStall(1897, 10, 1);
			break;
			case 6165:
				c.getThieving().stealFromStall(950, 30, 25);
			break;
			case 6166:
				c.getThieving().stealFromStall(1635, 60, 50);
			break;
			case 6164:
				c.getThieving().stealFromStall(7650, 100, 75);
			break;
			case 6162:
				c.getThieving().stealFromStall(1613, 170, 90);
			break;
			case 2558:
				if (System.currentTimeMillis() - c.lastLockPick < 3000 || c.freezeTimer > 0)
					break;
				if (c.getItems().playerHasItem(1523,1)) {
						c.lastLockPick = System.currentTimeMillis();
						if (Misc.random(10) <= 3){
							c.sendMessage("You fail to pick the lock.");
							break;
						}
					if (c.objectX == 3044 && c.objectY == 3956) {
						if (c.absX == 3045) {
							c.getPA().walkTo2(-1,0);
						} else if (c.absX == 3044) {
							c.getPA().walkTo2(1,0);
						}
					
					} else if (c.objectX == 3038 && c.objectY == 3956) {
						if (c.absX == 3037) {
							c.getPA().walkTo2(1,0);
						} else if (c.absX == 3038) {
							c.getPA().walkTo2(-1,0);
						}				
					} else if (c.objectX == 3041 && c.objectY == 3959) {
						if (c.absY == 3960) {
							c.getPA().walkTo2(0,-1);
						} else if (c.absY == 3959) {
							c.getPA().walkTo2(0,1);
						}					
					}
				} else {
					c.sendMessage("I need a lockpick to pick this lock.");
				}
			break;
		default:
			ScriptManager.callFunc("objectClick2_"+objectType, c, objectType, obX, obY);
			break;
		}
	}
	
	
	public void thirdClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		c.sendMessage("Object type: " + objectType);
		switch(objectType) {
			case 10177: // Dagganoth ladder 1st level
				c.getPA().movePlayer(1798, 4407, 3);
			break;
		default:
			ScriptManager.callFunc("objectClick3_"+objectType, c, objectType, obX, obY);
			break;
		}
	}
	
	/**
	* Start of Summoning
	* @author Situations and Kyle
	**/
	
	//Pack Yak
	public void handleYak1() {
	}
	
	//Steel Titan
	public void handleSteel() {
	}
	
	//Unicorn
	public void handleUni() {
	}
	
	//Tortois
	public void handleTort1() {
	}
	
	//Spirit Bird
	public void handleBird() {
	}
	
	//Wolperrrrrrtinger
	public void handleWolp() {
	}
	
	/**
	* End of Summoning
	**/
	
	public void firstClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		if (c.teleTimer > 0)
			return;
		switch(npcType) {
			case 6540:
				c.getDH().sendDialogues(4, npcType);
			break;
			case 3791:
				c.getDH().sendDialogues(22, npcType);
			break;
			case 872:
				c.getDH().sendDialogues(20, npcType);
			break;
			case 5029:
				c.getDH().sendDialogues(1, npcType);
			break;
			case 706:
				c.getDH().sendDialogues(9, npcType);
			break;
			case 2258:
				c.getDH().sendDialogues(17, npcType);
			break;
			case 2024:
				c.getDH().sendDialogues(12, npcType);
			break;
			case 1599:
				if (c.slayerTask <= 0) {
					c.getDH().sendDialogues(11,npcType);
				} else {
					c.getDH().sendDialogues(13,npcType);
				}
			break;
			case 8725:
				c.getDH().sendDialogues(17,npcType);
			break;
			case 1152:
				c.getDH().sendDialogues(16,npcType);
			break;
			case 494:
			case 902:
				c.getPA().openUpBank();
			break;
			case 2566:
				c.getShops().openSkillCape();
			break;
			case 3789:
				c.sendMessage("You currently have " + c.pcPoints + " pest control points.");
			break;
			case 3788:
				c.getShops().openVoid();
			break;
			case 316: //shrimp
				c.getFishing().setupFishing(317);
			break;
			case 334: //mantas
				c.getFishing().setupFishing(389);
			break;
			case 324: //tuna
				c.getFishing().setupFishing(359);
			break;
			case 314: //salmon
				c.getFishing().setupFishing(335);
			break;
			case 326: //monks
				c.getFishing().setupFishing(7944);
			break;
			case 522:
			case 523:
				c.getShops().openShop(1);
			break;
			case 9085:
				c.getShops().openShop(25);
			break;
			case 3103:
				c.getShops().openShop(26);
				break;
			case 8443:
				c.getShops().openShop(24);
			break;
			case 599:
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			break;
			case 904:
				c.sendMessage("You have " + c.magePoints + " points.");
			break;
		default:
			ScriptManager.callFunc("npcClick1_"+npcType, c, npcType);
			if(c.playerRights == 3) 
				Misc.println("First Click Npc : "+npcType);
			break;
		}
	}

	public void secondClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch(npcType) {
			case 5029:
				c.getShops().openShop(2);
			break;
			case 8443:
				c.getShops().openShop(24);
			break;
			case 1282:
				c.getShops().openShop(7);
			break;
			case 334: //sharks
				c.getFishing().setupFishing(383);
			break;
			case 3788:
				c.getShops().openVoid();
			break;
			case 494:
			case 902:
				c.getPA().openUpBank();
			break;
			case 324: //lobs
				c.getFishing().setupFishing(359);
			break;
			case 904:
				c.getShops().openShop(17);
			break;
			case 522:
			case 523:
				c.getShops().openShop(1);
			break;
			case 541:
				c.getShops().openShop(5);
			break;
			
			case 461:
				c.getShops().openShop(2);
			break;
			
			case 683:
				c.getShops().openShop(3);
			break;
			
			case 549:
				c.getShops().openShop(4);
			break;
			
			case 2538:
				c.getShops().openShop(6);
			break;
			
			case 519:
				c.getShops().openShop(8);
			break;
			case 3789:
				c.getShops().openShop(18);
			break;
			case 1:
			case 9:
			case 18:
			case 20:
			case 26:
			case 21:
				c.getThieving().stealFromNPC(npcType);
			break;
			default:
				ScriptManager.callFunc("npcClick2_"+npcType, c, npcType);
				if(c.playerRights == 3) 
					Misc.println("Second Click Npc : "+npcType);
				break;
			
		}
	}
	
	public void thirdClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch(npcType) {
		case 8443:
				c.getShops().openShop(24);
			break;
			default:
				ScriptManager.callFunc("npcClick3_"+npcType, c, npcType);
				if(c.playerRights == 3) 
					Misc.println("Third Click NPC : "+npcType);
				break;

		}
	}
	
	private void handleDagganothObjects(int objectId) {
		switch (objectId) {
		case 8930:
			c.getPA().movePlayer(1975, 4409, 3);
		break;
		
		case 10177: // Dagganoth ladder 1st level
			c.getPA().movePlayer(1798, 4407, 3);
		break;	
		
		case 10193:
			c.getPA().movePlayer(2545, 10143, 0);
		break;
		
		case 10194:
			c.getPA().movePlayer(2544, 3741, 0);
		break;
		
		case 10195: 
			c.getPA().movePlayer(1809, 4405, 2);
		break;
		
		case 10196:
			c.getPA().movePlayer(1807, 4405, 3);
		break;
		
		case 10197:
			c.getPA().movePlayer(1823, 4404, 2);
		break;
		
		case 10198:
			c.getPA().movePlayer(1825, 4404, 3);
		break;
		
		case 10199:
			c.getPA().movePlayer(1834, 4388, 2);
		break;
		
		case 10200:
			c.getPA().movePlayer(1834, 4390, 3);
		break;
	
		case 10201:
			c.getPA().movePlayer(1811, 4394, 1);
		break;
		
		case 10202:
			c.getPA().movePlayer(1812, 4394, 2);
		break;
		
		case 10203:
			c.getPA().movePlayer(1799, 4386, 2);
		break;
		
		case 10204:
			c.getPA().movePlayer(1799, 4388, 1);
		break;
		
		case 10205:
			c.getPA().movePlayer(1796, 4382, 1);
		break;
		
		case 10206:
			c.getPA().movePlayer(1796, 4382, 2);
		break;
		
		case 10207:
			c.getPA().movePlayer(1800, 4369, 2);
		break;
		
		case 10208:
			c.getPA().movePlayer(1802, 4370, 1);
		break;
		
		case 10209:
			c.getPA().movePlayer(1827, 4362, 1);
		break;
		
		case 10210:
			c.getPA().movePlayer(1825, 4362, 2);
		break;
		
		case 10211:
			c.getPA().movePlayer(1863, 4373, 2);
		break;
		
		case 10212:
			c.getPA().movePlayer(1863, 4371, 1);
		break;
		
		case 10213:
			c.getPA().movePlayer(1864, 4389, 1);
		break;
		
		case 10214:
			c.getPA().movePlayer(1864, 4387, 2);
		break;
		
		case 10215:
			c.getPA().movePlayer(1890, 4407, 0);
		break;
		
		case 10216:
			c.getPA().movePlayer(1890, 4406, 1);
		break;
		
		case 10217:
			c.getPA().movePlayer(1957, 4373, 1);
		break;
		
		case 10218:
			c.getPA().movePlayer(1957, 4371, 0);
		break;
		
		case 10219:
			c.getPA().movePlayer(1824, 4379, 3);
		break;
		
		case 10220:
			c.getPA().movePlayer(1824, 4381, 2);
		break;
		
		case 10221:
			c.getPA().movePlayer(1838, 4375, 2);
		break;
		
		case 10222:
			c.getPA().movePlayer(1838, 4377, 3);
		break;
		
		case 10223:
			c.getPA().movePlayer(1850, 4386, 1);
		break;
		
		case 10224:
			c.getPA().movePlayer(1850, 4387, 2);
		break;
		
		case 10225:
			c.getPA().movePlayer(1932, 4378, 1);
		break;
		
		case 10226:
			c.getPA().movePlayer(1932, 4380, 2);
		break;
		
		case 10227:
			if (obj(1961, 4392))
				c.getPA().movePlayer(1961, 4392, 2);
			else 
				c.getPA().movePlayer(1932, 4377, 1);
		break;
		
		case 10228:
			c.getPA().movePlayer(1961, 4393, 3);
		break;
		
		case 10229:
			c.getPA().movePlayer(1912, 4367, 0);
		break;
		
		case 10230:
			c.getPA().movePlayer(2899, 4449, 0);
		break;
		}
	}
	

}