package com.zero.objects;

import box2dLight.ConeLight;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zero.main.PolygonParser;

public class Plane extends Entity 
{	
	public static final float SHOT_DELAY = 0.2f;
	public static final float ROTATE_SPEED_FACTOR = 110f;
	public static final float THRUSTER_FACTOR = 170f;
	public static final float BOOST_FACTOR = 1000f;
	public static final float MAX_SPEED = 50f;
	public static final float REV_THRUSTER_FACTOR = 50f;

	private Boolean shotDelayOn = false;
	private float shotCounter = 0;
	private Sound thrusterSound = null;
	private Sound revThrusterSound = null;
	private Sound turboSound = null;
	private ConeLight headLamp;
	private Boolean boost = false;
	
	
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
		
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			this.boost = true;
		}
		if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || !Gdx.input.isKeyPressed(Keys.W)) {
			if (turboSound != null) {
				turboSound.stop();
				turboSound = null;
			}
			boost = false;
		}
		if(Gdx.input.isKeyPressed(Keys.W)) {
			body.applyLinearImpulse(getThrustVector(false), body.getWorldCenter());
			if (thrusterSound == null) {
				thrusterSound = manager.playSound("thruster", 1f, 0.3f, true);
			}
			if (boost && turboSound == null) {
				turboSound = manager.playSound("turbo", 1f, 0.2f, false);
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
		
		if (this.headLamp != null) {
			this.headLamp.setDirection((float)Math.toDegrees( body.getAngle()) - 90f);
		}
	}

	private Vector2 getThrustVector(Boolean reverse) {
		double rads = body.getAngle() + Math.toRadians(270);
		double factor;

		if (reverse) {
			factor = REV_THRUSTER_FACTOR;
		} else {
			factor = THRUSTER_FACTOR;
			
			if (boost) {
				factor += BOOST_FACTOR;
			}
		}
		
		if (body.getLinearVelocity().len() > MAX_SPEED) {
			return new Vector2(0f, 0f);
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
		pp.parseEntity("plane", body, (short)0x0001);

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

	@Override
	protected void createLights() {
		glowLight = new PointLight(manager.getLightEngine(), 128, new Color(1f, 1f, 1f, 0.5f), 5f, 0f, 0f);
		glowLight.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
		glowLight.attachToBody(body, 0f, 0f);
		
		this.headLamp = new ConeLight(manager.getLightEngine(), 128, new Color(Color.ORANGE), 30f, 0f, 0f, 0f, 20f);
		this.headLamp.attachToBody(body, 0, 0);
		this.headLamp.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
	}

	@Override
	protected void removeCustomLights() {
		this.headLamp.remove();
	}
}
