package com.zero.spaceshooter.utils;

import com.badlogic.gdx.math.Vector2;
import com.zero.objects.Player;

public class Chunks {

	protected static Chunks chunk;
	protected float chunkSize = 50f;
	protected Vector2 playerPos;
	protected Vector2 chunkCenter = new Vector2(0,0);
	protected Vector2 chunkId = new Vector2(0,0); 
	protected Player player;
	
	
	private Chunks() {
		
	}
	
	public static Chunks getInstance() {
		if (chunk == null) {
			chunk = new Chunks();
		}
		return chunk;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void calculateCurentChunk() {
		if(this.player != null) {
			this.playerPos = this.player.getShip().getBody().getWorldCenter();
			float x_p, y_p;
			float x_c, y_c;
			float x_c_c, y_c_c;
			x_p = (Math.abs(this.playerPos.x) - (chunkSize / 2));
			y_p = (Math.abs(this.playerPos.y) - (chunkSize / 2));
			if(x_p <= 0) {
				x_c = 0;
			} else {
				x_c = (float) (Math.floor(x_p / chunkSize) +1);
				if(this.playerPos.x < 0) {
					x_c *= -1;
				}
			}
			
			
			if(y_p <= 0) {
				y_c = 0;
			} else {
				y_c = (float) (Math.floor(y_p / chunkSize) +1);
				if(this.playerPos.y < 0) {
					y_c *= -1;
				}
			}
			
			this.chunkId = new Vector2(x_c, y_c);
		}
	}
	
	public Vector2 getChunkId() {
		this.calculateCurentChunk();
		return this.chunkId;
	}
	
	public Vector2 getChunkCenter() {
		this.calculateCurentChunk();
		return this.chunkCenter;
	}
	
}
