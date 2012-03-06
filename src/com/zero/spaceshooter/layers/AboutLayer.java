package com.zero.spaceshooter.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;



/**
 * Scene layer.
 * 
 */
public class AboutLayer extends Layer
{
	private static final String URL_LABEL_FONT = "normal-font";
	private static final String UI_FILE = "res/textures/UI/uiskin32.json";
	private static final String UI_SKIN = "res/textures/UI/uiskin32.png";
	
	private Table table;
	//private TextureRegion logo;
	private Skin skin;
	private Label urlLabel;
	private Label poweredByLabel;
	
	public AboutLayer(Stage stage)
	{
		this.width = stage.width();
		this.height = stage.height();
		
		Gdx.input.setCatchBackKey(true);
		
		loadTextures();
		
		buildElements();
		
	}
	
	/**
	 * Load view textures.
	 * 
	 */
	private void loadTextures()
	{
		skin = new Skin(Gdx.files.internal(UI_FILE), Gdx.files.internal(UI_SKIN));
		
		//TextureCache textureCache = TextureCache.instance();
		
		//TextureDefinition definition = textureCache.getDefinition(GameActorTextures.TEXTURE_LIBGDX_LOGO);
		//logo = textureCache.getTexture(definition);
		
	}
	
	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		// ---------------------------------------------------------------
		// Background.
		// ---------------------------------------------------------------
//		Image image = new Image(logo, Scaling.none);
//		image.width = width;
//		image.height = height;
		// ---------------------------------------------------------------
		// Labels
		// ---------------------------------------------------------------
		urlLabel = new Label("www.netthreads.co.uk", URL_LABEL_FONT, Color.WHITE, skin);
		poweredByLabel = new Label("Powered By", skin);
		
		// ---------------------------------------------------------------
		// Table
		// ---------------------------------------------------------------
		table = new Table();
		
		table.size((int) width, (int) height);
		
		table.row();
		table.add(urlLabel).expandY().expandX();
		table.row();
		table.add(poweredByLabel).expandY().expandX();
		table.row();
		//table.add(image).expandY().expandX();
		
		table.pack();
		
		addActor(table);
	}
	

	
}
