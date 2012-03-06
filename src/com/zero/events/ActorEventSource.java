package com.zero.events;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class ActorEventSource
{
	private final static int OBSERVERS_CAPACITY = 10;

	private final static int MAX_EVENTS = 40;

	private ArrayList<ActorEventObserver> observers;
	private ArrayBlockingQueue<ActorEvent> pool;
	private ArrayBlockingQueue<ActorEvent> events;

	private static ActorEventSource _instance = null;

	/**
	 * Singleton instance.
	 * 
	 * @return The instance.
	 */
	public synchronized static ActorEventSource instance()
	{
		if (_instance == null)
		{
			_instance = new ActorEventSource();
		}

		return _instance;
	}

	/**
	 * Create observer list.
	 * 
	 */
	public ActorEventSource()
	{
		observers = new ArrayList<ActorEventObserver>(OBSERVERS_CAPACITY);

		pool = new ArrayBlockingQueue<ActorEvent>(MAX_EVENTS);
		events = new ArrayBlockingQueue<ActorEvent>(MAX_EVENTS);

		// Fill event pool.
		for (int index = 0; index < MAX_EVENTS; index++)
		{
			pool.add(new ActorEvent());
		}
	}

	/**
	 * Update any observers.
	 * 
	 * @throws InterruptedException
	 */
	public void update()
	{
		//System.out.println("Update actor event");
		try
		{
			if (events.size() > 0)
			{
				ActorEvent event = events.poll();

				if (event != null)
				{
					boolean handled = false;

					int size = observers.size();
					for (int index = 0; index < size; index++)
					{
						handled = observers.get(index).handleEvent(event);

						if (handled)
							break;
					}

					// Place data event back onto pool.
					pool.put(event);
				}
			}
		}
		catch (InterruptedException e)
		{
			// Nowt
		}
	}

	/**
	 * Add observer.
	 * 
	 */
	public synchronized void addObserver(ActorEventObserver observer)
	{
		if (!observers.contains(observer))
		{
			observers.add(observer);
		}
	}

	/**
	 * Remove observer.
	 * 
	 * @param observer
	 */
	public synchronized void removeObserver(ActorEventObserver observer)
	{
		if (observers.contains(observer))
		{
			observers.remove(observer);
		}
	}

	/**
	 * Enqueue event
	 * 
	 * @param keyCode
	 * @param event
	 */
	public void sendEvent(int id, Actor actor)
	{
		process(id, actor);
	}

	/**
	 * Map event data to pooled event.
	 * 
	 * Place pooled event on to broadcast queue.
	 * 
	 * NOTE: This uses "poll" not "take". The reason being that the events
	 * source is not another thread but the main loop of the application. We
	 * cannot block the loop so we try to enqueue the event if we can otherwise
	 * we notify by an error message.
	 * 
	 * @param event
	 */
	private void process(int id, Actor actor)
	{
		try
		{
			ActorEvent event = pool.poll();

			if (event != null)
			{
				// Populate our reusable event.
				event.populate(id, actor, System.currentTimeMillis());

				events.put(event);
			}
			else
			{
				Gdx.app.log("ActorEventSource", "No event from pool available to service event, " + id);
			}
		}
		catch (InterruptedException e)
		{
			// Nowt
		}
	}

	/**
	 * Clear structures.
	 * 
	 */
	public void clear()
	{
		observers.clear();
		events.clear();
	}
}