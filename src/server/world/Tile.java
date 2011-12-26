package server.world;

public class Tile {

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Tile(int x, int y, int h) {
		this.x = x;
		this.y = y;
		this.h = h;
		height = h;
	}
	
	public Tile(int x, int y, int h, int f) {
		this.x = x;
		this.y = y;
		this.h = h;
		this.f = f;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getH() {
		return h;
	}
	
	public int getF() {
		return f;
	}
	
	private int x = -1;
	private int y = -1;
	private int h = -1;
	private int height = -1;
	private int f = -1;
}


