package com.floober.engine.entity.effects.entity;

import com.floober.engine.entity.effects.entity.EntityEffect;
import com.floober.engine.util.math.RandomUtil;

public class ShakeEffect extends EntityEffect {

	private final int shakeDelay;
	private int shakeSeverity;
	private int currentOffset;

	public ShakeEffect() {
		super(null); // no animation
		timer = System.nanoTime();
		shakeDelay = 20;
		shakeSeverity = 6;
	}

	// getters
	public int getOffset() { return currentOffset; }

	// setters
	public void setShakeSeverity(int shakeSeverity) {
		this.shakeSeverity = shakeSeverity;
	}

	private void shake() {
		currentOffset = RandomUtil.getInt(-shakeSeverity, shakeSeverity);
	}

	@Override
	public void update() {
		if (synchronize)
			checkSync();
		if (active) {
			long elapsed = (System.nanoTime() - timer) / 1000000;
			if (elapsed > shakeDelay) {
				shake();
				timer = System.nanoTime();
			}
		}
		else currentOffset = 0;
	}

	@Override
	public void render() {
		// nothing
	}

	@Override
	public boolean remove() {
		return false;
	}

}
