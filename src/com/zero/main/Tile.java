package com.zero.main;

public class Tile {
	
	protected int x;
	protected int y;
	protected String name;
	
	protected int type;
	
	public Tile(String name) {
		this.name = name;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getX() {
		return x;
	}
	
	public String getTitle() {
		return name;
	}
			
	
	public int getY() {
		return y;
	}
	
	public int getType() {
		return type;
	}
	
}
