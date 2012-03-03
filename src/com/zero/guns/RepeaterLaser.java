package com.zero.guns;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.zero.ammunition.Ammunition;
import com.zero.ammunition.Beam;
import com.zero.main.Manager;
import com.zero.ships.Ship;

public class RepeaterLaser extends Gun {

	public RepeaterLaser(Ship owner) {
		super(owner);
	}

	@Override
	public float getEnergyUse() {
		return 0.5f;
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
		Random rnd = new Random();
		int modifier = Math.min(-40, 40) + rnd.nextInt(Math.abs(40 - (-40)));
		
		return owner.getBody().getAngle() + (float)Math.toRadians(modifier);
	}

	@Override
	public void loadSound() {
		shotSound = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/laser.ogg"));
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
