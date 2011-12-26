package server.world;

import java.util.ArrayList;

import server.model.objects.Object;
import server.util.Misc;
import server.model.players.Client;
import server.Server;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();
	public void process() {
		for (Object o : objects) {
			if (o.tick > 0)
				o.tick--;
			else {
				updateObject(o);
				toRemove.add(o);
			}		
		}
		for (Object o : toRemove) {
			if (isObelisk(o.newId)) {
				int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);	
		}
		toRemove.clear();
	}
	
	public void removeObject(int x, int y) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				c.getPA().object(-1, x, y, 0, 10);			
			}	
		}	
	}
	
	public void updateObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);			
			}	
		}	
	}
	
	public void placeObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}	
		}
	}
	
	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}	
		return null;
	}
	
	public void loadObjects(Client c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o,c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
		}
		loadCustomSpawns(c);
		if (c.distanceToPoint(2813, 3463) <= 60) {
			c.getFarming().updateHerbPatch();
		}
	}
	
	private int[][] customObjects = {{}};
	public void loadCustomSpawns(Client c) {

		c.getPA().checkObjectSpawn(411, 3094, 3506, 2, 10); // Curse Prayers
		c.getPA().checkObjectSpawn(27254, 3221, 3224, 2, 10); // black portal

		c.getPA().checkObjectSpawn(13615, 2002, 4430, 0, 10);//portals
		c.getPA().checkObjectSpawn(13620, 2007, 4430, 0, 10);
		c.getPA().checkObjectSpawn(13619, 2014, 4430, 0, 10);

		c.getPA().checkObjectSpawn(2213, 3281, 2777, 0, 10);
		//EDGEVILLE - HOME - FOR NOW
		//Summononing
		c.getPA().checkObjectSpawn(28716, 3077, 3485, 2, 10); //Summon Pouch Creation
		//Prayer Recharge and Curse
		c.getPA().checkObjectSpawn(409, 3097, 3500, 2, 10); // Prayer Recharge
		c.getPA().checkObjectSpawn(411, 3095, 3500, 2, 10); //Curse Prayers
		c.getPA().checkObjectSpawn(-1, 3096, 3501, -1, 10); //Removed the tree there
		//Spellbook Switches
		c.getPA().checkObjectSpawn(6552, 3092, 3487, 2, 10); //ancient prayers
		c.getPA().checkObjectSpawn(410, 3097, 3486, 2, 10); //Lunar
		//Portals
		c.getPA().checkObjectSpawn(13616, 3083, 3488, 1, 10); //Lumbridge Portal
		c.getPA().checkObjectSpawn(13617, 3083, 3490, 1, 10); //Falador Portal
		c.getPA().checkObjectSpawn(13615, 3083, 3492, 1, 10); //Varrock Portal
		c.getPA().checkObjectSpawn(13619, 3083, 3494, 1, 10); //Ardougne Portal
		c.getPA().checkObjectSpawn(13620, 3083, 3496, 1, 10); //Yanille Portal

		c.getPA().checkObjectSpawn(4875, 2041, 4523, 1, 10);
		c.getPA().checkObjectSpawn(411, 2036, 4533, 2, 10); // Curse Prayers
		c.getPA().checkObjectSpawn(2213, 2031, 4529, 2, 10); // donor bank

		c.getPA().checkObjectSpawn(410, 3098, 3503, 0, 10); 

		c.getPA().checkObjectSpawn(4874, 3195, 3426, 1, 10);
		c.getPA().checkObjectSpawn(4875, 3195, 3424, 1, 10);
		c.getPA().checkObjectSpawn(4876, 3195, 3422, 0, 10);
		c.getPA().checkObjectSpawn(4877, 3195, 3420, 0, 10);
		c.getPA().checkObjectSpawn(4878, 3195, 3418, 0, 10);

		c.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
		c.getPA().checkObjectSpawn(14367, 3212, 3439, 2, 10); // VARROCK HOMEBANK 1
		c.getPA().checkObjectSpawn(14367, 3213, 3439, 2, 10); // VARROCK HOMEBANK 2
		c.getPA().checkObjectSpawn(411, 3215, 3438, 2, 10); // Curse Prayers
		c.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10307, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3040, 10308, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10311, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3022, 10312, 1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10341, -1, 0);
		c.getPA().checkObjectSpawn(1596, 3044, 10342, 1, 0);
		c.getPA().checkObjectSpawn(410, 3226, 3428, 0, 10);
		c.getPA().checkObjectSpawn(6552, 3228, 3432, 0, 10);
		c.getPA().checkObjectSpawn(6552, 2544, 4722, 0, 10);
		c.getPA().checkObjectSpawn(409, 3209, 3438, 0, 10);
		c.getPA().checkObjectSpawn(2213, 3047, 9779, 1, 10);
		c.getPA().checkObjectSpawn(2213, 2535, 4721, 0, 10);
		c.getPA().checkObjectSpawn(2213, 2534, 4721, 0, 10);
		c.getPA().checkObjectSpawn(2213, 3080, 9502, 1, 10);
		c.getPA().checkObjectSpawn(1530, 3093, 3487, 1, 10);
                c.getPA().checkObjectSpawn(1306, 2012, 4437, 1, 10);
                c.getPA().checkObjectSpawn(1306, 2003, 4437, 1, 10);
                c.getPA().checkObjectSpawn(562, 3494, 3479, 1, 10);
                c.getPA().checkObjectSpawn(1306, 3110, 3508, 1, 10);
                c.getPA().checkObjectSpawn(14859, 3046, 9776, 0, 10);//Rune Rock
		c.getPA().checkObjectSpawn(14859, 3045, 9776, 0, 10);//Rune Rock
                c.getPA().checkObjectSpawn(14859, 3044, 9776, 0, 10);//Rune Rock



		c.getPA().checkObjectSpawn(12356, 3203, 3430, 1, 10);
		c.getPA().checkObjectSpawn(2403, 3204, 3430, 0, 10);
                                          //X     Y     ID -> ID X Y
		c.getPA().checkObjectSpawn(2213, 2855, 3439, -1, 10);
		c.getPA().checkObjectSpawn(2090, 2839, 3440, -1, 10);
		c.getPA().checkObjectSpawn(2094, 2839, 3441, -1, 10);
		c.getPA().checkObjectSpawn(2092, 2839, 3442, -1, 10);
		c.getPA().checkObjectSpawn(2096, 2839, 3443, -1, 10);
		c.getPA().checkObjectSpawn(2102, 2839, 3444, -1, 10);
		c.getPA().checkObjectSpawn(2105, 2839, 3445, 0, 10);
		c.getPA().checkObjectSpawn(2213, 2530, 4712, 0, 10);
		c.getPA().checkObjectSpawn(1276, 2843, 3442, 0, 10);
		c.getPA().checkObjectSpawn(2213, 2535, 4721, 0, 10);
		c.getPA().checkObjectSpawn(2213, 2537, 4721, 0, 10);
		c.getPA().checkObjectSpawn(2213, 2536, 4721, 0, 10);
		c.getPA().checkObjectSpawn(1281, 2844, 3499, 0, 10);
		c.getPA().checkObjectSpawn(4156, 3083, 3440, 0, 10);
		c.getPA().checkObjectSpawn(1308, 2846, 3436, 0, 10);
		c.getPA().checkObjectSpawn(1309, 2846, 3439, -1, 10);
		c.getPA().checkObjectSpawn(1306, 2850, 3439, -1, 10);
		c.getPA().checkObjectSpawn(2783, 2841, 3436, 0, 10);
		c.getPA().checkObjectSpawn(2728, 2861, 3429, 0, 10);
		c.getPA().checkObjectSpawn(3044, 2857, 3427, -1, 10);
		c.getPA().checkObjectSpawn(320, 3048, 10342, 0, 10);
		c.getPA().checkObjectSpawn(409, 2533, 4711, 0, 10);
		c.getPA().checkObjectSpawn(-1, 2844, 3440, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2846, 3437, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2840, 3439, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2841, 3443, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2851, 3438, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3496, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3091, 3495, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3090, 3494, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3095, 3498, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3095, 3499, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3098, 3496, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3096, 3498, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3093, 3488, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3092, 3488, -1, 10);
		c.getPA().checkObjectSpawn(-1, 3092, 3496, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2538, 4713, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2535, 4714, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2543, 4712, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2539, 4702, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2537, 4720, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2536, 4719, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2536, 4718, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2534, 4717, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2534, 4718, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2536, 4720, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2535, 4721, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2535, 4720, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2534, 4720, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2533, 4720, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2533, 4719, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2539, 4720, -1, 10);
		c.getPA().checkObjectSpawn(-1, 2545, 4718, -1, 10);
		//stumps
