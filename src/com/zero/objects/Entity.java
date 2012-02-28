package com.zero.objects;

import box2dLight.Light;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.zero.ammunition.Ammunition;
import com.zero.main.Manager;

public abstract class Entity implements WorldObject {
	
	public static final float FULL_REVOLUTION_RADS = (float)Math.PI * 2;
	
	protected String name;
	protected Float x = 0f;
	protected Float y = 0f;
	protected Manager manager;
	protected TextureAtlas atlas;
	
	protected Sprite sprite;
	protected Body body = null;
	protected BodyDef bodyDef;
	
	protected float angleDifference = 0;
	protected Boolean shouldDraw = false;
	protected Light glowLight;
	
	public Entity(TextureAtlas atlas, String name)
	{
		this.name = name;
		this.atlas = atlas;
		
		manager = Manager.getInstance();
		sprite = atlas.createSprite(name);
	}
	
	public Entity(TextureAtlas atlas, String name, Float x, Float y) {
		this.name = name;
		this.atlas = atlas;
		
		manager = Manager.getInstance();
		this.x = x;
		this.y = y;
		
		sprite = atlas.createSprite(name);
	}
	
	public abstract void createPhysicsBody();
	public abstract void updatePosition(float delta);
	public abstract void hit();
	protected abstract void createLights();
	protected abstract void removeCustomLights();
	
	public void deactivate() {
	}
	
	public boolean collision(WorldObject with) {
		return false;
	}
	
	public void firedAt(Ammunition bullet) {
        hit();
    }
	
	public void update(float delta) {
		if (body == null && !manager.getWorld().isLocked()) {
			createPhysicsBody();
			if (body != null) {
				body.setUserData(this);
				createLights();
			}
		}
		
		//Update sprite position from physics body position in the world
		if (body != null) {
			Vector2 position = body.getPosition();
			x = position.x;
			y = position.y;
			
			if (body.getAngle() >= FULL_REVOLUTION_RADS) {
				body.setTransform(body.getPosition(), body.getAngle() - FULL_REVOLUTION_RADS);
			} else if (body.getAngle() <= -FULL_REVOLUTION_RADS) {
				body.setTransform(body.getPosition(), body.getAngle() + FULL_REVOLUTION_RADS);
			}
			
			sprite.setRotation((float)Math.toDegrees(  body.getAngle()  ) + angleDifference);
		}
		
		updatePosition(delta);
		
		if (!shouldDraw) {
			shouldDraw = true;
		}
	}
	
	public void draw() {
		if (shouldDraw && sprite != null) {
			Vector2 screen = manager.translateCoordsToScreen(new Vector2(x, y), 
					(float)this.sprite.getWidth() / 2, 
					(float)this.sprite.getHeight() / 2);
			
			sprite.setPosition(screen.x, screen.y);
			sprite.setScale(1f / (float)Manager.PTM );
			sprite.draw(manager.getBatch());
		}
	}
	
	public boolean dispose() {
		if (body != null && !manager.getWorld().isLocked()) { 
			manager.getWorld().destroyBody(body);
			glowLight.remove();
			removeCustomLights();
			return true;
		}
		return false;
	}
	
	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	public Body getBody() {
		return body;
	}
	
	public Float getWidth() {
		return sprite.getWidth();
	}
	
	public Float getHeight() {
		return sprite.getHeight();
	}
	
	public void removeLights() {
		if (glowLight != null) {
			glowLight.remove();
		}
		this.removeCustomLights();
	}
	
}
