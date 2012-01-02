package server.model.players.content.randomevent;

import server.model.players.Client;
import server.model.players.DialogueHandler;
import server.util.Misc;

public class DrillDemon extends EventHandler {

	public static void enterDrillEvent(Client c) {
		c.teleportToX = 3165;
		c.teleportToY = 4818;
		c.getDH().sendStatement("Follow Sergeant Damien's orders!");
		c.inDrillEvent = true;
		changeToSidebar(c, new int[] { 12, 147});
		setupSigns(c);
	}

	public static void doMatExercise(Client c, int object, int obX, int obY) {
		c.getPA().walkTo(c.absX - obX, c.absY - obY);
		c.turnPlayerTo(obX, obY);
	}

	private static void setupSigns(Client c) {
		for(int i = 0; i < positions.length; i++) {
			c.getPA().checkObjectSpawn(mats[i], positions[i][0], positions[i][1], 0, 10);
		}
	}

	public static void getCorrectActivity(Client c) {
		int type = Misc.random(activity.length);
		DialogueHandler.sendNpcChat(c, activity[type][0], c.talkingNpc, "Sergeant Damien");
		c.correctDrill = type;
	}

	private static void giveReward(Client c) {
		c.getItems().addItem(6654 + Misc.random(2), 1);
		c.sendMessage("Congratulations, you've completed the Drill Demon event!");
	}

	private static int[] animation = {
		2764, 2763, 2761, 2756,
	};

	private static int[] mats = {
		10076, 10077, 10078, 10079
	};

	private static int[][] positions = {
		{3160, 4821}, {3162, 4821}, {3164, 4821}, {3166, 4821},
	};

	private static Object[][] objects = {
		{"jogging", 10075}, {"sit-up", 10072}, {"star-jump", 10074}, {"push-ups", 10073}, 
	};

	private static String[] wrong = {
		"Wrong mat, worm! Look at the signs on the posts next",
		"time, and use the right mat for the exercise you're",
		"supposed to be doing.",
	};

	private static String[][] activity = {
		{"Get yourself over there and jog on that mat private!"},
		{"Get on the map and give me sit-ups, private!"},
		{"I want to see some star jumps, private!"},
		{"Drop and give me push ups, private!"},
	};
}