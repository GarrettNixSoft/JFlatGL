package com.gnix.jflatgl.core.entity.effects;

import com.gnix.jflatgl.core.camera.Camera;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.interpolators.IntTweener;
import com.gnix.jflatgl.core.util.math.MathUtil;
import com.gnix.jflatgl.core.util.math.RandomUtil;
import com.gnix.jflatgl.core.util.time.ScaledTimer;

public class ShakeEffect extends EntityEffect {

	private final int shakeDelayMS;
	private float shakeSeverityInitial;
	private float shakeSeverityFinal;
	private float shakeSeverityCurrent;
	private float currentOffset;

	private long lastElapsed;

	public ShakeEffect() {
		super(null); // no animation
		timer = new ScaledTimer(1);
		shakeDelayMS = 20;
		shakeSeverityInitial = 6;
		shakeSeverityFinal = 6;
		shakeSeverityCurrent = shakeSeverityInitial;
	}

	// getters
	public float getOffset() {
		return currentOffset;
	}

	// setters
	public void setShakeSeverityInitial(int shakeSeverityInitial) {
		this.shakeSeverityInitial = shakeSeverityInitial;
		this.shakeSeverityCurrent = shakeSeverityInitial;
	}

	public void setShakeSeverityFinal(int shakeSeverityFinal) {
		this.shakeSeverityFinal = shakeSeverityFinal;
	}

	private void shake() {
		currentOffset = RandomUtil.getFloat(-shakeSeverityCurrent, shakeSeverityCurrent);
	}

	@Override
	public void activate(float time) {
		super.activate(time);
		lastElapsed = 0;
	}

	@Override
	public void update() {

		((ScaledTimer) timer).update();

		if (synchronize)
			checkSync();
		if (active) {
			// modify severity over time
			shakeSeverityCurrent = MathUtil.interpolate(shakeSeverityInitial, shakeSeverityFinal, timer.getProgress());
			shakeSeverityCurrent = Math.max(shakeSeverityCurrent, 0);
			// check for new shake event
			long elapsed = timer.getTimeElapsed();
			Logger.log("elapsed: " + (elapsed - lastElapsed) + " / " + shakeDelayMS);
					//(System.nanoTime() - timer) / 1_000_000;
			if (elapsed - lastElapsed > shakeDelayMS) {
				shake();
				lastElapsed = elapsed;
//				timer = System.nanoTime();
			}

			if (timer.finished()) {
				deactivate();
			}
		}
		else currentOffset = 0;
	}

	@Override
	public void render(Camera camera) {
		// nothing
	}

	@Override
	public boolean remove() {
		return false;
	}

}
