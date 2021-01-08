package com.floober.engine.util.interpolators;

import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.time.ScaledTimer;

public class TimedPulseInterpolator extends FloatInterpolator {

	// timing
	private float pulseTime;
	private float pulseMin, pulseMax;
	private float pulseDelay;

	// pulses per cycle
	private int pulseCount;

	// current state
	private boolean waiting;
	private boolean pulseUp;
	private int currentPulse;

	/**
	 * Create a new TimedPulseInterpolator.
	 * The idea behind the order of constructor parameters is this:
	 * Every {@code pulseDelay} seconds, pulse {@code pulseCount} times for {@code pulseTime} seconds from {@code pulseMin} to {@code pulseMax}.
	 * @param pulseDelay The delay between pulse sets.
	 * @param pulseCount The number of pulses per cycle.
	 * @param pulseTime The duration of each pulse.
	 * @param pulseMin The starting value of the pulse.
	 * @param pulseMax The peak value of the pulse.
	 */
	public TimedPulseInterpolator(float pulseDelay, int pulseCount, float pulseTime, float pulseMin, float pulseMax) {
		this.pulseDelay = pulseDelay;
		this.pulseCount = pulseCount;
		this.pulseTime = pulseTime;
		this.pulseMin = pulseMin;
		this.pulseMax = pulseMax;
		timer = new ScaledTimer(-1);
	}

	// GETTERS
	@Override
	public float getValue() {
		if (waiting) return pulseMin;
		else {
			float progress = timer.getProgress();
			if (pulseUp) {
				progress *= 2;
			}
			else {
				progress -= 0.5f;
				progress *= 2;
				progress = 1 - progress;
			}
			return MathUtil.interpolate(pulseMin, pulseMax, progress);
		}
	}

	public boolean isWaiting() {
		return waiting;
	}

	// SETTERS
	public void setPulseDelay(float pulseDelay) {
		this.pulseDelay = pulseDelay;
	}

	public void setPulseTime(float pulseTime) {
		this.pulseTime = pulseTime;
	}

	public void setPulseCount(int pulseCount) {
		this.pulseCount = pulseCount;
	}

	public void setPulseMin(float pulseMin) {
		this.pulseMin = pulseMin;
	}

	public void setPulseMax(float pulseMax) {
		this.pulseMax = pulseMax;
	}

	/**
	 * Start pulsing. Begins in the pulse stage.
	 */
	@Override
	public void start() {
		timer.restart(pulseTime);
		waiting = false;
	}

	@Override
	public void reset() {
		timer.reset();
		timer.setTime(-1);
		waiting = true;
	}

	@Override
	public void update() {
		timer.update();
		if (waiting) {
			if (timer.finished()) {
				waiting = false;
				pulseUp = true;
				timer.restart(pulseTime);
			}
		}
		else {
			if (pulseUp) {
				if (timer.getProgress() > 0.5) pulseUp = false;
			}
			else if (timer.finished()) {
				currentPulse++;
				if (currentPulse == pulseCount) {
					currentPulse = 0;
					waiting = true;
					timer.restart(pulseDelay);
				}
				else {
					pulseUp = true;
					timer.restart();
				}
			}
		}
	}

	@Override
	public boolean finished() {
		return false;
	}
}