package com.zero.objects;

public interface EnergyHolder {
	public float getEnergyLevel();
	public boolean drawEnergy(float amount);
	public void refilEnergy(float amount, float delta);
}
