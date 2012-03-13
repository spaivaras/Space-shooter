package com.zero.main;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ResourceCache {
	public static ResourceCache cache;
	HashMap<String, Sound> sounds = null;
	HashMap<String, TextureAtlas> atlases = null;
	HashMap<String, PolygonParser> pParsers = null;
	
	public ResourceCache() {
		sounds = new HashMap<String, Sound>(20);
		atlases = new HashMap<String, TextureAtlas>(5);
		pParsers = new HashMap<String, PolygonParser>(5);
		
		loadResources();
	}
	
	private void loadResources() {
		loadPolygonParsers();
		loadSounds();
		loadTextureAtlases();
	}
	
	private void loadTextureAtlases() {
		TextureAtlas tmp =  new TextureAtlas(Gdx.files.internal("res/atlases/pack"));
		atlases.put("main", tmp);
	}
	
	private void loadSounds() {
		Sound tmp = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/thrust.ogg"));
		sounds.put("thrust", tmp);
		
		tmp = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/turbo.ogg"));
		sounds.put("turbo", tmp);
		
		tmp = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/death.wav"));
		sounds.put("death", tmp);
		
		tmp = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/hit.ogg"));
		sounds.put("hit", tmp);
		
		tmp = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/round.ogg"));
		sounds.put("round-shot", tmp);
		
		tmp = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/laser.ogg"));
		sounds.put("laser-sound", tmp);
	}
	
	private void loadPolygonParsers() {
		PolygonParser temp = new PolygonParser();
		temp.load("light-fighter");
		pParsers.put("light-fighter", temp);
	}
	
	public static ResourceCache getInstance() {
		if (cache == null) {
			cache = new ResourceCache();
		}
		return cache;
	}
	
	
	
	public PolygonParser getPolygonParser(String name) {
		return pParsers.get(name);
	}
	
	public Sound getSound(String name) {
		return sounds.get(name);
	}
	
	public TextureAtlas getTextureAtlas(String name) {
		return atlases.get(name);
	}
	
}
