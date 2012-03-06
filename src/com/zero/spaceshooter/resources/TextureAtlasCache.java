package com.zero.spaceshooter.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureAtlasCache {
	private static TextureAtlasCache instance = null;
	protected TextureAtlas atlas;
	
	public TextureAtlasCache() {
		atlas = new TextureAtlas(Gdx.files.internal("res-packed/pack"));
	}
	
	public synchronized static TextureAtlasCache instance()
	{
		if (instance == null)
		{
			instance = new TextureAtlasCache();
		}
		
		return instance;
	}
	
	public TextureAtlas getAtlas() {
		return this.atlas;
	}
}
