package com.floober.engine.core.util.time;

import com.floober.engine.core.renderEngine.display.DisplayManager;

/*
	@author Floober101
	A slightly more advanced timer, which responds to changes to the TimeScale.
	The disadvantage is that it must be updated each tick to properly track scaled time.
 */
public class ScaledTimer extends Timer {

	private long elapsed;

	public ScaledTimer() {
		super(1); // default 1s
	}

	public ScaledTimer(float time) {
		super(time);
	}

	/**
	 * Start this timer.
	 */
	public void start() {
		start = System.nanoTime();
		elapsed = 0;
	}

	/**
	 * Skip this timer's interval, forcing it to report as finished.
	 */
	public void skip() {
		elapsed = (long) (time * 1000) + 1;
	}

	/**
	 * Skip a portion of this timer's interval.
	 * @param amount the proportion of this timer's duration to skip, a float value in the range [0,1]
	 */
	public void skip(float amount) {
		elapsed = (long) (time * amount * 1000);
	}

	/**
	 * Update this timer. Must be called every frame for proper time scaling to work.
	 */
	public void update() {
		elapsed += DisplayManager.getCurrentFrameDeltaLong();
	}

	/**
	 * Get the scaled time elapsed, in milliseconds (note: need to confirm this).
	 * @return the scaled time as a {@code long}
	 */
	public long getTimeElapsed() {
		return elapsed;
	}

	/**
	 * Get this timer's progress as a percentage of its target time elapsed.
	 * @return a float value from [0, 1]
	 */
	public float getProgress() {
		return (float) (elapsed / (time * 1000.0));
	}

	/**
	 * Check if this timer has finished.
	 * @return {@code true} if the scaled time elapsed since calling {@code start()} exceeds the target time
	 */
	public boolean finished() {
		if (time == -1) return false; // -1 sets a perpetual timer
		else return elapsed > time * 1000;
	}

}