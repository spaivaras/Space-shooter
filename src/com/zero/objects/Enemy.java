package com.zero.objects;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.zero.interfaces.Manageable;
import com.zero.interfaces.ShipController;
import com.zero.interfaces.WorldObject;
import com.zero.main.WorldObjectPool;
import com.zero.ships.LightFighter;
import com.zero.ships.Ship;
import com.zero.spaceshooter.actors.ManagerActor;

public class Enemy implements ShipController, Manageable {
	private static final float SHOT_DELAY = 0.8f;
	
	private Ship ship = null;
	private WorldObject target;
	private float shotDelay = 0;
	
	private float shipStartX = 0;
	private float shipStartY = 10;
	
	public Enemy(float x, float y) {
		shipStartX = x;
		shipStartY = y;
		prepareShip();
	}
	public Enemy() {
		prepareShip();
	}
	
	private void prepareShip() {
		
		ship = (Ship)WorldObjectPool.getInstance().reuse("Light-fighter");
		if (ship == null) {
			ship = new LightFighter(this, shipStartX, shipStartY);
			ManagerActor.getInstance().addEntityNext(ship);
		} else {
			ship.getBody().setTransform(shipStartX, shipStartY, ship.getBody().getAngle());
			ship.setShipController(this);
		}	
	}
	
	public void update(float delta) {
		
		if (shotDelay > 0) {
			shotDelay -= delta;	
		} else if (shotDelay <= 0) {
			shotDelay = 0;
		}
		
		float distance = ship.getDistanceToTarget();
		if (distance <= 15 && distance > 0 && this.target != null && shotDelay == 0) {
			ship.shootMainGun();
			shotDelay = SHOT_DELAY;
		}
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

	@Override
	public void shipWasDestroyed(Vector2 position) {
		// TODO Auto-generated method stub
		System.out.println(position);
		
		float randomX = this.randomBetwean(position.x - 100, position.x - 50);
		float randomY = this.randomBetwean(position.y - 100, position.y - 50);
		this.spawnAtacker(randomX, randomY);
	}
	
	private void spawnAtacker(float x, float y) {
		this.ship = new LightFighter(this, x, y);
		ManagerActor.getInstance().addEntityNext(ship);
		ship.setTarget(target);
		ship.followTarget(true);
		
		ship.setLightColor(Color.RED);
		ship.toggleLights();
	}
	
	private float randomBetwean(float min, float max) {
		Random rnd = new Random();
		float value = Math.min(min, max) + rnd.nextInt((int)Math.abs(min - (-max)));
		return value;
	}

	@Override
	public boolean dispose() {
		WorldObjectPool.getInstance().cache(ship);
		return true;
	}
}


