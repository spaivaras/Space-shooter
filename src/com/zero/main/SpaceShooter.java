package com.zero.main;


import org.lwjgl.opengl.Display;

import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.zero.objects.BigAsteroid;
import com.zero.objects.Enemy;
import com.zero.objects.Plane;
import com.zero.objects.Player;
import com.zero.ships.LightFighter;

public class SpaceShooter implements ApplicationListener {

	private final static int MAX_FPS = 200;
	private final static int MIN_FPS = 120;
	public final static float TIME_STEP = 1f / MAX_FPS;
	private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
	private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
	private final static int VELOCITY_ITERS = 1;
	private final static int POSITION_ITERS = 1;

	float physicsTimeLeft;

	private SpriteBatch spriteBatch;
	private OrthographicCamera camera;
	private World world;
	private Box2DDebugRenderer renderer;
	private Manager manager;
	private RayHandler lightEngine;
	private Player player;
	private BitmapFont font;
	private Matrix4 normalProjection = new Matrix4();

	@Override
	public void create() {
		this.createCamera();

		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("res-packed/pack"));

		world = new World(new Vector2(0, 0), true);
		renderer = new Box2DDebugRenderer(true, true, true, true);
		spriteBatch = renderer.batch;

		this.createLights();	
		manager = Manager.getInstance();

		manager.setWorld(world);
		manager.setBatch(spriteBatch);
		manager.setLightEngine(lightEngine);
		manager.setCamera(camera);
		manager.setTextureAtlas(atlas, "main");

		world.setContactListener(manager);
		
		player = new Player();
		
		
//		player = new Plane(atlas, "player", 5f, 0f);
//		manager.addEntityNext(player);
//		manager.clampCameraTo(player);

		//		DummyPlane dPlane = new DummyPlane(atlas, "plane", 5f, -5f);
		//		manager.addEntity(dPlane);

//		Enemy enemy = new Enemy(atlas, "plane", 0f, -5f);
//		manager.addEntityNext(enemy);
//		
//		BigAsteroid asteroid = new BigAsteroid(atlas, "asteroid-big", 30f, 0f);
//		manager.addEntityNext(asteroid);
	
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		normalProjection.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
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

	@Override
	public void resize(int width, int height) {}

	@Override
	public void render() {
		//Some strange way to limit fps
		Display.sync(200);
		manager.updateCameraPosition();

		spriteBatch.setProjectionMatrix(camera.combined);

		boolean stepped = fixedStep(Gdx.graphics.getDeltaTime());
		
		float delta  = Gdx.graphics.getDeltaTime();
		player.update(delta);
		manager.update(delta);
		
		
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderer.render(world, camera.combined);

		spriteBatch.begin();
		manager.render();
		spriteBatch.end();

		this.renderLights(stepped);

		spriteBatch.setProjectionMatrix(normalProjection);
		spriteBatch.begin();

		font.setColor(Color.WHITE);
		font.draw(spriteBatch, "FPS: " + Integer.toString(Gdx.graphics.getFramesPerSecond())
				+ " - GLes 2.0: " + Gdx.graphics.isGL20Available()
				+ " - Heap size: "
				+ Math.round(Gdx.app.getJavaHeap() / 1024 / 1024) + " M"
				+ " - Native heap size: "
				+ Math.round(Gdx.app.getNativeHeap() / 1024 / 1024) + " M", 10, 20);
		
//		if (player.getEnergyLevel() < 30) {
//			font.setColor(Color.RED);
//		} else {
//			font.setColor(Color.BLUE);
//		}
//		
//		font.draw(spriteBatch, "Energy level: " + player.getEnergyLevel(), 10, Gdx.graphics.getHeight() - 20f);
		spriteBatch.end();
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

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}
