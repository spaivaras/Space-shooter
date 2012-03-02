package com.zero.scenes;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.zero.layers.Layer;
import com.zero.main.MainDirector;



public class Scene extends Stage implements Node
{
	private static final int DEFAULT_LAYER_CAPACITY = 10;
	
	/**
	 * Associated input multiplexer.
	 */
	private InputMultiplexer inputMultiplexer;
	
	/**
	 * Stage elements as nodes. We need this so we can call enter and exit on
	 * actors in order to manage registration and de-registration of event
	 * handlers.
	 */
	private Array<Node> nodes;

	public Scene()
	{
		super(MainDirector.instance().getWidth(), MainDirector.instance().getHeight(), MainDirector.instance().isStretch(), MainDirector.instance().getSpriteBatch());
		
		inputMultiplexer = new InputMultiplexer(this);
		
		nodes = new Array<Node>(DEFAULT_LAYER_CAPACITY);
	}
	
	/**
	 * Get input multiplexer.
	 * 
	 * @return The input multiplexer.
	 */
	public InputMultiplexer getInputMultiplexer()
	{
		return inputMultiplexer;
	}
	
	/**
	 * Add scene layer ensuring it adopts the same size as the owning scene.
	 * 
	 * Note layer in nodes list.
	 * 
	 * @param layer
	 *            The new layer.
	 */
	public void addLayer(Layer layer)
	{
		layer.width = this.width;
		layer.height = this.height;
		
		nodes.add(layer);
		
		addActor(layer);
	}
	
	/**
	 * Remove scene layer.
	 * 
	 * @param layer
	 *            The target layer.
	 */
	public void removeLayer(Layer layer)
	{
		int index = nodes.indexOf(layer, false);
		if (index >= 0)
		{
			nodes.removeIndex(index);

			removeActor(layer);
		}
	}
	
	/**
	 * Handle pre-display tasks.
	 * 
	 */
	@Override
	public void enter()
	{
		int size = nodes.size;
		for (int i = 0; i < size; i++)
		{
			nodes.get(i).enter();
		}
	}
	
	/**
	 * Handle post-display tasks.
	 * 
	 */
	@Override
	public void exit()
	{
		int size = nodes.size;
		for (int i = 0; i < size; i++)
		{
			nodes.get(i).exit();
		}
	}
	
}
