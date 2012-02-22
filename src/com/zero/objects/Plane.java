package com.zero.objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Plane extends Entity 
{	
	public static final int SHOT_DELAY = 150;
	public static final float ROTATE_SPEED_FACTOR = 0.3f;
	public static final float FLY_SPEED_FACTOR = 0.4f;
	
	private Boolean shotDelayOn = false;
	int shotCounter = 0;
	
	public Plane(String ref) 
			throws SlickException {
		super(ref);
	}

	@Override
	public void draw() {
		this.draw(x, y);
	}

	@Override
	public void update(GameContainer container, int delta) {
		Input input = container.getInput();
		
		if (shotDelayOn && shotCounter < SHOT_DELAY) {
			shotCounter += delta;
		} else if(shotDelayOn && shotCounter >= SHOT_DELAY) {
			shotDelayOn = false;
			shotCounter = 0;
		}
		
		if (input.isKeyDown(Input.KEY_A)) {
			this.rotate(-ROTATE_SPEED_FACTOR * delta);
		}
		if (input.isKeyDown(Input.KEY_D)) {
			this.rotate(ROTATE_SPEED_FACTOR * delta);
		}
		if (input.isKeyDown(Input.KEY_W)) {
			float hip = FLY_SPEED_FACTOR * delta;
			float rotation = this.getRotation();
			
			x += (float)(hip * Math.sin(Math.toRadians(rotation)));
			y -= (float)(hip * Math.cos(Math.toRadians(rotation)));
		}
		if (input.isKeyDown(Input.KEY_SPACE) && !shotDelayOn) {
			try {
				Bullet bullet = new Bullet("laser.png");
				bullet.shoot(this);
				manager.addEntity(bullet);
				
				shotDelayOn = true;
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

}
