package com.floober.engine.event;

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
