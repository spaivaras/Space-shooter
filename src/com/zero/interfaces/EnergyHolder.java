package com.zero.interfaces;

public interface EnergyHolder {
	public float getEnergyLevel();
	public boolean drawEnergy(float amount);
	public void refilEnergy(float delta);
}
