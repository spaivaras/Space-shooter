package com.zero.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.zero.interfaces.ShipController;
import com.zero.main.Manager;
import com.zero.ships.LightFighter;
import com.zero.ships.Ship;

public class Player implements ShipController {
	private Ship ship = null;
	private boolean weaponChanged = false;
	
	public Player() {
		ship = new LightFighter(this, 0, 0);
		Manager.getInstance().addEntityNext(ship);
	}
	
	public void update(float delta) {
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			ship.goForward();
		}
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			ship.boost(true);
		}
		if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || !Gdx.input.isKeyPressed(Keys.W)) {
			ship.boost(false);
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
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			ship.shootMainGun();
		}
		
		if (Gdx.input.isKeyPressed(Keys.E)) {
			if (!weaponChanged) {
				ship.changeMainWeapon();
				weaponChanged = true;
			}
		} else {
			weaponChanged = false;
		}
	}

	@Override
	public Ship getShip() {
		return ship;
	}
}


