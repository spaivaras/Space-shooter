package com.zero.events;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorEvent
{
	private int id;
	private long time;
	private Actor actor;

	/**
	 * Populate data event from system event.
	 * 
	 * @param event
	 */
	public void populate(int id, Actor node, long time)
	{
		// Copy fields.
		this.id = id;

		this.actor = node;

		this.time = time;
	}

	public int getId()
	{
		return id;
	}

	public Actor getActor()
	{
		return actor;
	}

	public long getTime()
	{
		return time;
	}
}
