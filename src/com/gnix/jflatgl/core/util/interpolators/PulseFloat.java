package com.gnix.jflatgl.core.util.interpolators;

import com.gnix.jflatgl.core.util.time.ScaledTimer;

public class PulseFloat extends FloatInterpolator {
	
	// timing
	private final int frequency;
	
	// scale
	private final float startValue;
	private final float range;
	
	// default constructor
	public PulseFloat() {
		timer = new ScaledTimer(-1);
		frequency = 1000;
		startValue = 0.5f;
		range = 0.25f;
	}
	
	// constructor with frequency
	public PulseFloat(int frequency) {
		timer = new ScaledTimer(-1);
		this.frequency = frequency;
		this.startValue = 0.5f;
		this.range = 0.25f;
	}
	
	// fully overloaded
	public PulseFloat(int frequency, float startValue, float range) {
		timer = new ScaledTimer(-1);
		this.frequency = frequency;
		this.startValue = startValue;
		this.range = range;
	}
	
	// this one is never complete
	public boolean finished() {
		return false;
	}

	public void reset() {
		value = startValue;
	}

	public void start() {
		running = true;
		timer.start();
	}

	public void update() {
		timer.update();
		if (running) {
			// get time elapsed
			long elapsed = timer.getTimeElapsed();
			// update alpha
			float modifier = (float) Math.cos(elapsed / (float) frequency);
			value = range * modifier + startValue;
		}
	}
	
}
