package com.floober.engine.util.interpolators;

import com.floober.engine.util.time.ScaledTimer;

public class FloatCycle extends FloatInterpolator {

	private float startValue, targetValue;

	public FloatCycle() {
		timer = new ScaledTimer(1);
		startValue = 0;
		targetValue = 1;
	}

	public FloatCycle(float time) {
		timer = new ScaledTimer(time);
		startValue = 0;
		targetValue = 1;
	}

	public FloatCycle(float time, float startValue, float targetValue) {
		timer = new ScaledTimer(time);
		this.startValue = startValue;
		this.targetValue = targetValue;
	}

	public void setStartValue(float startValue) {
		this.startValue = startValue;
	}
	public void setTargetValue(float targetValue) {
		this.targetValue = targetValue;
	}

	@Override
	public void update() {
		timer.update();
		if (running) {
			if (timer.finished()) {
				value = startValue;
				timer.reset();
			}
			else {
				float diff = targetValue - startValue;
				value = startValue + timer.getProgress() * diff;
			}
		}
	}

	@Override
	public boolean finished() {
		return false;
	}
}