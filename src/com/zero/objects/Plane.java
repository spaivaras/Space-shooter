package com.zero.objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.pooling.arrays.Vec2Array;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Plane extends Entity 
{	
	public Plane(String ref, Float x, Float y) throws SlickException {
		super(ref, x, y);
	}

	public static final int SHOT_DELAY = 100;
	public static final float ROTATE_SPEED_FACTOR = 0.03f;
	
	//@TODO: TO IMPLEMENT MAX!
	//public static final float THRUSTER_MAX = 1f;
    public static final float THRUSTER_FACTOR = 1.5f;
    public static final float REV_THRUSTER_FACTOR = 0.5f;
        
	private Boolean shotDelayOn = false;
	int shotCounter = 0;

	@Override
	public void draw() {
		this.draw(x, y);
	}

	@Override
	public void update(GameContainer container, int delta) {
		//Update sprite position from physics body position in the world
		Vec2 bodyPosition = manager.translateCoordsToScreen(body.getPosition(), (float)(getWidth() / 2), (float)(getHeight() / 2));
		x = bodyPosition.x;
		y = bodyPosition.y;	
		setRotation(-(float)Math.toDegrees(body.getAngle()));
		
		//Parse user input, shouldn't be here!
		Input input = container.getInput();
		if (shotDelayOn && shotCounter < SHOT_DELAY) {
			shotCounter += delta;
		} else if(shotDelayOn && shotCounter >= SHOT_DELAY) {
			shotDelayOn = false;
			shotCounter = 0;
		}
                    
		if (input.isKeyDown(Input.KEY_A)) {
			body.setTransform(body.getPosition(), body.getAngle() + ROTATE_SPEED_FACTOR);
		}
		if (input.isKeyDown(Input.KEY_D)) {
			body.setTransform(body.getPosition(), body.getAngle() - ROTATE_SPEED_FACTOR);
		}
		
		if (input.isKeyDown(Input.KEY_W)) {
			body.applyLinearImpulse(getThrustVector(false), new Vec2(0,0));
			manager.playSoundIfNotStarted("thruster", 1f, 0.2f, true);
		} else if(input.isKeyPressed(Input.KEY_W)) {
			manager.stopSound("thruster");
		}
		
		if(input.isKeyDown(Input.KEY_S)) {
			body.applyLinearImpulse(getThrustVector(true), new Vec2(0,0));
			manager.playSoundIfNotStarted("thruster", 3f, 0.1f, true);
		} else if(input.isKeyPressed(Input.KEY_S)) {
			manager.stopSound("thruster");
		}
		
		if (input.isKeyDown(Input.KEY_SPACE) && !shotDelayOn) {
			try {
				Bullet bullet = new Bullet("laser.png");
				bullet.shoot(this);
				manager.addEntity(bullet);
				shotDelayOn = true;
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Vec2 getThrustVector(Boolean reverse) {
		double rads = body.getAngle() + Math.toRadians(90);
				
		double factor;
		if (reverse) {
			factor = REV_THRUSTER_FACTOR;
		} else {
			factor = THRUSTER_FACTOR;
		}
		
		//x + d * cos(a)  y + d.sin(a)
		double x = factor * Math.cos(rads);
		double y = factor * Math.sin(rads);
		
		Vec2 vector = new Vec2((float)x, (float)y);
		if (reverse) {
			return vector.mul(-1f);
		}
		
		return vector; 
	}
        
    //Create physic based structures, body, shape, fixture
    //and registers physics body to physics world
	@Override
	public void createPhysicsBody() {
		bodyDef = new BodyDef();
		bodyDef.position = manager.translateCoordsToWorld(x, y);
		bodyDef.type = BodyType.DYNAMIC;
		body = manager.getWorld().createBody(bodyDef);
	
		Vec2[] vertices = new Vec2Array().get(8);
		PolygonShape shape1 = new PolygonShape();
        vertices[0].set(new Vec2(-49, -11.5f));
        vertices[1].set(new Vec2(-42, -34.5f));
        vertices[2].set(new Vec2(-28, -57.5f));
        vertices[3].set(new Vec2(19, -58.5f));
        vertices[4].set(new Vec2(19, 7.5f));
        vertices[5].set(new Vec2(-22, 8.5f));
        shape1.set(vertices, 6);
        
        PolygonShape shape2 = new PolygonShape();
        vertices[0].set(new Vec2(45, -9.5f));
        vertices[1].set(new Vec2(19, 7.5f));
        vertices[2].set(new Vec2(19, -58.5f));
        vertices[3].set(new Vec2(45, -32.5f));
        shape2.set(vertices, 4);
        
        PolygonShape shape3 = new PolygonShape();
        vertices[0].set(new Vec2(19, 7.5f));
        vertices[1].set(new Vec2(5, 61.5f));
        vertices[2].set(new Vec2(-7, 61.5f));
        vertices[3].set(new Vec2(-22, 8.5f));
        shape3.set(vertices, 4);
        
        
        fixtureDef = new FixtureDef();
        
    
        fixtureDef.shape = shape1;
        fixtureDef.density = 0.001f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        
        fixtureDef.shape = shape2;
        fixtureDef.density = 0.001f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);
        
        fixtureDef.shape = shape3;
        fixtureDef.density = 0.001f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);


        
        body.setFixedRotation(true);
        body.setLinearDamping(0.05f);
        
	}
}
