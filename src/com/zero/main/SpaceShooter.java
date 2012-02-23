package com.zero.main;

import lessur.engine.physics.Slick2dDebugDraw;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.zero.objects.Plane;

public class SpaceShooter extends BasicGame {

	Manager manager;
	World m_woWorld = null;
	Slick2dDebugDraw sDD = null;
	Body body;
	Plane plane;
	OBBViewportTransform transform;
	
	float FIXED_TIMESTEP = 1.0f / 1200.0f;
	   // Minimum remaining time to avoid box2d unstability caused by very small delta times
	   // if remaining time to simulate is smaller than this, the rest of time will be added to the last step,
	   // instead of performing one more single step with only the small delta time.
	   //float MINIMUM_TIMESTEP = 1.0f / 600.0f; 
	   float MINIMUM_TIMESTEP = FIXED_TIMESTEP / 2;
	   int VELOCITY_ITERATIONS = 40;
	   int POSITION_ITERATIONS = 40;
	   // maximum number of steps per tick to avoid spiral of death
	   int MAXIMUM_NUMBER_OF_STEPS = 25;
	
	public SpaceShooter() {
		super("Space shooter v0.0.1");
	}

	@Override
	public void render(GameContainer container, Graphics g) 
			throws SlickException {
		m_woWorld.drawDebugData();
		manager.render(container, g);
		
		sDD.drawString(10f, 40f, "testas", Color3f.BLUE);
	}

	@Override
	public void init(GameContainer container) 
			throws SlickException {
		manager = Manager.getInstance();
		
		plane = new Plane("plane.png");
		plane.setX(50f);
		plane.setY(50f);
		manager.addEntity(plane);
		
		
		sDD = new Slick2dDebugDraw(container.getGraphics(), container); // arg0 is the GameContainer in this case, I put this code in my init method
		sDD.setFlags(0x0001); //Setting the debug draw flags,
		
		Vec2 gravity = new Vec2(0.0f, -0.00f);
		boolean doSleep = true;
		m_woWorld = new World(gravity, doSleep);
		m_woWorld.setDebugDraw(sDD);
		
		
		
		BodyDef bd   = new BodyDef();
        bd.position.set(0, 0);
        bd.type = BodyType.DYNAMIC;
        
        CircleShape cs = new CircleShape();
        cs.m_radius = 61f;
        
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.5f;
        fd.friction = 0.5f;
        fd.restitution = 0.5f;
        
        body = m_woWorld.createBody(bd);
        body.createFixture(fd);
        body.setAngularVelocity(100);
        body.setAngularDamping(0.1f);
        body.setLinearVelocity(new Vec2(100, 100));
        body.setLinearDamping(0.3f);
        
        
        transform = new OBBViewportTransform();
        transform.setYFlip(true);
        transform.setExtents(container.getWidth() / 2 - 61, container.getHeight() / 2 - 61);
        
        System.out.println(container.getWidth() / 2);
        System.out.println(container.getHeight() / 2);

	}

	@Override
	public void update(GameContainer container, int delta) 
			throws SlickException {
		//m_woWorld.step(delta, 40, 40);
		
		
		
		
		 float frameTime = delta;
	       int stepsPerformed = 0;
	       while ( (frameTime > 0.0) && (stepsPerformed < MAXIMUM_NUMBER_OF_STEPS) ){
	          float deltaTime = Math.min( frameTime, FIXED_TIMESTEP );
	          frameTime -= deltaTime;
	          if (frameTime < MINIMUM_TIMESTEP) {
	             deltaTime += frameTime;
	             frameTime = 0.0f;
	          }
	          m_woWorld.step(deltaTime,VELOCITY_ITERATIONS,POSITION_ITERATIONS);
	          stepsPerformed++;
	          //afterStep(); // process collisions and result from callbacks called by the step
	       }
	       m_woWorld.clearForces();
	       
	       manager.update(container, delta);
	       
	       Vec2 pos = getWorldToScreen(body.getPosition());
			
			plane.setX(pos.x);
			plane.setY(pos.y);
			plane.setRotation(body.getAngle());
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
		app.setDisplayMode(1024, 768, false);
		app.setTargetFrameRate(200);
		//app.setVSync(true);
		
		app.start();
	}
	
	public Vec2 getWorldToScreen(Vec2 argWorld) {
        Vec2 screen = new Vec2();
        transform.getWorldToScreen(argWorld, screen);
        return screen;
    }
}
