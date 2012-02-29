package com.zero.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.zero.interfaces.ShipController;
import com.zero.main.Manager;
import com.zero.ships.LightFighter;
import com.zero.ships.Ship;

public class Player implements ShipController {
	private Ship ship = null;
	
	public Player() {
		ship = new LightFighter(this, 0, 0);
		Manager.getInstance().addEntityNext(ship);
	}
	
	public void update(float delta) {
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			ship.goForward();
			//body.applyLinearImpulse(getThrustVector(false), body.getWorldCenter());
			//stateTime += delta;
			//if (thrusterSound == null) {
			//	thrusterSound = manager.playSound("thruster", 1f, 0.3f, true);
			//}
			//if (boost && turboSound == null) {
			//	turboSound = manager.playSound("turbo", 1f, 0.2f, false);
			//}
			
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			ship.goBackwards();
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			ship.rotateLeft();
		}
		
		if(Gdx.input.isKeyPressed(Keys.D)) {
			ship.rotateRight();
		}
		
		
		
		
		
		
		
		
		if (Gdx.input.isKeyPressed(Keys.E)) {
//			gunSelected += 1;
//			if( gunSelected > 2) {
//				gunSelected = 0;
//			}
//			gunSwitched = true;
		} else if(!Gdx.input.isKeyPressed(Keys.E)) {
		//	gunSwitched = false;
		}
		
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
		//	this.boost = true;
		}
		if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || !Gdx.input.isKeyPressed(Keys.W)) {
//			if (turboSound != null) {
//				turboSound.stop();
//				turboSound = null;
//			}
//			boost = false;
		}
		
		
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			//guns.get(gunSelected).shoot();
		}
		
	}
}


