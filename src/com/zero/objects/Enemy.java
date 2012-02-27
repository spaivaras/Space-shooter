package com.zero.objects;

import java.util.Random;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zero.main.Manager;
import com.zero.main.PolygonParser;


public class Enemy extends Entity {

	public static final int MAX_COLOR_CYCLE_COUNT = 2;
	public static final float COLOR_CHANGE_TIME = 0.1f;
        public static final float ROTATE_SPEED_FACTOR = 110f;
	public static final float THRUSTER_FACTOR = 170f;
	public static final float REV_THRUSTER_FACTOR = 50f;
        
	private Boolean colored = false;
	private int colorCycleCount = MAX_COLOR_CYCLE_COUNT;
	private float colorCycleTime = 0;
	private Color colorOverlay;
        
        protected Entity target;
        
        protected Boolean activate = false;

	public Enemy(TextureAtlas atlas, String name, Float x, Float y) {
		super(atlas, name, x, y);
		this.angleDifference = 180;
	}

	@Override
	public void updatePosition(float delta) {
		if(colorCycleCount < MAX_COLOR_CYCLE_COUNT) {
			colorCycleTime  += delta;
			if (colorCycleTime >= COLOR_CHANGE_TIME ) {
				colored = !colored;
				colorCycleTime = 0;
				colorCycleCount++;
			}
		} else {
			colored = false;
		}
                if(activate) {
                    if(target.getX() < this.getX())  {
                        body.applyLinearImpulse(getThrustVector(false), body.getWorldCenter());
                    } else {
                        body.applyLinearImpulse(getThrustVector(true), body.getWorldCenter());
                    }
                    if(target.getY() < this.getY()) {
                        body.applyAngularImpulse(ROTATE_SPEED_FACTOR);
                    } else {
                        body.applyAngularImpulse(-ROTATE_SPEED_FACTOR);
                    }
                    //target.getY();
                            
                //body.applyLinearImpulse(getThrustVector(false), body.getWorldCenter());
                    //System.out.println("target" + target.getX());
                    //System.out.println("me" + this.getX());
                }
	}
        
        public void setTarget(Plane target) {
            this.target = target;
        }
        private Vector2 getThrustVector(Boolean reverse) {
		double rads = body.getAngle() + Math.toRadians(270);
		double factor;

		if (reverse) {
			factor = REV_THRUSTER_FACTOR;
		} else {
			factor = THRUSTER_FACTOR;
		}

		//x + d * cos(a)  y + d.sin(a)
		double x = factor * Math.cos(rads);
		double y = factor * Math.sin(rads);

		Vector2 vector = new Vector2((float)x, (float)y);
		if (reverse) {
			return vector.mul(-1f);
		}

		return vector; 
	}
	
	public void draw() {
		if (shouldDraw && sprite != null) {
			Vector2 screen = manager.translateCoordsToScreen(new Vector2(x, y), 
					(float)this.sprite.getWidth() / 2, 
					(float)this.sprite.getHeight() / 2);
			
			if (colored) {
				sprite.setColor(colorOverlay);
			} else {
				sprite.setColor(Color.WHITE);
			}
			
			sprite.setPosition(screen.x, screen.y);
			sprite.setScale(1f / (float)Manager.PTM );
			sprite.draw(manager.getBatch());
		}
	}

	//Create physic based structures, body, shape, fixture
	//and registers physics body to physics world
	@Override
	public void createPhysicsBody() {
		
		bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(x, y));
		bodyDef.type = BodyType.DynamicBody;
		body = manager.getWorld().createBody(bodyDef);

		PolygonParser pp = new PolygonParser();
		pp.parseEntity("plane", body, (short)0x0002);

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

		colorOverlay = new Color(randomizer.nextFloat(), randomizer.nextFloat(), randomizer.nextFloat(), 1.0f);
		colorCycleCount = 0;

		manager.playSound("hit", 2.5f, 0.2f, false);
	}
        public void hit(Entity newTarget) {
            this.target = newTarget;
            this.activate = true;
            hit();
        }

	@Override
	protected void createLights() {
		glowLight = new PointLight(manager.getLightEngine(), 128, new Color(1f, 1f, 1f, 0.5f), 5f, 0f, 0f);
		glowLight.setMaskBits(body.getFixtureList().get(0).getFilterData().maskBits);
		glowLight.attachToBody(body, 0f, 0f);
		
	}

	@Override
	protected void removeCustomLights() {
		// TODO Auto-generated method stub
		
	}
}