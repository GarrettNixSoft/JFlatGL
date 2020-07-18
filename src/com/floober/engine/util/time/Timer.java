package com.floober.engine.util.time;

/*
	@author Floober101
	A simple Timer. Start it, then call it at any time to check
	now many milliseconds have passed since it was started.
 */
public class Timer {

	protected final float time;
	protected long start;

	public Timer(float time) {
		this.time = time;
	}

	// ACTIONS
	public void start() {
		start = System.nanoTime();
	}

	public void reset() {
		start();
	}

	// GETTERS
	public float getTime() {
		return time;
	}
	public long getTimeElapsed() { return TimeScale.getScaledTime(start); }
	public float getTimeElapsedPercentage() {
		return (float) (TimeScale.getScaledTime(start) / (time * 1000.0));
	}

	public boolean finished() {
		if (time == -1) return false; // -1 sets a perpetual timer
		else return TimeScale.getScaledTime(start) > time * 1000;
	}

}
