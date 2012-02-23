package com.zero.objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Plane extends Entity 
{	
	public static final int SHOT_DELAY = 150;
	public static final float ROTATE_SPEED_FACTOR = 0.3f;
	public static final float FLY_SPEED_FACTOR = 0.4f;
	
        public static final float THRUSTER_MAX = 1f;
        public static final float THRUSTER_FACTOR = 0.1f;
        public static final int THRUSTER_DELAY = 100;
        
	private Boolean shotDelayOn = false;
        private Boolean thrustDelayOn = false;
	int shotCounter = 0;
        int thrustCounter = 0;
        float thruster = 0f;
	
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
                
                if (thrustDelayOn && thrustCounter < THRUSTER_DELAY) {
			thrustCounter += delta;
		} else if(thrustDelayOn && thrustCounter >= THRUSTER_DELAY) {
			thrustDelayOn = false;
			thrustCounter = 0;
		}
                
                
		if (input.isKeyDown(Input.KEY_A)) {
			this.rotate(-ROTATE_SPEED_FACTOR * delta);
		}
		if (input.isKeyDown(Input.KEY_D)) {
			this.rotate(ROTATE_SPEED_FACTOR * delta);
		}
		if (input.isKeyDown(Input.KEY_W) && !thrustDelayOn) {
                    thrustDelayOn = true;
                    if(thruster < THRUSTER_MAX) {
                        thruster += THRUSTER_FACTOR;
                    }
                      
		}
                
                if(input.isKeyDown(Input.KEY_S) && !thrustDelayOn) {
                    thrustDelayOn = true;
                    if(thrustDelayOn && thruster > 0f) {
                        thruster -= THRUSTER_FACTOR;
                        if(thruster < 0f) {
                            thruster = 0;
                        }
                    }
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
                this.fly(delta);

	}
        
        public void fly(int delta) {
            float hip = thruster * FLY_SPEED_FACTOR * delta;
            float rotation = this.getRotation();

            x += (float)(hip * Math.sin(Math.toRadians(rotation)));
            y -= (float)(hip * Math.cos(Math.toRadians(rotation)));
        }

}
