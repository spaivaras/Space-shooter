package com.zero.objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import com.zero.main.Manager;

public class Bullet extends Entity {
	
	public static final float BULLET_SPEED_FACTOR = 0.1f;
	public static final int ALIVE_TIME = 200;
	
	private int totalAliveTime = 0;
	private Entity shooter = null;
	
	public Bullet(String ref, Float x, Float y, Float angle, Entity shooter) throws SlickException {
		super(ref, x, y);
		body.setTransform(body.getPosition(), angle);
		manager.playSound("laser", 4f, 0.3f, false);
		this.shooter = shooter;
	}
	
	public void updatePosition(GameContainer container, int delta) {
		body.applyLinearImpulse(getThrustVector(), body.getWorldCenter());
		
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
		
		Vec2 vector = new Vec2((float)x, (float)y).mul(-1);
		return vector;
	}
	
	@Override
	public void createPhysicsBody() {

		bodyDef = new BodyDef();
		bodyDef.position = new Vec2(x, y +  (this.height / Manager.PTM));
		bodyDef.type = BodyType.DYNAMIC;
		body = manager.getWorld().createBody(bodyDef);
	
		PolygonShape shape = new PolygonShape();
		
		Float halfX = (float)this.getWidth() / Manager.PTM / 2;
		Float halfY = (float)this.getHeight() / Manager.PTM / 2;
		
		shape.setAsBox(halfX, halfY);
		  
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
	    fixtureDef.density = 0.01f;
	    fixtureDef.friction = 0f;
	    fixtureDef.restitution = 0.0f;
	    fixtureDef.isSensor = true;
	    body.createFixture(fixtureDef);
		
        body.setLinearDamping(0f);
        body.setAngularDamping(0f);
        body.setFixedRotation(true);
        body.setBullet(true);
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
