package com.zero.ships;

import java.util.ArrayList;
import java.util.Iterator;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zero.guns.Gun;
import com.zero.guns.Pistol;
import com.zero.guns.RaptorLaser;
import com.zero.guns.RepeaterLaser;
import com.zero.interfaces.ShipController;
import com.zero.main.PolygonParser;

public class LightFighter extends Ship {
	
	private static final String NAME = "light-fighter";
	private static final String TEXTURE_REGION = "spaceships";
	private static final int TEXTURE_WIDTH = 94;
	private static final int TEXTURE_HEIGHT = 100;
	private static final float ANIMATION_INTERVAL = 0.09f;
	private static final Color DEFAULT_HEADLIGHT_COLOR = Color.ORANGE;
	private static final float BASE_ENERGY_REGENERATION = 3f;
	
	private TextureRegion[] shipAnimationSprites;
	private Animation shipAnimation;
	private float stateTime = 0f;
	
	private Light headLight = null;
	
	private ArrayList<Gun> arsenal;
	private int selectedGun = 0;
	

	public LightFighter(ShipController controller, float x, float y) {
		super();
		homeX = x;
		homeY = y;
		this.controller = controller;
		energyLevel = 100f;
	}
	
	@Override
	protected void updateInternal(float delta) {
		if (shipAnimation == null) {
			prepareSprites();
		}
		if (mainGun == null) {
			arsenal = new ArrayList<Gun>(3);
			
			arsenal.add(new RaptorLaser(this));
			arsenal.add(new RepeaterLaser(this));
			arsenal.add(new Pistol(this));
			
			selectedGun = 0;
			mainGun = arsenal.get(selectedGun);
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
	
	protected void loadSounds() {
		thrusterSound = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/thrust.ogg"));
		boostSound = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/turbo.ogg"));
		deathSound = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/death.wav"));
	}
	
	@Override
	public void refilEnergy(float delta) {
		if (energyLevel < 100) {
			energyLevel += (delta * BASE_ENERGY_REGENERATION);
			if (energyLevel > 100) {
				energyLevel = 100;
			}
		}

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
		pp.parseEntity(NAME, body, controller.getCollisionBits());
		
        body.setLinearDamping(0.6f);
		body.setAngularDamping(0.9f);
		body.setTransform(body.getPosition(), (float)Math.toRadians(180));
	}

	@Override
	protected void createLight() {
		mainLight = new PointLight(manager.getLightEngine(), 128, new Color(1f, 1f, 1f, 0.5f), 7f, 0f, 0f);
		mainLight.setMaskBits(~body.getFixtureList().get(0).getFilterData().categoryBits);
		mainLight.attachToBody(body, 0f, 0f);
		mainLight.setActive(false);
		
		headLight = new ConeLight(manager.getLightEngine(), 128, new Color(Color.ORANGE), 30f, 0f, 0f, 0f, 20f);
		headLight.attachToBody(body, 0, 0);
		headLight.setMaskBits(~body.getFixtureList().get(0).getFilterData().categoryBits);
		headLight.setActive(false);
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
	
	protected float getBoostFactor() {
		return 800;
	}

	@Override
	protected void playDeathSound() {

		long dsi = deathSound.play(1.5f);
		deathSound.setPitch(dsi, 0.7f);
	}
	
	@Override
	protected void playThrusterSound() {
		if (thrusterSoundId > -1) {
			return;
		}
		thrusterSoundId = thrusterSound.loop(0.3f);
		thrusterSound.setPitch(thrusterSoundId, 1.0f);
	}

	@Override
	protected void stopThrusterSound() {
		if (thrusterSoundId == -1) {
			return;
		}
		
		thrusterSound.stop(thrusterSoundId);
		thrusterSoundId = -1;
	}

	@Override
	protected void playRevThrusterSound() {
		if (revThrusterSoundId > -1) {
			return;
		}
		revThrusterSoundId = thrusterSound.loop(0.06f);
		thrusterSound.setPitch(revThrusterSoundId, 4.0f);
	}

	@Override
	protected void stopRevThrusterSound() {
		if (revThrusterSoundId == -1) {
			return;
		}
		
		thrusterSound.stop(revThrusterSoundId);
		revThrusterSoundId = -1;
	}
	
	@Override
	protected void playBoostSound() {
		if (boostSoundId > -1) {
			return;
		}
		boostSoundId = boostSound.play(0.1f);
		thrusterSound.setPitch(revThrusterSoundId, 2.0f);
	}

	@Override
	protected void stopBoostSound() {
		if (boostSoundId == -1) {
			return;
		}
		
		boostSound.stop(boostSoundId);
		boostSoundId = -1;
	}

	@Override
	public void changeMainWeapon() {
		selectedGun++;
		if (selectedGun > 2) {
			selectedGun = 0;
		}
		
		mainGun = arsenal.get(selectedGun);
		
	}

	@Override
	public void toggleLights() {
		if (!isAlive) {return;}
		if (mainLight != null) {
			mainLight.setActive(!mainLight.isActive());
		}
		
		if (headLight != null) {
			headLight.setActive(!headLight.isActive());
		}
	}
	
	public void setLightColor(Color color) {
		if (!isAlive) {return;}
		if (color == null) {
			color = DEFAULT_HEADLIGHT_COLOR;
		}
		
		if (headLight != null) {
			headLight.setColor(color);
		}
	}

	@Override
	protected float getBoostEnergyUsage() {
		return 0.5f;		
	}

	@Override
	protected void disposeGuns() {

		Iterator<Gun> itr = arsenal.iterator(); 
		while(itr.hasNext()) {
			Gun gun = itr.next(); 
			gun.dispose();
		} 
		
		arsenal.clear();
	}
}
