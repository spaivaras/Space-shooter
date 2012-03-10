package com.zero.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.zero.ships.Ship;

public interface ShipController {
	public Ship getShip();
	public short getCollisionBits();
	public void shipWasHit(WorldObject who);
	public void shipWasDestroyed(Vector2 position);
}
