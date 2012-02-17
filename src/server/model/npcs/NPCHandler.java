package server.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import server.Config;
import server.Server;
import server.model.players.Client;
import server.util.Misc;
import server.world.map.VirtualWorld;
import server.event.EventManager;
import server.event.Event;
import server.event.EventContainer;

public class NPCHandler {
    

        
	public static int maxNPCs = 12500;
	public static int maxListedNPCs = 50000;
	public static int maxNPCDrops = 12500;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static int npcCombat[] = new int[maxNPCs];
	public static NPCList NpcList[] = new NPCList[maxListedNPCs];

	public NPCHandler() {
		for(int i = 0; i < maxNPCs; i++) {
			npcs[i] = null;
		}
		for(int i = 0; i < maxListedNPCs; i++) {
			NpcList[i] = null;
		}
		loadNPCList("./Data/CFG/npc.cfg");
		loadAutoSpawn("./Data/CFG/spawn-config.cfg");
	}
	
	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (Server.playerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					int nX = Server.npcHandler.npcs[i].getX() + offset(i);
					int nY = Server.npcHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY)* -1;
					int offY = (nX - pX)* -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i), npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);					
				}
			}		
		}
	}
	
	public boolean switchesAttackers(int i) {
		switch(npcs[i].npcType) {
			case 2551:
			case 2552:
			case 2553:
			case 2559:
			case 2560:
			case 2561:
			case 2563:
			case 2564:
			case 2565:
			case 2892:
                        case 50:
			case 2894:
			case 8133:
			return true;
		}
	
		return false;
	}
	
	public void multiAttackDamage(int i) {
		int max = getMaxHit(i);
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (Server.playerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					if (npcs[i].attackType == 2) {
						if (!c.prayerActive[16] || !c.curseActive[c.curses().DEFLECT_MAGIC]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
								int dam = Misc.random(max);
								c.getCombat().appendHit(c, dam, 0, 2, false, 0);						
							} else {
								c.getCombat().appendHit(c, 0, 0, 2, false, 0);								
							}
						} else {
							if (c.curseActive[c.curses().DEFLECT_MAGIC])
								c.curses().deflectNPC(npcs[i], 0, 2);
							c.getCombat().appendHit(c, 0, 0, 2, false, 0);	
						}
					} else if (npcs[i].attackType == 1) {
						if (!c.prayerActive[17] || !c.curseActive[c.curses().DEFLECT_MISSILES]) {
							int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
								c.getCombat().appendHit(c, dam, 0, 1, false, 0);						
							} else {
								c.getCombat().appendHit(c, 0, 0, 2, false, 0);	
							}
						} else {
							if (c.curseActive[c.curses().DEFLECT_MISSILES])
								c.curses().deflectNPC(npcs[i], 0, 1);
							c.getCombat().appendHit(c, 0, 0, 2, false, 0);							
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);					
					}
				}
				c.getPA().refreshSkill(3);
			}		
		}
	}
	
	public int getClosePlayer(int i) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(Server.playerHandler.players[j].absX, Server.playerHandler.players[j].absY, npcs[i].absX, npcs[i].absY, 2 + distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((Server.playerHandler.players[j].underAttackBy <= 0 && Server.playerHandler.players[j].underAttackBy2 <= 0) || Server.playerHandler.players[j].inMulti())
						if (Server.playerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
				}
			}	
		}
		return 0;
	}
	
	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				if (goodDistance(Server.playerHandler.players[j].absX, Server.playerHandler.players[j].absY, npcs[i].absX, npcs[i].absY, 2 + distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((Server.playerHandler.players[j].underAttackBy <= 0 && Server.playerHandler.players[j].underAttackBy2 <= 0) || Server.playerHandler.players[j].inMulti())
						if (Server.playerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);
				}
			}	
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() -1));
		else
			return 0;
	}
	
	public int npcSize(int i) {
		switch (npcs[i].npcType) {
		case 2883:
		case 2882:
		case 2881:
			return 3;
		}
		return 0;
	}
	
	public boolean isAggressive(int i) {
		switch (npcs[i].npcType) {
			case 2550:
			case 2551:
			case 2552:
			case 2553:
			case 2558:
			case 2559:
			case 2560:
			case 2561:
			case 2562:
			case 2563:
			case 2564:
			case 2565:
			case 2892:
			case 2894:
			case 2881:
			case 2882:
			case 2883:
			case 75:
			case 90:
                        case 50:
			case 103:
			case 10604:
			case 82:
			case 86:
			case 78:
									
			return true;		
		}
		if (isFightCaveNpc(i) || isBarbNpc(i))
			return true;
		return false;
	}
	
	public boolean isFightCaveNpc(int i) {
		switch (npcs[i].npcType) {
			case 2627:
			case 2630:
			case 2631:
			case 2741:
			case 2743: 
			case 2745:
			return true;		
		}
		return false;
	}
	
	public boolean isBarbNpc(int i) {
		return Server.barbDefence.killableNpcs(i);
	}
	
	/**
	* Summon npc, barrows, etc
	**/
	public void spawnNpc(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if(slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if(headIcon) 
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if(attackPlayer) {
			newNPC.underAttack = true;
			if(c != null) {
				if(server.model.minigames.Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] != newNPC.npcType) {
					if(newNPC.npcType == 2025 || newNPC.npcType == 2026 || newNPC.npcType == 2027 || newNPC.npcType == 2028 || newNPC.npcType == 2029 || newNPC.npcType == 2030) {
						newNPC.forceChat("You dare disturb my rest!");
					}
				}
				if(server.model.minigames.Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] == newNPC.npcType) {
					newNPC.forceChat("You dare steal from us!");
				}
				
				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
	}
	
	public void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if(slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}
	
	public void spawnNpc3(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				c.barbLeader = slot = i;
				break;
			}
		}
		if(slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if(headIcon) 
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if(attackPlayer) {
			newNPC.underAttack = true;
			if(c != null) {
				if(server.model.minigames.Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] != newNPC.npcType) {
					if(newNPC.npcType == 2025 || newNPC.npcType == 2026 || newNPC.npcType == 2027 || newNPC.npcType == 2028 || newNPC.npcType == 2029 || newNPC.npcType == 2030) {
						newNPC.forceChat("You dare disturb my rest!");
					}
				}
				if(server.model.minigames.Barrows.COFFIN_AND_BROTHERS[c.randomCoffin][1] == newNPC.npcType) {
					newNPC.forceChat("You dare steal from us!");
				}
				
				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
	}
	
	public NPC summonNPC(Client c, int npcType, int x, int y, int heightLevel, int walkingType, int HP, int maxHit, int attack, int defence) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = walkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = newNPC.summonedFor = c.getId();
		npcs[slot] = newNPC;
		return newNPC;
	}
	
	/**
	* Emotes
	**/
	
	public static int getCombatEmote(int i, String type) {
		return Server.emoteHandler.getCombatEmote(i, type);
	}
	
	/**
	* Attack delays
	**/
	public int getNpcDelay(int i) {
		switch(npcs[i].npcType) {
			case 2025:
			case 2028:
			return 7;
			
			case 2745:
			return 8;
			
			case 2558:
			case 2559:
			case 2560:
			case 2561:
			case 2550:
			return 6;
			//saradomin gw boss
			case 2562:
			return 2;
			
			case 8133:
			return 10;
			
			case 10773:
                            		case 50:
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 8;
			default:
			return 5;
		}
	}
	
	/**
	* Hit delays
	**/
	public int getHitDelay(int i) {
		switch(npcs[i].npcType) {
			case 2881: case 2882: case 3200: case 2892: case 2894:
			return 3;
			
			case 2743:
			case 2631:
			case 2558:
			case 2559:
			case 2560:
				return 3;
			
			case 5993:
				return 3;
				
			case 5229: case 5230: case 5231: case 5232: case 5233: case 5234: case 5235: case 5236: case 5237: // Penance ranger
				return 3;
				
			case 2745:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;
			
			case 2025:
			return 4;
			case 2028:
			return 3;

			default:
			return 2;
		}
	}
		
	/**
	* Npc respawn time
	**/
	public int getRespawnTime(int i) {
		switch(npcs[i].npcType) {
                    		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 4291: // Cyclops
		case 4292: // Ice cyclops
		case 1592:
			return 110;
			case 2881:
			case 2882:
			case 2883:
			case 2558:
			case 2559:
			case 2560:
			case 2561:
			case 2562:
			case 2563:
			case 2564:
			case 2550:
			case 2551:
			case 2552:
			case 2553:
			return 100;
			case 3777:
			case 3778:
			case 3779:
			case 3780:
			return 500;
			case 10773:
			return 50;
			default:
			return 25;
		}
	}
	
	
	
	
	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if(slot == -1) return;

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	public void newNPCList(int npcType, String npcName, int combat, int HP) {
		int slot = -1;
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] == null) {
				slot = i;
				break;
			}
		}

		if(slot == -1) return;

		NPCList newNPCList = new NPCList(npcType);
		newNPCList.npcName = npcName;
		newNPCList.npcCombat = combat;
		newNPCList.npcHealth = HP * 10;
		NpcList[slot] = newNPCList;
	}

	public Client allPlayers(int i) {
		return (Client) Server.playerHandler.players[i];
	}
	
	private Client c2 = null;

	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null) continue;
			npcs[i].clearUpdateFlags();
			
		}
                
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] != null) {
				NPC n = npcs[i];
					if (npcs[i].splashDelay > 0)
						npcs[i].splashDelay--;
					if (npcs[i].splashDelay == 4) {
						corpSplash(n);
						for (int p = 1; p < Server.playerHandler.players.length; p++) {
							c2 = allPlayers(p);
							if (c2 != null) {
								if (c2.getX() == npcs[i].splashCoord[0][0] && c2.getY() == npcs[i].splashCoord[0][1])
									c2.dealDamage(10);
							}
						}
					} else if (npcs[i].splashDelay == 0) {
						for (int p = 1; p < Server.playerHandler.players.length; p++) {
							c2 = allPlayers(p);
							for (int coords = 1; coords < npcs[i].splashCoord.length; coords++) {
								if (c2 != null) {
									if (c2.getX() == npcs[i].splashCoord[coords][0] && c2.getY() == npcs[i].splashCoord[coords][1])
										c2.dealDamage(10);
									c2.getPA().stillGfx(1808, npcs[i].splashCoord[coords][0], npcs[i].splashCoord[coords][1], 0, 0);
								}
							}
						}
						npcs[i].splashDelay = -1;
					}
	
				if (npcs[i].summonedFor > 0 && !npcs[i].isDead)
					summoningFollow(i, npcs[i].summonedFor);
				
				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}
				
				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}
				
				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}
				
				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					applyDamage(i);
				}
				
				if(npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}
					
				if(npcs[i].spawnedBy > 0) {
					if(Server.playerHandler.players[npcs[i].spawnedBy] == null
					|| Server.playerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel	
					|| Server.playerHandler.players[npcs[i].spawnedBy].respawnTimer > 0 
					|| !Server.playerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(), npcs[i].getY(), Server.playerHandler.players[npcs[i].spawnedBy].getX(), Server.playerHandler.players[npcs[i].spawnedBy].getY(), 20)) {
							
						if(Server.playerHandler.players[npcs[i].spawnedBy] != null) {
							for(int o = 0; o < Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++){
								if(npcs[i].npcType == Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
									if (Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1)
										Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
								}
							}
						}
						npcs[i] = null;
					}
				}
				if (npcs[i] == null) continue;
				
				/**
				* Attacking player
				**/
				if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && !switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				} else if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				}
				
				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000)
					npcs[i].underAttackBy = 0;
				
				if((npcs[i].killerId > 0 || npcs[i].underAttack) && !npcs[i].walkingHome && retaliates(npcs[i].npcType)) {
					if(!npcs[i].isDead) {
						int p = npcs[i].killerId;
						if(Server.playerHandler.players[p] != null) {
							Client c = (Client) Server.playerHandler.players[p];					
							followPlayer(i, c.playerId);
							if (npcs[i] == null) continue;
							if(npcs[i].attackTimer == 0) {
								if(c != null) {
									attackPlayer(c, i);
								} else {
									npcs[i].killerId = 0;
									npcs[i].underAttack = false;
									npcs[i].facePlayer(0);
								}
							}
						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}
				
				
		
				/**
				* Random walking and walking home
				**/
				if (npcs[i] == null) continue;
				if((!npcs[i].underAttack || npcs[i].walkingHome) && npcs[i].randomWalk && !npcs[i].isDead) {
					npcs[i].facePlayer(0);
					npcs[i].killerId = 0;	
					if(npcs[i].spawnedBy == 0) {
						if((npcs[i].absX > npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE) || (npcs[i].absX < npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE) || (npcs[i].absY > npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE) || (npcs[i].absY < npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)) {
							npcs[i].walkingHome = true;
						}
					}

					if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
						npcs[i].walkingHome = false;
					} else if(npcs[i].walkingHome) {
						npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
			      		npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);
						npcs[i].getNextNPCMovement(i); 
						npcs[i].updateRequired = true;
					}
					if(npcs[i].walkingType == 1) {
						if(Misc.random(3)== 1 && !npcs[i].walkingHome) {
							int MoveX = 0;
							int MoveY = 0;			
							int Rnd = Misc.random(9);
							if (Rnd == 1) {
								MoveX = 1;
								MoveY = 1;
							} else if (Rnd == 2) {
								MoveX = -1;
							} else if (Rnd == 3) {
								MoveY = -1;
							} else if (Rnd == 4) {
								MoveX = 1;
							} else if (Rnd == 5) {
								MoveY = 1;
							} else if (Rnd == 6) {
								MoveX = -1;
								MoveY = -1;
							} else if (Rnd == 7) {
								MoveX = -1;
								MoveY = 1;
							} else if (Rnd == 8) {
								MoveX = 1;
								MoveY = -1;
							}
										
							if (MoveX == 1) {
								if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}
							
							if (MoveX == -1) {
								if (npcs[i].absX - MoveX > npcs[i].makeX - 1)  {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}
							
							if(MoveY == 1) {
								if(npcs[i].absY + MoveY < npcs[i].makeY + 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}
							
							if(MoveY == -1) {
								if(npcs[i].absY - MoveY > npcs[i].makeY - 1)  {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}
								

							int x = (npcs[i].absX + npcs[i].moveX);
							int y = (npcs[i].absY + npcs[i].moveY);
							if (VirtualWorld.I(npcs[i].heightLevel, npcs[i].absX, npcs[i].absY, x, y, 0))
								npcs[i].getNextNPCMovement(i);
							else
							{
								npcs[i].moveX = 0;
								npcs[i].moveY = 0;
							} 
							npcs[i].updateRequired = true;
						}
					}
				}
		
				
				if (npcs[i].isDead) {
					if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {
						npcs[i].updateRequired = true;
						npcs[i].facePlayer(0);
						npcs[i].killedBy = getNpcKillerId(i);
						npcs[i].animNumber = getCombatEmote(i, "Death"); // dead emote
						npcs[i].animUpdateRequired = true;
						npcs[i].freezeTimer = 0;
						npcs[i].applyDead = true;
						npcs[i].splashDelay = -1;
						killedBarrow(i);
						if (isFightCaveNpc(i))
							killedTzhaar(i);
						if (isBarbNpc(i))
							killedBarb(i);
						npcs[i].actionTimer = 4; // delete time
						resetPlayersInCombat(i);
					} else if (npcs[i].actionTimer == 0 && npcs[i].applyDead == true &&  npcs[i].needRespawn == false) {						
						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						if (!npcs[i].inBarbDef())
						dropItems(i);
						appendSlayerExperience(i);
						appendKillCount(i);
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;				
						npcs[i].HP = npcs[i].MaxHP;
						npcs[i].animNumber = 0x328;
						npcs[i].updateRequired = true;
						npcs[i].animUpdateRequired = true;
						if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
							Server.objectManager.removeObject(npcs[i].absX, npcs[i].absY);
						}
						if (npcs[i].npcType == 2745) {
							handleJadDeath(i);
						}
					} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn == true) {					
						if(npcs[i].spawnedBy > 0 || npcs[i].summonedFor > 0) {
							npcs[i] = null;
						} else {
							int old1 = npcs[i].npcType;
							int old2 = npcs[i].makeX;
							int old3 = npcs[i].makeY;
							int old4 = npcs[i].heightLevel;
							int old5 = npcs[i].walkingType;
							int old6 = npcs[i].MaxHP;
							int old7 = npcs[i].maxHit/10;
							int old8 = npcs[i].attack;	
							int old9 = npcs[i].defence;
							
							npcs[i] = null;
							newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9);
						}
					}
				}
			}
		}
	}
       
	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
			case 2550:
				if (npcs[i].firstAttacker > 0)
					return false;
			break;
		}
		return true;
	}
	   
	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
			case 2558:
			return true;
			case 2562:
			if (npcs[i].attackType == 2)
				return true;
			case 2550:
			if (npcs[i].attackType == 1)
				return true;	
			default:
			return false;
		}
	
	
	}
	
	/**
	* Npc killer id?
	**/
	
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int count = 0;
		int killerId = 0;
		for (int p = 1; p < Config.MAX_PLAYERS; p++)  {	
			if (Server.playerHandler.players[p] != null) {
				if(Server.playerHandler.players[p].lastNpcAttacked == npcId) {
					if(Server.playerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = Server.playerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					Server.playerHandler.players[p].totalDamageDealt = 0;
				}	
			}
		}				
		return killerId;
	}
		
	/**
	 * 
	 */
	private void killedBarrow(int i) {
		Client c = (Client)Server.playerHandler.players[npcs[i].killedBy];
		if(c != null) {
			for(int o = 0; o < c.barrowsNpcs.length; o++){
				if(npcs[i].npcType == c.barrowsNpcs[o][0]) {
					c.barrowsNpcs[o][1] = 2; // 2 for dead
					c.barrowsKillCount++;
				}
			}
		}
	}
	
	private void killedTzhaar(int i) {
		final Client c2 = (Client)Server.playerHandler.players[npcs[i].spawnedBy];
		c2.tzhaarKilled++;
		//System.out.println("To kill: " + c2.tzhaarToKill + " killed: " + c2.tzhaarKilled);
		if (c2.tzhaarKilled == c2.tzhaarToKill) {
			//c2.sendMessage("STARTING EVENT");
			c2.waveId++;
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer c) {
					if (c2 != null) {
						Server.fightCaves.spawnNextWave(c2);
					}	
					c.stop();
				}
			}, 7500);
		}
	}
	
	private void killedBarb(int i) {
		final Client c2 = (Client)Server.playerHandler.players[npcs[i].spawnedBy];
		c2.barbsKilled++;
		if (c2.barbsKilled == c2.barbsToKill) {
			c2.barbWave++;
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer c) {
					if (c2 != null) {
						Server.barbDefence.spawnWave(c2);
					}	
					c.stop();
				}
			}, 2500);
		}
	}
	
	public void handleJadDeath(int i) {
		Client c = (Client)Server.playerHandler.players[npcs[i].spawnedBy];
		c.getItems().addItem(6570,1);
		c.sendMessage("Congratulations on completing the fight caves minigame!");
		c.getPA().resetTzhaar();
		c.waveId = 300;
	}
	
	
	/**
	* Dropping Items!
	**/
	
	public boolean rareDrops(int i) {
		return Misc.random(NPCDrops.dropRarity.get(npcs[i].npcType)) == 0;
	}
	
	
	public void dropItems(int i) {
		int npc = 0;
		//long start = System.currentTimeMillis();
		Client c = (Client)Server.playerHandler.players[npcs[i].killedBy];
		if(c != null) {
			if (npcs[i].npcType == 912 || npcs[i].npcType == 913 || npcs[i].npcType == 914)
				c.magePoints += 1;

			if (NPCDrops.constantDrops.get(npcs[i].npcType) != null) {
				for (int item : NPCDrops.constantDrops.get(npcs[i].npcType)) {
					Server.itemHandler.createGroundItem(c, item, npcs[i].absX, npcs[i].absY, 1, c.playerId);
					//if (c.clanId >= 0)
						//Server.clanChat.handleLootShare(c, item, 1);
				}	
			}
                        
                        Server.itemHandler.createGroundItem(c, bonesnashesDrop(npcs[i].npcType), npcs[i].absX, npcs[i].absY, 1, c.playerId);
			
			if (NPCDrops.dropRarity.get(npcs[i].npcType) != null) {
				if (rareDrops(i)) {
					int random = Misc.random(NPCDrops.rareDrops.get(npcs[i].npcType).length-1);
					Server.itemHandler.createGroundItem(c, NPCDrops.rareDrops.get(npcs[i].npcType)[random][0], npcs[i].absX, npcs[i].absY, NPCDrops.rareDrops.get(npcs[i].npcType)[random][1], c.playerId);
					if (c.clanId >= 0)
						Server.clanChat.handleLootShare(c, NPCDrops.rareDrops.get(npcs[i].npcType)[random][0], NPCDrops.rareDrops.get(npcs[i].npcType)[random][1]);
				} else {
					int random = Misc.random(NPCDrops.normalDrops.get(npcs[i].npcType).length-1);
					Server.itemHandler.createGroundItem(c, NPCDrops.normalDrops.get(npcs[i].npcType)[random][0], npcs[i].absX, npcs[i].absY, NPCDrops.normalDrops.get(npcs[i].npcType)[random][1], c.playerId);
					//Server.clanChat.handleLootShare(c, NPCDrops.normalDrops.get(npcs[i].npcType)[random][0], NPCDrops.normalDrops.get(npcs[i].npcType)[random][1]);
				}
			}	
			
		}
		//System.out.println("Took: " + (System.currentTimeMillis() - start));
	}
	
	public void appendKillCount(int i) {
		Client c = (Client)Server.playerHandler.players[npcs[i].killedBy];
		if(c != null) {
			int[] kcMonsters = {122,49,2558,2559,2560,2561,2550,2551,2552,2553,2562,2563,2564,2565};
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (c.killCount < 20) {
						c.killCount++;
						c.sendMessage("Killcount: " + c.killCount);
					} else {
						c.sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}	
	}
	
	
	
	

	//id of bones dropped by npcs
	public int bonesnashesDrop(int NPCID) {
		String npc = getNpcListName(NPCID).replaceAll("_", " ").toLowerCase();//
                System.out.println(npc);                
               /*String [] impiousashes = {"imp", "icefiend", "pyrefiend", "killerwatt"};
                for (int a = 0; a < impiousashes.length; a++) {
                    if(npc.contains(impiousashes[a])){
			return -1; //IMPIOUS ASHES (DONT KNOW ID)
		}
                }
                String [] wolfbones = {"wolf", "werewolf", "canifis citizens", "hati"};
                for (int b = 0; b < wolfbones.length; b++) {
                    if(npc.contains(wolfbones[b])){
			return 2859; //Wolf Bones
		}
                }
                String [] smallmonkeybones = {"monkey"};
                for (int c = 0; c < smallmonkeybones.length; c++) {
                    if(npc.contains(smallmonkeybones[c])){
			return 3179; //Small Monkey Bones
		}
                }
                String [] batbones = {"bat"};
                for (int d = 0; d < batbones.length; d++) {
                    if(npc.contains(batbones[d])){
			return 530; //Bat Bones
		}
                }*/
                if (npc.contains("imp") || npc.contains("icefiend") || npc.contains("pyrefiend") || npc.contains("killerwatt")) {
			return -1; //IMPIOUS ASHES (DONT KNOW ID)
		}
                if (npc.contains("wolf") || npc.contains("werewolf") || npc.contains("canifis citizens") || npc.contains("hati")) {
			return 2859; //Wolf Bones
		}
                if (npc.contains("monkey")) {
			return 3179; //Small Monkey Bones
		}
                if (npc.contains("bat")) {
			return 530; //Bat Bones
		}
                 String [] accursedashes = {"death spawn", "lesser demon", "greater demon", "evil spirit", "fungal mage"};
                for (int e = 0; e < accursedashes.length; e++) {
                    if(npc.contains(accursedashes[e])){
			return -1; //Accursed ashes (Dont know ID)
		}
                }
                String [] bigbones = {"giant frog", "ogre", "giant skeleton", "ork", "mountain troll", "giant", "glod", "cyclossus", "cyclops", "giant mole", "nex"};
                for (int f = 0; f < bigbones.length; f++) {
                    if(npc.contains(bigbones[f])){
			return 532; //Big Bones
		}
                }
                /*String [] jogrebones = {"jogre"};
                for (int g = 0; g < jogrebones.length; g++) {
                    if(npc.contains(jogrebones[g])){
                    return 3125; //Jogre Bones
                }
                }
                String [] bigmonkeybones = {"monkey guard"};
                for (int h = 0; h < bigmonkeybones.length; h++) {
                    if(npc.contains(bigmonkeybones[h])){
                    return 3181; //Big Monkey Bones
                }
                }
                String [] zogrebones = {"skogre", "zogre"};
                for (int i = 0; i < zogrebones.length; i++) {
                    if(npc.contains(zogrebones[i])){
                    return 4812; //Zogre Bones
                }
                }
                String [] shaikahanbones = {"shaikahan"};
                for (int j = 0; j < shaikahanbones.length; j++) {
                    if(npc.contains(shaikahanbones[j])){
                    return 3123; //Shaikahan bones
                }  
                }
                String [] babydragonbones = {"baby black dragon", "baby red dragon", "baby blue dragon"};
                for (int k = 0; k < babydragonbones.length; k++) {
                    if(npc.contains(babydragonbones[k])){
                    return 534; //Baby dragon bones
                }
                }
                String [] wyvernbones = {"wyvern"};
                for (int l = 0; l < wyvernbones.length; l++) {
                    if(npc.contains(wyvernbones[l])){
                    return 6812; //Wyvern bones
                }
                }*/
                                if (npc.contains("jogre")) {
                    return 3125; //Jogre Bones
                }
                if (npc.contains("monkey guard")) {
                    return 3181; //Big Monkey Bones
                }
                if (npc.contains("skogre") || npc.contains("skogre")) {
                    return 4812; //Zogre Bones
                }
                if (npc.contains("shaikahan")) {
                    return 3123; //Shaikahan bones
                }     	
                if (npc.contains("baby black dragon") || npc.contains("baby red dragon") || npc.contains("baby blue dragon")) {
                    return 534; //Baby dragon bones
                }   
                if (npc.contains("wyvern")) {
                    return 6812; //Wyvern bones
                } 
                 String [] infernalashes = {"doomion", "holthion", "othanian", "agrith naar", "nechryael", "abyssal demon", "zakl'n gritch", "tstanon karlak", "balfrug kreeyath", "tormented demon", "k'ril tsutsaroth"};
                for (int m = 0; m < infernalashes.length; m++) {
                    if(npc.contains(infernalashes[m])){
                    return -1; //Infernal Ashes (Dont know ID)
                }
                }
                /*if (npc.contains("dragon")) {
			return 536;
		}*/
                if(npc.contains("general graardor")) { 
                    return 4834; //OURG BONES
                }
                /*String [] dragonbones = {"green dragon"};
                for (int n = 0; n < dragonbones.length; n++) {
                    if(npc.contains(dragonbones[n])){
			//return 536;
                        return 11694;
		}
                }
                String [] ourgbones = {"general graardor"};
                for (int o = 0; o < ourgbones.length; o++) {
                    if(npc.contains(ourgbones[o])){
                    return 4834; //OURG BONES
                }
                }*/
                String [] dagganothbones = {"dagganoth prime", "dagganoth rex", "dagganoth sentinel", "dagganoth supreme"};
                for (int p = 0; p < dagganothbones.length; p++) {
                    if(npc.contains(dagganothbones[p])){
			return 536;
		}
                }
                
                /*String [] frostdragonbones = {"frost dragon"};
                for (int q = 0; q < frostdragonbones.length; q++) {
                    if(npc.contains(frostdragonbones[q])){
			return -1; //Frost dragon bones (Dont know ID)
		}
                }*/
                if (npc.contains("frost dragon")) {
			return -1; //Frost dragon bones (Dont know ID)
		}
                return 526;
		}	

	public int getStackedDropAmount(int itemId, int npcId) {
		switch (itemId) {
			case 995:
				switch (npcId) {
					case 1:
					return 50+ Misc.random(50);
					case 9:
					return 133 + Misc.random(100);
					case 1624:
					return 1000 + Misc.random(300);
					case 1618:
					return 1000 + Misc.random(300);
					case 1643:
					return 1000 + Misc.random(300);
					case 1610:
					return 1000 + Misc.random(1000);
					case 1613:
					return 1500 + Misc.random(1250);
					case 1615:
					return 3000;
					case 18:
					return 500;
					case 101:
					return 60;
					case 913:
					case 912:
					case 914:
					return 750 + Misc.random(500);
					case 1612:
					return 250 + Misc.random(500);
					case 1648:
					return 250 + Misc.random(250);
					case 90:
					return 200;
					case 82:
					return 1000 + Misc.random(455);
					case 52:
					return 400 + Misc.random(200);
					case 49:
					return 1500 + Misc.random(2000);
					case 1341:
					return 1500 + Misc.random(500);
					case 26:
					return 500 + Misc.random(100);
					case 20:
					return 750 + Misc.random(100);
					case 21: 
					return 890 + Misc.random(125);
					case 117:
					return 500 + Misc.random(250);
					case 2607:
					return 500 + Misc.random(350);
				}			
			break;
			case 11212:
			return 10 + Misc.random(4);
			case 565:
			case 561:
			return 10;
			case 560:
			case 563:
			case 562:
			return 15;
			case 555:
			case 554:
			case 556:
			case 557:
			return 20;
			case 892:
			return 40;
			case 886:
			return 100;
			case 6522:
			return 6 + Misc.random(5);
			
		}
	
		return 1;
	}
	
	/**
	* Slayer Experience
	**/	
	public void appendSlayerExperience(int i) {
		int npc = 0;
		Client c = (Client)Server.playerHandler.players[npcs[i].killedBy];
		if(c != null) {
			if (c.slayerTask == npcs[i].npcType){
				c.taskAmount--;
				c.getPA().addSkillXP((npcs[i].MaxHP * Config.SLAYER_EXPERIENCE)/10, 18);
				if (c.taskAmount <= 0) {
					c.getPA().addSkillXP(((npcs[i].MaxHP * 8) * Config.SLAYER_EXPERIENCE)/10, 18);
					c.slayerTask = -1;
					c.sendMessage("You completed your slayer task. Please see a slayer master to get a new one.");
				}
			}
		}
	}
	
	/**
	 *	Resets players in combat
	 */
	
	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null)
				if (Server.playerHandler.players[j].underAttackBy2 == i)
					Server.playerHandler.players[j].underAttackBy2 = 0;
		}
	}
	
	
	/**
	* Npc Follow Player
	**/
	
	public int GetMove(int Place1,int Place2) { 
		if ((Place1 - Place2) == 0) {
            return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
        	return 0;
   	 }
	
	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
			case 2892:
			case 2894:
			return false;
		}
		return true;
	}
	
	public void followPlayer(int i, int playerId) {
		if (Server.playerHandler.players[playerId] == null) {
			return;
		}
		if (Server.playerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true; 
	      	npcs[i].underAttack = false;	
			return;
		}
		
		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}
		
		int playerX = Server.playerHandler.players[playerId].absX;
		int playerY = Server.playerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, distanceRequired(i)))
			return;
		if((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absX > npcs[i].makeX - Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY < npcs[i].makeY + Config.NPC_FOLLOW_DISTANCE) && (npcs[i].absY > npcs[i].makeY - Config.NPC_FOLLOW_DISTANCE))) {
			if(npcs[i].heightLevel == Server.playerHandler.players[playerId].heightLevel) {
				if(Server.playerHandler.players[playerId] != null && npcs[i] != null) {
					if(playerY < npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if(playerY > npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if(playerX < npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if(playerX > npcs[i].absX)  {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if(playerX == npcs[i].absX || playerY == npcs[i].absY) {
						int o = Misc.random(3);
						switch(o) {
							case 0:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY+1);
							break;
							
							case 1:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY-1);
							break;
							
							case 2:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX+1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
							
							case 3:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX-1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
						}	
					}
					int x = (npcs[i].absX + npcs[i].moveX);
					int y = (npcs[i].absY + npcs[i].moveY);
					npcs[i].facePlayer(playerId);
					if (checkClipping(i))
						npcs[i].getNextNPCMovement(i);
					else {
						npcs[i].moveX = 0;
						npcs[i].moveY = 0;
					}
					npcs[i].facePlayer(playerId);
			      	npcs[i].updateRequired = true;
				}	
			}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true; 
		   	npcs[i].underAttack = false;	
		}
	}
	
	public void summoningFollow(int i, int playerId) {
		if (Server.playerHandler.players[playerId] == null) {
			return;
		}
		Client c = (Client) Server.playerHandler.players[playerId];
		NPC n = npcs[i];
		int playerX = c.getX();
		int playerY = c.getY();
		n.randomWalk = false;
		if(n.heightLevel == Server.playerHandler.players[playerId].heightLevel && goodDistance(n.getX(), n.getY(), playerX, playerY, 12)) {
			if(c != null && n != null) {
				if(playerY < n.absY) {
					n.moveX = GetMove(n.absX, playerX);
					n.moveY = GetMove(n.absY, playerY + 1);
				} else if(playerY > n.absY) {
					n.moveX = GetMove(n.absX, playerX);
					n.moveY = GetMove(n.absY, playerY - 1);
				} else if(playerX < n.absX) {
					n.moveX = GetMove(n.absX, playerX + 1);
					n.moveY = GetMove(n.absY, playerY);
				} else if(playerX > n.absX)  {
					n.moveX = GetMove(n.absX, playerX - 1);
					n.moveY = GetMove(n.absY, playerY);
				} else if(playerX == n.absX || playerY == n.absY) {
					int o = Misc.random(3);
					switch(o) {
						case 0:
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY+1);
						break;
							
						case 1:
						n.moveX = GetMove(n.absX, playerX);
						n.moveY = GetMove(n.absY, playerY-1);
						break;
							
						case 2:
						n.moveX = GetMove(n.absX, playerX+1);
						n.moveY = GetMove(n.absY, playerY);
						break;
							
						case 3:
						n.moveX = GetMove(n.absX, playerX-1);
						n.moveY = GetMove(n.absY, playerY);
						break;
					}	
				}
			}	
		} else {
			c.getSummoning().callFamiliar();
		}
		n.facePlayer(playerId);
		n.getNextNPCMovement(i);
		n.facePlayer(playerId);
		n.updateRequired = true;
	}
	
	
	public boolean checkClipping(int i) {
		NPC npc = npcs[i];
		int size = npcSize(i);
		
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (!VirtualWorld.I(npc.heightLevel, npc.absX + x, npc.absY + y, npc.absX + npc.moveX, npc.absY + npc.moveY, 0))
					return false;				
			}
		}
		return true;
	}
	
	/**
	* load spell
	**/
	public void loadSpell2(int i) {
		npcs[i].attackType = 3;
		int random = Misc.random(3);
		if (random == 0) {
			npcs[i].projectileId = 393; //red
			npcs[i].endGfx = 430;
		} else if (random == 1) {
			npcs[i].projectileId = 394; //green
			npcs[i].endGfx = 429;
		} else if (random == 2) {
			npcs[i].projectileId = 395; //white
			npcs[i].endGfx = 431;
		} else if (random == 3) {
			npcs[i].projectileId = 396; //blue
			npcs[i].endGfx = 428;
		}
	}
	
	public void loadSpell(int i) {
            Client c = (Client) Server.playerHandler.players[npcs[i].killerId];
		NPC n = npcs[i];
		switch(npcs[i].npcType) {
			/* 0 - melee, 1 - range, 2 - mage */
			case 5247: // Penance queen
				int penance = Misc.random(1);
				if (penance == 0) {
					n.projectileId = 871;
					n.endGfx = 872;
					n.attackType = 1;
				} else {
					n.projectileId = -1;
					n.endGfx = -1;
					n.attackType = 0;
				}
			break;
			case 1183: // Elf warrior (ranged)
				n.gfx100(250);
				n.projectileId = 249;
				n.attackType = 1;
			break;
			case 5229: case 5230: case 5231: case 5232: case 5233: case 5234: case 5235: case 5236: case 5237: // Penance ranger (ranged)
				n.projectileId = 866;
				n.endGfx = 865;
				n.attackType = 1;
			break;
			case 2892: // Spinolyp (mage) 
				n.projectileId = 94;
				n.attackType = 2;
				n.endGfx = 95;
			break;
			case 2894: // Spinolyp (ranged)
				npcs[i].projectileId = 298;
				npcs[i].attackType = 1;
			break;
		case 50:
			int r5 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY, Server.playerHandler.players[npcs[i].killerId].absX, Server.playerHandler.players[npcs[i].killerId].absY, 2))
				r5 = Misc.random(5);
			else
				r5 = Misc.random(3);
			if (r5 == 0) {
				npcs[i].projectileId = 393; // red
				npcs[i].attackType = 3;
			} else if (r5 == 1) {
				npcs[i].projectileId = 394; // green
				npcs[i].attackType = 3;
                                if (c.poisonDamage <= 0) {
					c.getPA().appendPoison(c);
				}
			} else if (r5 == 2) {
				npcs[i].projectileId = 395; // white
				npcs[i].attackType = 3;
				if (c.freezeTimer <= 0) {
					c.freezeTimer = 19;
					c.sendMessage("You have been Frozen!");
				}
			} else if (r5 == 3) {
				npcs[i].projectileId = 396; // blue
				npcs[i].attackType = 3;
			} else if (r5 == 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].attackType = 0;
			} else if (r5 == 5) {
				npcs[i].projectileId = -1; // melee
				npcs[i].attackType = 0;
			}
			break;
			case 2025:
			npcs[i].attackType = 2;
			int r = Misc.random(3);
			if(r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if(r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if(r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if(r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
			case 2881://supreme
				npcs[i].attackType = 1;
				npcs[i].projectileId = 298;
			break;
			
			case 2882://prime
				npcs[i].attackType = 2;
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 477;
			break;
			
			case 2028:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 27;
			break;
			
			case 3200:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = 1;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = 2;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
			case 2745:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY, Server.playerHandler.players[npcs[i].spawnedBy].absX, Server.playerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			}			
			break;
			case 2743:
				npcs[i].attackType = 2;
				npcs[i].projectileId = 445;
				npcs[i].endGfx = 446;
			break;
			
			case 2631:
				npcs[i].attackType = 1;
				npcs[i].projectileId = 443;
			break;
		}
	}
		
	/**
	* Distanced required to attack
	**/	
	public int distanceRequired(int i) {
		if (npcs[i].attackType == 1) 
			return 8;
		switch(npcs[i].npcType) {

			case 2025:
			case 2028:
				return 6;
			case 2562:
				return 2;
			case 2881://dag kings
			case 2882:
			case 3200://chaos ele
			case 2743:
                        case 50:
			case 2631:
			case 2745:
			case 5229: case 5230: case 5231: case 5232: case 5233: case 5234: case 5235: case 5236: case 5237: // Penance rangers
			case 8133:
				return 8;
			case 2883://rex
				return 1;
			case 2552:
			case 2553:
			case 2556:
			case 2557:
			case 2558:
			case 2559:
			case 2560:
			case 2564:
			case 2565:
				return 9;
			//things around dags
			case 2892:
			case 2894:
				return 10;
			default:
			return 1;
		}
	}
	
	public int followDistance(int i) {
		switch (npcs[i].npcType) {
			case 2550:
			case 2551:
			case 2562:
			case 2563:
			return 8;
			case 2883:
			return 4;
 		case 50:
			return 18;
			case 2881:
			case 2882:
			return 1;
		
		}
		return 0;
		
	
	}
	
	public int getProjectileSpeed(int i) {
		switch(npcs[i].npcType) {
			case 2881:
			case 2882:
			case 3200:
			return 85;
			case 8133:
			return 100;
			case 2745:
			return 130;
case 50:
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 85;

			
			case 2025:
			return 85;
			
			case 2028:
			return 80;
			
			default:
			return 85;
		}
	}
	
	/**
	*NPC Attacking Player
	**/
	
	public void attackPlayer(Client c, int i) {
		if(npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.playerId) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti() && (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}
			npcs[i].facePlayer(c.playerId);
			boolean special = false;//specialCase(c,i);
			if(goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), distanceRequired(i)) || special) {
				if(c.respawnTimer <= 0) {
					npcs[i].facePlayer(c.playerId);
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[i].attackType = 0;
					if (npcs[i].npcType == 8133) {
						corpAttack(c, i, Misc.random(2));
						if (npcs[i].attackType == 5)
							npcs[i].hitDelayTimer = -1;
					} else if (special)
						loadSpell2(i);
					else
						loadSpell(i);
					usingSpecial = false;
					handleSpecialNPC(npcs[i]);
					if (npcs[i].attackType == 3)
						npcs[i].hitDelayTimer += 2;
					if (multiAttacks(i)) {
						multiAttackGfx(i, npcs[i].projectileId);
						startAnimation(getCombatEmote(i, "Attack"), i);
						npcs[i].oldIndex = c.playerId;
						return;
					}
					if(npcs[i].projectileId > 0) {
						int nX = Server.npcHandler.npcs[i].getX() + offset(i);
						int nY = Server.npcHandler.npcs[i].getY() + offset(i);
						int pX = c.getX();
						int pY = c.getY();
						int offX = (nY - pY)* -1;
						int offY = (nX - pX)* -1;
						c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i), npcs[i].projectileId, 43, 31, !npcs[i].splash ? -c.getId() - 1 : 0, 65);
					}
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = c.playerId;
					if (npcs[i].npcType != 8133 && !usingSpecial)
						startAnimation(getCombatEmote(i, "Attack"), i);
					c.getPA().removeAllWindows();
				} 
			}			
		}
	}
	
	public boolean usingSpecial;
	private void handleSpecialNPC(NPC n) {
		if (Misc.random(2) != 0)
			return;
		switch (n.npcType) {
			case 10773:
				n.requestAnimation(13155, 0);
				n.gfx0(1);
				n.attackType = 3;
				usingSpecial = true;
			break;
			default:
			break;
		}
	}
	
	public void corpAttack(Client c, int npc, int type) {
		NPC n = Server.npcHandler.npcs[npc];
		n.projectile = c;
		switch (type) {
			case 0: // Melee
				startAnimation(10058, npc);
				n.attackType = 0;
			break;
			case 1: // Magic (Single target)
				startAnimation(10053, npc);
				n.projectileId = 1825;
				n.endGfx = -1;
				n.attackType = 2;
			break;
			case 2: // Magic (Splash (no intended target))
				startAnimation(10053, npc);
				n.attackType = 2;
				n.splashCoord[0][0] = c.getX();
				n.splashCoord[0][1] = c.getY();
				n.projectileId = 1824;
				n.splash = true;
				n.splashDelay = 8;
				n.attackType = 5;
			break;
		}
	}
	
	private void corpSplash(NPC n) {
		if (n == null)
			return;
		for (int i = 1; i < n.splashCoord.length; i++) {
			boolean neg = Misc.random(1) == 1;
			n.splashCoord[i][0] = n.splashCoord[0][0] + (neg ? (-1 - Misc.random(2)) : (1 + Misc.random(2)));
			neg = Misc.random(1) == 1;
			n.splashCoord[i][1] = n.splashCoord[0][1] + (neg ? (-1 - Misc.random(2)) : (1 + Misc.random(2)));
			n.projectile.getPA().createPlayersProjectile(n.splashCoord[0][0], n.splashCoord[0][1], (n.splashCoord[0][1] - n.splashCoord[i][1]), (n.splashCoord[0][0] - n.splashCoord[i][0]), 50, 100, 1824, 43, 31, 0, 65);
		}
	}
	
	public int offset(int i) {
		switch (npcs[i].npcType) {
			case 50:
			return 2;
			case 2881:
			case 2882:
			return 1;
			case 2745:
			case 2743:
			return 1;
				case 8133:
					return 3;
		}
		return 0;
	}
	
	public boolean specialCase(Client c, int i) { //responsible for npcs that much 
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 8) && !goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), distanceRequired(i)))
			return true;
		return false;
	}
	
	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780 && !(npcType >= 2440 && npcType <= 2446);
	}
	public String soakType(int i) {
        if(npcs[i].attackType == 0) {
            return "Melee";
        }
        if(npcs[i].attackType == 1) {
            return "Range";
        } 
        if(npcs[i].attackType == 2 || npcs[i].attackType == 3 ){
            return "Magic";
        }
        return "";
        }
	public void applyDamage(int i) {
		if(npcs[i] != null) {
			if(Server.playerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			Client c = (Client) Server.playerHandler.players[npcs[i].oldIndex];
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet == 1)
					c.npcIndex = i;
			if(c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0 && c.oldNpcIndex == 0) {
				c.startAnimation(c.getCombat().getBlockEmote());
			}
			if(c.respawnTimer <= 0) {	
				int damage = 0;
				if(npcs[i].attackType == 0) {
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc.random(Server.npcHandler.npcs[i].attack)) {
						damage = 0;
					}				
					if(c.prayerActive[18] || c.curseActive[c.curses().DEFLECT_MELEE]) { // protect from melee
						if (c.curseActive[c.curses().DEFLECT_MELEE])
							c.curses().deflectNPC(npcs[i], 0, 0);
						damage = 0;
					}				
					if (c.constitution - damage < 0) { 
						damage = c.constitution;
					}
				}
				
				if(npcs[i].attackType == 1) { // range
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc.random(Server.npcHandler.npcs[i].attack)) {
						damage = 0;
					}					
					if(c.prayerActive[17] || c.curseActive[c.curses().DEFLECT_MISSILES]) {
						if (c.curseActive[c.curses().DEFLECT_MISSILES])
							c.curses().deflectNPC(npcs[i], 0, 1);
						damage = 0;
					}				
					if (c.constitution- damage < 0) { 
						damage = c.constitution;
					}
					if (npcs[i].endGfx > 0) {
						c.gfx100(npcs[i].endGfx);
					}
				}
				
				if(npcs[i].attackType == 2) { // magic
					damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(Server.npcHandler.npcs[i].attack)) {
						damage = 0;
						magicFailed = true;
					}				
					if(c.prayerActive[16] || c.curseActive[c.curses().DEFLECT_MAGIC]) { // protect from magic
						if (c.curseActive[c.curses().DEFLECT_MAGIC])
							c.curses().deflectNPC(npcs[i], 0, 2);
						damage = 0;
						magicFailed = true;
					}				
					if (c.constitution - damage < 0) { 
						damage = c.constitution;
					}
					if(npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
						c.gfx100(npcs[i].endGfx);
					} else {
						c.gfx100(85);
					}
				}
				
				if (npcs[i].attackType == 3) { 
					int anti = c.getPA().antiFire();
					switch (anti) {
					case 0:
						damage = Misc.random(300) + 150;
						c.sendMessage("You are badly burnt by the dragon fire!");
					break;
					case 1:
						c.sendMessage("You deflect some of the dragon's fire.");
						damage = Misc.random(120);
					break;
					case 2:
						c.sendMessage("You deflect some of the dragon's fire.");
						damage = Misc.random(60);
					break;
					}
					if (c.constitution - damage < 0)
						damage = c.constitution;
					c.gfx100(npcs[i].endGfx);
				}
				
				handleSpecialEffects(c, i, damage);
                                int soak = c.getCombat().damageSoaked(damage, soakType(i));
				damage -= soak;
				c.logoutDelay = System.currentTimeMillis(); // logout delay
                                if (npcs[i].attackType == 3) {
				c.getCombat().appendHit(c, damage, 0, -1, false, soak);
                                } else {
                                c.getCombat().appendHit(c, damage, 0, npcs[i].attackType, false, soak);
                                }
                               
			}
		}
	}
	
	public void handleSpecialEffects(Client c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getPA().refreshSkill(5);
						c.getPA().appendPoison(c);
					}
				}			
			}	
		}
	
	}
		
		

	public void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}
	
	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
		  for (int j = 0; j <= distance; j++) {
			if ((objectX + i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
				return true;
			} else if ((objectX - i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
				return true;
			} else if (objectX == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
				return true;
			}
		  }
		}
		return false;
	}
	
      
	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
			case 2558:
				if (npcs[i].attackType == 2)
					return 28;
				else
					return 68;
			case 2562:
				return 31;
			case 2550:
				return 36;
		}
		return 1;
	}
	
	
	public boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./"+FileName));
		} catch(FileNotFoundException fileex) {
			Misc.println(FileName+": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			Misc.println(FileName+": error loading file.");
			return false;
		}
		while(EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), getNpcListHP(Integer.parseInt(token3[0])), Integer.parseInt(token3[5]), Integer.parseInt(token3[6]), Integer.parseInt(token3[7]));
				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try { characterfile.close(); } catch(IOException ioexception) { }
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch(IOException ioexception1) { EndOfFile = true; }
		}
		try { characterfile.close(); } catch(IOException ioexception) { }
		return false;
	}

	public int getNpcListHP(int npcId) {
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] != null) {
				if (NpcList[i].npcId == npcId) {
					return NpcList[i].npcHealth;
				}
			}
		}
		return 0;
	}
	
	public static String getNpcListName(int npcId) {
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] != null) {
				if (NpcList[i].npcId == npcId) {
					return NpcList[i].npcName;
				}
			}
		}
		return "nothing";
	}

	public boolean loadNPCList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./"+FileName));
		} catch(FileNotFoundException fileex) {
			Misc.println(FileName+": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch(IOException ioexception) {
			Misc.println(FileName+": error loading file.");
			return false;
		}
		while(EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]), Integer.parseInt(token3[3]));
					npcCombat[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try { characterfile.close(); } catch(IOException ioexception) { }
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch(IOException ioexception1) { EndOfFile = true; }
		}
		try { characterfile.close(); } catch(IOException ioexception) { }
		return false;
	}
	

}
