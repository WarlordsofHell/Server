package server.model.players.skills;

import server.model.players.Client;
import server.model.npcs.NPC;
import server.Server;

public class Summoning {

	Client c;
	
	public Summoning(Client c) {
		this.c = c;
	}
	
	private int[] pouches = {12089, 12093, 12790};
	
	public void summonFamiliar(int npcId) {
		c.summoned = Server.npcHandler.summonNPC(c, npcId, c.getX(), c.getY() + 1, c.heightLevel, 0, 100, 1, 1, 1);
		c.summoned.gfx0(1315);
	}
	
	public void dismissFamiliar() {
		c.summoned.isDead = true;
		c.summoned.applyDead = true;
		c.summoned.actionTimer = 0;
		c.summoned.npcType = -1;
		c.summoned.updateRequired = true;
	}
	
	private boolean isPouch(int itemId) {
		boolean isPouch = false;
		for (int i = 0; i < pouches.length; i++) {
			isPouch = itemId == pouches[i];
		}
		return isPouch;
	}
	
	public void handlePouch(int itemId) {
		if (isPouch(itemId)) {
			if (c.summoned == null || c.summoned.isDead) {
				c.getItems().deleteItem(itemId, 1);
				summonFamiliar(getIdForPouch(itemId));
			} else {
				c.sendMessage("Please dismiss your current familiar before summoning another.");
			}
		}
	}
	
	public void callFamiliar() {
		c.summoned.gfx0(1315);
		int id = c.summoned.npcId;
		c.summoned = new NPC(c.summoned.npcId, c.summoned.npcType);
		Server.npcHandler.npcs[id] = c.summoned;
		c.summoned.absX = c.absX;
		c.summoned.absY = c.absY + 1;
		c.summoned.makeX = c.absX;
		c.summoned.makeY = c.absY + 1;
		c.summoned.summonedFor = c.getId();
		c.summoned.updateRequired = true;
	}
	
	private int getIdForPouch(int itemId) {
		switch (itemId) {
			case 12089: return 6870; // Wolpertinger
			case 12093: return 6874; // Pack yak
			case 12790: return 7344; // Steel titan
		}
		return -1;
	}
	
	public void openBoB() { 
		if(c.getOutStream() != null && c != null) {
			for (int i = 0; i < 30; i++) {
				c.bobItems[i] = 5699;	
				c.getItems().sendFrame34(c.bobItems[i], 1, 7423, i);
			}
			c.usingBoB = true;
			c.getItems().resetItems(5064);
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(21302);
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
		}
	}
	
}