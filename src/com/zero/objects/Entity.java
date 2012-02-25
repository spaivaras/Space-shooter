package com.zero.objects;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.zero.main.Manager;

public abstract class Entity extends Image {
	
	protected Float x = 0f;
	protected Float y = 0f;
	protected Manager manager;
	protected Body body = null;
	protected BodyDef bodyDef;
	protected Shape shape;
	protected FixtureDef fixtureDef;
	protected float angleDifference = 0;
	
	
	public Entity(String ref, Float x, Float y) 
			throws SlickException {
		super(ref);
		manager = Manager.getInstance();
		this.x = x;
		this.y = y;
		createPhysicsBody();
	}
	
	public abstract void createPhysicsBody();
	public abstract void updatePosition(GameContainer container, int delta);
	public abstract Boolean collision(Entity with);
	public abstract void hit();
	
	public void update(GameContainer container, int delta) {
		//Update sprite position from physics body position in the world
		if (body != null) {
			Vec2 bodyPosition = manager.translateCoordsToScreen(body.getPosition(), (float)(getWidth() / 2), (float)(getHeight() / 2));
			x = bodyPosition.x;
			y = bodyPosition.y;	
			setRotation(-(float)Math.toDegrees(body.getAngle()) - angleDifference);
		}
		
		updatePosition(container, delta);
	}
	
	public void draw() {
		super.draw(x, y);
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
	
}
