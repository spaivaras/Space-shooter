package com.zero.guns;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.zero.ammunition.Ammunition;
import com.zero.main.Manager;
import com.zero.ships.Ship;
import com.zero.spaceshooter.actors.ManagerActor;


public abstract class Gun implements Disposable {
	
	protected Ship owner;
	protected Sound shotSound;
	protected boolean reloading;
	protected float timeReloading;

	public Gun(Ship owner) {
		this.owner = owner;
		this.loadSound();
	}
	
	public abstract float getEnergyUse();
	public abstract float getDamage();
	public abstract Vector2 getNozzlePosition(float bulletHeight);
	public abstract float getNozzleAngle();
	public abstract void loadSound();
	public abstract void playSound();
	public abstract float getTimeToReload();

	public abstract Class<? extends Ammunition> getAmmunitionClass();
	
	public void shoot() {
		if (reloading) {
			return;
		}
		
		if (!owner.drawEnergy(getEnergyUse())) {
			return;
		}
		
		Class<? extends Ammunition> bulletClass = getAmmunitionClass();
		
		try {
			Ammunition bullet = bulletClass.newInstance();
			bullet.setGun(this);
			ManagerActor.getInstance().addEntityNext(bullet);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		playSound();
		reloading = true;
	}
	
	public Ship getOwner() {
		return owner;
	}
	
	public void dispose() {
		if (shotSound != null) {
			shotSound.dispose();
		}
	} 
	
	public void update(float delta) {
		if (reloading && timeReloading < this.getTimeToReload()) {
			timeReloading += delta;
		} else if(reloading && timeReloading >= this.getTimeToReload()) {
			reloading = false;
			timeReloading = 0;
		}
	}
}
