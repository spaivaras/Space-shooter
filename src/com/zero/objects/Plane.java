package com.zero.objects;

import java.util.ArrayList;

import box2dLight.ConeLight;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zero.guns.Gun;
import com.zero.guns.Pistol;
import com.zero.guns.RaptorLaser;
import com.zero.guns.RepeaterLaser;
import com.zero.main.Manager;
import com.zero.main.PolygonParser;

public class Plane extends Entity
{	
	public static final float ROTATE_SPEED_FACTOR = 110f;
	public static final float THRUSTER_FACTOR = 170f;
	public static final float BOOST_FACTOR = 1000f;
	public static final float MAX_SPEED = 50f;
	public static final float REV_THRUSTER_FACTOR = 50f;

	private Sound thrusterSound = null;
	private Sound revThrusterSound = null;
	private Sound turboSound = null;
	private ConeLight headLamp;
	private boolean boost = false;
	private ArrayList<Gun> guns;
	private int gunSelected = 0;
	private boolean gunSwitched = false;
	
	private float energyLevel = 100f;
	
	private TextureRegion[] shipAnimationSprites;
	private Animation shipAnimation;
	private float stateTime = 0f;
	
	
	public Plane(TextureAtlas atlas, String name, Float x, Float y) {
		super(atlas, name, x, y);
		
		//sprite = atlas.createSprite(name);
		
		
		this.angleDifference = 180;
		guns = new ArrayList<Gun>(3);
		
		guns.add(new RaptorLaser(this));
		guns.add(new RepeaterLaser(this));
		guns.add(new Pistol(this));
	}

	@Override
	public void updatePosition(float delta) {
	
		
		guns.get(0).update(delta);
		guns.get(1).update(delta);
		guns.get(2).update(delta);
		
		
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
		pp.parseEntity(this.name, body, (short)0x0001);

		body.setLinearDamping(0.6f);
		body.setAngularDamping(0.9f);
		body.setTransform(body.getPosition(), (float)Math.toRadians(180));
	}

	
	@Override
	public void hit() {
		manager.playSound("hit", 2.5f, 0.2f, false);
	}
	
	@Override
	protected void createLights() {
		glowLight = new PointLight(manager.getLightEngine(), 128, new Color(1f, 1f, 1f, 0.5f), 7f, 0f, 0f);
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
	
	public void draw() {
		if (shouldDraw) {
			sprite = new Sprite(shipAnimation.getKeyFrame(stateTime, true));
			Vector2 screen = manager.translateCoordsToScreen(new Vector2(x, y), 
					(float)this.sprite.getWidth() / 2, 
					(float)this.sprite.getHeight() / 2);
						
			sprite.setRotation((float)Math.toDegrees(  body.getAngle()  ) + angleDifference);
			
			sprite.setPosition(screen.x, screen.y);
			sprite.setScale(1f / (float)Manager.PTM );
			sprite.draw(manager.getBatch());
		}
	}

	@Override
	public float getEnergyLevel() {
		return energyLevel;
	}

	@Override
	public boolean drawEnergy(float amount) {
		if (energyLevel >= amount) {
			energyLevel -= amount;
			return true;
		}
		return false;
	}
}
