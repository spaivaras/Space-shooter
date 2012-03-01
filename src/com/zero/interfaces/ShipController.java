package com.zero.interfaces;

import com.zero.ships.Ship;

public interface ShipController {
	public Ship getShip();
	public short getCollisionBits();
	public void shipWasHit(WorldObject who);
}
