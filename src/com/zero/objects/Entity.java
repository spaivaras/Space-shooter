package com.zero.objects;

import org.jbox2d.collision.shapes.Shape;
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
	protected Body body;
	protected BodyDef bodyDef;
	protected Shape shape;
	protected FixtureDef fixtureDef;
	
	
	public Entity(String ref, Float x, Float y) 
			throws SlickException {
		super(ref);
		manager = Manager.getInstance();
		this.x = x;
		this.y = y;
		createPhysicsBody();
	}
	
	public abstract void draw();
	public abstract void update(GameContainer container, int delta);
	public abstract void createPhysicsBody();
	
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
	
}
