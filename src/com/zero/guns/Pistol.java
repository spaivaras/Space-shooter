package com.zero.guns;

import com.badlogic.gdx.math.Vector2;
import com.zero.ammunition.Ammunition;
import com.zero.ammunition.MetalRound;
import com.zero.main.ResourceCache;
import com.zero.ships.Ship;
import com.zero.spaceshooter.actors.ManagerActor;

public class Pistol extends Gun {

	public Pistol(Ship owner) {
		super(owner);
	}

	@Override
	public float getEnergyUse() {
		return 5f;
	}

	@Override
	public float getDamageModifier() {
		return 1f;
	}

	@Override
	public Class<? extends Ammunition> getAmmunitionClass() {
		return MetalRound.class;
	}

	@Override
	public Vector2 getNozzlePosition(float bulletHeight) {
		Vector2 position = new Vector2();
		position.x = 0f;
		position.y = - owner.getSize().y / ManagerActor.PTM / 2 - bulletHeight / ManagerActor.PTM;
		position = owner.getBody().getWorldPoint(position);
		
		return position;
	}

	@Override
	public float getNozzleAngle() {
		return owner.getBody().getAngle();
	}

	@Override
	public void loadSound() {
		shotSound = ResourceCache.getInstance().getSound("round-shot");
	}

	@Override
	public void playSound() {
		long id = shotSound.play(0.3f);
		shotSound.setPitch(id, 2f);		
	}

	@Override
	public float getTimeToReload() {
		// TODO Auto-generated method stub
		return 0.4f;
	}
}
