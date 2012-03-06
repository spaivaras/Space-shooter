package com.zero.spaceshooter.resources;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.zero.spaceshooter.definitions.GameActorTextures;
import com.zero.texture.TextureDefinition;


/**
 * Represents a generic texture cache backed by the {@link TextureAtlas}.
 * 
 */
public class TextureCache implements Disposable
{
	private static TextureCache instance = null;
	
	private TextureAtlas textureAtlas = null;
	
	private Map<String, TextureDefinition> definitions;
	
	/**
	 * Singleton instance.
	 * 
	 * @return An instance of this class.
	 */
	public synchronized static TextureCache instance()
	{
		if (instance == null)
		{
			instance = new TextureCache();
		}
		
		return instance;
	}
	
	private TextureCache()
	{
		definitions = new HashMap<String, TextureDefinition>();
	}
	
	/**
	 * Pack pack file textures (textures file resides in same directory).
	 * 
	 * @param packFile
	 */
	public void load(String packFile)
	{
		if (textureAtlas == null)
		{
			textureAtlas = new TextureAtlas(Gdx.files.internal(packFile));
		}
	}
	
	/**
	 * Load predefined textures.
	 * 
	 * This requires texture definitions to be added to the
	 * {@link GameActorTextures} structure.
	 */
	public void load()
	{
		if (textureAtlas == null)
		{
			textureAtlas = new TextureAtlas();
		}
		else
		{
			dispose();
			
			textureAtlas = new TextureAtlas();
		}
		
		for (TextureDefinition definition : GameActorTextures.TEXTURES)
		{
			Texture texture = new Texture(Gdx.files.internal(definition.getPath()));
			TextureRegion textureRegion = new TextureRegion(texture);
			
			textureAtlas.addRegion(definition.getName(), textureRegion);
			definitions.put(definition.getName(), definition);
		}
		
	}
	
	/**
	 * Fetch texture region from cache.
	 * 
	 * @param name
	 *            The texture name.
	 * 
	 * @return The texture region.
	 */
	public TextureRegion getTexture(TextureDefinition definition)
	{
		return textureAtlas.findRegion(definition.getName());
	}
	
	/**
	 * Fetch texture region from cache.
	 * 
	 * @param name
	 *            The texture name.
	 * 
	 * @return The texture region.
	 */
	public TextureDefinition getDefinition(String name)
	{
		return definitions.get(name);
	}
	
	/**
	 * Dispose of cache data.
	 * 
	 */
	@Override
	public void dispose()
	{
		definitions.clear();
		textureAtlas.dispose();
	}
}
