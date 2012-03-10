package com.zero.spaceshooter.scenes;

import com.zero.spaceshooter.actors.ManagerActor;
import com.zero.spaceshooter.layers.ControlLayer;
import com.zero.spaceshooter.layers.FpsLayer;
import com.zero.spaceshooter.layers.GameLayer;
import com.zero.spaceshooter.layers.game.StarField;
import com.zero.spaceshooter.layers.game.Map;
import com.zero.spaceshooter.layers.game.MapInfo;

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
		ManagerActor manager = game.getManagerActor();
		
		long seed = 111;
		//Map map = new Map(this.width, this.height, manager, game.getPlayer(), seed);
		MapInfo mapinfo = new MapInfo(game.getPlayer(), this.width, this.height);
		
		// ---------------------------------------------------------------
		// Star field as background.
		// ---------------------------------------------------------------
		StarField backGround = new StarField(width, height, manager);
		addLayer(backGround);
		
		// ---------------------------------------------------------------
		// Main game layers.
		// ---------------------------------------------------------------
		//addLayer(map);
		addLayer(game);
		addLayer(mapinfo);
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
