package com.floober.engine.util.time;

import com.floober.engine.display.DisplayManager;

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

	public void start() {
		start = System.nanoTime();
		elapsed = 0;
	}

	public void update() {
		elapsed += DisplayManager.getCurrentFrameDelta();
	}

	public long getTimeElapsed() {
		return elapsed;
	}

	public float getProgress() {
		return (float) (elapsed / (time * 1000.0));
	}

	public boolean finished() {
		if (time == -1) return false; // -1 sets a perpetual timer
		else return elapsed > time * 1000;
	}

}