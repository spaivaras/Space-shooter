package com.zero.ammunition;

import box2dLight.Light;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.zero.guns.Gun;
import com.zero.interfaces.WorldObject;
import com.zero.main.Manager;

public abstract class Ammunition implements WorldObject {
	
	public static short COLLISION_BITS = 0x0008; 
	
	protected Manager manager;
	protected Sprite sprite;
	protected float spriteX;
	protected float spriteY;
	protected Body body;
	protected boolean shouldDraw = false;
	protected Gun gun;
	protected Light light;
	
	private float totalAliveTime = 0;
	
	
	public Ammunition() {
		this.manager = Manager.getInstance();
		this.createSprite();
	}
	
	public Ammunition(Gun gun) {
		this();
		this.gun = gun;
	}
	
	protected abstract String getTextureName();
	protected abstract void createPhysicsBody();
	protected abstract void updateInternal(float delta);
	protected abstract float getSpeed();
	protected abstract float getLifeTime();
	protected abstract void createLight();
	
	protected Vector2 getThrustVector() {
		double rads = body.getAngle() + Math.toRadians(90);
		
		//x + d * cos(a)  y + d.sin(a)
		double x = this.getSpeed() * Math.cos(rads);
		double y = this.getSpeed() * Math.sin(rads);
		
		Vector2 vector = new Vector2((float)x, (float)y).mul(-1);
		return vector;
	}
	
	protected void createSprite() {
		TextureAtlas atlas =  manager.getTextureAtlas("ammunition");
		sprite = atlas.createSprite(this.getTextureName());
	}
	
	public void update(float delta) {
		if (body == null && !manager.getWorld().isLocked()) {
			createPhysicsBody();
			if (body != null) {
				body.setUserData(this);
				this.createLight();
			}
		}
		
		updateInternal(delta);
		body.applyLinearImpulse(getThrustVector(), body.getWorldCenter());
		
		//Update sprite position from physics body position in the world
		if (body != null && sprite != null) {
			Vector2 position = body.getPosition();
			spriteX = position.x;
			spriteY = position.y;
			sprite.setRotation((float)Math.toDegrees(body.getAngle()));
		}
		
		if (!shouldDraw) {
			shouldDraw = true;
		}
		
		totalAliveTime += delta;
		
		if (totalAliveTime >= this.getLifeTime()) {
			manager.removeEntityNex(this);
		}
	}
	
	public void draw() {
		if (shouldDraw && sprite != null) {
			Vector2 screen = manager.translateCoordsToScreen(new Vector2(spriteX, spriteY), 
					(float)this.sprite.getWidth() / 2, 
					(float)this.sprite.getHeight() / 2);
			
			sprite.setPosition(screen.x, screen.y);
			sprite.setScale(1f / (float)Manager.PTM);
			sprite.draw(manager.getBatch());
		}
	}
	
	public boolean dispose() {
		if (body != null && !manager.getWorld().isLocked()) { 
			manager.getWorld().destroyBody(body);
			if (light != null) {
				light.remove();
			}
			return true;
		}
		return false;
	}

	public Gun getGun() {
		return gun;
	}

	public void setGun(Gun gun) {
		this.gun = gun;
	}
	
	public boolean collision(WorldObject with) {
		if (with.equals(gun.getOwner())) {
			return false;
		}
		
		with.firedAt(this);
		return true;
	}
	
	public void firedAt(Ammunition bullet) {}
}
