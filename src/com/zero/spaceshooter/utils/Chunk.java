package com.zero.spaceshooter.utils;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import sun.java2d.Disposer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zero.interfaces.WorldObject;
import com.zero.objects.Enemy;
import com.zero.spaceshooter.actors.ManagerActor;

public class Chunk extends Disposer {

	protected float x, y;
	static public final float CHUNCK_SIZE = 50;
	protected ArrayList<Vector2> positions = new ArrayList<Vector2>(4);
	protected float x0,y0, x1,y1, x2,y2, x3,y3;
	protected ArrayList<WorldObject> entities = new ArrayList<WorldObject>();
	protected ManagerActor manager;
	
	public Chunk(Vector2 chunkpos) {
		this.x = chunkpos.x;
		this.y = chunkpos.y;
		this._calculate();
		this.manager = ManagerActor.getInstance();
	}
	
	public Chunk(float x, float y) {
		this.x = (float) Math.floor(x);
		this.y = (float) Math.floor(y);
		this._calculate();
		this.manager = ManagerActor.getInstance();
	}
	
	protected void _loadObjects() {
		if(!this._loadFromFile()) {
			this._loadFromRandom();
		}
	}
	
	protected boolean _saveObjects() {
		return true;
	}
	
	public void enter() {
		this._loadObjects();
		for (WorldObject entity : entities) {
			this.manager.addEntityNext(entity);
		}
	}
	
	public void exit() {
		this._saveObjects();
		for (WorldObject entity : entities) {
			this.manager.removeEntityNex(entity);
		}
	}
	protected boolean _loadFromFile() {
		
		return false;
	}
	
	protected boolean _loadFromRandom() {
		Random rnd = new Random();
		for(int i = 0; i < 5; i++) {
			entities.add(new Enemy(this.x0 + rnd.nextInt(50), this.y0 + rnd.nextInt(50)).getShip());
		}
		return true;
	}
	
	private float randomBetwean(float min, float max) {
		Random rnd = new Random();
		float value = Math.min(min, max) + rnd.nextInt((int)Math.abs(max - min));
		return value;
	}
	
	protected void _calculate() {
		this.x0 = this.x - (Chunk.CHUNCK_SIZE / 2);
		this.y0 = this.y - (Chunk.CHUNCK_SIZE / 2);
		positions.add(0, new Vector2(x0, y0));
		
		this.x1 = this.x - (Chunk.CHUNCK_SIZE / 2);
		this.y1 = this.y + (Chunk.CHUNCK_SIZE / 2);
		positions.add(1, new Vector2(x1, y1));
		
		this.x2 = this.x + (Chunk.CHUNCK_SIZE / 2);
		this.y2 = this.y + (Chunk.CHUNCK_SIZE / 2);
		positions.add(2, new Vector2(x2, y2));
		
		this.x3 = this.x + (Chunk.CHUNCK_SIZE / 2);
		this.y3 = this.y - (Chunk.CHUNCK_SIZE / 2);
		positions.add(3, new Vector2(x3, y3));
		
	}
	
	public ArrayList<Vector2> getPositions() {
		return positions;
	}
	
	public Vector2 getCenter() {
		return new Vector2(x, y);
	}
	
}
