package com.zero.main;


import org.lwjgl.opengl.Display;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class SpaceShooter implements ApplicationListener {
	
	SpriteBatch spriteBatch;
	Sprite planeSprite;
	OrthographicCamera cam;
    World world;
    Box2DDebugRenderer renderer;
	Body body;
	
	@Override
	public void create() {
		cam = new OrthographicCamera(800, 600);
        cam.zoom = 0.03125f;
		
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("res-packed/pack"));
		planeSprite = atlas.createSprite("plane");
	
		
		world = new World(new Vector2(0, 0), true);
		renderer = new Box2DDebugRenderer();
		spriteBatch = renderer.batch;

		PolygonShape polygonShape = new PolygonShape();
		polygonShape.setAsBox(1.90625f, 1.890625f);
	
		BodyDef polygonBodyDef = new BodyDef();
		polygonBodyDef.type = BodyType.DynamicBody;
		body = world.createBody(polygonBodyDef);
		body.createFixture(polygonShape, 1f);

		polygonShape.dispose();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		//Some strange way to limit fps
		Display.sync(200);
		
		int centerX = Gdx.graphics.getWidth() / 2;
		int centerY = Gdx.graphics.getHeight() / 2;

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        world.step(Math.min(0.032f, Gdx.graphics.getDeltaTime()), 3, 4);
        cam.update();
        cam.apply(Gdx.gl10);
        renderer.render(world, cam.projection);
		
		
		//Gdx.graphics.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(planeSprite.getTexture(), centerX - (int)planeSprite.getWidth() / 2, 
				centerY - (int)planeSprite.getHeight() / 2,
				0, 0, (int)planeSprite.getWidth(), (int)planeSprite.getHeight());
		spriteBatch.end();
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
