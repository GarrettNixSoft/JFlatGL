package com.gnix.jflatgl.core.util.interpolators;

import com.gnix.jflatgl.core.util.time.ScaledTimer;

/*
 * @author Floober
 * 
 * The AlphaFadeEffect is used to generate an alpha value
 * that will fade from the provided maximum value to zero
 * in the given time.
 * 
 */
public class FadeFloat extends FloatInterpolator {
	
	// speed
	protected float time;
	
	// min/max alpha
	protected float max;
	protected float min;
	
	// default 1 second
	public FadeFloat() {
		timer = new ScaledTimer(1.0f);
		time = 1.0f;
		value = max = originalValue = 1.0f;
		min = 0;
	}
	
	// overloaded
	public FadeFloat(float time) {
		timer = new ScaledTimer(time);
		this.time = time;
		value = max = 1.0f;
		min = 0;
	}

	public FadeFloat(float time, float max) {
		timer = new ScaledTimer(time);
		this.time = time;
		this.max = max;
		value = max;
		min = 0;
	}

	// GETTERS
	public boolean isRunning() {
		return running;
	}
	public boolean finished() {
		return value == 0;
	}

	// SETTERS
	public void setTime(float time) {
		this.time = time;
	}
	public void setMax(float max) {
		this.max = max;
	}
	public void setMin(float min) {
		this.min = min;
	}

	// ACTIONS
	public void reset() {
		timer.reset();
		value = max;
	}
	
	@Override
	public void update() {
		timer.update();
		if (running) {
			long elapsed = timer.getTimeElapsed();
			if (value == 0 && autoReset) {
				if (elapsed > 50) reset();
			}
			float percent = elapsed / (time * 1000);
			float diff = max - min;
			value = max - (percent * diff);
			if (value < 0) value = 0;
		}
	}

}
