package com.zero.objects;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Bullet extends Entity {
	
	public static final float BULLET_SPEED_FACTOR = 0.9f;
	
	private Boolean moving = false;
	private Boolean visible = false;
	
	private int totalAliveTime = 0;
	
	public Bullet(String ref) throws SlickException {
		super(ref);
	}

	public void shoot(Entity source) {
		x = source.getX() + source.getWidth() / 2 - this.getWidth() / 2;
		y = source.getY() - this.getHeight() + 10;
		
		Float rotationy = this.getHeight() + source.getCenterOfRotationY() - 10;
		this.setCenterOfRotation(this.getCenterOfRotationX(), rotationy);
		
		this.setRotation(source.getRotation());
		
		manager.playSound("laser", 4f, 1f, false);
		
		visible = true;
		moving = true;
	}
	
	public void draw() {
		if (visible) {
			super.draw(x, y);
		}
	}
	
	public void update(GameContainer container, int delta) {
		if (moving) {
			float hip = BULLET_SPEED_FACTOR * delta;
			float rotation = this.getRotation();
			
			x += (float)(hip * Math.sin(Math.toRadians(rotation)));
			y -= (float)(hip * Math.cos(Math.toRadians(rotation)));
			
			totalAliveTime += delta;
			
			if (totalAliveTime >= 500) {
				this.moving = false;
				this.visible = false;
				manager.removeEntity(this);
			}
		}
	}
}
