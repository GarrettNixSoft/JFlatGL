package com.gnix.jflatgl.core.util.interpolators;

import com.gnix.jflatgl.core.util.time.ScaledTimer;

public abstract class FloatInterpolator {

	// timing
	protected ScaledTimer timer;
	protected boolean running;
	
	// alpha value
	protected float value;
	protected float originalValue;

	// auto reset feature
	protected boolean autoReset;
	
	public float getValue() {
		return value;
	}

	public void setAutoReset(boolean autoReset) {
		this.autoReset = autoReset;
	}

	public boolean isAutoReset() {
		return autoReset;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean started() {
		return timer.started();
	}

	public void start() {
		running = true;
		timer.start();
	}

	public void reset() {
		running = false;
		value = originalValue;
	}

	public abstract void update();
	public abstract boolean finished();
	
}
