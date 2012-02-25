package com.zero.objects;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import com.zero.main.PolygonParser;


public class DummyPlane extends Entity {

	public static final int MAX_COLOR_CYCLE_COUNT = 2;
	public static final int COLOR_CHANGE_TIME = 70;
	
	private Boolean colored = false;
	private int colorCycleCount = MAX_COLOR_CYCLE_COUNT;
	private int colorCycleTime = 0;
	private Color colorOverlay;
	
	
	public DummyPlane(String ref, Float x, Float y) throws SlickException {
		super(ref, x, y);
		angleDifference = 180f;
	}

	public void draw() {
		Vec2 screen = manager.translateCoordsToScreen(new Vec2(x, y), 
				(float)this.getWidth() / 2, 
				(float)this.getHeight() / 2);
		
		if (colored) {
			super.draw(screen.x, screen.y);
			super.drawFlash(screen.x, screen.y, this.getWidth(), this.getHeight(), colorOverlay);
		} else{
			super.draw(screen.x, screen.y);
		}
	}
	
	@Override
	public void updatePosition(GameContainer container, int delta) {
		if(colorCycleCount < MAX_COLOR_CYCLE_COUNT) {
			colorCycleTime  += delta;
			if (colorCycleTime >= COLOR_CHANGE_TIME ) {
				colored = !colored;
				colorCycleTime = 0;
				colorCycleCount++;
			}
		} 
	}
	
    //Create physic based structures, body, shape, fixture
    //and registers physics body to physics world
	@Override
	public void createPhysicsBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position = new Vec2(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		body = manager.getWorld().createBody(bodyDef);
		
		PolygonParser pp = new PolygonParser();
		pp.parseEntity("plane", body);
        
        body.setLinearDamping(0.3f);
        body.setAngularDamping(0.5f);
        body.setTransform(body.getPosition(), (float)Math.toRadians(180));
	}

	@Override
	public Boolean collision(Entity with) {
		return false;
	}

	@Override
	public void hit() {
		Random randomizer = new Random();
		
		colorOverlay = new Color(randomizer.nextFloat(), randomizer.nextFloat(), randomizer.nextFloat(), 1f);
		colorCycleCount = 0;
		
		manager.playSound("hit", 2.5f, 0.2f, false);
	}
}