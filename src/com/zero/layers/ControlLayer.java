package com.zero.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.zero.definitions.GameActorEvents;
import com.zero.main.MainDirector;


public class ControlLayer extends Layer
{
	/**
	 * Create layer.
	 * 
	 * @param stage
	 */
	public ControlLayer()
	{
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Catch escape key.
	 * 
	 */
	@Override
	public boolean keyDown(int keycode)
	{
		boolean handled = false;

		if (keycode == Keys.BACK || keycode == Keys.ESCAPE)
		{
			MainDirector.instance().sendEvent(GameActorEvents.EVENT_TRANSITION_TO_MENU_SCENE, this);

			handled = true;
		}

		return handled;
	}
}
