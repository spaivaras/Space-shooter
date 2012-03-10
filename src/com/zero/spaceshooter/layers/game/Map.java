package com.zero.spaceshooter.layers.game;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.zero.objects.Player;
import com.zero.spaceshooter.MainDirector;
import com.zero.spaceshooter.actors.BigAsteroid;
import com.zero.spaceshooter.actors.ManagerActor;
import com.zero.spaceshooter.layers.Layer;

public class Map extends Layer {
	
	private MainDirector director;
	protected ManagerActor manager;
	protected Random randomizer;
	protected Player player;
	protected TextureAtlas atlas;
	protected long seed;
	
	protected float chunkSize = 50f;
	protected Vector2 playerPos;
	protected Vector2 chunkCenter = new Vector2(0,0);
	
	
	
	public Map(float width, float height, ManagerActor manager, Player player, long seed) {
		this.width = width;
		this.height = height;
		this.manager = manager;
		this.player = player;
		randomizer = new Random();
		randomizer.setSeed(seed);
		init();
		initActors();
	}
	
	public void init() {
		calculateCurentChunk(this.player);
		
 	}
	
	public Vector2 calculateCurentChunk(Player player) {
		this.playerPos = player.getShip().getPosition();
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
		
		this.chunkCenter = new Vector2(x_c, y_c);
		return this.chunkCenter;
	}
	
	public int hash32shift(int key)
	{
	  key = ~key + (key << 15); // key = (key << 15) - key - 1;
	  key = key ^ (key >>> 12);
	  key = key + (key << 2);
	  key = key ^ (key >>> 4);
	  key = key * 2057; // key = (key + (key << 3)) + (key << 11);
	  key = key ^ (key >>> 16);
	  return key;
	}

	public int noise(int x, int y, int seed)
	{
	    return hash32shift(seed+hash32shift(x+hash32shift(y)));
	}
	
	public void initActors() {
		
	}

	public void act(float delta) {
		//super.act(delta);
		//calculateCurentChunk(this.player);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		//manager.switchCamera(false);
		//super.draw(batch, parentAlpha);
		
	}
	
}
