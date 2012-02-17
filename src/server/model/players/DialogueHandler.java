package server.model.players;

import server.util.Misc;

public class DialogueHandler {

	private Client c;
	
	public DialogueHandler(Client client) {
		this.c = client;
	}
	
	/**
	 * Handles all talking
	 * @param dialogue The dialogue you want to use
	 * @param npcId The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		switch(dialogue) {
                    case 106:
				sendOption5("One 6-sided die", "Two 6-sided dice", "One 4-sided die", "One 8-sided die", "More...");
				c.dialogueAction = 106;
				c.teleAction = 0;
				c.nextChat = 0;
				break;

			case 107:
				sendOption5("One 10-sided die", "One 12-sided die", "One 20-sided die", "Two 10-sided dice for 1-100", "Back...");
				c.dialogueAction = 107;
				c.teleAction = 0;
				c.nextChat = 0;
				break;
		case 0:
			c.talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 1:
			sendPlayerChat1("Hey, what is this place?", 9850);
			c.nextChat = 2;
		break;
		case 2:
			sendNpcChat4("This place is the Barbarian Coluseum. We hold combat",
						"related events within the arena you see to the west.", 
						"Most importantly, you can play the Barbarian Defence mini-",
						"game here. To start, enter the trapdoor to the west.", c.talkingNpc, "Commander Connad");
			c.nextChat = 3;
		break;
		case 3:
			sendPlayerChat1("Thanks.", 9850);
			c.nextChat = 0;
		break;
		case 4:
			sendPlayerChat1("Do you have any items for sale?", 9850);
			c.nextChat = 5;
		break;
		case 5:
			sendNpcChat1("Sure, which shop are you interested in seeing?", c.talkingNpc, "Nastroth");
			c.nextChat = 6;
		break;
		case 6:
			sendOption4("Melee/Welfare gear", "Magic gear", "Ranged gear", "Food/potions");
			c.dialogueAction = 2;
			c.nextChat = 0;
		break;
		case 12:
			sendPlayerChat1("Can you repair my Barrows for me?", 9850);
			c.nextChat = 13;
		break;
		case 13:
			sendNpcChat1("Sure, hand it over.", c.talkingNpc, "Strange Old Man");
			c.nextChat = 14;
		break;
		case 14:
			c.getPA().fixAllBarrows();
			sendNpcChat1("Here you go, " + c.playerName + ", all fixed!", c.talkingNpc, "Strange Old Man");
			c.nextChat = 0;
		break;
		case 17:
			sendPlayerChat1("Do you have any items for sale?", 9850);
			c.nextChat = 18;
		break;
		case 18:
			sendNpcChat3("Yes, I do. I sell a large variety of powerful items in exchange",
						"for player killing points. Feel free to browse through my shops",
						"until you find what you're looking for.", c.talkingNpc, "Mandrith");
			c.nextChat = 19;
		break;
		case 19:
			//sendOption2("Pk points shop", "Barrows shop");
			sendOption4("Pk points shop", "View Pk Points", "Exchange PvP Artifacts", "Cancel");
			c.dialogueAction = 0;
		break;
		case 20:
			sendNpcChat3("Hello there, " + c.playerName + ". I can teleport you to",
						"various wilderness locations. Where would you like me to",
						"teleport you to?", c.talkingNpc, "Watchtower Wizard");
			c.nextChat = 21;
		break;
		case 21:
			sendOption4("East dragons", "West dragons", "44 portals", "Magebank");
			c.dialogueAction = 1;
			c.nextChat = 0;
		break;
		case 22:
			sendOption4("Minigames", "Monsters", "Slayer", "Skilling");
			c.dialogueAction = 4;
			c.nextChat = 0;
		break;
		case 23:
			//sendOption4("Pest Control", "Duel Arena", "Dungeoneering", "Corporal Beast");
			//c.dialogueAction = 5;
			//c.nextChat = 0;
		break;
		case 24:
			sendStatement("You currently have " + c.pkp + " PK points.");
			c.dialogueAction = 0;
			c.nextChat = 0;
		break;
		case 25:
			sendStatement("Coming soon.");
			c.nextChat = 0;
		break;
		case 26:
			c.getDH().sendOption4("Normal Magic", "Ancient Magicks", "Lunar Magic", "Cancel");
			c.nextChat = 0;
			c.dialogueAction = 6;
		break;
		case 27:
			c.getDH().sendOption4("Barrows", "Fight Caves", "Barbarian Defence", "More...");
			c.nextChat = 0;
			c.dialogueAction = 7;
		break;
		case 28:
			c.getDH().sendOption5("Duel Arena", "God Wars Dungeon", "Warriors Guild", "Pest control", "Dungeoneering");//need the mapping for gwd and warriors guild fml
			c.nextChat = 0;
			c.dialogueAction = 8;
		break;
		case 29:
			c.getDH().sendOption5("Rock Crabs", "Experiments", "Bork", "Corporeal Beast", "Next");
			c.nextChat = 0;
			c.dialogueAction = 9;
		break;
		case 30:
			c.getDH().sendOption2("Slayer Tower", "Slayer Cave");
			c.nextChat = 0;
			c.dialogueAction = 10;
		break;
		case 31:
			c.getDH().sendOption4("Crafting Guild", "Fishing Guild", "Mining Guild", "Thieving");
			c.nextChat = 0;
			c.dialogueAction = 11;
		break;
		case 32:
			c.getDH().sendOption4("Tormented Demons", "Undead lair", "Chaos tunnels", "Back");//need the mapping for chaos tunnels dunno if we can get
			c.nextChat = 0;
			c.dialogueAction = 12;
		break;
		case 33:
			sendNpcChat1("Good luck, tzhaar-" + c.playerName + "!", c.talkingNpc, "Watchtower Wizard");
			c.nextChat = 0;
		break;
		case 34:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			c.dialogueAction = 13;
			c.nextChat = 35;
			break;
		case 35:
			sendOption2("Yea! I'm fearless!",  "No way! That looks scary!");
			c.dialogueAction = 13;
			c.nextChat = 0;
			break;
			
			
			/*
		case 11: //11
			npcTalk2("Good afternoon, sir. In need of a haircut or shave, are", "we?", 598, "Hairdresser", 590);
			c.nextChat = 12;
			break;

		case 12: //12
			choice3("A haircut, please.", "A shave, please.", "No, thank you.");
			c.dialogueAction = 4;
			break;

		case 13: //13
			playerTalk("A haircut, please.");
			c.nextChat = 14;
			break;

		case 14:  //14
			npcTalk("Certainly, sir. The fee will be 2,000 coins.", 598, "Hairdresser", 590);
			c.nextChat = 15;
			break;

		case 15:  //15
			if (c.getItems().playerHasItem(995, 2000)) {
				npcTalk2("Please select a hairstyle you would", "like from this brochure.", 598, "Hairdresser", 590);
				//npcTalk2("Please select a beard and color you would", "like from this brochure.", 598, "Hairdresser", 590);
				c.nextChat = 16;
			} else {
				npcTalk2("It looks like you don't have 2,000 coins,", "please revisit when you do.", 598, "Hairdresser", 610);
				c.nextChat = 0;
			}
			break;

		case 16:  //16
			c.getPA().showInterface(2653); // hairstyle interface
			break;
 
		case 17:  //17
			playerTalk("No, thank you.");
			c.nextChat = 18;
			break;

		case 18:  //18
			npcTalk("Very well. Come back if you change your mind.", 598, "Hairdresser", 590);
			c.nextChat = 0;
			break;
			
		case 19:  //19
			if (c.getItems().playerHasItem(995, 2000)) {
				npcTalk2("Please select a beard and color you would", "like from this brochure.", 598, "Hairdresser", 590);
				c.nextChat = 20;
			} else {
				npcTalk2("It looks like you don't have 2,000 coins,", "please revisit when you do.", 598, "Hairdresser", 610);
				c.nextChat = 0;
			}
			break;
			
		case 20:  //20
			c.getPA().showInterface(2007); // hair/beard interface
			break;
			
		case 21:  //21
			playerTalk("A shave, please.");
			c.nextChat = 22;
			break;
			
		case 22:  //22
			npcTalk("Certainly, sir. The fee will be 2,000 coins.", 598, "Hairdresser", 590);
			c.nextChat = 19;
			break;
			*/
		}
	}
	
	/*
	 * Information Box
	 */
	
	public void sendStartInfo(String text, String text1, String text2, String text3, String title) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendFrame164(6179);
	}
	
	/*
	 * Options
	 */
	
	private void sendOption(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2470);
	 	c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126("Click here to continue", 2473);
		c.getPA().sendFrame164(13758);
	}	
	
	private void sendOption2(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}
	
	private void sendOption3(String s, String s1, String s2) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame126(s2, 2462);
		c.getPA().sendFrame164(2459);
	}
	
	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}
	
	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Select an Option", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}

	/*
	 * Statements
	 */
	
	private void sendStatement(String s) { // 1 line click here to continue chat box interface
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}
	
	/*
	 * Npc chat head
	 */
	
	private void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 9850);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}
	
	private void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4887, 9850);
		c.getPA().sendFrame126(name, 4888);
		c.getPA().sendFrame126(s, 4889);
		c.getPA().sendFrame126(s1, 4890);
		c.getPA().sendFrame75(ChatNpc, 4887);
		c.getPA().sendFrame164(4887);
	}
	
	private void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		c.getPA().sendFrame200(4894, 9850);
		c.getPA().sendFrame126(name, 4895);
		c.getPA().sendFrame126(s, 4896);
		c.getPA().sendFrame126(s1, 4897);
		c.getPA().sendFrame126(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendFrame164(4893);
	}
	
	private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		c.getPA().sendFrame200(4901, 9850);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}
	
	/*
	 * Player chat head
	 */
	
	private void sendPlayerChat1(String s, int emoteid) {
		c.getPA().sendFrame200(969, emoteid);
		c.getPA().sendFrame126(Misc.capitalize(c.playerName), 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}
	
	private void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 9850);
		c.getPA().sendFrame126(c.playerName, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
	}
	
	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 9850);
		c.getPA().sendFrame126(c.playerName, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
	}
	
	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 9850);
		c.getPA().sendFrame126(c.playerName, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}
}
