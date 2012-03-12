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
	}
	
	public void init() {
		
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
