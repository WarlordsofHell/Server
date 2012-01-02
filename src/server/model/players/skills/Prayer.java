package server.model.players.skills;

import java.util.HashMap;
import java.util.Map;

import server.model.players.Client;

public class Prayer {
	
	private Client c;
	
	public Prayer(Client c) {
		this.c = c;
	}
	
	public enum Bones {
		NORMAL(526, 5, 14, "bones"),
		NORMAL2(2530, 5, 14, "bones"),
		BURNT(528, 5, 14, "burnt bones"),
		BAT(530, 5, 14, "bat bones"),
		WOLF(2859, 5, 14, "wolf bones"),
		MONKEY1(3179, 5, 14, "monkey bones"),
		MONKEY2(3180, 5, 14, "monkey bones"),
		MONKEY3(3181, 5, 14, "monkey bones"),
		MONKEY4(3182, 5, 14, "monkey bones"),
		MONKEY5(3183, 5, 14, "monkey bones"),
		MONKEY7(3185, 5, 14, "monkey bones"),
		MONKEY8(3186, 5, 14, "monkey bones"),
		MONKEY9(3187, 5, 14, "monkey bones"),
		JOGRE(3125, 15, 45, "jogre bones"),
		ZOGRE(4812, 23, 68, "zogre bones"),
		BURNTJOGRE(3127, 25, 75, "burnt jogre bones"),
		SHAIKAHAN(3123, 25, 75, "shaikahan bones"),
		BIG(532, 15, 45, "big bones"),
		BABYDRAGON(534, 30, 90, "baby dragon bones"),
		WYVERN(6812, 50, 150, "wyvern bones"),
		DRAGON(536, 72, 216, "dragon bones"),
		FAYGR(4830, 84, 236, "faygr bones"),
		RAURG(4832, 96, 288, "raurg bones"),
		OURG(4834, 140, 420, "ourg bones"),
		DAGANNOTH(6729, 125, 375, "dagannoth bones"),
		FROSTDRAGON(18830, 180, 520, "frost dragon bones"),
		ANCIENT(15410, 200, 600, "ancient bones");
		
		private int id, bury, altar;
		private String name;
		
		private Bones(int id, int bury, int altar, String name) {
			this.id = id;
			this.bury = bury;
			this.altar = altar;
			this.name = name;
		}
		
		public int getId() {
			return id;
		}
		
		public int getBuryXP() {
			return bury;
		}
		
		public int getAltarXP() {
			return altar;
		}
		
		public String getName() {
			return name;
		}
		
		private final static Map<Integer, Bones> bones = new HashMap<Integer, Bones>();
		
		public static Bones forId(int id) {
			return bones.get(id);
		}
		
		static {
			for (Bones b : Bones.values()) {
				bones.put(b.getId(), b);
			}
		}
	}
	
	public boolean isBone(int id) {
		Bones bon = Bones.forId(id);
		if (bon == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void handleBones(int boneID, boolean altar) {
		Bones bon = Bones.forId(boneID);
		if (bon == null) { 
			return;
		}
		if (System.currentTimeMillis() - c.buryDelay >= 1200) {
			c.getItems().deleteItem(bon.getId(), 1);
			c.getPA().addSkillXP(altar ? bon.getAltarXP() : bon.getBuryXP(), c.playerPrayer);
			c.buryDelay = System.currentTimeMillis();
			c.startAnimation(altar ? 896 : 827);
			c.sendMessage(altar ? "The gods are pleased with your offerings." : "You bury the bones.");
			if(altar) {
				//c.getPA().createPlayersStillGfx(624, c.objectX, c.objectY, 0, 0);
				c.getPA().createPlayersStillGfx(624, 3097, 3500, 0, 0);
			}
		}
	}
}