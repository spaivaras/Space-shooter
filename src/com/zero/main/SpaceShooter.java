package com.zero.main;

import lessur.engine.physics.Slick2dDebugDraw;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.zero.objects.Plane;

public class SpaceShooter extends BasicGame {

	Manager manager;
	World world = null;
	Slick2dDebugDraw sDD = null;
	Plane plane;
	
	float FIXED_TIMESTEP = 1.0f / 1000.0f;
	// Minimum remaining time to avoid box2d unstability caused by very small delta times
	// if remaining time to simulate is smaller than this, the rest of time will be added to the last step,
	// instead of performing one more single step with only the small delta time.
	float MINIMUM_TIMESTEP = FIXED_TIMESTEP / 2;
	int VELOCITY_ITERATIONS = 1;
	int POSITION_ITERATIONS = 1;
	// maximum number of steps per tick to avoid spiral of death
	int MAXIMUM_NUMBER_OF_STEPS = 100;
	
	public SpaceShooter() {
		super("Space shooter v0.0.1");
	}

	@Override
	public void render(GameContainer container, Graphics g) 
			throws SlickException {
		//draw out physics debug data, like shapes
		world.drawDebugData();
		
		//draw our sprites
		manager.render(container, g);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		
		sDD = new Slick2dDebugDraw(container.getGraphics(), container); // arg0 is the GameContainer in this case, I put this code in my init method
		sDD.setFlags(0x0001); //Setting the debug draw flags,
		
		Vec2 gravity = new Vec2(0.0f, 0.00f);
		world = new World(gravity, true);
		world.setDebugDraw(sDD);
		
		manager = Manager.getInstance();
		manager.setWorld(world);
		manager.setContainer(container);
		
		plane = new Plane("res/plane.png", 400f, 300f);
		manager.addEntity(plane);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		float frameTime = delta;
		int stepsPerformed = 0;
		while ( (frameTime > 0.0) && (stepsPerformed < MAXIMUM_NUMBER_OF_STEPS) ){
			float deltaTime = Math.min( frameTime, FIXED_TIMESTEP );
			frameTime -= deltaTime;
			if (frameTime < MINIMUM_TIMESTEP) {
				deltaTime += frameTime;
				frameTime = 0.0f;
			}
			world.step(deltaTime,VELOCITY_ITERATIONS,POSITION_ITERATIONS);
			stepsPerformed++;
			//afterStep(); // process collisions and result from callbacks called by the step
	    }
		world.clearForces();
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
		app.setDisplayMode(800, 600, false);
		
		//Lock to custom frame rate
		app.setTargetFrameRate(200);
		
		//VSYNC will lock frame rate to monitor refresh rate if possible
		//app.setVSync(true);
		
		app.start();
	}
}
