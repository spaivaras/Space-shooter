package com.zero.spaceshooter.layers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.zero.objects.Player;
import com.zero.spaceshooter.layers.Layer;
import com.zero.spaceshooter.utils.Chunks;

public class MapInfo extends Layer {

	private final static int MAX_FPS = 200;
	private final static int MIN_FPS = 120;
	public final static float TIME_STEP = 1f / MAX_FPS;
	private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
	private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
	private final static int VELOCITY_ITERS = 1;
	private final static int POSITION_ITERS = 1;
	
	float physicsTimeLeft;
	
	private static final String FONT_FILE_SMALL = "res/fonts/font.fnt";
	private static final String FONT_IMAGE_SMALL = "res/fonts/font.png";

	private BitmapFont smallFont;

	static final char chars[] = new char[100];
	static final StringBuilder textStringBuilder = new StringBuilder(100);
	protected Player player;
	protected Chunks chunk;
	
	
	
	/**
	 * Create layer which displays the FPS in the bottom corner.
	 * 
	 * @param width
	 * @param height
	 */
	public MapInfo(Player player, float width, float height)
	{
		this.width = width;
		this.height = height;
		this.player = player;
		this.chunk = Chunks.getInstance();
		this.chunk.setPlayer(player);
		buildElements();
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		smallFont = new BitmapFont(Gdx.files.internal(FONT_FILE_SMALL), Gdx.files.internal(FONT_IMAGE_SMALL), false);
	}

	private boolean fixedStep(float delta) {
		physicsTimeLeft += delta;
		if (physicsTimeLeft > MAX_TIME_PER_FRAME)
			physicsTimeLeft = MAX_TIME_PER_FRAME;

		boolean stepped = false;
		while (physicsTimeLeft >= TIME_STEP) {
			physicsTimeLeft -= TIME_STEP;
			stepped = true;
		}
		return stepped;
	}	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		
		super.draw(batch, parentAlpha);
		//batch.setProjectionMatrix(normalProjection);

		// FPS
		textStringBuilder.setLength(0);
		textStringBuilder.append("Player x:");
		textStringBuilder.append(Math.round(player.getShip().getBody().getPosition().x));
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, Gdx.graphics.getHeight() - 30);
		
		textStringBuilder.setLength(0);
		textStringBuilder.append("Player y:");
		textStringBuilder.append(Math.round(player.getShip().getBody().getPosition().y));
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, Gdx.graphics.getHeight() - 50);
		
		textStringBuilder.setLength(0);
		textStringBuilder.append("Player speeds:");
		textStringBuilder.append(Math.round(player.getShip().getSpeed()));
		textStringBuilder.append("m/s");
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, Gdx.graphics.getHeight() - 70);
		
		Vector2 chunkPos = this.chunk.getChunkId();
		textStringBuilder.setLength(0);
		textStringBuilder.append("Chunk ID:");
		textStringBuilder.append(chunkPos.x);
		textStringBuilder.append("_");
		textStringBuilder.append(chunkPos.y);
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, Gdx.graphics.getHeight() - 90);
		
		
	}
	
}
