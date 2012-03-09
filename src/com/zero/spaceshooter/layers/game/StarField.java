package com.zero.spaceshooter.layers.game;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.zero.main.Manager;
import com.zero.spaceshooter.actors.ManagerActor;
import com.zero.spaceshooter.layers.Layer;

public class StarField extends Layer {
	
	//How many stars to render once on screen
	//And how many layers parallax effect contains
	private final int NUMBER_OF_STARS = 300;
	private  final int NUMBER_OF_LAYERS = 8;

	//Color values for different layers of parallax, mainly alpha differs not he color itself
	private final Color[] layerColors = new Color[] {
			new Color(1f, 1f, 1f, 200f/255f), 
			new Color(1f, 1f, 1f, 190f/255f), 
			new Color(1f, 1f, 1f, 170f/255f), 
			new Color(1f, 1f, 0f, 160f/255f), 
			new Color(1f, 1f, 1f, 128f/255f), 
			new Color(1f, 1f, 1f, 96f/255f), 
			new Color(1f, 1f, 1f, 64f/255f), 
			new Color(1f, 1f, 1f, 32f/255f) 
	};

	//Speed modifiers for moving parallax layers around. The deepest one is the slowest one
	private final float[] movementFactors = new float[] {
			0.9f, 0.8f, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f
	};

	//One star size in pixels^2
	private final int starSize = 2;

	private Vector2 lastPosition = new Vector2();
	private Vector2 position = new Vector2();
	private Vector2[] stars = new Vector2[NUMBER_OF_STARS];
	
	protected ManagerActor manager;
	protected Random rnd = new Random();
	protected Texture starTexture;

	public StarField(float width, float height, ManagerActor manager) {
		this.width = width;
		this.height = height;
		this.manager = manager;
		
		//Create a dynamic white color texture, without loading any image file
		Pixmap p = new Pixmap(starSize, starSize, Pixmap.Format.RGBA8888);
		p.setColor(Color.WHITE);
		p.fillRectangle(0, 0, starSize, starSize);
		starTexture = new Texture(p);
		
		//Get initial position of main camera (used to determine how much camera was moved)
		Vector2 tempPosition = getCameraPosition();
		tempPosition.mul(1f / Manager.PTM);
		Reset(tempPosition);
	}
	
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		//Calculate camera movement
		lastPosition.set(position);
		position.set(getCameraPosition());
		position.mul(1f / Manager.PTM);
		
		Vector2 movement = new Vector2(position);
		movement.sub(lastPosition);
		
		//Invert movement vector that stars would go in opposite direction than camera
		movement.mul(-1.0f);
		
		//Calculate in what level is star. Add movement to it based on camera movement * layer speed factor
		//And check if new star position is not out of bounds, if it its, move it to next side of screen randomizing 
		//one of axis
		for (int i = 0; i < NUMBER_OF_STARS; i++) {
			int depth = i % NUMBER_OF_LAYERS;
			
			Vector2 starMovement = new Vector2(movement);
			starMovement.mul(movementFactors[depth]);
			stars[i].add(starMovement);
			
			if (stars[i].x < 0)
            {
                stars[i].x = (int)width;
                stars[i].y = rnd.nextInt((int)height);
            }
            if (stars[i].x > (int)width)
            {
                stars[i].x = 0;
                stars[i].y = rnd.nextInt((int)height);
            }
            if (stars[i].y < 0)
            {
                stars[i].y = (int)height;
                stars[i].x = rnd.nextInt((int)width);
            }
            if (stars[i].y > (int)height)
            {
                stars[i].y = 0;
                stars[i].x = rnd.nextInt((int)width);
            }
			
            //Set color by layer depth array, and draw sprite to screen
			batch.setColor(layerColors[depth]);
			batch.draw(starTexture, stars[i].x, stars[i].y);
		}
	}
	
	public void Reset(Vector2 position)
    {
		//Populate array with random stars coortinates
        for (int i = 0; i < stars.length; ++i)
        {
            stars[i] = new Vector2(rnd.nextInt((int)width), 
            		rnd.nextInt((int)height));
        }

        // reset the position
        this.lastPosition.set(position);
        this.position.set(position);
    }
	
	private Vector2 getCameraPosition() {
		OrthographicCamera camera = manager.getCamera(false);
		Vector3 cameraPosition = camera.position;
		
		return manager.translateCoordsToScreen(new Vector2(cameraPosition.x, cameraPosition.y));
	}
}
