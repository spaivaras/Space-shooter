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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import com.zero.events.ActorEvent;
import com.zero.events.ActorEventObserver;
import com.zero.main.Manager;
import com.zero.objects.Enemy;
import com.zero.objects.Player;
import com.zero.shaders.MapShader;
import com.zero.spaceshooter.MainDirector;



public class GameLayer extends Layer implements ActorEventObserver {
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
	private Enemy enemy;
	private BitmapFont font;
	private Matrix4 normalProjection = new Matrix4();
	private MapShader map;
	private MainDirector director;
	protected boolean running = false;

	public GameLayer() {
		
		director = MainDirector.instance();
		createView();	
	}

	public void enter() {
		
		this.running = true;
	}
	
	public void createView() {
		System.out.println("createView");
		this.createCamera();

		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("res-packed/pack"));

		world = new World(new Vector2(0, 0), true);
		

		this.createLights();	
		manager = Manager.getInstance();

		manager.setWorld(world);
		//manager.setBatch(spriteBatch);
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
	public boolean handleEvent(ActorEvent event) {
		// TODO Auto-generated method stub
		return false;
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
	
	/**
	 * Draw layer contents.
	 * 
	 */
	@Override
	public void draw(SpriteBatch spriteBatch, float parentAlpha)
	{	
		
			super.draw(spriteBatch, parentAlpha);
			
			if (this.running)
			{
				world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
			}
			font.setColor(Color.WHITE);
			font.draw(spriteBatch, "Energy level: " + player.getShip().getEnergyLevel(), 10, Gdx.graphics.getHeight() - 20f);
			/*
			manager.setBatch(spriteBatch);
		
		//Some strange way to limit fps
				Display.sync(200);
				float delta = director.getDelta();
				boolean stepped = fixedStep(delta);
				
				//float delta  = Gdx.graphics.getDeltaTime();
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				manager.updateCameraPosition();

				
				
				
				spriteBatch.setProjectionMatrix(camera.combined);

				
				player.update(delta);
				enemy.update(delta);
				manager.update(delta);
				//renderer.render(world, camera.combined);

				//spriteBatch.begin();
					manager.render();
				//spriteBatch.end();

				this.renderLights(stepped);

				spriteBatch.setProjectionMatrix(normalProjection);
				
				//spriteBatch.begin();
					font.setColor(Color.WHITE);
					font.draw(spriteBatch, "Energy level: " + player.getShip().getEnergyLevel(), 10, Gdx.graphics.getHeight() - 20f);
				//spriteBatch.end();
		/*
		super.draw(batch, parentAlpha);
		
		if (this.running)
		{
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		}
		*/
	}

}
