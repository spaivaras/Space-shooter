package com.zero.spaceshooter.layers.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
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
	
	
	
	public Map(float width, float height, ManagerActor manager, Player player, long seed) {
		this.width = width;
		this.height = height;
		this.manager = manager;
		this.player = player;
		randomizer = new Random();
		randomizer.setSeed(seed);
		
		initActors();
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
		director = MainDirector.instance();
		atlas = director.getAtlas();
		int x = (int) Math.floor((double) 0f);
		int y = (int) Math.floor((double) 0f);
		System.out.println(noise(x, y, 1521));
		addActor(new BigAsteroid(atlas, "asteroid-big", 0f, 0f, 1f));
		addActor(new BigAsteroid(atlas, "asteroid-big", 32f, 0f, 0.5f));
		addActor(new BigAsteroid(atlas, "asteroid-big", 64f, 0f, 0f));
		addActor(new BigAsteroid(atlas, "asteroid-big", 400f, 0f, 1f));
	}

	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		manager.switchCamera(false);
		super.draw(batch, parentAlpha);
		
	}
	
}
