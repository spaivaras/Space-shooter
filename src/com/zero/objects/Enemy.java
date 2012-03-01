package com.zero.objects;

import com.badlogic.gdx.graphics.Color;
import com.zero.interfaces.ShipController;
import com.zero.interfaces.WorldObject;
import com.zero.main.Manager;
import com.zero.ships.LightFighter;
import com.zero.ships.Ship;

public class Enemy implements ShipController {
	private Ship ship = null;
	private WorldObject target;
	
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

	@Override
	public short getCollisionBits() {
		return 0x0002;
	}
	
	public void setTarget(WorldObject target) {
		this.target = target;
		
		ship.setTarget(target);
		ship.followTarget(true);
		
		ship.setLightColor(Color.RED);
		ship.toggleLights();
	}

	@Override
	public void shipWasHit(WorldObject who) {
		if (target == null) {
			this.setTarget(who);
		}
	}
	
}


