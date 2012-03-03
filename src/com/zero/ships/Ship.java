package com.zero.ships;

import box2dLight.Light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.zero.ammunition.Ammunition;
import com.zero.guns.Gun;
import com.zero.interfaces.EmmiterController;
import com.zero.interfaces.EnergyHolder;
import com.zero.interfaces.ShipController;
import com.zero.interfaces.WorldObject;
import com.zero.main.Manager;

public abstract class Ship implements WorldObject, EnergyHolder, EmmiterController {

	public static final float FULL_REVOLUTION_RADS = (float)Math.PI * 2;
	public static final float APPROACH_DISTANCE = 10;

	protected float energyLevel = 0;
	protected float lifeLevel = 100f;
	protected Sprite sprite = null;
	protected Manager manager = null;
	protected Body body = null;
	protected float homeX = 0f, homeY = 0f;
	protected Light mainLight = null;
	protected ShipController controller = null;
	protected ParticleEffect hitEffect = null;
	protected ParticleEffect explosionEffect = null;
	protected boolean isAlive = true;
	private boolean isHit = false;
	private boolean isExploding = false;
	private boolean dummyExplosion = false;
	

	protected boolean thrustersActive = false;
	protected boolean revThrustersActive = false;
	protected boolean leftRotationActive = false;
	protected boolean rightRotationActive = false;
	protected boolean boostOn = false;

	protected Sound thrusterSound = null;
	protected Sound boostSound = null;
	protected Sound deathSound = null;
	protected long thrusterSoundId = -1;
	protected long revThrusterSoundId = -1;
	protected long boostSoundId = -1;

	protected Gun mainGun = null;

	protected WorldObject target = null;
	protected boolean shouldFollowTarget = false;

	protected abstract Sprite getCustomSprite();
	protected abstract void createPhysicsBody();
	protected abstract void createLight();
	protected abstract void updateInternal(float delta);
	protected abstract void removeCustomLights();

	public abstract void changeMainWeapon();

	protected abstract float getThrustersFactor();
	protected abstract float getRevThrustersFactor();
	protected abstract float getRotationFactor();
	protected abstract float getMaxLinearSpeed();
	protected abstract float getBoostFactor();

	public abstract void toggleLights();
	public abstract void setLightColor(Color color);

	protected abstract float getBoostEnergyUsage();

	protected abstract void loadSounds();
	protected abstract void playThrusterSound();
	protected abstract void stopThrusterSound();
	protected abstract void playRevThrusterSound();
	protected abstract void stopRevThrusterSound();
	protected abstract void playBoostSound();
	protected abstract void stopBoostSound();
	protected abstract void playDeathSound();
	
	protected abstract void disposeGuns();

	public Ship() {
		manager = Manager.getInstance();
		this.loadSounds();
		hitEffect = new ParticleEffect();
		hitEffect.load(Gdx.files.internal("res/emmiters/hit.emmiter"), manager.getTextureAtlas("main"));

		explosionEffect = new ParticleEffect();
		explosionEffect.load(Gdx.files.internal("res/emmiters/explosion.emmiter"), manager.getTextureAtlas("main"));
		explosionEffect.start();
		
		dummyExplosion = true;
	}

	public Body getBody() {
		return body;
	}

	public Vector2 getSize() {
		Sprite customSprite;
		if (sprite != null) {
			customSprite = sprite;
		} else {
			customSprite = getCustomSprite();
		}

		if (customSprite == null) {
			return new Vector2(0f, 0f);
		}

		return new Vector2(customSprite.getWidth(), customSprite.getHeight());
	}

	@Override
	public boolean drawEnergy(float amount) {
		if (energyLevel >= amount) {
			energyLevel -= amount;
			return true;
		}
		return false;
	}

	public float getEnergyLevel() {
		return energyLevel;
	}

	@Override
	public void draw() {
		if (isAlive) {
			Sprite drawSprite;
			if (sprite != null) {
				drawSprite = sprite;
			} else {
				drawSprite = this.getCustomSprite();
				if (drawSprite == null) {
					return;
				}
			}

			if (body == null) {
				return;
			}

			Vector2 screen = manager.translateCoordsToScreen(body.getPosition(), 
					drawSprite.getWidth() / 2, 
					drawSprite.getHeight() / 2);

			drawSprite.setPosition(screen.x, screen.y);
			drawSprite.setRotation((float)Math.toDegrees(body.getAngle()) + 180);
			drawSprite.draw(manager.getBatch());
		}
	}
	
	@Override
	public void drawEmmiters() {
		Vector2 screen = manager.translateCoordsToScreen(body.getWorldCenter());

		hitEffect.setPosition(screen.x, screen.y);
		hitEffect.draw(manager.getBatch());

		explosionEffect.setPosition(screen.x, screen.y);
		if (isExploding) {
			explosionEffect.draw(manager.getBatch());
		}
	}

