package com.zero.spaceshooter.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;

abstract class SpriteActors extends Actor {
	protected String spriteName;
	protected Sprite mainSprite;
	protected float x, y, width, height, alpha;
	
	public SpriteActors(TextureAtlas atlas, String name, Float x, Float y, float alpha) {
			this.spriteName = name;
			this.x = x;
			this.y = y;
			this.alpha = alpha;
			
			mainSprite = atlas.createSprite(name);
		}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setColor(1, 1, 1, parentAlpha);
		mainSprite.setColor(1, 1, 1, alpha);
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
