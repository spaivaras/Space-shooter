package com.zero.objects;

import java.util.Random;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zero.main.Manager;
import com.zero.main.PolygonParser;


public class DummyPlane extends Entity {

	public static final int MAX_COLOR_CYCLE_COUNT = 2;
	public static final float COLOR_CHANGE_TIME = 0.1f;

	private Boolean colored = false;
	private int colorCycleCount = MAX_COLOR_CYCLE_COUNT;
	private float colorCycleTime = 0;
	private Color colorOverlay;


	public DummyPlane(TextureAtlas atlas, String name, Float x, Float y) {
		super(atlas, name, x, y);
		this.angleDifference = 180;
	}

	@Override
	public void updatePosition(float delta) {
		if(colorCycleCount < MAX_COLOR_CYCLE_COUNT) {
			colorCycleTime  += delta;
			if (colorCycleTime >= COLOR_CHANGE_TIME ) {
				colored = !colored;
				colorCycleTime = 0;
				colorCycleCount++;
			}
		} else {
			colored = false;
		}
	}
	
	public void draw() {
		if (shouldDraw && sprite != null) {
			Vector2 screen = manager.translateCoordsToScreen(new Vector2(x, y), 
					(float)this.sprite.getWidth() / 2, 
					(float)this.sprite.getHeight() / 2);
			
			if (colored) {
				sprite.setColor(colorOverlay);
			} else {
				sprite.setColor(Color.WHITE);
			}
			
			sprite.setPosition(screen.x, screen.y);
			sprite.setScale(1f / (float)Manager.PTM );
			sprite.draw(manager.getBatch());
		}
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
		pp.parseEntity("plane", body, (short)0x0002);

		body.setLinearDamping(0.3f);
		body.setAngularDamping(0.5f);
		body.setTransform(body.getPosition(), (float)Math.toRadians(180));
	}

	@Override
	public Boolean collision(Entity with) {
		return false;
	}

	@Override
	public void hit() {
		Random randomizer = new Random();

		colorOverlay = new Color(randomizer.nextFloat(), randomizer.nextFloat(), randomizer.nextFloat(), 1.0f);
		colorCycleCount = 0;

		manager.playSound("hit", 2.5f, 0.2f, false);
	}

	@Override
	protected void createLights() {
		glowLight = new PointLight(manager.getLightEngine(), 128, new Color(1f, 1f, 1f, 0.5f), 5f, 0f, 0f);
		glowLight.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
		glowLight.attachToBody(body, 0f, 0f);
		
	}

	@Override
	protected void removeCustomLights() {
		// TODO Auto-generated method stub
		
	}
}