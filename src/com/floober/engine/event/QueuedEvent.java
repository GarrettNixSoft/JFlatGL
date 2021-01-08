package com.floober.engine.event;

public abstract class QueuedEvent {

	private boolean started;
	protected boolean complete;

	private final boolean blocking;

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
	 * Start this event. Flags this event as started
	 * and calls its {@code onStart()} method.
	 */
	public void start() {
		started = true;
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
