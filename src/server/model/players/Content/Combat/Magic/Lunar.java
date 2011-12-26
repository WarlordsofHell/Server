package server.model.players.combat.magic;

import server.Server;
import server.event.*;
import server.util.Misc;
import server.model.players.*;

public class Lunar {

	private static int astral = 9075, fire = 554, water = 555, air = 556, earth = 557, mind = 558,
	               body = 559, death = 560, nats = 561, chaos = 562, law = 563, cosmic = 564,
	               blood = 565, soul = 566;

	public static void magicOnItems(final Client c, int slot, int itemId, int spellId) {
		if (System.currentTimeMillis() - c.lunarDelay < 2000)
			return;
		switch(spellId) {
			case 30017:
				if (c.playerLevel[6] < 65) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral) || !c.getItems().playerHasItem(fire, 5) || !c.getItems().playerHasItem(water, 4)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				if (itemId == 2317 || itemId == 2319 || itemId == 2321) {
					c.startAnimation(4413);
					c.gfx100(746);
					c.getItems().deleteItem(itemId, 1);
					c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 1);
					c.getItems().deleteItem(fire, c.getItems().getItemSlot(fire), 5);
					c.getItems().deleteItem(water, c.getItems().getItemSlot(water), 4);
					c.sendMessage("You bake the pie");
					c.getPA().addSkillXP(1024, 6);
					c.getPA().refreshSkill(6);
					c.lunarDelay = System.currentTimeMillis();
					if (itemId == 2317)
						c.getItems().addItem(2323, 1);
					else if (itemId == 2319)
						c.getItems().addItem(2327, 1);
					else if (itemId == 2321)
						c.getItems().addItem(2325, 1);
				} else
					c.sendMessage("This spell only works on an uncooked pie!");
			break;

