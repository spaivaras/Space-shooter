package com.zero.main;


import org.lwjgl.opengl.Display;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.zero.objects.Plane;

public class SpaceShooter implements ApplicationListener {
	
	private final static int MAX_FPS = 200;
	private final static int MIN_FPS = 120;
	public final static float TIME_STEP = 1f / MAX_FPS;
	private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
	private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
	private final static int VELOCITY_ITERS = 6;
	private final static int POSITION_ITERS = 2;

	float physicsTimeLeft;
	
	SpriteBatch spriteBatch;
	Sprite planeSprite;
	OrthographicCamera cam;
    World world;
    Box2DDebugRenderer renderer;
	Body body;
	Manager manager;
	
	@Override
	public void create() {
		cam = new OrthographicCamera(800, 600);
        cam.zoom = (float)1 / Manager.PTM;
		
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("res-packed/pack"));
		
		world = new World(new Vector2(0, 0), true);
		renderer = new Box2DDebugRenderer();
		spriteBatch = renderer.batch;

		manager = Manager.getInstance();
		manager.setWorld(world);
		manager.setBatch(spriteBatch);
		
		Plane player = new Plane(atlas, "plane", 0f, 0f);
		manager.addEntity(player);
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		//Some strange way to limit fps
		Display.sync(200);
		boolean stepped = fixedStep(Gdx.graphics.getDeltaTime());
		
		manager.update(Gdx.graphics.getDeltaTime());
	    cam.update();

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
       
        cam.apply(Gdx.gl10);
       // renderer.render(world, cam.projection);
		
		spriteBatch.begin();
		spriteBatch.setColor(Color.WHITE);
			manager.render();
		spriteBatch.end();
		
	}
	
	private boolean fixedStep(float delta) {
		physicsTimeLeft += delta;
		if (physicsTimeLeft > MAX_TIME_PER_FRAME)
			physicsTimeLeft = MAX_TIME_PER_FRAME;

		boolean stepped = false;
		while (physicsTimeLeft >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
			physicsTimeLeft -= TIME_STEP;
			stepped = true;
		}
		return stepped;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
