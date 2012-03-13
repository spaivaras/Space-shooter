package com.zero.ammunition;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zero.spaceshooter.actors.ManagerActor;


public class MetalRound extends Ammunition {

	protected void createSprite() {
		Pixmap p = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
		p.setColor(Color.RED);
		p.fillCircle(8, 8, 8);
		sprite = new Sprite(new Texture(p));
	}
	
	@Override
	protected String getTextureName() {
		return null;
	}

	@Override
	protected void createPhysicsBody() {
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(gun.getNozzlePosition(this.sprite.getHeight() / 2));
		bodyDef.type = BodyType.DynamicBody;
		body = manager.getWorld().createBody(bodyDef);
	
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / ManagerActor.PTM);
		  
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
		return 3f;
	}

	@Override
	protected float getLifeTime() {
		return 0.3f;
	}
	
	@Override
	public float getAmmunitionDamage() {
		return 50f;
	}

	@Override
	protected void createLight() {
		light = new PointLight(manager.getLightEngine(), 20, new Color(Color.RED), 2, 0, 0);
		light.attachToBody(body, 0, 0);
		light.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
	}
}
