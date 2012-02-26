package com.zero.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zero.main.Manager;

public class Walls  {
	Manager manager;
	
	public Walls(Manager manager) {
		this.manager = manager;
	}

	public void generateWalls() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(0f, 0f);
		bodyDef.type = BodyType.StaticBody;
		Body body = manager.getWorld().createBody(bodyDef);
	
		Float halfX = (float)(Gdx.graphics.getWidth() - 5) / 2 / 32;
		Float halfY = (float)(Gdx.graphics.getHeight() - 5) / 2 / 32;
		
		//Bottom
		EdgeShape shape = new EdgeShape();
		Vector2 v1 = new Vector2(-halfX, -halfY);
		Vector2 v2 = new Vector2(halfX, -halfY);
		shape.set(v1, v2);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = 0x0004;
		fixtureDef.filter.maskBits = -1;
		body.createFixture(fixtureDef);
		shape.dispose();
		//Bottom end
		
		//Ceiling
		shape = new EdgeShape();
		v1 = new Vector2(-halfX, halfY);
		v2 = new Vector2(halfX, halfY);
		shape.set(v1, v2);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = 0x0004;
		fixtureDef.filter.maskBits = -1;
		body.createFixture(fixtureDef);
		shape.dispose();
		//Ceiling end
		
		//Right
		shape = new EdgeShape();
		v1 = new Vector2(halfX, halfY);
		v2 = new Vector2(halfX, -halfY);
		shape.set(v1, v2);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = 0x0004;
		fixtureDef.filter.maskBits = -1;
		body.createFixture(fixtureDef);
		shape.dispose();
		//Right end
		
		//Left
		shape = new EdgeShape();
		v1 = new Vector2(-halfX, halfY);
		v2 = new Vector2(-halfX, -halfY);
		shape.set(v1, v2);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = 0x0004;
		fixtureDef.filter.maskBits = -1;
		body.createFixture(fixtureDef);
		shape.dispose();
		//Left end
	}
}