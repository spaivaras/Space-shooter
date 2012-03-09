package com.zero.spaceshooter.actors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BigAsteroid extends SpriteActors {

	
	public BigAsteroid(TextureAtlas atlas, String name, Float x, Float y, float alpha) {
		super(atlas, name, x, y, alpha);
		this.width = 32;
		this.height = 32;
		//this.angleDifference = 180f;
		//mainSprite.flip(true, false);
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
}