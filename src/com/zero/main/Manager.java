package com.zero.main;

import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.zero.objects.Entity;

public class Manager {

	private static Manager manager;
	private CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
	
	public void render(GameContainer container, Graphics g) {
		for (Entity entity : entities) {
			entity.draw();
		}
	}

	public void update(GameContainer container, int delta) {
		for (Entity entity : entities) {
			entity.update(container, delta);
		}
	}
	
	public void addEntity(Entity entity) {
			entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
		System.out.println("Remove entity: " + entity.toString());
//		try {
//			entity.destroy();
//		} catch (SlickException e) {
//			e.printStackTrace();
//		}
	}
	
	public static Manager getInstance() {
		if (manager == null) {
			manager = new Manager();
		}
		return manager;
	}

}
