package com.zero.spaceshooter.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

abstract class SpriteActors extends Actor {
	protected String name;
	protected Sprite mainSprite;
	protected float x, y, width, height;
	
	public SpriteActors(TextureAtlas atlas, String name, Float x, Float y) {
			this.name = name;
			this.x = x;
			this.y = y;
			
			mainSprite = atlas.createSprite(name);
		}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(1, 1, 1, parentAlpha);
        batch.draw(mainSprite, x, y, width, height);
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.update(delta);
	}
	
	public abstract void update(float delta);

}
