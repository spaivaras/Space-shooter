package com.zero.objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Bullet extends Entity {
	
	public static final float BULLET_SPEED_FACTOR = 7f;
	public static final int ALIVE_TIME = 200;
	
	private int totalAliveTime = 0;
	private Entity shooter = null;
	private Boolean shouldDraw = false;
	
	public Bullet(String ref, Float x, Float y, Float angle, Entity shooter) throws SlickException {
		super(ref, x, y);
		body.setTransform(body.getPosition(), angle);
		manager.playSound("laser", 4f, 0.3f, false);
		this.shooter = shooter;
	}
	
	public void draw() {
		if (shouldDraw) {
			super.draw(x, y);
		}
	}
	
	public void updatePosition(GameContainer container, int delta) {
		body.applyLinearImpulse(getThrustVector(), body.getWorldCenter());
		if (!shouldDraw) {
			shouldDraw = true;
		}
		
		totalAliveTime += delta;
		
		if (totalAliveTime >= ALIVE_TIME) {
			manager.removeEntity(this);
		}
	}

	private Vec2 getThrustVector() {
		double rads = body.getAngle() + Math.toRadians(90);
		
		//x + d * cos(a)  y + d.sin(a)
		double x = BULLET_SPEED_FACTOR * Math.cos(rads);
		double y = BULLET_SPEED_FACTOR * Math.sin(rads);
		
		Vec2 vector = new Vec2((float)x, (float)y);
		return vector.mul(-1f);
	}
	
	@Override
	public void createPhysicsBody() {
		bodyDef = new BodyDef();
		bodyDef.position = new Vec2(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		body = manager.getWorld().createBody(bodyDef);
	
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(5.5f, 25f);
		  
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
	    fixtureDef.density = 0.001f;
	    fixtureDef.friction = 0f;
	    fixtureDef.restitution = 0.0f;
	    fixtureDef.isSensor = true;
	    body.createFixture(fixtureDef);
		
        body.setLinearDamping(0.00f);
        body.setAngularDamping(0.0f);
        body.setFixedRotation(true);
        body.setBullet(true);
        body.setUserData(this);
	}

	@Override
	public Boolean collision(Entity with) {
		if (with.equals(shooter)) {
			return false;
		}
		
		with.hit();
		return true;
	}

	@Override
	public void hit() {
	}
}
