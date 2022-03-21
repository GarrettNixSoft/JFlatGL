package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.core.util.time.Timer;

/**
 * The ScaleEvent will change its target component's
 * scale over time by a given amount. A negative
 * growth value will shrink the component's scale.
 */
public class ScaleEvent extends GUIEvent {

	private final Timer timer;
	private final float amount;
	private float startingScale;

	public ScaleEvent(GUIComponent targetComponent, float amount, float time) {
		super(false, targetComponent);
		this.amount = amount;
		timer = new Timer(time);
	}

	public float getProgress() {
		return timer.getProgress();
	}

	@Override
	public void onStart() {
		assert targetComponent != null;
		startingScale = targetComponent.getScale();
		timer.start();
	}

	@Override
	public void update() {
		assert targetComponent != null;
		targetComponent.setScale(startingScale + amount * timer.getProgress());
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		// nothing
	}
}