package com.zero.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.zero.ammunition.Ammunition;
import com.zero.ammunition.Beam;
import com.zero.main.Manager;
import com.zero.ships.Ship;

public class RaptorLaser extends Gun {

	public RaptorLaser(Ship owner) {
		super(owner);
	}

	@Override
	public float getEnergyUse() {
		return 1f;
	}

	@Override
	public float getDamage() {
		return 0f;
	}

	@Override
	public Class<? extends Ammunition> getAmmunitionClass() {
		return Beam.class;
	}

	@Override
	public Vector2 getNozzlePosition(float bulletHeight) {
		Vector2 position = new Vector2();
		position.x = 0f;
		position.y = - owner.getSize().y / Manager.PTM / 2 - bulletHeight / Manager.PTM;
		position = owner.getBody().getWorldPoint(position);
		
		return position;
	}

	@Override
	public float getNozzleAngle() {
		return owner.getBody().getAngle();
	}

	@Override
	public void loadSound() {
		shotSound = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/laser.ogg"));
	}

	@Override
	public void playSound() {
		long id = shotSound.play(0.3f);
		shotSound.setPitch(id, 4f);		
	}

	@Override
	public float getTimeToReload() {
		// TODO Auto-generated method stub
		return 0.2f;
	}
}
