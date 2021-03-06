package com.zero.events;



public interface ActorEventObserver
{
	/**
	 * Handle controller events.
	 * 
	 * @param event
	 *            The actor event.
	 * 
	 * @return True if handled.
	 */
	public boolean handleEvent(ActorEvent event);
}