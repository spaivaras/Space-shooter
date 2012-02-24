package com.zero.objects;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import com.zero.main.PolygonParser;


public class DummyPlane extends Entity {

	public DummyPlane(String name, Float x, Float y) throws SlickException {
		super(name, x, y);
		angleDifference = 180f;
	}

	@Override
	public void updatePosition(GameContainer container, int delta) {
	}
	
    //Create physic based structures, body, shape, fixture
    //and registers physics body to physics world
	@Override
	public void createPhysicsBody() {
		bodyDef = new BodyDef();
		bodyDef.position = manager.translateCoordsToWorld(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		body = manager.getWorld().createBody(bodyDef);
	
		PolygonParser pp = new PolygonParser();
		pp.parseEntity("plane", body);
        
        body.setLinearDamping(0.01f);
        body.setAngularDamping(0.01f);
        body.setTransform(body.getPosition(), (float)Math.toRadians(180));
	}
}