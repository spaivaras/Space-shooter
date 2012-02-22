package com.zero.objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.zero.main.Manager;

public abstract class Entity extends Image {
	
	protected Float x = 0f;
	protected Float y = 0f;
	protected Manager manager;
	
	public Entity(String ref) 
			throws SlickException {
		super(ref);
		manager = Manager.getInstance();
	}
	
	public abstract void draw();
	public abstract void update(GameContainer container, int delta);
	
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
