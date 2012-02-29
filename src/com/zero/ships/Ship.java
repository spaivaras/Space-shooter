package com.zero.ships;

import box2dLight.Light;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.zero.ammunition.Ammunition;
import com.zero.interfaces.ShipController;
import com.zero.interfaces.WorldObject;
import com.zero.main.Manager;
import com.zero.objects.EnergyHolder;

public abstract class Ship implements WorldObject, EnergyHolder {

	protected float energyLevel = 0;
	protected Sprite sprite = null;
	protected Manager manager = null;
	protected Body body = null;
	protected float homeX = 0f, homeY = 0f;
	protected Light mainLight = null;
	protected ShipController controller = null;
	
	protected boolean thrustersActive = false;
	protected boolean revThrustersActive = false;
	protected boolean leftRotationActive = false;
	protected boolean rightRotationActive = false;

	@Override
	public abstract void refilEnergy(float amount, float delta);
	protected abstract Sprite getCustomSprite();
	protected abstract void createPhysicsBody();
	protected abstract void createLight();
	protected abstract void updateInternal(float delta);
	protected abstract void removeCustomLights();
	protected abstract float getThrustersFactor();
	protected abstract float getRevThrustersFactor();
	protected abstract float getRotationFactor();
	protected abstract float getMaxLinearSpeed();
	

	public Ship() {
		manager = Manager.getInstance();
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

		drawSprite.setScale(1f / (float)Manager.PTM );
		drawSprite.draw(manager.getBatch());
	}

	@Override
	public void update(float delta) {
		if (body == null && !manager.getWorld().isLocked()) {
			createPhysicsBody();
			if (body != null) {
				body.setUserData(this);
				this.createLight();
			}
		}

		updateInternal(delta);
		if (thrustersActive) {
			body.applyLinearImpulse(getThrustVector(false), body.getWorldCenter());
		}
		
		if (revThrustersActive) {
			body.applyLinearImpulse(getThrustVector(true), body.getWorldCenter());
		}
		
		if (leftRotationActive) {
			body.applyAngularImpulse(this.getRotationFactor());
		}
		
		if (rightRotationActive) {
			body.applyAngularImpulse(-this.getRotationFactor());
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
			
//			if (boost) {
//				factor += BOOST_FACTOR;
//			}
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

	@Override
	public boolean dispose() {
		if (body != null && !manager.getWorld().isLocked()) { 
			manager.getWorld().destroyBody(body);
			if (mainLight != null) {
				mainLight.remove();
			}
			this.removeCustomLights();
			return true;
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
		// TODO Auto-generated method stub

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

}
