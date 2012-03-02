package com.zero.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.zero.definitions.GameActorEvents;
import com.zero.main.MainDirector;


/**
 * Scene layer.
 * 
 */
public class MenuLayer extends Layer
{
	private static final String UI_FILE = "data/uiskin60.json";
	private static final String UI_SKIN = "data/uiskin60.png";

	private Table table;
	private Skin skin;
	private Label titleLabel;
	private Button startButton;
	private Button settingsButton;
	private Button aboutButton;

	// Director of the action.
	private MainDirector director;

	/**
	 * Construct the screen.
	 * 
	 * @param stage
	 */
	public MenuLayer(Stage stage)
	{
		this.width = stage.width();
		this.height = stage.height();

		this.director = MainDirector.instance();

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
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		// Title
		titleLabel = new Label("DEMO", "large-font", Color.YELLOW, skin);
		
		// ---------------------------------------------------------------
		// Buttons.
		// ---------------------------------------------------------------
		startButton = new TextButton("Start", skin.getStyle(TextButtonStyle.class), "startButton");
		settingsButton = new TextButton("Settings", skin.getStyle(TextButtonStyle.class), "settingsButton");
		aboutButton = new TextButton("About", skin.getStyle(TextButtonStyle.class), "exitButton");

		// ---------------------------------------------------------------
		// Table
		// ---------------------------------------------------------------
		table = new Table();

		table.size((int) width, (int) height);

		table.row();
		table.add(titleLabel).expandY().expandX();
		table.row();
		table.add(startButton).expandY().expandX();
		table.row();
		table.add(settingsButton).expandY().expandX();
		table.row();
		table.add(aboutButton).expandY().expandX();

		// Listener.
		startButton.setClickListener(new ClickListener()
		{
			@Override
			public void click(Actor actor, float x, float y)
			{
				System.out.println(x + "  " + y + " " + actor.toString());
				//director.sendEvent(GameActorEvents.EVENT_TRANSITION_TO_GAME_SCENE, actor);
			}
		});

		// Listener.
		settingsButton.setClickListener(new ClickListener()
		{
			@Override
			public void click(Actor actor, float x, float y)
			{
				System.out.println(x + "  " + y + " " + actor.toString());
				//director.sendEvent(GameActorEvents.EVENT_TRANSITION_TO_SETTINGS_SCENE, actor);
			}
		});

		// Listener.
		aboutButton.setClickListener(new ClickListener()
		{
			@Override
			public void click(Actor actor, float x, float y)
			{
				//System.out.println(x + "  " + y + " " + actor.toString());
				director.sendEvent(GameActorEvents.EVENT_TRANSITION_TO_ABOUT_SCENE, actor);
			}
		});

		table.pack();

		// Add table to view
		addActor(table);

	}

}
