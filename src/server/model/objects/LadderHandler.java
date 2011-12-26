/**
 * 
 */
package server.model.objects;

import server.event.Event;
import server.event.EventContainer;
import server.event.EventManager;
import server.model.players.Client;
import server.event.CycleEventHandler;
import server.event.CycleEvent;
import server.event.CycleEventContainer;

/**
 * @author bailey <wtf ur zerk>
 * Handles Ladders & stairs
 */
public class LadderHandler {
	
	public Client c;
	
	public LadderHandler(Client c) {
		this.c = c;
	}
	
	public void handleLadder(final int i) {
		ladder();
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer container) {
				if(c.heightLevel == 0) {
					c.getPA().movePlayer(c.absX, c.absY, 1);
				} else if(c.heightLevel == 1) {
					c.getPA().movePlayer(c.absX, c.absY, 0);
				} else if(c.heightLevel == 2) {
					c.getPA().movePlayer(c.absX, c.absY, 1);
				}
			}
				@Override
				public void stop() {

				}
		}, 2);
	}
	
	
	public void ladderOption(final String climbType) {
			ladder();
			EventManager.getSingleton().addEvent(new Event() {
				public void execute(EventContainer e) {
					if (climbType.equalsIgnoreCase("up")) {
						c.getPA().movePlayer(c.absX, c.absY, c.heightLevel += 1);
					}
					if (climbType.equalsIgnoreCase("down")) {
						c.getPA().movePlayer(c.absX, c.absY, c.heightLevel -= 1);
					}
					e.stop();
				}
			}, 2);
	}	
	
	public void getOption() {
		String s = "Climb up the ladder.", s1 = "Climb down the ladder.";
		c.getDH().sendOption2(s, s1);
		c.dialogueAction = 500;
	}
	
	private int x, y;
	
	public void ladder() {
		x = c.objectX;
		y = c.objectY;
		faceLadder(x, y);
		c.startAnimation(828); c.stopMovement(); c.resetWalkingQueue();
		c.getPA().requestUpdates(); c.getPA().removeAllWindows();
	}
	
	public void faceLadder(final int x, final int y) {
		if(c.FocusPointX >= x) {
			c.turnPlayerTo(0, y - 2);
		} else if(c.FocusPointY >= y) {
			c.turnPlayerTo(x - 2, 0);
		} else if(c.FocusPointX < x) {
			c.turnPlayerTo(x, y);
		} else if(c.FocusPointY <= y) {
			c.turnPlayerTo(x, y);
		}
	}
	
	
}