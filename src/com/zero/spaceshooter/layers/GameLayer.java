package com.zero.spaceshooter.layers;

import org.lwjgl.opengl.Display;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.zero.events.ActorEvent;
import com.zero.events.ActorEventObserver;
import com.zero.interfaces.WorldObject;
import com.zero.main.Manager;
import com.zero.objects.Enemy;
import com.zero.objects.Player;
import com.zero.shaders.MapShader;
import com.zero.spaceshooter.MainDirector;
import com.zero.spaceshooter.SpaceShooter2;
import com.zero.spaceshooter.actors.BigAsteroid;
import com.zero.spaceshooter.actors.ManagerActor;
import com.zero.spaceshooter.resources.TextureAtlasCache;


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
	private Matrix4 normalProjection = new Matrix4();
	
	protected ManagerActor manager;
	protected Player player;
	protected Enemy enemy;
	
	
	public GameLayer(SpriteBatch batch) {
		this.batch = batch;
		director = MainDirector.instance();
		//createView();
		initActors();
		initManagerActor();
	}

	public void enter() {
		batch = this.getStage().getSpriteBatch();
		this.running = true;
		
	}
	
	/**
	 * Build View elements.
	 * 
	 */
	private void createView()
	{
		// create the world
		world = new World(new Vector2(0, 0), true);

	}	
	
	
	private void initActors() {
		/*
		TextureAtlas atlas = TextureAtlasCache.instance().getAtlas();
		BigAsteroid actor = new BigAsteroid(atlas, "asteroid-big", camera.position.x, camera.position.y);
		actor.visible = true;
		super.addActor(actor);
		*/
	}
	
	private void createCamera() {
		camera = new OrthographicCamera(800, 600);
		camera.position.set(0, 0, 0);
		camera.zoom = 1f / (float)Manager.PTM;
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
		this.createCamera();

		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("res-packed/pack"));

		world = new World(new Vector2(0, 0), true);

		this.createLights();	
		manager = ManagerActor.getInstance();

		manager.setWorld(world);
		manager.setBatch(batch);
		manager.setLightEngine(lightEngine);
		manager.setCamera(camera);
		manager.setTextureAtlas(atlas, "main");

		world.setContactListener(manager);
		
		player = new Player();
		manager.clampCameraTo(player.getShip());
		
		 enemy = new Enemy();		
	
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		normalProjection.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		addActor(manager);
	}
	
	
	
	public void act(float delta) {
		boolean stepped = fixedStep(delta);
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
		/*
		 * 
super.draw(batch, parentAlpha);
		
		if (this.running)
		{
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		}
		 */
		font.draw(spriteBatch, "Energy level: " + player.getShip().getEnergyLevel(), 10, Gdx.graphics.getHeight() - 20f);
		boolean stepped = fixedStep(Gdx.graphics.getDeltaTime());
		spriteBatch.setProjectionMatrix(camera.combined);

		
		super.draw(batch, parentAlpha);
		
		this.renderLights(stepped);

		spriteBatch.setProjectionMatrix(normalProjection);
		
		
		
		
		
		
	}
	
	private void renderLights(Boolean worldSteped) {
		if (worldSteped) {
			lightEngine.update();
		}

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
	/*
	public void clampCameraTo(WorldObject controller) {
		this.cameraController = controller;
	}
	
	public void updateCameraPosition() {
		if (camera == null || cameraController == null || cameraController.getBody() == null) {
			camera.update();
			return;
		}

		Ray posBorder = camera.getPickRay(Gdx.graphics.getWidth(), 0);

		Vector2 translateV = new Vector2();

		if (posBorder.origin.x - cameraController.getBody().getPosition().x < CAMERA_EDGE) {
			translateV.x = CAMERA_EDGE - (posBorder.origin.x - cameraController.getBody().getPosition().x);
		} 
		if (posBorder.origin.y - cameraController.getBody().getPosition().y < CAMERA_EDGE) {
			translateV.y = CAMERA_EDGE - (posBorder.origin.y - cameraController.getBody().getPosition().y);
		}	

		camera.translate(translateV.x, translateV.y, 0f);
		translateV.set(0f, 0f);

		Ray negBorder = camera.getPickRay(0, Gdx.graphics.getHeight());
		if ( Math.abs(negBorder.origin.x - cameraController.getBody().getPosition().x) < CAMERA_EDGE) {
			translateV.x = -(CAMERA_EDGE - Math.abs(negBorder.origin.x - cameraController.getBody().getPosition().x));
		}
		if ( Math.abs(negBorder.origin.y - cameraController.getBody().getPosition().y) < CAMERA_EDGE) {
			translateV.y = -(CAMERA_EDGE - Math.abs(negBorder.origin.y - cameraController.getBody().getPosition().y));
		} 
		camera.translate(translateV.x, translateV.y, 0f);
		camera.update();
	}
*/
	@Override
	public boolean handleEvent(ActorEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
