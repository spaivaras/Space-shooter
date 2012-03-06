package com.zero.spaceshooter.scenes;

import com.zero.spaceshooter.layers.ControlLayer;
import com.zero.spaceshooter.layers.FpsLayer;
import com.zero.spaceshooter.layers.GameLayer;

public class GameScene extends Scene {

	//private Layer gameLayer;
	//private Layer starsLayer;
	private ControlLayer controlLayer;

	public GameScene()
	{
		
		controlLayer = new ControlLayer();
		
		getInputMultiplexer().addProcessor(controlLayer);
		
		addLayer(controlLayer);
		
		GameLayer game = new GameLayer(this.getSpriteBatch());
		addLayer(game);
		
		// ---------------------------------------------------------------
		// FPS layer.
		// ---------------------------------------------------------------
		FpsLayer fpsLayer = new FpsLayer(this.width, this.height);
		
		addLayer(fpsLayer);
		
		/*
		// ---------------------------------------------------------------
		// Label layer.
		// ---------------------------------------------------------------
		labelLayer = new LabelLayer(this.width, this.height);
		
		// Route input events to layer.
		getInputMultiplexer().addProcessor(labelLayer);
		
		addLayer(labelLayer);
		
		// ---------------------------------------------------------------
		// Simulation layer.
		// ---------------------------------------------------------------
		simulationLayer = new SimulationLayer(this.width, this.height);
		
		// Route input events to layer.
		getInputMultiplexer().addProcessor(labelLayer);
		
		addLayer(simulationLayer);
		
		// ---------------------------------------------------------------
		// Main menu layer.
		// ---------------------------------------------------------------
		gameLayer = new GameLayer(this);
		
		addLayer(gameLayer);
		*/
	}
}
