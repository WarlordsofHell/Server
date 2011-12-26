package server.world.map;

import server.world.Tile;
import server.util.Misc;

public class Area {

	int lowX;
	int highX;
	int lowY;
	int highY;

	public Area(int lowX, int lowY, int highX, int highY) {
		this.lowX = lowX;
		this.lowY = lowY;
		this.highX = highX;
		this.highY = highY;
	}

	public Tile getRandomTile() {
		int x = random(lowX, highX);
		int y = random(lowY, highY);
		return new Tile(x, y);
	}

	public int random(int min, int max) {
		return Misc.random(max - min) + min;
	}

	public int getHighX() {
		return highX;
	}

	public int getHighY() {
		return highY;
	}

	public int getLowY() {
		return lowY;
	}

	public int getLowX() {
		return lowX;
	}

	public int getTileCount() {
		return ((getHighX() - getLowX()) + (getHighY() - getLowY())) + 1;
	}

}
