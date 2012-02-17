package server.model.players;

import java.util.HashMap;
import server.util.Misc;

/**
 * @author Sanity
 */

public class Food {
	
	
	private Client c;
	
	public Food (Client c) {
		this.c = c;	
	}
	public static enum FoodToEat {		
		MANTA(391, 22, "Manta ray"),
		SHARK(385, 20, "Shark"),
		LOBSTER(379, 12, "Lobster"),
		TROUT(333, 7, "Trout"),
		SALMON(329, 9, "Salmon"),
		SWORDFISH(373, 14, "Swordfish"),
		TUNA(361, 10, "Tuna"),
		MONKFISH(7946, 16, "Monkfish"),
		SEA_TURTLE(397, 22, "Sea turtle"),
		COOKED_KARAMBWAN(3144, 18, "Cooked karambwan"),
		TUNA_POTATO(7060, 22, "Tuna potato"),
		ROCKTAIL(15272, 23, "Rocktail"),
		PURPLE_SWEETS(10476, Misc.random(2) + 1, "Purple sweets");
		
		
		private int id; private int heal; private String name;
		
		private FoodToEat(int id, int heal, String name) {
			this.id = id;
			this.heal = heal;
			this.name = name;		
		}
		
		public int getId() {
			return id;
		}

		public int getHeal() {
			return heal;
		}
		
		public String getName() {
			return name;
		}
		public static HashMap <Integer,FoodToEat> food = new HashMap<Integer,FoodToEat>();
		
		public static FoodToEat forId(int id) {
			return food.get(id);
		}
		
		static {
		for (FoodToEat f : FoodToEat.values())
			food.put(f.getId(), f);
		}
	}
	
	public void eat(int id, int slot) {
		if (c.duelRule[6]) {
			c.sendMessage("You may not eat in this duel.");
			return;
		}
		if (System.currentTimeMillis() - c.foodDelay >= 1500 && c.constitution > 0) {
			c.getCombat().resetPlayerAttack();
			c.attackTimer += 2;
			c.startAnimation(829);
			c.getItems().deleteItem(id,slot,1);
			FoodToEat f = FoodToEat.food.get(id);
			if (c.constitution < (f.getId() == 15272 ? (c.maxConstitution * 1.1) : c.maxConstitution)) {
				c.constitution += f.getHeal() * 10;
				if (c.constitution > (f.getId() == 15272 ? (c.maxConstitution * 1.1) : c.maxConstitution))
					c.constitution = (int) (f.getId() == 15272 ? (c.maxConstitution * 1.1) : c.maxConstitution);
			}
			c.foodDelay = System.currentTimeMillis();
			c.sendMessage("You eat the " + f.getName() + ".");
		}		
	}

	
	public boolean isFood(int id) {
		return FoodToEat.food.containsKey(id);
	}	
	

}