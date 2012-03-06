package com.zero.spaceshooter.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.zero.interfaces.EmmiterController;
import com.zero.interfaces.WorldObject;

import com.zero.main.Manager;
import com.zero.main.Map;


public class ManagerActor extends Actor implements ContactListener {

	//Pixel to meter ratio
	public static final int PTM = 32;
	public static final float CAMERA_EDGE = 6f;

	private static ManagerActor manager;
	private ArrayList<WorldObject> entities = new ArrayList<WorldObject>();
	private ArrayList<WorldObject> needsToBeRemoved = new ArrayList<WorldObject>();
	private ArrayList<WorldObject> needsToBeAdded = new ArrayList<WorldObject>();
	private HashMap<String, Sound> sounds;

	protected World world = null;
	protected SpriteBatch batch = null;
	protected RayHandler lightEngine = null;
	protected OrthographicCamera camera;
	protected OrthographicCamera noZoomCamera;
	protected WorldObject cameraController;

	protected TextureAtlas mainAtlas;

	protected Map _map;
	private Texture texture;       
	private TextureRegion[] regions = new TextureRegion[4]; // #2

	private ManagerActor() {
		loadSounds();
		loadAtlases();
		createCameras();
	}

	public void setMap(Map map) {
		texture = new Texture(Gdx.files.internal("res/hero.png"));
		regions[0] = new TextureRegion(texture, 0, 0, 32, 32);          // #3
		regions[1] = new TextureRegion(texture, 32, 0, 32, 32);    // #4
		regions[2] = new TextureRegion(texture, 0, 32, 32, 32);         // #5
		regions[3] = new TextureRegion(texture, 32, 32, 32, 32);    // #6
		this._map = map;
	}

	public Map getMap() {
		return this._map;
	}


	public void render(SpriteBatch batch, float parentAlpha) {
		for (WorldObject entity : entities) {
			entity.draw();
		}
	}
	
	public void renderEmmiters(SpriteBatch batch, float parentAlpha) {
		
		for (WorldObject entity : entities) {
			if (entity instanceof EmmiterController) {
				((EmmiterController) entity).drawEmmiters();
			}
		}
	}

	public void update(float delta) {

		Iterator<WorldObject> itr = needsToBeRemoved.iterator(); 
		while(itr.hasNext()) {
			WorldObject entity = itr.next(); 
			if (entity.dispose()) {
				entities.remove(entity);
				itr.remove();
			}
		} 

		if(!world.isLocked()) {
			itr = needsToBeAdded.iterator(); 
			while(itr.hasNext()) {
				WorldObject entity = itr.next(); 
				entities.add(entity);
				itr.remove();
			}
		}

		for (WorldObject entity : entities) {
			entity.update(delta);
		}
	}

	public Sound playSound(String key, float pitch, float gain, boolean loop) {
		Sound temp = sounds.get(key);

		if (temp != null) {
			long id;
			if (loop) {
				id = temp.loop(gain);
			} else {
				id = temp.play(gain);
			}
			temp.setPitch(id, pitch);
		}

		return temp;
	}

	public void stopSound(String key) {
		Sound temp = sounds.get(key);
		if (temp != null) {
			temp.stop();
		}
	}

	public static ManagerActor getInstance() {
		if (manager == null) {
			manager = new ManagerActor();
		}
		return manager;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Vector2 translateCoordsToWorld(Float x, Float y) {
		Vector2 result = new Vector2(-(Gdx.graphics.getWidth() / 2) + x,  Gdx.graphics.getHeight() / 2 - y);
		return result;
	}

	public Vector2 translateCoordsToScreen(Vector2 coordWorld) {
		Float screenX = coordWorld.x;
		Float screenY = coordWorld.y;
		return new Vector2(screenX, screenY).mul(PTM);
	}

	public Vector2 translateCoordsToScreen(Vector2 coordWorld, float offsetX, float offsetY) {
		Vector2 center = translateCoordsToScreen(coordWorld);
		center.x -= offsetX;
		center.y -= offsetY;

		return center;
	}

	private void loadSounds() {
		sounds = new HashMap<String, Sound>();
		Sound hit = (Sound) Gdx.audio.newSound(Gdx.files.internal("res/sounds/hit.ogg"));
		sounds.put("hit", hit);
	}
	
	private void loadAtlases() {
		mainAtlas =  new TextureAtlas(Gdx.files.internal("res/atlases/pack"));
	}

	@Override
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		if (b.getUserData() != null 
				&& a.getUserData() != null 
				&& needsToBeRemoved.indexOf((WorldObject)b.getUserData()) == -1) {

			WorldObject caller = (WorldObject)b.getUserData();
			WorldObject receiver = (WorldObject)a.getUserData();
			boolean shouldRemove = caller.collision(receiver);

			if (shouldRemove) {
				needsToBeRemoved.add(caller);
			}
		}
	}

	public void addEntityNext(WorldObject entity) {
		needsToBeAdded.add(entity);
	}

	public void removeEntityNex(WorldObject entity) {
		needsToBeRemoved.add(entity);
	}

	@Override
	public void endContact(Contact contact) {
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public RayHandler getLightEngine() {
		return lightEngine;
	}

	public void setLightEngine(RayHandler lightEngine) {
		this.lightEngine = lightEngine;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public void clampCameraTo(WorldObject controller) {
		this.cameraController = controller;
	}

	private void createCameras() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 0, 0);
		camera.zoom = 1f / (float)Manager.PTM;
		
		noZoomCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		noZoomCamera.position.set(0,0, 0);
		noZoomCamera.zoom = 1f;
	}
	
	public void updateCameraPosition() {
		if (camera == null || cameraController == null || cameraController.getBody() == null) {
			camera.update();
			noZoomCamera.update();
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
		translateV = translateV.mul(PTM);
		noZoomCamera.translate(translateV.x, translateV.y, 0f);
		
		translateV.set(0f, 0f);

		Ray negBorder = camera.getPickRay(0, Gdx.graphics.getHeight());
		if ( Math.abs(negBorder.origin.x - cameraController.getBody().getPosition().x) < CAMERA_EDGE) {
			translateV.x = -(CAMERA_EDGE - Math.abs(negBorder.origin.x - cameraController.getBody().getPosition().x));
		}
		if ( Math.abs(negBorder.origin.y - cameraController.getBody().getPosition().y) < CAMERA_EDGE) {
			translateV.y = -(CAMERA_EDGE - Math.abs(negBorder.origin.y - cameraController.getBody().getPosition().y));
		} 
		camera.translate(translateV.x, translateV.y, 0f);
		translateV = translateV.mul(PTM);
		noZoomCamera.translate(translateV.x, translateV.y, 0f);
		
		camera.update();
		noZoomCamera.update();
	}
	
	public void switchCamera(boolean zoomed) {
		if (zoomed) {
			batch.setProjectionMatrix(camera.combined);
		} else {
			batch.setProjectionMatrix(noZoomCamera.combined);
		}
	}
	
	public OrthographicCamera getCamera(boolean zoomed) {
		
		if (zoomed) {
			return camera;
		} else {
			return noZoomCamera;
		}
	}
	
	public TextureAtlas getTextureAtlas(String name) {
		return mainAtlas;
	}

	public void setTextureAtlas(TextureAtlas atlas, String name) {
		this.mainAtlas = atlas;
	}

	public void act(float delta) {
		super.act(delta);
		this.update(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		this.render(batch, parentAlpha);
	}

	@Override
	public Actor hit(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
}

