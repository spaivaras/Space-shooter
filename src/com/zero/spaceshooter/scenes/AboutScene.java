package com.zero.spaceshooter.scenes;

import com.zero.spaceshooter.layers.AboutLayer;
import com.zero.spaceshooter.layers.ControlLayer;
import com.zero.spaceshooter.layers.Layer;



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
