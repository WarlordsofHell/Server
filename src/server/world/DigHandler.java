package server.world;

import server.event.Event;
import server.event.EventContainer;
import server.event.EventManager;
import server.model.players.Client;
import server.world.map.Area;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DigHandler {

	public DigHandler() {
		loadSpots("./Data/cfg/digspots.cfg");
	}
	private final int ACTION_DELAY = 1300;
	private List<DigSpot> spots = new ArrayList<DigSpot>();

	public void startDigging(final Client c) {
		if(System.currentTimeMillis() - c.lastDig < ACTION_DELAY)
			return;

		c.lastDig = System.currentTimeMillis();
		c.startAnimation(830);
		c.getPA().removeAllWindows();
		c.sendMessage("You dig into the ground...");

		c.getPA().stillGfx(572, c.absX, c.absY, 0, 6);


		for (DigSpot dig : getSpots()) {
			if(dig == null) continue;

			if (c.absX == dig.getTile().getX() && c.absY == dig.getTile().getY() && !dig.isItemFound()) {
				digUpItem(c, dig);
				return;
			}
		}
	}

	public void digUpItem(final Client e, final DigSpot dig) {
		e.getPA().createPlayersStillGfx(571, dig.getTile().getX(), dig.getTile().getY(), 0, 6);
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer c) {

				if (dig != null && e != null) {
					e.getItems().addItem(dig.getItem(), dig.getAmount());
					String name = e.getItems().getItemName(dig.getItem());
					boxMessage(e, "You have dug up " + (dig.getAmount() > 1 ?  "some" : "a") + " " + name + ".");
					e.sendMessage("You have dug up " + (dig.getAmount() > 1 ?  "some" : "a") + " " + name + ".");
					dig.setItemFound(true);
					dig.ticks = 10;
				}

				c.stop();
			}
		}, ACTION_DELAY);
	}
	
	public void boxMessage(Client c, String s) {
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	public List<DigSpot> getSpots() {
		return spots;
	}

	public void process() {
		for (DigSpot dig : getSpots()) {
			if(dig == null) continue;

			if(dig.getTicks() > 0)
				dig.ticks--;

			if (dig.getTicks() == 0 && dig.isItemFound()) {
				dig.setRandomTile();
				dig.setItemFound(false);
			}
		}
	}

	public boolean loadSpots(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./"+fileName));
		} catch(FileNotFoundException fileex) {
			System.out.println(fileName+": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch(IOException ioexception) {
			System.out.println(fileName+": error loading file.");
			return false;
		}
		while(!EndOfFile && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spot")) {
					spots.add(new DigSpot(Integer.parseInt(token3[0]),
						Integer.parseInt(token3[1]),
						Integer.parseInt(token3[2]),
						Integer.parseInt(token3[3]),
						Integer.parseInt(token3[4]),
						Integer.parseInt(token3[5])));
				}
			} else {
				if (line.equals("[END]")) {
					try {
						objectFile.close();
					} catch(IOException ioexception) {
						System.out.println("Error closing file: " + fileName);
					}
					return true;
				}
			}
			try {
				line = objectFile.readLine();
			} catch(IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch(IOException ioexception) {
			System.out.println("[2]Error closing file: " + fileName);
		}
		return false;
	}

	private class DigSpot {

		private int item;
		private int amount;
		public int ticks;
		private Tile tile;
		private Tile oldTile;
		private Area area;
		private boolean itemFound;

		DigSpot(int item, int amount, int lx, int ly, int hx, int hy) {
			this.item = item;
			this.amount = amount;
			ticks = 0;
			area = new Area(lx, ly, hx, hy);
			tile = area.getRandomTile();
			itemFound = false;
		}

		public void setRandomTile() {
			oldTile = tile;
			tile = area.getRandomTile();
			if(tile.getX() == oldTile.getX() && tile.getY() == oldTile.getY() && area.getTileCount() > 1)
				setRandomTile();
		}

		public int getItem() {
			return item;
		}

		public int getAmount() {
			return amount;
		}

		public int getTicks() {
			return ticks;
		}

		public Tile getTile() {
			return tile;
		}

		public Tile getOldTile() {
			return oldTile;
		}

		public Area getArea() {
			return area;
		}

		public boolean isItemFound() {
			return itemFound;
		}

		public void setItemFound(boolean itemFound) {
			this.itemFound = itemFound;
		}

	}

}