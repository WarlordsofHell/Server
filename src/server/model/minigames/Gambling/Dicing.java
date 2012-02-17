/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.minigames.Gambling;

import server.model.players.Client;
import server.util.Misc;
import server.Server;

/**
 *
 * @author Bas van Driel
 */
public class Dicing {
    
    	public Client c;
	
	public Dicing(Client c) {
		this.c = c;
	}

    public void FetchDice()
	{
		int rnd;
		String Message = "";
		if (c.cDice == 0 || (System.currentTimeMillis() - c.diceDelay <= 1000)) {
			return;
		}
		switch (c.cDice) {
		//Dice
			case 15096: rnd = Misc.random(19)+1; Message = ("rolled @red@"+ rnd +"on a twenty-sided die."); break;
			case 15094: rnd = Misc.random(11)+1; Message = ("rolled @red@"+ rnd +"on a twelve-sided die."); break;
			case 15092: rnd = Misc.random(9)+1; Message = ("rolled @red@"+ rnd +"on a ten-sided die."); break;
			case 15090: rnd = Misc.random(7)+1; Message = ("rolled @red@"+ rnd +"on an eight-sided die."); break;
			case 15100: rnd = Misc.random(3)+1; Message = ("rolled @red@"+ rnd +"on a four-sided die."); break;
			case 15086: rnd = Misc.random(5)+1;	Message = ("rolled @red@"+ rnd +"on a six-sided die."); break;
			case 15088: rnd = Misc.random(11)+1; Message = ("rolled @red@"+ rnd +"on two six-sided dice."); break;
			case 15098: rnd = Misc.random(99)+1; Message = ("rolled @red@"+ rnd +"on the percentile dice."); break;
		}
		c.sendMessage("You " + Message);
			if (c.clanDice){
				if (c.clanId >= 0) {
					Server.clanChat.messageToClan("Clan Chat channel-mate <col=16711680>"+c.playerName+"</col> "+Message, c.clanId);
				}
			}
		c.cDice = 0;
	}

	public void useDice(int itemId, boolean clan){
			if (System.currentTimeMillis() - c.diceDelay >= 3000) {
				c.sendMessage("Rolling...");
				c.startAnimation(11900);
				c.diceDelay = System.currentTimeMillis();
				c.cDice = itemId;
				c.clanDice = clan;
			switch (itemId) {
				//Gfx's
				case 15086: c.gfx0(2072); break;
				case 15088: c.gfx0(2074); break;
				case 15090: c.gfx0(2071); break;
				case 15092: c.gfx0(2070); break;
				case 15094: c.gfx0(2073); break;
				case 15096: c.gfx0(2068); break;
				case 15098: c.gfx0(2075); break;
				case 15100: c.gfx0(2069); break;
			}
		}

	}
}