if(Server.treesToRespawn2 != null){
	for(int a : Server.treesToRespawn2){
		if(Server.trees[a] != null){
			c.getPA().object(Server.trees[a].stumpId, Server.trees[a].stumpX, 

Server.trees[a].stumpY, 0, 10);
		}
	}
}
//end of stumps
		if (c.heightLevel == 0)
			c.getPA().checkObjectSpawn(2492, 2911, 3614, 1, 10);
		else
			c.getPA().checkObjectSpawn(-1, 2911, 3614, 1, 10);
	}
	
	public final int IN_USE_ID = 14825;
	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}
	public int[] obeliskIds = {14829,14830,14827,14828,14826,14831};
	public int[][] obeliskCoords = {{3154,3618},{3225,3665},{3033,3730},{3104,3792},{2978,3864},{3305,3914}};
	public boolean[] activated = {false,false,false,false,false,false};
	
	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,16));
			}
		}	
	}
	
	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}
	
	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				int xOffset = c.absX - obeliskCoords[port][0];
				int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1)) {
					c.getPA().startTeleport2(obeliskCoords[random][0] + xOffset, obeliskCoords[random][1] + yOffset, 0);
				}
			}		
		}
	}
	
	public boolean loadForPlayer(Object o, Client c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
	}
	
	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}	
	}




}