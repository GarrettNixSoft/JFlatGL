package com.floober.engine.core.util.time;

import com.floober.engine.core.util.Logger;

/*
	@author Floober101
	A simple Timer. Start it, then call it at any time to check
	now many milliseconds have passed since it was started.
 */
public class Timer {

	protected float time;
	protected long start = -1;

	/**
	 * Create a new timer.
	 * @param time The target duration, in seconds.
	 */
	public Timer(float time) {
		this.time = time;
	}

	// ACTIONS
	/**
	 * Start this timer. If it is already running, this call is ignored
	 * and a warning will be printed in the error log.
	 */
	public void start() {
		if (start != -1) {
			Logger.logError("Tried to start a timer in progress!");
		}
		else {
			start = System.nanoTime();
		}
	}

	/**
	 * Stop the timer and reset it. This returns the timer to a state
	 * where it can be started again at any time. Subsequent calls to
	 * {@code started()} will return false until {@code start()} is
	 * called again.
	 */
	public void reset() {
		start = -1;
	}

	public void reset(float newTime) {
		reset();
		setTime(newTime);
	}

	/**
	 * Restart the timer. Calls {@code reset()} and {@code restart()}.
	 */
	public void restart() {
		reset();
		start();
	}

	/**
	 * Restart the timer with a new time target. Calls {@code reset()}, {@code setTime()} and {@code start()}.
	 * @param newTime The new duration.
	 */
	public void restart(float newTime) {
		reset();
		setTime(newTime);
		start();
	}

	// GETTERS

	/**
	 * Get the target duration of this timer.
	 * @return The target duration, in seconds.
	 */
	public float getTime() {
		return time;
	}

	/**
	 * Get the time elapsed since this timer was started.
	 * @return The time elapsed, in milliseconds.
	 */
	public long getTimeElapsed() {
		return TimeScale.getRawTime(start);
	}

	/**
	 * Get the progress of this timer.
	 * @return The current progress as a percentage of the target duration elapsed.
	 */
	public float getProgress() {
		return (float) (TimeScale.getRawTime(start) / (time * 1000.0));
	}

	/**
	 * Check if the timer is in progress.
	 * @return {@code true} if the timer has been started.
	 */
	public boolean started() {
		return start != -1;
	}

	/**
	 * Check if the timer is finished.
	 * @return {@code true} if the set duration of time has passed since the timer was started.
	 */
	public boolean finished() {
		if (time == -1) return false; // -1 sets a perpetual timer
		else return TimeScale.getRawTime(start) > time * 1000;
	}

	// SETTERS
	/**
	 * Set the duration of this timer. Fails if the timer is in progress.
	 * @param time The new target duration.
	 */
	public void setTime(float time) {
		if (start != -1) {
			Logger.logError("Tried to modify duration of timer in progress!");
		}
		else {
			this.time = time;
		}
	}

}
