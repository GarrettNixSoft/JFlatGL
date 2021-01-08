package com.floober.engine.event;

import com.floober.engine.util.Logger;
import com.floober.engine.util.data.Queue;

public class TriggerEventQueue<T extends QueuedEvent> extends EventQueue<T> {

	protected Queue<T> events;
	protected boolean running;

	public TriggerEventQueue() {
		super();
		running = false;
	}

	/**
	 * Start this event queue.
	 */
	public void start() {
		running = true;
		if (events.peek() != null) events.peek().start();
	}

	/**
	 * Process the events in the queue.
	 * <br>
	 * If the queue has not been started yet, this method returns
	 * immediately.
	 * <br>
	 * If the event currently at the front of the queue reports it
	 * has finished ({@code complete()} returns {@code true}), then
	 * it is removed and the next event will be started,
	 * if it exists. Otherwise, the current event's {@code update()}
	 * method will be called.
	 */
	@Override
	public void update() {
		// wait until triggered
		if (!running) return;
		// get the current event
		QueuedEvent event = events.peek();
		if (event == null) return;
		if (!event.isStarted()) event.start();
		event.update();
		if (event.isComplete()) {
			event.onFinish();
			events.poll();
			if (events.isEmpty()) {
				Logger.logEvent("All events completed. This queue has expired. (Last event was type " + event.getClass() + ")");
				running = false;
			}
			else {
				Logger.logEvent("Starting next event, of type " + events.peek().getClass());
				assert events.peek() != null;
				events.peek().onStart();
			}
		}
	}

	/**
	 * An alias for {@code isEmpty()}. Used for
	 * queues which never receive new events once
	 * they have begun being processed.
	 * @return true if there are no events remaining in the queue
	 */
	public boolean expired() {
		return isEmpty();
	}

}