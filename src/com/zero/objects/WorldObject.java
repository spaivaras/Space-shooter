package com.zero.objects;

import com.zero.ammunition.Ammunition;

public interface WorldObject {
	public void draw();
	public void update(float delta);
	public boolean dispose();
	public boolean collision(WorldObject with);
	public void firedAt(Ammunition bullet);
}
