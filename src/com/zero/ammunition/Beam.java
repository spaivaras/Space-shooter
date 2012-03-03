package com.zero.ammunition;

import box2dLight.ConeLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zero.main.Manager;


public class Beam extends Ammunition {

	@Override
	protected String getTextureName() {
		return "laser";
	}

	@Override
	protected void createPhysicsBody() {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(gun.getNozzlePosition(this.sprite.getHeight() / 2));
		bodyDef.type = BodyType.DynamicBody;
		body = manager.getWorld().createBody(bodyDef);
	
		PolygonShape shape = new PolygonShape();
		
		Float halfX = (float)sprite.getWidth() / Manager.PTM / 2;
		Float halfY = (float)sprite.getHeight() / Manager.PTM / 2;
		
		shape.setAsBox(halfX, halfY);
		  
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
	    fixtureDef.density = 0.01f;
	    fixtureDef.friction = 0f;
	    fixtureDef.restitution = 0.0f;
	    fixtureDef.isSensor = true;
	    fixtureDef.filter.categoryBits = Ammunition.COLLISION_BITS;
	    fixtureDef.filter.maskBits = (short)~Ammunition.COLLISION_BITS;
	    body.createFixture(fixtureDef);
	    
	    shape.dispose();
		
        body.setLinearDamping(0f);
        body.setAngularDamping(0f);
        body.setFixedRotation(true);
        body.setBullet(true);
        
        body.setTransform(body.getPosition(), gun.getNozzleAngle());
	}

	@Override
	protected void updateInternal(float delta) {
	}

	@Override
	protected float getSpeed() {
		return 0.1f;
	}

	@Override
	protected float getLifeTime() {
		return 0.2f;
	}

	@Override
	protected void createLight() {
		light = new ConeLight(manager.getLightEngine(), 10, new Color(0f, 0f, 1f, 1f), 15f, 0f, 0f, 0f, 10f);
		light.attachToBody(body, 0, 0);
		light.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
		light.setDirection((float)Math.toDegrees( body.getAngle()) - 90 );
	}

	@Override
	public float getAmmunitionDamage() {
		return 25f;
	}
}
