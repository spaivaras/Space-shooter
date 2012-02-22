package com.zero.main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.zero.objects.Plane;

public class SpaceShooter extends BasicGame {

	Manager manager;
	
	public SpaceShooter() {
		super("Space shooter v0.0.1");
	}

	@Override
	public void render(GameContainer container, Graphics g) 
			throws SlickException {
		manager.render(container, g);
	}

	@Override
	public void init(GameContainer container) 
			throws SlickException {
		manager = Manager.getInstance();
		
		Plane plane = new Plane("plane.png");
		plane.setX(400f);
		plane.setY(300f);
		manager.addEntity(plane);
	}

	@Override
	public void update(GameContainer container, int delta) 
			throws SlickException {
		manager.update(container, delta);
	}

	/**
	 * @param args
	 * @throws SlickException 
	 */
	public static void main(String[] args) 
			throws SlickException {
		AppGameContainer app = new AppGameContainer(new SpaceShooter());
		
		//change last parameter to true for full screen
		//VSYNC will lock frame rate to monitor refresh rate if possible
		app.setDisplayMode(800, 600, false);
		//app.setVSync(true);
		
		app.start();
	}
}
