package com.zero.spaceshooter;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputAdapter;
import com.zero.events.ActorEvent;
import com.zero.events.ActorEventObserver;
import com.zero.spaceshooter.definitions.GameActorEvents;
import com.zero.spaceshooter.scenes.AboutScene;
import com.zero.spaceshooter.scenes.GameScene;
import com.zero.spaceshooter.scenes.MenuScene;
import com.zero.spaceshooter.scenes.Scene;

public class SpaceShooter2 extends InputAdapter implements ApplicationListener, ActorEventObserver {
	
	public static int DEFAULT_WIDTH = 1024;
	public static int DEFAULT_HEIGHT = 768;
	MainDirector director;
	MenuScene menuScene;
	AboutScene aboutScene;

	@Override
	public void create() {
				//The Director needs the graphics context so we can't create it in the constructor.
				this.director = MainDirector.instance();

				// Set initial width and height.
				this.director.setWidth(DEFAULT_WIDTH);
				this.director.setHeight(DEFAULT_HEIGHT);

				// Add this as an event observer.
				this.director.registerEventHandler(this);

				// Load/Re-load textures
				//TextureCache.instance().load();

				// Load/Re-load sounds.
				//SoundCache.instance().load();

				menuScene = getMenuScene();

				this.director.setScene(menuScene);
		
	}

	
	private MenuScene getMenuScene() {
		if (menuScene == null)
		{
			menuScene = new MenuScene();
		}

		return menuScene;
	}


	@Override
	public void resize(int width, int height) {
		director.recalcScaleFactors(width, height);
	}

	@Override
	public void render() {
		director.update();
	}

	
	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		MainDirector.instance().dispose();
	}


	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		switch (event.getId())
		{
		case GameActorEvents.EVENT_TRANSITION_TO_MENU_SCENE:
			transitionTo(new MenuScene());
			handled = true;
			break;
		case GameActorEvents.EVENT_TRANSITION_TO_SETTINGS_SCENE:
			//transitionToSettingsScene();
			handled = true;
			break;
		case GameActorEvents.EVENT_TRANSITION_TO_GAME_SCENE:
			transitionTo(new GameScene());
			handled = true;
			break;
		case GameActorEvents.EVENT_TRANSITION_TO_ABOUT_SCENE:
			transitionTo(new AboutScene());
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}
	
	
	protected void transitionTo(Scene newscene) {
		
		this.director.getScene().dispose();

		//TransitionScene transitionScene = MoveInRTransitionScene.$(inScene, outScene, DURATION_ABOUT_TRANSITION, Bounce.OUT);

		this.director.setScene(newscene);
	}
	/**
	 * Run transition.
	 * 
	 */
	private void transitionToAboutScene()
	{
		Scene inScene = getAboutScene();
		Scene outScene = this.director.getScene();

		//TransitionScene transitionScene = MoveInRTransitionScene.$(inScene, outScene, DURATION_ABOUT_TRANSITION, Bounce.OUT);

		this.director.setScene(inScene);
	}

	/**
	 * Generate scene.
	 * 
	 * @return The target scene.
	 */
	public AboutScene getAboutScene()
	{
		if (aboutScene == null)
		{
			aboutScene = new AboutScene();
		}

		return aboutScene;
	}
}