			case 30154:
				if (c.playerLevel[6] < 77)
					return;
				if(!c.getItems().playerHasItem(astral, 2) || !c.getItems().playerHasItem(fire, 6) || !c.getItems().playerHasItem(air, 10)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				if(itemId != 1783) {
					c.sendMessage("You can only use this spell on a bucket of sand!");
					return;
				}
				c.startAnimation(4412);
				c.gfx0(729);
				c.getItems().deleteItem(itemId, c.getItems().getItemSlot(itemId), 1);
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 2);
				c.getItems().deleteItem(fire, c.getItems().getItemSlot(fire), 6);
				c.getItems().deleteItem(air, c.getItems().getItemSlot(air), 10);
				c.getItems().addItem(1775, 1);
				c.lunarDelay = System.currentTimeMillis();
				c.getPA().addSkillXP(1600, 6);
				c.getPA().refreshSkill(6);
			break;
		} // closes switch
	} // closes magiconitems

	public static void Button(final Client c, int actionButtonId) {
	if (System.currentTimeMillis() - c.lunarDelay < 2000)
			return;
		switch (actionButtonId){
			case 117104:
				if(c.playerLevel[6] < 68) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral) || !c.getItems().playerHasItem(water, 3) || !c.getItems().playerHasItem(fire)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				if(c.getItems().playerHasItem(229)) {
					c.startAnimation(6294);
					c.gfx0(1061);
					c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 1);
					c.getItems().deleteItem(fire, c.getItems().getItemSlot(fire), 1);
					c.getItems().deleteItem(water, c.getItems().getItemSlot(water), 3);
					c.getItems().deleteItem(229, 1);
					c.getItems().addItem(227, 1);
					c.getPA().addSkillXP(200, 6);
					c.getPA().refreshSkill(6);
					c.lunarDelay = System.currentTimeMillis();
				} else
					c.sendMessage("You have run out of empty vials.");
			break;

			case 117147:
				if(c.playerLevel[6] < 71)	{
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 2) || !c.getItems().playerHasItem(earth, 2)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				c.startAnimation(6303);
				c.gfx0(1074);
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 1);
				c.getItems().deleteItem(earth, c.getItems().getItemSlot(earth), 1);
				c.getItems().addItem(11159, 1);
				c.sendMessage("You get a hunter kit.");
				c.getPA().addSkillXP(700, 6);
				c.getPA().refreshSkill(6);
				c.lunarDelay = System.currentTimeMillis();
			break;

			case 117170:
				if(c.playerLevel[6] < 74) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 2) || !c.getItems().playerHasItem(cosmic, 2) || !c.getItems().playerHasItem(law, 2)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 2);
				c.getItems().deleteItem(cosmic, c.getItems().getItemSlot(cosmic), 2);
				c.getItems().deleteItem(law, c.getItems().getItemSlot(law), 2);
				cureAll(c);
				c.getPA().addSkillXP(1400, 6); //get real xp amount
				c.getPA().refreshSkill(6);
				c.lunarDelay = System.currentTimeMillis();
			break;

			case 118106:
				if(c.playerLevel[6] < 95) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 4) || !c.getItems().playerHasItem(blood, 3) || !c.getItems().playerHasItem(law, 6)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 4);
				c.getItems().deleteItem(blood, c.getItems().getItemSlot(blood), 3);
				c.getItems().deleteItem(law, c.getItems().getItemSlot(law), 6);
				HealAll(c);
				c.lunarDelay = System.currentTimeMillis();
			break;

			case 117139:
				if(c.playerLevel[6] < 71) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 2) || !c.getItems().playerHasItem(cosmic, 2) || !c.getItems().playerHasItem(law)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 2);
				c.getItems().deleteItem(cosmic, c.getItems().getItemSlot(cosmic), 2);
				c.getItems().deleteItem(law, c.getItems().getItemSlot(law), 1);
				c.poisonDamage = -1;
				c.startAnimation(4411);
				c.gfx0(748);
				c.getPA().addSkillXP(500, 6);
				c.getPA().refreshSkill(6);
				c.lunarDelay = System.currentTimeMillis();
			break; 

			case 118034:
				if(c.playerLevel[6] < 86) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 3) || !c.getItems().playerHasItem(earth, 15) || !c.getItems().playerHasItem(nats, 1)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				if (!c.getItems().playerHasItem(1511)) {
					c.sendMessage("You need to have logs in your inventory to use this spell.");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 3);
				c.getItems().deleteItem(earth, c.getItems().getItemSlot(earth), 15);
				c.getItems().deleteItem(nats, c.getItems().getItemSlot(nats), 1);
				c.getItems().deleteItem(1511, 1);
				c.getItems().addItem(960, 1);
				c.startAnimation(6298);
				c.gfx0(1063);
				c.getPA().addSkillXP(400, 6);
				c.getPA().refreshSkill(6);
				c.lunarDelay = System.currentTimeMillis();
			break;

			case 118010: //boost stats - add later with events
				c.sendMessage("Add this yourself or wait until I have time to do it.");
			break;

			case 117242: //boost other stats - add later with events
				c.sendMessage("Add this yourself or wait until I have time to do it.");
			break;

			case 118114:
				if(c.playerLevel[6] < 96) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 3) || !c.getItems().playerHasItem(cosmic, 2) || !c.getItems().playerHasItem(law)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				c.startAnimation(6299);
				c.gfx0(1062);
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 3);
				c.getItems().deleteItem(cosmic, c.getItems().getItemSlot(cosmic), 2);
				c.getItems().deleteItem(law, c.getItems().getItemSlot(law), 1);
				c.getPA().addSkillXP(1000, 6);
				c.getPA().refreshSkill(6);
				//c.getDH().sendDialogues(1682, c.npcType); //add your own way of switching mage books
				c.lunarDelay = System.currentTimeMillis();
			break;

			case 117226:
				if (c.playerLevel[6] < 79) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 2) || !c.getItems().playerHasItem(cosmic) || !c.getItems().playerHasItem(body, 5)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				if(c.indream) {
					c.sendMessage("You already in deep sleep");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 2);
				c.getItems().deleteItem(cosmic, c.getItems().getItemSlot(cosmic), 1);
				c.getItems().deleteItem(body, c.getItems().getItemSlot(body), 5);
				c.getPA().addSkillXP(1000, 6);
				c.getPA().refreshSkill(6);
				c.getPA().resetFollow();
				Dream(c);
				c.dream = 5;
				c.lunarDelay = System.currentTimeMillis();
			break;
		}
	}

	public static void CastingLunarOnPlayer(final Client c, int castingSpellId) {
		final Client castOnPlayer = (Client) Server.PlayerHandler.players[c.playerIndex];
		c.stopMovement();
		c.getCombat().resetPlayerAttack();
		switch(castingSpellId) {
			case 30130:
				if(c.playerLevel[6] < 75) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if(!c.getItems().playerHasItem(astral, 2) || !c.getItems().playerHasItem(cosmic, 2) || c.getItems().playerHasItem(body, 5)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 2);
				c.getItems().deleteItem(cosmic, c.getItems().getItemSlot(cosmic), 2);
				c.getItems().deleteItem(body, c.getItems().getItemSlot(body), 5);
				c.startAnimation(6293);
				c.gfx0(1060);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Attack Level: "+castOnPlayer.playerLevel[0]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[0])+"", 8147);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Strength Level: "+castOnPlayer.playerLevel[2]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[2])+"", 8148);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Defence Level: "+castOnPlayer.playerLevel[1]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[1])+"", 8149);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Hitpoints Level: "+castOnPlayer.playerLevel[3]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[3])+"", 8150);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Range Level: "+castOnPlayer.playerLevel[4]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[4])+"", 8151);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Prayer Level: "+castOnPlayer.playerLevel[5]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[5])+"", 8152);
				c.getPA().sendFrame126(""+castOnPlayer.playerName+"'s Magic Level: "+castOnPlayer.playerLevel[6]+"/"+castOnPlayer.getLevelForXP(castOnPlayer.playerXP[6])+"", 8153);
				c.getPA().showInterface(8134);
				castOnPlayer.gfx0(736);
			break;

			case 30298:
				if(c.playerLevel[6] < 93) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if (!c.getItems().playerHasItem(557, 10) || !c.getItems().playerHasItem(9075, 3) || !c.getItems().playerHasItem(560, 2)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				if (System.currentTimeMillis() - c.lastVeng < 30000) {
					c.sendMessage("You can only cast vengeance every 30 seconds.");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 3);
				c.getItems().deleteItem(death, c.getItems().getItemSlot(death), 2);
				c.getItems().deleteItem(earth, c.getItems().getItemSlot(astral), 10);
				castOnPlayer.vengOn = true;
				c.lastVeng = System.currentTimeMillis();
				castOnPlayer.gfx100(725);
				c.getPA().addSkillXP(1240, 6);
				c.getPA().refreshSkill(6);
				c.startAnimation(4411);
			break;

			case 30048:
				if(c.playerLevel[6] < 68) {
					c.sendMessage("You do not have the required magic level to use this spell.");
					return;
				}
				if (!c.getItems().playerHasItem(earth, 10) || !c.getItems().playerHasItem(astral) || !c.getItems().playerHasItem(law)) {
					c.sendMessage("You do not have the required runes to cast this spell.");
					return;
				}
				if (castOnPlayer.poisonDamage < 0) {
					c.sendMessage("This player is not poisoned.");
					return;
				}
				c.getItems().deleteItem(astral, c.getItems().getItemSlot(astral), 1);
				c.getItems().deleteItem(law, c.getItems().getItemSlot(law), 1);
				c.getItems().deleteItem(earth, c.getItems().getItemSlot(earth), 10);
				castOnPlayer.poisonDamage = -1;
				c.sendMessage("You have been cured by " + Misc.optimizeText(c.playerName) + ".");
				c.startAnimation(4411);
				castOnPlayer.gfx100(745);
				c.getPA().addSkillXP(620, 6);
				c.getPA().refreshSkill(6);
			break;

			case 30290:
				healOther(c, castOnPlayer);
				c.lunarDelay = System.currentTimeMillis();
			break;

			case 30282: //make method for heal all
				energyTransfer(c);
				c.lunarDelay = System.currentTimeMillis();
			break;
		}
	}

	private static void energyTransfer(final Client c) {	
		if (c.playerIndex > 0) {	
			Player q = Server.PlayerHandler.players[c.playerIndex];			
			final int oX = q.getX();
			final int oY = q.getY();
			if(c.playerLevel[6] < 91) {
				c.sendMessage("You need a magic level of 91 to cast this spell.");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
                if (!q.acceptAid) {
                	c.sendMessage("This player has their accept Aid off, therefore you cannot aid them!");
                	return;
                }
			if(!c.getItems().playerHasItem(9075, 3) || !c.getItems().playerHasItem(563, 2) || !c.getItems().playerHasItem(561, 1)) {
				c.sendMessage("You don't have the required runes to cast this spell.");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
			if(c.specAmount == 10) {
				c.startAnimation(4411);
				c.getItems().updateSpecialBar();
				//q.getItems().updateSpecialBar();
				q.gfx100(736);//Just use c.gfx100
				c.getItems().deleteItem2(9075, 3);
				c.getItems().deleteItem2(563, 2);//For these you need to change to deleteItem(item, itemslot, amount);.
				c.getItems().deleteItem2(561, 1);
				q.specAmount = 10.0;
				c.specAmount = 0;
				//q.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
				c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
				c.getPA().addSkillXP(1750, 6);
				c.turnPlayerTo(oX,oY);
				c.getPA().refreshSkill(6);
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
			}
			if (c.specAmount < 10) {
				c.sendMessage("You need full special bar to use this on spell on someone.");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
		}
	}

	private static void healOther(final Client c, Player q) {	
		if (c.playerIndex > 0) {	
			final int oX = q.getX();
			final int oY = q.getY();
			if(c.playerLevel[6] < 92) {
				c.sendMessage("You need a magic level of 92 to cast this spell.");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
			if (c.playerLevel[3] - c.playerLevel[3] * .75 < 1) {
				c.sendMessage("Your hitpoints are too low to do this!");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
                if (!q.acceptAid) {
                	c.sendMessage("This player has their accept Aid off, therefore you cannot veng them!");
                	return;
                }
			if(c.playerLevel[1] < 40) {
				c.sendMessage("You need a defence level of 40 to cast this spell.");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
			if(!c.getItems().playerHasItem(9075, 3) || !c.getItems().playerHasItem(563, 3) || !c.getItems().playerHasItem(565, 1)) {
				c.sendMessage("You don't have the required runes to cast this spell.");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
			if(System.currentTimeMillis() - c.lastCast < 30000) {
				c.sendMessage("You can only heal others every 30 seconds.");
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				c.turnPlayerTo(oX,oY);
				return;
			}
			if (q.playerLevel[3] + q.playerLevel[3] * .75 > q.playerLevel[3]) {
				q.playerLevel[3] = q.playerLevel[3];
				c.startAnimation(4411);
				q.gfx100(734);
				c.playerLevel[3] -= c.playerLevel[3] * .75;
				c.getItems().deleteItem(9075,c.getItems().getItemSlot(9075), 3);
				c.getItems().deleteItem(563,c.getItems().getItemSlot(563), 3);
				c.getItems().deleteItem(565,c.getItems().getItemSlot(565), 1);
				c.getPA().addSkillXP(1750, 6);
				c.turnPlayerTo(oX,oY);
				c.getPA().refreshSkill(6);
				c.getPA().refreshSkill(3);
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				//c.handleHitMask(c.playerLevel[3] * .75);
				//c.dealDamage(c.playerLevel[3] * .75);
				c.lastCast = System.currentTimeMillis();
			} else {
				c.startAnimation(4411);
				q.gfx100(734);
				q.playerLevel[3] += c.playerLevel[3] * .75;
				c.playerLevel[3] -= c.playerLevel[3] * .75;
				c.getItems().deleteItem(9075,c.getItems().getItemSlot(9075), 3);
				c.getItems().deleteItem(563,c.getItems().getItemSlot(563), 3);
				c.getItems().deleteItem(565,c.getItems().getItemSlot(565), 1);
				c.getPA().addSkillXP(1750, 6);
				c.turnPlayerTo(oX,oY);
				c.getPA().refreshSkill(6);
				c.getPA().refreshSkill(3);
				c.getCombat().resetPlayerAttack();
				c.stopMovement();
				//c.handleHitMask(c.playerLevel[3] * .75);
				//c.dealDamage(c.playerLevel[3] * .75);
				c.lastCast = System.currentTimeMillis();
			}
		}
	}

	private static void Dream(final Client c) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer event) {
				if (c.playerLevel[3] == c.getLevelForXP(c.playerXP[3]) && c.indream != true) {
					c.indream = false;
					c.sendMessage("You already have full hitpoints");
					event.stop();
					return;
				} else {
					if (c.dream == 5) {
						c.indream = true;
						c.startAnimation(6295);
						c.sendMessage("The sleeping has an effect on your health");
					} else if (c.dream == 0) {
						c.indream = true;
						c.startAnimation(6296);
						c.gfx0(1056);
						c.playerLevel[3]++;
						c.getPA().refreshSkill(3);
					}
					if (c.playerLevel[3] == c.getLevelForXP(c.playerXP[3]) && c.indream) {
						c.indream = false;
						c.sendMessage("You wake up for your dream");
						c.startAnimation(6297);
					}
					if (!c.indream) {
						c.sendMessage("You wake up.");
						event.stop();
					}
					if (System.currentTimeMillis() - c.logoutDelay < 10000 || c.followId > 0 || c.followId2 > 0 || c.spellId > 0) {
						c.startAnimation(6297);
						c.sendMessage("You wake up.");
						c.indream = false;
						event.stop();
					}
					if (c.dream > 0)
						c.dream--;
				}
			}
			@Override
			public void stop() {	
			}
		}, 2);
	}

	private static void cureAll(Client c) {
		for (Player p : Server.PlayerHandler.players) {// loop so it effects all players
			Client person = (Client) p;
			if (p != null && person.distanceToPoint(c.absX, c.absY) <= 2) {
				Client castOn = (Client) p;// specific player's client
				c.poisonDamage = -1;
				castOn.sendMessage("You have been cured by " + Misc.optimizeText(c.playerName) + ".");
				c.startAnimation(4409);
				castOn.gfx100(745);
			}
		}
	}

	private static void HealAll(Client c) {
		for (Player p : Server.PlayerHandler.players) {											
			Client person = (Client) p;
			if (p != null && person.distanceToPoint(c.absX, c.absY) <= 1) {
				Client castOn = (Client) p;
				castOn.playerLevel[3] += 5;
				castOn.getPA().refreshSkill(3);
				c.getPA().refreshSkill(3);
				castOn.sendMessage("You have been cured by " + Misc.optimizeText(c.playerName) + ".");
				c.startAnimation(4409);
				castOn.gfx100(744);
				castOn.playerLevel[3] += 2 * c.playerLevel[3];
				if (castOn.playerLevel[3] + (2 * castOn.playerLevel[3]) > castOn.playerLevel[3])
					castOn.playerLevel[3] = castOn.getPA().getLevelForXP(c.playerXP[3]);
				castOn.sendMessage("You have been healed by " + Misc.optimizeText(c.playerName)
						+ ".");
			}
		}
	}

	private static void statrestore(Client c) {
		for (Player p : Server.PlayerHandler.players) {										
			Client person = (Client) p;
			if (p != null && person.distanceToPoint(c.absX, c.absY) <= 2) {
				Client castOn = (Client) p;// specific player's client
				c.poisonDamage = -1;
				castOn.sendMessage("" + Misc.optimizeText(c.playerName)
						+ " has shared there stat restore potion with you.");
				c.startAnimation(4409);
				castOn.gfx100(733);
			}
		}
	}
}