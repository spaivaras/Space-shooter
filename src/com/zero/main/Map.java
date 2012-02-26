package com.zero.main;

import java.util.concurrent.CopyOnWriteArrayList;

public class Map {

	protected int blockWidth;
	protected int blockHeight;
	
	protected int _x;
	protected int _y;
	
	protected CopyOnWriteArrayList<Tile> _map;
	
	public void generate() {
		GenerateMap generator = new GenerateMap();
		generator.generate(100, 100);
		this._map = generator.getMap();
	}
	
	public CopyOnWriteArrayList<Tile> getMap() {
		return this._map;
	}
	
	
	public void setBlockSize(int width, int height) {
		this.blockHeight = height;
		this.blockWidth = width;
	}
	
	public int getBlockWidth() {
		return this.blockWidth;
	}
	public int getBlockHeight() {
		return this.blockHeight;
	}
	
	
	
}
