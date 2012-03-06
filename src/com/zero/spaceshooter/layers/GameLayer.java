package com.zero.spaceshooter.layers;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.zero.events.ActorEvent;
import com.zero.events.ActorEventObserver;
import com.zero.objects.Enemy;
import com.zero.objects.Player;
import com.zero.spaceshooter.MainDirector;
import com.zero.spaceshooter.actors.ManagerActor;


public class GameLayer extends Layer implements ActorEventObserver {

	private final static int MAX_FPS = 200;
	private final static int MIN_FPS = 120;
	public final static float TIME_STEP = 1f / MAX_FPS;
	private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
	private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
	private final static int VELOCITY_ITERS = 1;
	private final static int POSITION_ITERS = 1;
	
	float physicsTimeLeft;
	
	protected MainDirector director;
	protected boolean running = false;
	protected World world;
	protected OrthographicCamera camera;
	private RayHandler lightEngine;
	protected SpriteBatch batch;
	private BitmapFont font;
	
	protected ManagerActor manager;
	protected Player player;
	protected Enemy enemy;
	
	private boolean worldSteped = false;
	private Matrix4 normalProjection = new Matrix4();
	
	public GameLayer(SpriteBatch batch) {
		this.batch = batch;
		director = MainDirector.instance();
		initManagerActor();
	}

	public void enter() {
		batch = this.getStage().getSpriteBatch();
		this.running = true;
		
	}
	
	private void createLights() {
		RayHandler.setColorPrecisionHighp();
		RayHandler.setGammaCorrection(true);

		lightEngine = new RayHandler(world);
		lightEngine.setAmbientLight(0.3f);
		lightEngine.setCulling(true);
		lightEngine.setBlur(true);
		lightEngine.setBlurNum(10);
	}
	
	protected void initManagerActor() {


		world = new World(new Vector2(0, 0), true);

		this.createLights();	
		manager = ManagerActor.getInstance();

		manager.setWorld(world);
		manager.setBatch(batch);
		manager.setLightEngine(lightEngine);

		world.setContactListener(manager);
		
		player = new Player();
		manager.clampCameraTo(player.getShip());
		
		 enemy = new Enemy();		
	
		font = new BitmapFont();
		font.setColor(Color.RED);
		
		normalProjection.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		
		addActor(manager);
	}

	public void act(float delta) {
		worldSteped = fixedStep(delta);
		manager.updateCameraPosition();
		player.update(delta);
		enemy.update(delta);
		manager.update(delta);
	}
	
	/**
	 * Draw layer contents.
	 * 
	 */
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha)
	{	
		manager.switchCamera(false);
		super.draw(batch, parentAlpha);
		
		this.renderLights(worldSteped);
		
		//Need to desiced how to render properly above lights, but with contact to some objects
		spriteBatch.end();
		manager.switchCamera(false);
		spriteBatch.begin();
		manager.renderEmmiters(spriteBatch, parentAlpha);
		spriteBatch.end();
		
		spriteBatch.setProjectionMatrix(normalProjection);
		spriteBatch.begin();
		if (player.getShip().getEnergyLevel() > 30) {
			font.setColor(Color.BLUE);
		} else {
			font.setColor(Color.RED);
		}
		
		font.draw(spriteBatch, "Energy level: " + player.getShip().getEnergyLevel(), 10 , Gdx.graphics.getHeight() - 20);
	}
	
	private void renderLights(Boolean worldSteped) {
		if (worldSteped) {
			lightEngine.update();
		}

		OrthographicCamera camera = manager.getCamera(true);
		
		lightEngine.setCombinedMatrix(camera.combined, camera.position.x,
				camera.position.y, camera.viewportWidth * camera.zoom,
				camera.viewportHeight * camera.zoom);

		lightEngine.render();
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
	public boolean handleEvent(ActorEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
