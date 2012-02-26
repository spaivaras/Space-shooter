package com.zero.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zero.main.PolygonParser;

public class Plane extends Entity 
{	
	public static final float SHOT_DELAY = 0.2f;
	public static final float ROTATE_SPEED_FACTOR = 90f;
	public static final float THRUSTER_FACTOR = 120f;
	public static final float REV_THRUSTER_FACTOR = 50f;

	private Boolean shotDelayOn = false;
	float shotCounter = 0;
	Sound thrusterSound = null;
	Sound revThrusterSound = null;
	
	public Plane(TextureAtlas atlas, String name, Float x, Float y) {
		super(atlas, name, x, y);
		this.angleDifference = 180;
	}

	@Override
	public void updatePosition(float delta) {
		if (shotDelayOn && shotCounter < SHOT_DELAY) {
			shotCounter += delta;
		} else if(shotDelayOn && shotCounter >= SHOT_DELAY) {
			shotDelayOn = false;
			shotCounter = 0;
		}
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			body.applyLinearImpulse(getThrustVector(false), body.getWorldCenter());
			if (thrusterSound == null) {
				thrusterSound = manager.playSound("thruster", 1f, 0.3f, true);
			}
		} else if(thrusterSound != null) {
			thrusterSound.stop();
			thrusterSound = null;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			body.applyLinearImpulse(getThrustVector(true), body.getWorldCenter());
			if (revThrusterSound == null) {
				revThrusterSound = manager.playSound("thruster", 3f, 0.2f, true);
			}
		} else if(revThrusterSound != null) {
			revThrusterSound.stop();
			revThrusterSound = null;
		}
		
		if(Gdx.input.isKeyPressed(Keys.A)) {
			body.applyAngularImpulse(ROTATE_SPEED_FACTOR);
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			body.applyAngularImpulse(-ROTATE_SPEED_FACTOR);
		}
		if(Gdx.input.isKeyPressed(Keys.SPACE) && !shotDelayOn) {
			shotDelayOn = true;	
			
			Bullet laser = new Bullet(atlas, "laser", this);
			manager.addEntity(laser);					
		}
	}

	private Vector2 getThrustVector(Boolean reverse) {
		double rads = body.getAngle() + Math.toRadians(270);
		double factor;

		if (reverse) {
			factor = REV_THRUSTER_FACTOR;
		} else {
			factor = THRUSTER_FACTOR;
		}

		//x + d * cos(a)  y + d.sin(a)
		double x = factor * Math.cos(rads);
		double y = factor * Math.sin(rads);

		Vector2 vector = new Vector2((float)x, (float)y);
		if (reverse) {
			return vector.mul(-1f);
		}

		return vector; 
	}

	//Create physic based structures, body, shape, fixture
	//and registers physics body to physics world
	@Override
	public void createPhysicsBody() {
	    bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(x, y));
		bodyDef.type = BodyType.DynamicBody;
		body = manager.getWorld().createBody(bodyDef);

		PolygonParser pp = new PolygonParser();
		pp.parseEntity("plane", body);

		body.setLinearDamping(0.6f);
		body.setAngularDamping(0.9f);
		body.setTransform(body.getPosition(), (float)Math.toRadians(180));
	}

	@Override
	public Boolean collision(Entity with) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void hit() {
		// TODO Auto-generated method stub

	}
}
