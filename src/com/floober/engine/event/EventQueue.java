package com.floober.engine.event;

import com.floober.engine.util.Logger;
import com.floober.engine.util.data.Queue;

/**
 * A specialized {@code Queue} that only accepts {@code QueuedEvent}s.
 * @param <T> - all valid subclasses of {@code QueuedEvent}
 */

public class EventQueue<T extends QueuedEvent> {

	protected Queue<T> events;

	/**
	 * Create a new empty EventQueue.
	 */
	public EventQueue() {
		events = new Queue<>();
	}

	/**
	 * Add an event to the queue.
	 * @param e the event to enqueue
	 */
	public void queueEvent(T e) {
		if (!events.push(e)) Logger.logWarning("[EventQueue] An element was not" +
				"added to the queue because it is full (" + events.size() + " elements)");
	}

	/**
	 * Update the queue.
	 * <br>
	 * If the queue is currently empty, this method returns immediately.
	 * <br>
	 * If the event at the head of the queue has not started, its {@code start()}
	 * method will be called. Otherwise, it will be updated, and if it reports it
	 * is finished ({@code isComplete()} returns {@code true}) then its {@code onFinish()}
	 * method will be called and it will be removed from the queue.
	 */
	public void update() {
		if (!events.isEmpty()) {
			QueuedEvent currentEvent = events.peek();
			if (!currentEvent.isStarted()) currentEvent.start();
			else {
				currentEvent.update();
				if (currentEvent.isComplete()) {
					currentEvent.onFinish();
					events.remove();
				}
			}
		}
	}

	/**
	 * Check if this event queue is currently empty.
	 * @return true if the queue is empty
	 */
	public boolean isEmpty() {
		return events.isEmpty();
	}

	/**
	 * Get the number of events currently in this queue.
	 * @return the number of events
	 */
	public int getNumEvents() {
		return events.size();
	}

}