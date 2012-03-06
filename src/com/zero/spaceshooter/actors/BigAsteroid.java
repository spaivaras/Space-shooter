package com.zero.spaceshooter.actors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class BigAsteroid extends SpriteActors {

	
	public BigAsteroid(TextureAtlas atlas, String name, Float x, Float y) {
		super(atlas, name, x, y);
		this.width = 100;
		this.height = 100;
		//this.angleDifference = 180f;
		//mainSprite.flip(true, false);
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
}