package com.floober.engine.core.util.interpolators;

public class FadeInFloat extends FadeFloat {
	
	public FadeInFloat() {
		super();
		value = originalValue = 0;
	}
	
	public FadeInFloat(float time) {
		super(time);
		value = 0;
	}

	public FadeInFloat(float time, float max) {
		super(time, max);
		value = 0;
	}
	
	@Override
	public boolean finished() {
		return value == max;
	}
	
	@Override
	public void reset() {
		timer.reset();
		value = min;
	}
	
	@Override
	public void update() {
		timer.update();
		if (running) {
			if (value >= max && autoReset) {
				reset();
			}
			long elapsed = timer.getTimeElapsed();
			float percent = elapsed / (time * 1000);
			float diff = max - min;
			value = min + (percent * diff);
			if (value > max) value = max;
		}
	}
	
}