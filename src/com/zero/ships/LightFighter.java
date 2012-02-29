package com.zero.ships;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zero.interfaces.ShipController;
import com.zero.main.PolygonParser;

public class LightFighter extends Ship {
	
	private static final String NAME = "light-fighter";
	private static final String TEXTURE_REGION = "spaceships";
	private static final int TEXTURE_WIDTH = 94;
	private static final int TEXTURE_HEIGHT = 100;
	private static final float ANIMATION_INTERVAL = 0.1f;
	
	private TextureRegion[] shipAnimationSprites;
	private Animation shipAnimation;
	private float stateTime = 0f;
	
	private Light headLight = null;
	

	public LightFighter(ShipController controller, float x, float y) {
		super();
		homeX = x;
		homeY = y;
		this.controller = controller;
	}
	
	@Override
	protected void updateInternal(float delta) {
		if (shipAnimation == null) {
			prepareSprites();
		}
		stateTime += delta;
		
		if (headLight != null) {
			headLight.setDirection((float)Math.toDegrees( body.getAngle()) - 90f);
		}
		
		if (thrustersActive) {
			stateTime += delta;
		} else {
			stateTime = 0;
		}
	}
	
	@Override
	public void refilEnergy(float amount, float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Sprite getCustomSprite() {
		return  new Sprite(shipAnimation.getKeyFrame(stateTime, true));
	}

	@Override
	protected void createPhysicsBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(homeX, homeY);
		bodyDef.type = BodyType.DynamicBody;
		body = manager.getWorld().createBody(bodyDef);
		
		PolygonParser pp = new PolygonParser();
		pp.parseEntity(NAME, body, (short)0x0001);
		
        body.setLinearDamping(0.6f);
		body.setAngularDamping(0.9f);
		body.setTransform(body.getPosition(), (float)Math.toRadians(180));
	}

	@Override
	protected void createLight() {
		mainLight = new PointLight(manager.getLightEngine(), 128, new Color(1f, 1f, 1f, 0.5f), 7f, 0f, 0f);
		mainLight.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
		mainLight.attachToBody(body, 0f, 0f);
		
		headLight = new ConeLight(manager.getLightEngine(), 128, new Color(Color.ORANGE), 30f, 0f, 0f, 0f, 20f);
		headLight.attachToBody(body, 0, 0);
		headLight.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
	}
	
	@Override
	protected void removeCustomLights() {
		if (headLight != null) {
			headLight.remove();
		}
	}

	private void prepareSprites() {
		TextureRegion region = manager.getTextureAtlas("main").findRegion(TEXTURE_REGION);
		TextureRegion[][] tmp = region.split(TEXTURE_WIDTH, TEXTURE_HEIGHT);
		int index = 0;
        
		shipAnimationSprites = new TextureRegion[5];
		shipAnimationSprites[index++] = tmp[4][0];
		
		for (int j = 0; j < 4; j++) {
			shipAnimationSprites[index++] = tmp[0][j];
        }
		
		shipAnimation = new Animation(ANIMATION_INTERVAL, shipAnimationSprites);
	}

	@Override
	protected float getThrustersFactor() {
		return 170;
	}

	@Override
	protected float getRevThrustersFactor() {
		return 50;
	}

	@Override
	protected float getMaxLinearSpeed() {
		return 50;
	}

	@Override
	protected float getRotationFactor() {
		return 110;
	}
}
