package com.gnix.jflatgl.core.util.interpolators;

import com.gnix.jflatgl.core.util.time.ScaledTimer;

public class TransitionFloat extends FloatInterpolator {

	private float targetValue;

	public TransitionFloat() {
		timer = new ScaledTimer();
	}

	public void transitionTo(float start, float value, float time) {
		if (!running) {
			originalValue = start;
			targetValue = value;
			timer.restart(time);
			running = true;
		}
	}

	@Override
	public void update() {
		timer.update();
		timer.ifFinishedOrElse(
				() -> {
					value = targetValue;
					running = false;
				},
				progress -> {
					float totalDelta = targetValue - originalValue;
					value = originalValue + totalDelta * progress;
				});
	}

	@Override
	public boolean finished() {
		return timer.finished();
	}

}
