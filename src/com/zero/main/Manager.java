package com.zero.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.zero.objects.Entity;

public class Manager implements ContactListener {

	//Pixel to meter ratio
	public static final int PTM = 32;
	
	private static Manager manager;
	private CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
	private ArrayList<Entity> needsToBeRemoved = new ArrayList<Entity>();
	private HashMap<String, Audio> sounds;
	
	protected World world = null;
	protected GameContainer container = null;
	
	private Manager() {
		loadSounds();
	}
	
	public void render(GameContainer container, Graphics g) {
		for (Entity entity : entities) {
			entity.draw();
		}
	}

	public void update(GameContainer container, int delta) {
		
		for (Entity entity : needsToBeRemoved) {
			removeEntity(entity);
		}
		
		needsToBeRemoved.clear();
		
		for (Entity entity : entities) {
			entity.update(container, delta);
		}
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
		if (entity.getBody() != null) {
			world.destroyBody(entity.getBody());
		}
	}
	
	public void playSound(String key, Float pitch, Float gain, Boolean loop) {
		Audio temp = sounds.get(key);
		if (temp != null) {
			temp.playAsSoundEffect(pitch, gain, loop);
		}
	}
	
	public void playSoundIfNotStarted(String key, Float pitch, Float gain, Boolean loop) {
		Audio temp = sounds.get(key);
		if (temp != null && !temp.isPlaying()) {
			temp.playAsSoundEffect(pitch, gain, loop);
		}
	}
	
	public void stopSound(String key) {
		Audio temp = sounds.get(key);
		if (temp != null && temp.isPlaying()) {
			temp.stop();
		}
	}
	
	public static Manager getInstance() {
		if (manager == null) {
			manager = new Manager();
		}
		return manager;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}

	public GameContainer getContainer() {
		return container;
	}

	public void setContainer(GameContainer container) {
		this.container = container;
	}
	
	public Vec2 translateCoordsToWorld(Float x, Float y) {
		Vec2 result = new Vec2(-(container.getWidth() / 2) + x,  container.getHeight() / 2 - y);
		return result;
	}
	
	public Vec2 translateCoordsToScreen(Vec2 coordWorld) {
		Float screenX = coordWorld.x * PTM + (manager.getContainer().getWidth() / 2);
		Float screenY = -coordWorld.y * PTM + (manager.getContainer().getHeight() / 2);
        return new Vec2(screenX, screenY);
	}
	
	public Vec2 translateCoordsToScreen(Vec2 coordWorld, Float offsetX, Float offsetY) {
		Vec2 center = translateCoordsToScreen(coordWorld);
		center.x -= offsetX;
		center.y -= offsetY;
		
		return center;
	}
	
	private void loadSounds() {
		sounds = new HashMap<String, Audio>();
		try {
			Audio laserSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/laser.ogg"));
			sounds.put("laser", laserSound);
			
			Audio thrusterSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/thrust.ogg"));
			sounds.put("thruster", thrusterSound);
			
			Audio hitSound = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/hit.ogg"));
			sounds.put("hit", hitSound);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beginContact(Contact contact) {
	}

	@Override
	public void endContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		
		if (b.getUserData() != null 
				&& a.getUserData() != null 
				&& needsToBeRemoved.indexOf((Entity)b.getUserData()) == -1) {
			
			Entity caller = (Entity)b.getUserData();
			Entity receiver = (Entity)a.getUserData();
			Boolean shouldRemove = caller.collision(receiver);
			
			if (shouldRemove) {
				needsToBeRemoved.add(caller);
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {}
}
