package com.zero.spaceshooter.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class FpsLayer extends Layer {

	private static final float FPS_X = 10;
	private static final float FPS_Y = 25;

	private static final String FONT_FILE_SMALL = "res/fonts/font.fnt";
	private static final String FONT_IMAGE_SMALL = "res/fonts/font.png";

	private static final String TEXT_FPS = "fps:";

	private BitmapFont smallFont;

	static final char chars[] = new char[100];
	static final StringBuilder textStringBuilder = new StringBuilder(100);
	
	private Matrix4 normalProjection = new Matrix4();
	

	/**
	 * Create layer which displays the FPS in the bottom corner.
	 * 
	 * @param width
	 * @param height
	 */
	public FpsLayer(float width, float height)
	{
		this.width = width;
		this.height = height;

		buildElements();
		/*
		normalProjection.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
				*/
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		smallFont = new BitmapFont(Gdx.files.internal(FONT_FILE_SMALL), Gdx.files.internal(FONT_IMAGE_SMALL), false);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		//batch.setProjectionMatrix(normalProjection);

		// FPS
		textStringBuilder.setLength(0);
		textStringBuilder.append(TEXT_FPS);
		textStringBuilder.append(Gdx.graphics.getFramesPerSecond());
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, 90);
		
		textStringBuilder.setLength(0);
		textStringBuilder.append("GLes 2.0:");
		textStringBuilder.append(Gdx.graphics.isGL20Available());
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, 70);
		
		
		textStringBuilder.setLength(0);
		textStringBuilder.append("Heap size:");
		textStringBuilder.append(Math.round(Gdx.app.getJavaHeap() / 1024 / 1024) + " M");
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, 50);
		
		textStringBuilder.setLength(0);
		textStringBuilder.append("Native heap size: ");
		textStringBuilder.append(Math.round(Gdx.app.getNativeHeap() / 1024 / 1024) + " M");
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		smallFont.draw(batch, textStringBuilder, 10, 30);
	}
	
}
