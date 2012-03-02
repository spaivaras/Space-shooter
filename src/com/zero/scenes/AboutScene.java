package com.zero.scenes;

import com.zero.layers.AboutLayer;
import com.zero.layers.ControlLayer;
import com.zero.layers.Layer;



/**
 * About scene.
 * 
 */
public class AboutScene extends Scene
{
	private Layer controlLayer;
	private Layer aboutLayer;

	public AboutScene()
	{
		// ---------------------------------------------------------------
		// Control layer
		// ---------------------------------------------------------------
		
		controlLayer = new ControlLayer();
		getInputMultiplexer().addProcessor(controlLayer);
		addLayer(controlLayer);
		
		// ---------------------------------------------------------------
		// About layer
		// ---------------------------------------------------------------
		aboutLayer = new AboutLayer(this);
		addLayer(aboutLayer);
	}
}
