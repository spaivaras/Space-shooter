package com.zero.spaceshooter.actors;

import java.util.ArrayList;
import java.util.Iterator;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.zero.ammunition.Ammunition;
import com.zero.interfaces.EmmiterController;
import com.zero.interfaces.Manageable;
import com.zero.interfaces.WorldObject;
import com.zero.main.Map;


public class ManagerActor extends Actor implements ContactListener {

	//Pixel to meter ratio
	public static final int PTM = 32;
	public static final float CAMERA_EDGE = 6f;

	private static ManagerActor manager;
	private ArrayList<Manageable> entities = new ArrayList<Manageable>(100);
	private ArrayList<Manageable> needsToBeRemoved = new ArrayList<Manageable>(10);
	private ArrayList<Manageable> needsToBeAdded = new ArrayList<Manageable>(10);

	protected World world = null;
	protected SpriteBatch batch = null;
	protected RayHandler lightEngine = null;
	protected OrthographicCamera camera;
	protected OrthographicCamera noZoomCamera;
	protected WorldObject cameraController;

	protected Map _map;
	private Texture texture;       
	private TextureRegion[] regions = new TextureRegion[4]; // #2

	private ManagerActor() {
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
	
	public ArrayList<Manageable> test() {
		return this.entities;
	}
	
	public Map getMap() {
		return this._map;
	}


	public void render(SpriteBatch batch, float parentAlpha) {
		for (Manageable entity : entities) {
			if (entity instanceof WorldObject) {
				((WorldObject)entity).draw();
			}
		}
	}
	
	public void renderEmmiters(SpriteBatch batch, float parentAlpha) {
		
		for (Manageable entity : entities) {
			if (entity instanceof EmmiterController) {
				((EmmiterController) entity).drawEmmiters();
			}
		}
	}

	public void update(float delta) {
		
		Iterator<Manageable> itr = needsToBeRemoved.iterator(); 
		while(itr.hasNext()) {
			Manageable entity = itr.next(); 
			if (entity.dispose()) {
				entities.remove(entity);
				itr.remove();
			}
		} 

		itr = needsToBeAdded.iterator(); 
		while(itr.hasNext()) {
			Manageable entity = itr.next(); 
			if (entity instanceof WorldObject) {
				if(!world.isLocked()) {
					entities.add(entity);
					itr.remove();
				}
			} else {
				entities.add(entity);
				itr.remove();
			}
		}
		
		for (Manageable entity : entities) {
			entity.update(delta);
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

	@Override
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		if (b.getUserData() != null 
				&& a.getUserData() != null 
				&& needsToBeRemoved.indexOf((WorldObject)b.getUserData()) == -1) {

			WorldObject caller = (WorldObject)b.getUserData();
			WorldObject receiver = (WorldObject)a.getUserData();
			
			if (caller instanceof Ammunition) {
				Ammunition tmp = (Ammunition)caller;
				if (tmp.isActive()) {
					caller.collision(receiver);
				}
				
			} else if (receiver instanceof Ammunition) {
				Ammunition tmp = (Ammunition)receiver;
				if (tmp.isActive()) {
					receiver.collision(caller);
				}
			}
		}
	}

	public void addEntityNext(Manageable entity) {
		needsToBeAdded.add(entity);
	}

	public void removeEntityNex(Manageable entity) {
		if(entities.indexOf(entity) >= 0 ) { 
			needsToBeRemoved.add(entity);
		}
		if(needsToBeAdded.indexOf(entity) >= 0) {
			System.out.println("check");
			needsToBeAdded.remove(entity);
		}
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
		camera.zoom = 1f / (float)ManagerActor.PTM;
		
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
	
	public void exit() {
		manager = null;
	}
}

