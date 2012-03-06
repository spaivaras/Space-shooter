package com.zero.spaceshooter.scenes;

import com.zero.spaceshooter.layers.Layer;
import com.zero.spaceshooter.layers.MenuLayer;

public class MenuScene extends Scene
{
	private Layer menuLayer;
	//private Layer starsLayer;

	public MenuScene()
	{

		
		// ---------------------------------------------------------------
		// Main menu layer.
		// ---------------------------------------------------------------
		menuLayer = new MenuLayer(this);
		
		addLayer(menuLayer);
	}

}