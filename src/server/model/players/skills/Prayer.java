package server.model.players.skills;


import server.Config;
import server.model.players.Client;
public class Prayer {

	Client c;
	
	public int[][] bonesExp = {{526,5},{532,15},{534,30},{536,72},{6729,125},{18830,225}};	
	
	public Prayer(Client c) {
		this.c = c;
	}
	
	public void buryBone(int id, int slot) {
		if(System.currentTimeMillis() - c.buryDelay > 1500) {
			c.getItems().deleteItem(id, slot, 1);
			c.sendMessage("You bury the bones.");
			c.getPA().addSkillXP(getExp(id)*Config.PRAYER_EXPERIENCE,5);
			c.buryDelay = System.currentTimeMillis();
			c.startAnimation(827);
		}	
	}
	
	public void bonesOnAltar(int id) {
		c.getItems().deleteItem(id, c.getItems().getItemSlot(id), 1);
		c.sendMessage("The gods are pleased with your offering.");
		c.getPA().addSkillXP(getExp(id)*4*Config.PRAYER_EXPERIENCE, 5);
	}
	
	public boolean isBone(int id) {
		for (int j = 0; j < bonesExp.length; j++)
			if (bonesExp[j][0] == id)
				return true;
		return false;
	}
	
	public int getExp(int id) {
		for (int j = 0; j < bonesExp.length; j++) {
			if (bonesExp[j][0] == id)
				return bonesExp[j][1];
		}
		return 0;
	}
}