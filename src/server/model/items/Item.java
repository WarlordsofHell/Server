package server.model.items;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import server.Config;
import server.Server;

public class Item {

	public static boolean playerCape(int itemId) {
		String[] data = {
			"cloak", "Veteran cape", "Max cape", "Completionist cape", "Dungeoneering master cape", "cape", "Cape", "attractor", "Attractor", "Ava's", "Dungeoneering Cape", "Dungeoneering Cape(T)", "Ardounge Cloak",
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBoots(int itemId) {
		String[] data = {
			"Shoes", "shoes", "boots", "Boots", "Flippers", "flippers"
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerGloves(int itemId) {
		String[] data = {
			"Gloves", "gloves", "glove", "Glove", "Vamb", "vamb", "gauntlets", "Gauntlets", "bracers", "Bracers", "Vambraces", "vambraces"
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerShield(int itemId) {
		String[] data = {
			"kiteshield", "deflector", "book", "Kiteshield", "toktz-ket-xil", "Toktz-ket-xil", "shield", "Shield", "Kite", "kite", "Defender", "defender"
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerAmulet(int itemId) {
		String[] data = {
			"amulet", "Amulet", "scarf", "Necklace", "necklace", "Pendant", "pendant", "Symbol", "symbol", "stole", "Stole"
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerArrows(int itemId) {
		String[] data = {
			"Arrows", "arrows", "Arrow", "arrow", "Bolts", "bolts", "rack", "Rack", "Shots", "shot", "Shot", "shots",
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerRings(int itemId) {
		String[] data = {
			"ring", "rings", "Ring", "Rings",
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerHats(int itemId) {
		String[] data = {
			"boater", "Royal crown", "cowl", "peg", "coif", "helm", "bearhead",
			"Coif", "mask", "hat", "headband", "hood", "hood", "Veteran hood", "Max hood", "Completionist hood",
			"disguise", "cavalier", "full", "tiara", "wreath",
			"helmet", "Hat", "ears", "partyhat", "helm(t)",	
			"helm(g)", "beret", "facemask", "sallet", "Mask", "Dragon Mask", "dragon mask",
			"hat(g)", "hat(t)", "cap", "bandana", "Helm", "Mitre", "mitre",
			"Bomber cap", "headdress", "Top hat", "Royal crown",
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerLegs(int itemId) {
		String[] data = {
			"tassets", "chaps", "bottoms", "gown", "trousers", "MarshBot",
			"platelegs", "robe", "plateskirt", "legs", "leggings", "Proselyte Tasset", "Proselyte tasset",
			"shorts", "Skirt", "skirt", "cuisse", "Pantaloons", "Trousers", "Lord Marshal Trousers", "Elite void knight top",
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if((item.endsWith(data[i]) || item.contains(data[i])) && (!item.contains("top") && (!item.contains("robe (g)") && (!item.contains("robe (t)"))))) {
				item1 = true;
			}
		}
		return item1;
	}

	public static boolean playerBody(int itemId) {
		String[] data = {
			"body", "top", "Priest gown", "apron", "shirt", 
			"platebody", "robetop", "body(g)", "body(t)", "White platebody",
			"Wizard robe (g)", "Wizard robe (t)", "body", "brassard", "blouse", 
			"tunic", "leathertop", "Saradomin plate", "chainbody", "Top", "Lord Marshal Top", "Primal platebody",
			"hauberk", "Shirt", "torso", "chestplate", "jacket", "Vesta's chainbody", "chainbody", "Chainbody", "leather body", "Leather Body",
		};
		String item = getItemName(itemId);
		if (item == null) {
			return false;
		}
		boolean item1 = false;
		for(int i = 0; i < data.length; i++ ) {
			if(item.endsWith(data[i]) || item.contains(data[i])) {
				item1 = true;
			}
		}
		return item1;
	}

	private static String[] fullbody = {
		"top", "chestplate", "shirt","platebody","Ahrims robetop", "White platebody",
		"Elite void knight top", "Karils leathertop","brassard","Robe top","robetop", "Vesta's chainbody",
		"platebody (t)","platebody (g)","chestplate", "Lord Marshal Top", "Primal platebody",
		"torso", "hauberk", "Dragon chainbody", "blouse", "jacket", "chainbody", "Chainbody", "leather body", "Leather Body",
	};

	private static String[] fullhat = {
		"med helm", "coif", "Dharok's helm", "Slayer helmet", "hood", "Initiate helm",
		"Coif","Helm of neitiznot","Armadyl helmet","Berserker helm", "Bearhead",
		"Archer helm", "Farseer helm", "Warrior helm", "Void", "Lumberjack hat", "Reindeer hat",
		"Larupia hat", "mask", "headdress", "Kyatt hat", "Bomber cap", "Dwarven helmet", "Dragon Mask", "3rd age mage hat", "Statius's Full Helm", "dragon mask", "Royal crown",
	};

	private static String[] fullmask = {
		"full helm", "Royal crown", "headdress", "mask", "Torva Fullhelm", "Verac's helm", "Guthan's helm", "Karil's coif", "mask", "Torag's helm", "sallet", "Saradomin helm", "Lunar helm", "Dragon Mask", "dragon mask",
	};

	public static boolean isFullBody(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullbody.length; i++) {
			if (weapon.endsWith(fullbody[i]) || weapon.contains(fullbody[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullHelm(int itemId) {
		String weapon = getItemName(itemId);
			if (weapon == null)
				return false;
		for (int i = 0; i < fullhat.length; i++) {
			if (weapon.endsWith(fullhat[i]) && itemId != 2631 && itemId != 11277 && itemId != 11278 && itemId != 15509 && itemId != 15019 && itemId != 16006 && itemId != 15080 && itemId != 4502) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullMask(int itemId) {
		String weapon = getItemName(itemId);
			if (weapon == null)
				return false;
		for (int i = 0; i < fullmask.length; i++) {
			if (weapon.endsWith(fullmask[i]) && itemId != 2631 && itemId != 9925 && itemId != 10728 && itemId != 11277 && itemId != 11278 && itemId != 4502) {
				return true;
			}
		}
		return false;
	}
	
	public static String getItemName(int id) {
		for (int j = 0; j < Server.itemHandler.ItemList.length; j++) {
			if (Server.itemHandler.ItemList[j] != null)
				if (Server.itemHandler.ItemList[j].itemId == id)
					return Server.itemHandler.ItemList[j].itemName;	
		}
		return null;
	}
	
	
	public static boolean[] itemStackable = new boolean[Config.ITEM_LIMIT];
	public static boolean[] itemIsNote = new boolean[Config.ITEM_LIMIT];
	public static int[] targetSlots = new int[Config.ITEM_LIMIT];
	static {
		int counter = 0;
		int c;
		
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/stackable.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemStackable[counter] = false;
                    itemStackable[15015] = true;
                    itemStackable[15016] = true;
					itemStackable[13883] = true; //morrigan throwing axe
					itemStackable[13879] = true; //morrigan javelin
itemStackable[13957] = true; //morrigan javelin
itemStackable[18016] = true;
itemStackable[12158] = true;
itemStackable[12159] = true;
itemStackable[12160] = true;
itemStackable[12163] = true;
itemStackable[12155] = true;
				} else {
					itemStackable[counter] = true;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading stackabledata! Trace:");
			e.printStackTrace();
		}
        itemStackable[15016] = true;//morrigan
		
		counter = 0;
		
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemIsNote[counter] = true;
				} else {
					itemIsNote[counter] = false;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
		
		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading stackabledata! Trace:");
			e.printStackTrace();
		}
        itemStackable[15015] = true;//morri
		
		counter = 0;
		
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemIsNote[counter] = true;
				} else {
					itemIsNote[counter] = false;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
		
		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading stackabledata! Trace:");
			e.printStackTrace();
		}
        itemStackable[15243] = true;//hc shot
		
		counter = 0;
		
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/notes.dat"));
			while ((c = dataIn.read()) != -1) {
				if (c == 0) {
					itemIsNote[counter] = true;
				} else {
					itemIsNote[counter] = false;
				}
				counter++;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
		
		counter = 0;
		try {
			FileInputStream dataIn = new FileInputStream(new File("./Data/data/equipment.dat"));
			while ((c = dataIn.read()) != -1) {
				targetSlots[counter++] = c;
			}
			dataIn.close();
		} catch (IOException e) {
			System.out.println("Critical error while loading notedata! Trace:");
			e.printStackTrace();
		}
	}
}