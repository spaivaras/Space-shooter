package com.zero.objects;

import com.zero.interfaces.ShipController;
import com.zero.main.Manager;
import com.zero.ships.LightFighter;
import com.zero.ships.Ship;

public class Enemy implements ShipController {
	private Ship ship = null;
	private boolean weaponChanged = false;
	
	public Enemy() {
		ship = new LightFighter(this, 10, 0);
		Manager.getInstance().addEntityNext(ship);
	}
	
	public void update(float delta) {
		
	}

	@Override
	public Ship getShip() {
		return ship;
	}
}


