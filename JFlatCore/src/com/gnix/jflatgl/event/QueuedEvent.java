package com.gnix.jflatgl.event;

import com.gnix.jflatgl.core.util.Logger;

/**
 * {@code QueuedEvent}s provide for a structured sequence of
 * events run through an {@code EventQueue}. In a {@code MultiEventQueue},
 * They can be configured to be "blocking", where they will
 * prevent new events from beginning until they are completed,
 * or as "mustWait", where they will prevent themselves from
 * beginning until all currently running events are complete.
 * All other {@code QueuedEvent}s will simply be started in
 * sequence and can run simultaneously.
 */

public abstract class QueuedEvent {

	private boolean started;
	protected boolean complete;

	private final boolean blocking;
	private boolean mustWait = false;

	public QueuedEvent(boolean blocking) {
		this.blocking = blocking;
	}

	/**
	 * Check whether other events are allowed
	 * to begin while this event is active.
	 * @return true if other events must wait
	 */
	public boolean isBlocking() {
		return blocking;
	}

	/**
	 * Check whether this event is allowed to
	 * begin while other events are being processed.
	 * @return true if this event must wait for an empty queue
	 */
	public boolean mustWait() {
		return mustWait;
	}

	public void setMustWait(boolean mustWait) {
		this.mustWait = mustWait;
	}

	/**
	 * Start this event. Flags this event as started
	 * and calls its {@code onStart()} method.
	 */
	public void start() {
		started = true;
		Logger.log("STARTED EVENT: " + getClass().getName());
		onStart();
	}

	// call on event start
	public abstract void onStart();

	// run event
	public abstract void update();

	// call when event ends
	public abstract void onFinish();

	// check if an event is started
	public boolean isStarted() { return started; }

	// check if event is complete
	public boolean isComplete() {
		return complete;
	}

}
