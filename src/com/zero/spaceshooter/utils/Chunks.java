package com.zero.spaceshooter.utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zero.objects.Player;

public class Chunks {

	protected static Chunks chunks;
	protected float chunkSize = Chunk.CHUNCK_SIZE;
	protected Vector2 playerPos;
	protected Vector2 chunkCenter = new Vector2(0,0);
	protected Vector2 chunkId = new Vector2(0,0);
	protected Vector2 lastChunkId = new Vector2(0, 0);
	protected Player player;
	protected ArrayList<Vector2> chunkLogic = new ArrayList<Vector2>(9);
	protected ArrayList<Chunk> currentChunks = new ArrayList<Chunk>();
	protected ArrayList<Chunk> oldChunks = new ArrayList<Chunk>();
	
	private Chunks() {
		chunkLogic.add(0, new Vector2(-1, 1));
		chunkLogic.add(1, new Vector2(0, 1));
		chunkLogic.add(2, new Vector2(1, 1));
		chunkLogic.add(3, new Vector2(-1, 0));
		chunkLogic.add(4, new Vector2(0, 0));
		chunkLogic.add(5, new Vector2(1, 0));
		chunkLogic.add(6, new Vector2(-1, -1));
		chunkLogic.add(7, new Vector2(0, -1));
		chunkLogic.add(8, new Vector2(1, -1));
		this.calculateCurentChunk();
		this.setChunks(this.chunkId);
	}
	
	public static Chunks getInstance() {
		if (chunks == null) {
			chunks = new Chunks();
		}
		return chunks;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void calculateCurentChunk() {
		if(this.player != null) {
			this.playerPos = this.player.getShip().getBody().getWorldCenter();
			float x_p, y_p;
			float x_c, y_c;
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
	
	public Vector2 getCentralChunkId() {
		this.calculateCurentChunk();
		return this.chunkId;
	}
	
	public Vector2 getChunkCenter() {
		this.calculateCurentChunk();
		return this.chunkCenter;
	}
	
	/**
	 * 
	 * @param i chunkID
	 * @return Chunk
	 */
	public Chunk getChunk(int i) {
		this.calculateCurentChunk();
		if(currentChunks.size() != 9) {
			this.setChunks(this.chunkId);
		}
		if(0 <= i && i <= 8) {
			return currentChunks.get(i);
		}
		
		return new Chunk(this.chunkId);
	}
	
	public ArrayList<Chunk> getChunkList() {
		return currentChunks;
	}
	
	public void setChunks(Vector2 chunkid) {
		for(int i = 0; i < 9; i++) {
			Vector2 id = chunkLogic.get(i);
			Chunk chunk = new Chunk((chunkid.x + id.x) * Chunk.CHUNCK_SIZE, (chunkid.y + id.y) * Chunk.CHUNCK_SIZE);
			if(i != 0) {
				chunk.enter();
			}
			currentChunks.add(i, chunk);
		}
	}
	
	protected void replaceChunk(int i) {
		Chunk chunk = currentChunks.get(i);
		chunk.exit();
		currentChunks.remove(i);
		Vector2 id = chunkLogic.get(i);
		Chunk newchunk = new Chunk((this.chunkId.x + id.x) * Chunk.CHUNCK_SIZE, (this.chunkId.y + id.y) * Chunk.CHUNCK_SIZE);
		newchunk.enter();
		currentChunks.set(i, newchunk);
	}

	
	protected Chunk getNewChunk(int i) {
		Vector2 id = chunkLogic.get(i);
		Chunk newchunk = new Chunk((this.chunkId.x + id.x) * Chunk.CHUNCK_SIZE, (this.chunkId.y + id.y) * Chunk.CHUNCK_SIZE);
		newchunk.enter();
		return newchunk;
	}
	
	protected void moveTop() {
		Chunk chunk;
		for(int i = 0; i < 3; i++) {
			chunk = currentChunks.get(i);
			chunk.exit();
			///currentChunks.remove(chunk);
		}
		for(int i = 0; i < 6; i++) {
			currentChunks.set(i, currentChunks.get(i+3));
		}
		
		for(int i = 6; i < 9; i++) {
			currentChunks.set(i, this.getNewChunk(i));
		}
		
	}
	
	protected void moveLeft() {
		Chunk chunk;
		for(int i = 1; i < 4; i++) {
			chunk = currentChunks.get((i*3)-1);
			chunk.exit();
			///currentChunks.remove(chunk);
		}
		currentChunks.set(2, currentChunks.get(1));
		currentChunks.set(1, currentChunks.get(0));
		
		currentChunks.set(5, currentChunks.get(4));
		currentChunks.set(4, currentChunks.get(3));
		
		currentChunks.set(8, currentChunks.get(7));
		currentChunks.set(7, currentChunks.get(6));
		
		for(int i = 0; i < 3; i++) {
			currentChunks.set((i*3), this.getNewChunk((i*3)));
		}
		
	}
	
	protected void moveBottom() {
		Chunk chunk;
		for(int i = 6; i < 9; i++) {
			chunk = currentChunks.get(i);
			chunk.exit();
			//currentChunks.remove(chunk);
		}
		for(int i = 6; i > 0; i--) {
			currentChunks.set(i+2, currentChunks.get(i-1));
		}
		
		for(int i = 0; i < 3; i++) {
			currentChunks.set(i, this.getNewChunk(i));
		}
	}
	
	protected void moveRight() {
		Chunk chunk;
		for(int i = 0; i < 3; i++) {
			chunk = currentChunks.get((i*2));
			chunk.exit();
			//currentChunks.remove(chunk);
		}
		currentChunks.set(0, currentChunks.get(1));
		currentChunks.set(1, currentChunks.get(2));
		
		currentChunks.set(3, currentChunks.get(4));
		currentChunks.set(4, currentChunks.get(5));
		
		currentChunks.set(6, currentChunks.get(7));
		currentChunks.set(7, currentChunks.get(8));
		
		for(int i = 1; i < 4; i++) {
			currentChunks.set((i*3)-1, this.getNewChunk((i*3)-1));
		}
	}
	public void update() {
		if(!lastChunkId.equals(chunkId)) {
			if(lastChunkId.y == chunkId.y) {
				if(lastChunkId.x > chunkId.x) {
					this.moveRight();
				} else {
					this.moveLeft();
				}
			}
			
			if(lastChunkId.x == chunkId.x) {
				if(lastChunkId.y > chunkId.y) {
					this.moveTop();
				} else {
					this.moveBottom();
				}
			}
			lastChunkId = chunkId.cpy();
		}
	}
	
}
