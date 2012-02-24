package com.zero.objects;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.GameContainer;

import com.zero.main.Manager;

public class Walls  {

	GameContainer container;
	Manager manager;
	
	public Walls(GameContainer container, Manager manager) {
		this.container = container;
		this.manager = manager;
	}

	public void generateWalls() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position = manager.translateCoordsToWorld(0f, (float)container.getHeight());
		bodyDef.type = BodyType.STATIC;
		Body body = manager.getWorld().createBody(bodyDef);
	
		//Bottom
		PolygonShape shape = new PolygonShape();
		Vec2 v1 = new Vec2(0f, 0f);
		Vec2 v2 = new Vec2(container.getWidth(), 0f);
		shape.setAsEdge(v1, v2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		//Bottom end
		
		//Ceiling
		shape = new PolygonShape();
		v1 = new Vec2(0f, container.getHeight());
		v2 = new Vec2(container.getWidth(), container.getHeight());
		shape.setAsEdge(v1, v2);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		//Ceiling end
		
		//Right
		shape = new PolygonShape();
		v1 = new Vec2(container.getWidth(), 0f);
		v2 = new Vec2(container.getWidth(), container.getHeight());
		shape.setAsEdge(v1, v2);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		//Right end
		
		//Left
		shape = new PolygonShape();
		v1 = new Vec2(0f, 0f);
		v2 = new Vec2(0f, container.getHeight());
		shape.setAsEdge(v1, v2);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		//Left end
	}
}