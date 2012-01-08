/**
 * Slayer Class
 * @author Jili X I
 * @date 12/14/11 
 */

package server.model.players.skills;

import server.Config;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.util.Misc;

public class Slayer {
        
    private Client c;
    	public Slayer(Client c) {
		this.c = c;
	}
	
	public static SlayerTask getEasyTask(Client c){
		SlayerTask task = null;
		do {
			task = EasyTask.values()[(int)(Math.random()*EasyTask.values().length)];
		} while(task.getReq() >= c.playerLevel[c.playerSlayer]);
			String description = task.getDesc();
			c.slayerTask = task.getId();
			c.taskAmount = Misc.random(50) + 25;
			c.sendMessage("You have been assigned to kill " + c.taskAmount + " " + description + ".");
			return task;
	}

	public static SlayerTask getMediumTask(Client c) {
		SlayerTask task = null;
		do {
			task = MediumTask.values()[(int)(Math.random()*MediumTask.values().length)];
		} while(task.getReq() >= c.playerLevel[c.playerSlayer]);
			String description = task.getDesc();
			c.slayerTask = task.getId();
			c.taskAmount = Misc.random(70) + 25;
			c.sendMessage("You have been assigned to kill " + c.taskAmount + " " + description + ".");
			return task;		
	}
	
	public static SlayerTask getHardTask(Client c) {
		SlayerTask task = null;
		do {
			task = HardTask.values()[(int)(Math.random()*HardTask.values().length)];
		} while(task.getReq() >= c.playerLevel[c.playerSlayer]);
			String description = task.getDesc();
			c.slayerTask = task.getId();
			c.taskAmount = Misc.random(90) + 25;
			c.sendMessage("You have been assigned to kill " + c.taskAmount + " " + description + ".");
			return task;			
	}

	
	public boolean canSlay(Client c, int i) {
		for(EasyTask e : EasyTask.values()) {
			if(c.playerLevel[c.playerSlayer] < e.getReq() && NPCHandler.npcs[i].npcType == e.getId())
				return false;
		}
		for(MediumTask m: MediumTask.values()) {
			if(c.playerLevel[c.playerSlayer] < m.getReq() && NPCHandler.npcs[i].npcType == m.getId())
				return false;
		}
		for(HardTask h: HardTask.values()) {
			if(c.playerLevel[c.playerSlayer] < h.getReq() && NPCHandler.npcs[i].npcType == h.getId())
				return false;
		}
		for(NonTask n: NonTask.values()) {
			if(c.playerLevel[c.playerSlayer] < n.getReq() && NPCHandler.npcs[i].npcType == n.getId())
				return false;
		}
		return true;
	}
	
	public void appendSlayerExperience(int i) {
		Client c = (Client)PlayerHandler.players[NPCHandler.npcs[i].killedBy];
		if(c != null) {
			if (c.slayerTask == NPCHandler.npcs[i].npcType){
				c.taskAmount--;
				c.getPA().addSkillXP(NPCHandler.npcs[i].MaxHP * Config.SLAYER_EXPERIENCE, c.playerSlayer);
				if (c.taskAmount <= 0) {
					c.getPA().addSkillXP((NPCHandler.npcs[i].MaxHP * 8) * Config.SLAYER_EXPERIENCE, c.playerSlayer);
					c.slayerTask = -1;
					c.sendMessage("You have completed your slayer task, please see the slayer master to get a new one.");
				}
			}
		}
	}
	
	interface SlayerTask {
		public int getId();
		public int getReq();
		public String getDesc();
	}

	enum EasyTask implements SlayerTask {
		YAK(5529, 1, "Yak's"),
		GOBLIN(101, 1, "Goblin's"),
		ROCK_CRAB(1265, 1, "Rock Crab's"),
		AL_KHARID_WARRIOR(18, 1, "Al-Kharid Warrior's"),
		CHAOS_DRUID(181, 1, "Chaos Druid's"),
		GHOST(103, 1, "Ghost's"),
		GIANT_BAT(78, 1, "Giant Bat's"),
		HILL_GIANT(117, 1, "Hill Giants"),
		CHAOS_DWARF(119, 1, "Chaos Dwarf's"),
		CRAWLING_HAND(1648, 5, "Crawling hand's"),
		BANSHEE(1612, 15, "Banshee's");
		
		private int id, req;
		private String desc;
		
		EasyTask(int id, int req, String desc) {
			this.id = id;
			this.req = req;
			this.desc = desc;
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public int getReq() {
			return req;
		}

		@Override
		public String getDesc() {
			return desc;
		}
		
		

	}
	
	enum MediumTask implements SlayerTask {
		MOSS_GIANT(112, 1, "Moss Giant's"),
		ICE_WARRIOR(125, 1, "Ice Warrior's"),
		BABY_BLUE_DRAGON(48, 1, "Baby Blue Dragon's"),
		INFERNAL_MAGE(1643, 45, "Infernal Mage's"),
		BLOODVELD(1618, 50, "Bloodveld's"),
		GREEN_DRAGON(1610, 1, "Green Dragon's"),
		LESSER_DEMON(82, 1, "Lesser Demon's"),
		DAGANNOTH(1341, 1, "Dagannoth's");
		
		private int id, req;
		private String desc;
		
		MediumTask(int id, int req, String desc) {
			this.id = id;
			this.req = req;
			this.desc = desc;
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public int getReq() {
			return req;
		}

		@Override
		public String getDesc() {
			return desc;
		}
	}
	
	enum HardTask implements SlayerTask {
		DUST_DEVIL(1624, 65, "Dust Devil's"),
		GARGOYLE(1610, 75, "Gargoyle's"),
		NECHRYAEL(1613, 80, "Nechryael's"),
		ABYSSAL_DEMON(1615, 85, "Abyssal Demon's"),
		DARK_BEAST(2783, 90, "Dark Beast's"),
		BLUE_DRAGON(55, 1, "Blue Dragon's"),
		BLACK_DEMON(84, 1, "Black Demon's"),
		HELL_HOUND(49, 1, "Hell Hound's");
		
		
		private int id, req;
		private String desc;
		
		HardTask(int id, int req, String desc) {
			this.id = id;
			this.req = req;
			this.desc = desc;
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public int getReq() {
			return req;
		}

		@Override
		public String getDesc() {
			return desc;
		}
	}
	
	enum NonTask implements SlayerTask {
		ICE_STRYKEWYRM(9463, 93, "Ice Strykewyrm's"),
		DESERT_STRYKEWYRM(9465, 77, "Desert Strykewyrm's"),
		JUNGLE_STRYKEWYRM(9467, 73, "Jungle Strykewyrm's");
		
		private int id, req;
		private String desc;
		
		NonTask(int id, int req, String desc) {
			this.id = id;
			this.req = req;
			this.desc = desc;
		}

		@Override
		public int getId() {
			return id;
		}

		@Override
		public int getReq() {
			return req;
		}

		@Override
		public String getDesc() {
			return desc;
		}
	}
	
}