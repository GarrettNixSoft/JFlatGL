package com.floober.engine.util.time;

import com.floober.engine.util.Logger;

public class Tweener extends Timer {

	private float startingValue, endingValue;

	/**
	 * Create a new Tweener with zero values.
	 */
	public Tweener() {
		super(0);
		startingValue = 0;
		endingValue = 0;
	}

	/**
	 * Create a new Tweener.
	 * @param time The target duration.
	 * @param startingValue The starting value.
	 * @param endingValue The target value.
	 */
	public Tweener(float time, float startingValue, float endingValue) {
		super(time);
		this.startingValue = startingValue;
		this.endingValue = endingValue;
	}

	// GETTERS
	public float getStartingValue() {
		return startingValue;
	}
	public float getEndingValue() {
		return endingValue;
	}

	public float getValue() {
		float delta = endingValue - startingValue;
		Logger.log("" + startingValue + getProgress() * delta);
		return startingValue + getProgress() * delta;
	}

	// SETTERS
	public void setTargetValues(float startingValue, float endingValue) {
		if (start != -1) {
			Logger.logError("Tried to modify Tweener in progress!");
		}
		else {
			this.startingValue = startingValue;
			this.endingValue = endingValue;
		}
	}

}
