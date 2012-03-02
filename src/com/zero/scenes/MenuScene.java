package com.zero.scenes;

import com.zero.layers.Layer;
import com.zero.layers.MenuLayer;

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