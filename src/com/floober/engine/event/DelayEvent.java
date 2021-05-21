package com.floober.engine.event;

import com.floober.engine.util.Logger;
import com.floober.engine.util.time.Timer;

/**
 * Generic delay, can be used in any event queue
 * to pause for a specified duration.
 */

public class DelayEvent extends QueuedEvent {

	private final Timer timer;

	public DelayEvent(float duration) {
		super(true);
		timer = new Timer(duration);
	}

	public float getDuration() { return timer.getTime(); }

	@Override
	public void onStart() {
		timer.start();
		Logger.logEvent("Pausing for " + timer.getTime() + " seconds");
	}

	@Override
	public void update() {
		complete = timer.finished();
	}

	@Override
	public void onFinish() {
		// nothing to clean up
	}
}
