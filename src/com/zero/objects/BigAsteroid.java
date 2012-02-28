package com.zero.objects;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.MassData;
import com.zero.main.PolygonParser;


public class BigAsteroid extends Entity {

	public BigAsteroid(TextureAtlas atlas, String name, Float x, Float y) {
		super(atlas, name, x, y);
		this.angleDifference = 180f;
		sprite.flip(true, false);
	}

	@Override
	public void updatePosition(float delta) {
	}

	@Override
	public void createPhysicsBody() {
		
		bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(x, y));
		bodyDef.type = BodyType.DynamicBody;
		body = manager.getWorld().createBody(bodyDef);

		PolygonParser pp = new PolygonParser();
		pp.parseEntity("asteroid-big", body, (short)0x0016);

		body.setLinearDamping(0.3f);
		body.setAngularDamping(0.5f);
		MassData a = new MassData();
		a.mass = 100000f;
		body.setMassData(a);
		
		body.setGravityScale(0.001f);
	}


	@Override
	public void hit() {
	}

	@Override
	protected void createLights() {
		
	}

	@Override
	protected void removeCustomLights() {	
	}
}