	@Override
	public void update(float delta) {
		if (body == null && !manager.getWorld().isLocked() && isAlive) {
			createPhysicsBody();
			if (body != null) {
				body.setUserData(this);
				this.createLight();
			}
		}
		
		if (isHit) {
			hitEffect.update(delta);
		}
		if (isExploding || dummyExplosion) {
			explosionEffect.update(delta);
		}
		
		if (hitEffect.isComplete()) {
			isHit = false;
		}
		
		if (explosionEffect.isComplete()) {
			isExploding = false;
			dummyExplosion = false;
		}

		if (mainGun != null) {
			mainGun.update(delta);
		}

		if (body.getAngle() >= FULL_REVOLUTION_RADS) {
			body.setTransform(body.getPosition(), body.getAngle() - FULL_REVOLUTION_RADS);
		} else if (body.getAngle() <= -FULL_REVOLUTION_RADS) {
			body.setTransform(body.getPosition(), body.getAngle() + FULL_REVOLUTION_RADS);
		}

		refilEnergy(delta);

		if (shouldFollowTarget && target != null) {
			this.rotateToTarget();
			this.flyToTarget();
		}

		updateInternal(delta);

		if (leftRotationActive) {
			body.applyAngularImpulse(this.getRotationFactor());
		}

		if (rightRotationActive) {
			body.applyAngularImpulse(-this.getRotationFactor());
		}

		if (thrustersActive) {
			body.applyLinearImpulse(getThrustVector(false), body.getWorldCenter());
			this.playThrusterSound();
			if (boostOn) {
				this.playBoostSound();
			}
		} else {
			this.stopThrusterSound();
		}

		if (!boostOn) {
			this.stopBoostSound();
		}

		if (revThrustersActive) {
			body.applyLinearImpulse(getThrustVector(true), body.getWorldCenter());
			this.playRevThrusterSound();
		} else {
			this.stopRevThrusterSound();
		}

		thrustersActive = revThrustersActive = leftRotationActive = rightRotationActive = false;
	}

	private Vector2 getThrustVector(Boolean reverse) {
		double rads = body.getAngle() + Math.toRadians(270);
		double factor;

		if (reverse) {
			factor = this.getRevThrustersFactor();
		} else {
			factor = this.getThrustersFactor();

			if (boostOn) {
				if (this.drawEnergy(this.getBoostEnergyUsage())) {
					factor += this.getBoostFactor();
				} else {
					boostOn = false;
				}
			}
		}

		if (body.getLinearVelocity().len() > this.getMaxLinearSpeed()) {
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

	private void rotateToTarget() {
		Float angleToTarget = (float)Math.atan2(
				(double)(body.getPosition().x - target.getBody().getPosition().x),
				(double)(body.getPosition().y - target.getBody().getPosition().y)
				);

		if (angleToTarget < 0) {
			angleToTarget = FULL_REVOLUTION_RADS + angleToTarget;
		}

		Float reverseAngle;
		if (body.getAngle() < 0) {
			reverseAngle = Math.abs(body.getAngle());
		} else {
			reverseAngle = FULL_REVOLUTION_RADS - Math.abs(body.getAngle());
		}

		if (reverseAngle > angleToTarget && Math.abs(angleToTarget - reverseAngle) <= Math.PI) {
			rotateLeft();
		} else if(reverseAngle > angleToTarget) {
			rotateRight();
		} else if(reverseAngle < angleToTarget && Math.abs(angleToTarget - reverseAngle) <= Math.PI) {
			rotateRight();
		} else if(reverseAngle > angleToTarget) {
			rotateLeft();
		}
	}

	private void flyToTarget() {
		Float distance = body.getWorldCenter().dst(target.getBody().getWorldCenter());
		if (distance > APPROACH_DISTANCE) {
			goForward();
		} else {
			if(shouldFollowTarget) {
				goBackwards();
			}
		}
	}

	@Override
	public boolean dispose() {
		if (mainLight != null && mainLight.isActive()) {
			mainLight.setActive(false);
			removeCustomLights();
		}
		
		thrusterSound.dispose();
		boostSound.dispose();
		disposeGuns();
		
		if (explosionEffect.isComplete()) {
			if (body != null && !manager.getWorld().isLocked()) { 
				manager.getWorld().destroyBody(body);
				mainLight.remove();
				deathSound.dispose();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean collision(WorldObject with) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void firedAt(Ammunition bullet) {
		if (!isAlive) {
			return;
		}
		
		manager.playSound("hit", 2f, 0.2f, false);
		controller.shipWasHit(bullet.getGun().getOwner());
		hitEffect.start();
		isHit = true;
		
		float damage = bullet.getDamage();
		lifeLevel -= damage;
		System.out.println("Life left: " + lifeLevel);

		
		if (lifeLevel <= 0) {
			die();
		}
	}

	public void die() {
		isAlive = false;
		isExploding = true;
		explosionEffect.start();
		manager.removeEntityNex(this);
		playDeathSound();
	}

	public void goForward() {
		thrustersActive = true;
	}

	public void goBackwards() {
		revThrustersActive = true;
	}

	public void rotateLeft() {
		leftRotationActive = true;
	}

	public void rotateRight() {
		rightRotationActive = true;
	}

	public void shootMainGun() {
		if (mainGun != null) {
			mainGun.shoot();
		}
	}

	public void boost(boolean on) {
		boostOn = on;
	}

	public void setTarget(WorldObject target) {
		this.target = target;
	}

	public void followTarget(boolean follow) {
		shouldFollowTarget = follow;
	}
}
