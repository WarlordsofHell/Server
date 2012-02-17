package server.model.npcs;

import server.Server;
import server.util.Misc;
import server.util.Stream;
import server.model.players.Client;

public class NPC {
	public int npcId;
	public int npcType;
	public int absX, absY;
	public int heightLevel;
	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction, walkingType, combatLevel;
	public int spawnX, spawnY;
    public int viewX, viewY;
	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public int attackType, projectileId, endGfx, spawnedBy, summonedFor, hitDelayTimer, HP, MaxHP, hitDiff, animNumber, actionTimer, enemyX, enemyY;
	public boolean applyDead, isDead, needRespawn, respawns;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex, underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean updateRequired;
	public boolean forcedChatRequired;
    public boolean faceToUpdateRequired;
	public int firstAttacker;
	public String forcedText;
	
	public NPC(int _npcId, int _npcType) {
		npcId = _npcId;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;		
	}
	
	public int getCombatLevel() {
		return Server.npcHandler.npcCombat[npcType];
	}
	
	public int splashDelay = -1;
	public boolean splash;
	public int[][] splashCoord = new int[5][2];
	public Client projectile = null;
	
	public void updateNPCMovement(Stream str) {
		if (direction == -1) {
			if (updateRequired) {
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {
			str.writeBits(1, 1);
			str.writeBits(2, 1);		
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);		
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	* Text update
	**/
	
	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}
	
	/**
	*Graphics
	**/	
	
	public int mask80var1 = 0;
    public int mask80var2 = 0;
    protected boolean mask80update = false;
	
    public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
	    str.writeDWord(mask80var2);
    }
	
	public void gfx100(int gfx){
		mask80var1 = gfx;
        mask80var2 = 6553600;
        mask80update = true;
		updateRequired = true;
	}
	
	public void gfx0(int gfx){
		mask80var1 = gfx;
        mask80var2 = 65536;
        mask80update = true;
		updateRequired = true;
	}
	
	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}
	
	public void requestAnimation(int animId, int i) {
		animNumber = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}
	
	/**
	*
	Face
	*
	**/
	
	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;
	
	private void appendSetFocusDestination(Stream str) {
        str.writeWordBigEndian(FocusPointX);
        str.writeWordBigEndian(FocusPointY);
    }
	
	public void turnNpc(int i, int j) {
        FocusPointX = 2 * i + 1;
        FocusPointY = 2 * j + 1;
        updateRequired = true;

    }
	
	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}
        	
	public void facePlayer(int player) {
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}
	
	
	public void appendNPCUpdateBlock(Stream str, Client c) {
		if(!updateRequired) return;		
		int updateMask = 0;
		if(animUpdateRequired) updateMask |= 0x10; 
		if(hitUpdateRequired2) updateMask |= 8;
		if(mask80update) updateMask |= 0x80;
		if(dirUpdateRequired) updateMask |= 0x20;
		if(forcedChatRequired) updateMask |= 1;
		if(hitUpdateRequired) updateMask |= 0x40;		
		if(FocusPointX != -1) updateMask |= 4;		
			
		str.writeByte(updateMask);
				
		if (animUpdateRequired) appendAnimUpdate(str);
		if (hitUpdateRequired2) appendHitUpdate2(str, c);
		if (mask80update)       appendMask80Update(str);
		if (dirUpdateRequired)  appendFaceEntity(str);
		if(forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired)  appendHitUpdate(str, c);
		if(FocusPointX != -1) appendSetFocusDestination(str);
		
	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
	}

	
	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if(dir == -1) return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if(Server.npcHandler.npcs[i].freezeTimer == 0) {
			direction = getNextWalkingDirection();
		}
	}


	public void appendHitUpdate(Stream str, Client c) {		
		if (HP <= 0) {
			isDead = true;
		}
		str.writeWordA(hitDiff); 
		str.writeByteS(hitMask);
		str.writeByte(hitIcon);
		str.writeWordA(HP); 
		str.writeWordA(MaxHP); 
	}
	
	public int hitDiff2 = 0;
	public boolean hitUpdateRequired2 = false;
	public int hitIcon, hitMask, hitIcon2, hitMask2;
	public void appendHitUpdate2(Stream str, Client c) {		
		if (HP <= 0) {
			isDead = true;
		}
		str.writeWordA(hitDiff2);
		str.writeByteC(hitMask2);
		str.writeByte(hitIcon2);
		str.writeWordA(HP); 
		str.writeWordA(MaxHP); 	
	}
	
	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;		
		}
		updateRequired = true;
	}
	
	public int getX() {
		return absX;
	}
	
	public int getY() {
		return absY;
	}
	
	public boolean inMulti() {
		if((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607) || 
			(absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839) ||  
			(absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967) || 
			(absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967) || 
			(absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831) || 
			(absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903) || 
			(absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711) || 
			(absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647) ||
			(absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619) ||
			(absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117) ||
			(absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630) ||
			(absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464) ||
			(absX >= 2654 && absX <= 2721 && absY >= 3705 && absY <= 3737) ||
			(coordsCheck(3147, 3193, 9737, 9778)) ||
			(absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)) {
			return true;
		}
		return false;
	}
	
	public boolean inBarbDef() {
		return (coordsCheck(3147, 3193, 9737, 9778));
	}
	
	public boolean coordsCheck(int X1, int X2, int Y1, int Y2) {
		return absX >= X1 && absX <= X2 && absY >= Y1 && absY <= Y2;
	}
	
	public boolean inWild() {
		if(absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966 ||
			absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {	
			return true;
		}
		return false;
	}
	
	public String barbRandom(Client c, int type) {
		switch (type) {
			case 0:	
				return "KILL THAT MONGREL!";
			case 1:
				return "YOU'RE MINE, " + c.playerName.toUpperCase() + "!";
			case 2:
				return "YOU REALLY THINK YOU CAN BEAT ME?";
			case 3:
				return "IS THAT ALL YOU'VE GOT WEAKLING?";
			case 4:
				return "FAILURE IS NOT AN OPTION MY MINIONS! ATTACK!";
		}
		return "";
	}
}
