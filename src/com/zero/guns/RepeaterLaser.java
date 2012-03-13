package com.zero.guns;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.zero.ammunition.Ammunition;
import com.zero.ammunition.Beam;
import com.zero.main.ResourceCache;
import com.zero.ships.Ship;
import com.zero.spaceshooter.actors.ManagerActor;

public class RepeaterLaser extends Gun {

	public RepeaterLaser(Ship owner) {
		super(owner);
	}

	@Override
	public float getEnergyUse() {
		return 0.5f;
	}

	@Override
	public float getDamageModifier() {
		return 0.1f;
	}

	@Override
	public Class<? extends Ammunition> getAmmunitionClass() {
		return Beam.class;
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
		Random rnd = new Random();
		int modifier = Math.min(-5, 5) + rnd.nextInt(Math.abs(5 - (-5)));

		return owner.getBody().getAngle() + (float)Math.toRadians(modifier);
	}

	@Override
	public void loadSound() {
		shotSound = ResourceCache.getInstance().getSound("laser-sound");
	}

	@Override
	public void playSound() {
		long id = shotSound.play(0.2f);
		shotSound.setPitch(id, 6f);		
	}

	@Override
	public float getTimeToReload() {
		// TODO Auto-generated method stub
		return 0.1f;
	}
}
