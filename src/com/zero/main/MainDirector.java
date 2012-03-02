package com.zero.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.zero.events.ActorEventObserver;
import com.zero.events.ActorEventSource;
import com.zero.scenes.Scene;


/**
 * Scene director.
 * 
 * This class routes actor events around observers and updates the view. It also maintains global note of view width and
 * height.
 * 
 */
public class MainDirector implements Disposable
{
	private static final boolean DEFAULT_STRETCH = true;

	private static MainDirector instance = null;

	private ActorEventSource eventSource;

	private int width;
	private int height;
	private boolean stretch;

	private Scene scene;

	private float scaleFactorX;
	private float scaleFactorY;

	private SpriteBatch spriteBatch;

	/**
	 * Access singleton instance
	 * 
	 * @return instance of class
	 */
	public synchronized static MainDirector instance()
	{
		if (instance == null)
		{
			instance = new MainDirector();
		}

		return instance;
	}

	/**
	 * Create reference to command pipeline.
	 * 
	 */
	public MainDirector()
	{
		scene = null;

		// This required Graphics context.
		spriteBatch = new SpriteBatch();

		stretch = DEFAULT_STRETCH;

		// Latch onto event source.
		eventSource = ActorEventSource.instance();

		// These are scale factors for adjusting touch events to the actual size
		// of the view-port.
		scaleFactorX = 1;
		scaleFactorY = 1;

		// Set up tween default configuration.
		
	}

	/**
	 * Update main loop.
	 * 
	 */
	public void update()
	{
		// Update events.
		eventSource.update();

		// Update View
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (scene != null)
		{
			float delta = Gdx.graphics.getDeltaTime();

			scene.act(delta);

			scene.draw();
		}
		else
		{
			Gdx.app.log("Director", "WTF! - No scene");
		}
	}

	/**
	 * Set the current scene.
	 * 
	 * @param scene
	 */
	public synchronized void setScene(Scene scene)
	{
		// If already active scene...
		if (this.scene != null)
		{
			// Exit stage left..
			this.scene.exit();
		}

		this.scene = scene;

		if (this.scene != null)
		{
			// Enter stage right..
			this.scene.enter();

			// NOTE: Route input events to the scene.
			Gdx.input.setInputProcessor(scene.getInputMultiplexer());
		}

	}

	/**
	 * Return currently running scene.
	 * 
	 * @return The current scene view.
	 */
	public synchronized Scene getScene()
	{
		return scene;
	}

	/**
	 * Return scene width.
	 * 
	 * @return The width.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Return scene height.
	 * 
	 * @return The height.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Set display width/height.
	 * 
	 * @param width
	 * @param height
	 */
	public void setWidthHeight(int width, int height)
	{
		this.width = width;
		this.height = height;

		if (scene != null)
		{
			scene.setViewport(width, height, stretch);
		}
	}

	/**
	 * Set display width.
	 * 
	 * @param width
	 */
	public void setWidth(int width)
	{
		this.width = width;

		if (scene != null)
		{
			scene.setViewport(width, height, stretch);
		}
	}

	/**
	 * Set display height.
	 * 
	 * @param height
	 */
	public void setHeight(int height)
	{
		this.height = height;

		if (scene != null)
		{
			scene.setViewport(width, height, stretch);
		}
	}

	/**
	 * Adjust the scale factors for touch/mouse events to match the size of the stage.
	 * 
	 * @param width
	 *            The new width.
	 * @param height
	 *            The new height.
	 */
	public void recalcScaleFactors(int width, int height)
	{
		scaleFactorX = (float) this.width / width;
		scaleFactorY = (float) this.height / height;
	}

	/**
	 * Is stretch flag set.
	 * 
	 * @return Stretch flag.
	 */
	public boolean isStretch()
	{
		return stretch;
	}

	/**
	 * Set stretch flag.
	 * 
	 * @param stretch
	 */
	public void setStretch(boolean stretch)
	{
		this.stretch = stretch;

		if (scene != null)
		{
			scene.setViewport(width, height, stretch);
		}
	}

	/**
	 * Send event to observers.
	 * 
	 * @param id
	 * @param actor
	 */
	public void sendEvent(int id, Actor actor)
	{
		eventSource.sendEvent(id, actor);
	}

	/**
	 * Add event observer event handler.
	 * 
	 * DO NOT PUT THIS INTO THE CONSTRUCTOR. IT MUST GO INTO THE "ENTER" HANDLER.
	 * 
	 * @param observer
	 *            The event observer.
	 */
	public void registerEventHandler(ActorEventObserver observer)
	{
		eventSource.addObserver(observer);
	}

	/**
	 * Add event observer event handler.
	 * 
	 * DO NOT FORGET TO PUT THIS INTO THE "EXIT" HANDLER IF YOU HAVE MATCHING "REGISTER" IN THE ENTER HANDLER.
	 * 
	 * @param observer
	 *            The event observer.
	 */
	public void deregisterEventHandler(ActorEventObserver observer)
	{
		eventSource.removeObserver(observer);
	}

	/**
	 * Clear all handlers.
	 * 
	 */
	public void clearEventHandlers()
	{
		// Clear all event subscriptions.
		eventSource.clear();
	}

	/**
	 * Return scale factor for touch/mouse events.
	 * 
	 * @return The x scale factor.
	 */
	public float getScaleFactorX()
	{
		return scaleFactorX;
	}

	/**
	 * Return scale factor for touch/mouse events.
	 * 
	 * @return The y scale factor.
	 */
	public float getScaleFactorY()
	{
		return scaleFactorY;
	}

	public SpriteBatch getSpriteBatch()
	{
		return spriteBatch;
	}

	public void setSpriteBatch(SpriteBatch spriteBatch)
	{
		this.spriteBatch = spriteBatch;
	}

	@Override
	public void dispose()
	{
		spriteBatch.dispose();
	}

}
