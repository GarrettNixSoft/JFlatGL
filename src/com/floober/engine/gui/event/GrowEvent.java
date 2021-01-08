package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.util.Logger;
import com.floober.engine.util.time.Timer;

/**
 * The GrowEvent will change its target component's
 * scale over time by a given amount. A negative
 * growth value will shrink the component's scale.
 */
public class GrowEvent extends GUIEvent {

	private final Timer timer;
	private final float amount;

	public GrowEvent(GUIComponent targetComponent, float amount, float time) {
		super(false, targetComponent);
		this.amount = amount;
		timer = new Timer(time);
	}

	public float getProgress() {
		return timer.getProgress();
	}

	@Override
	public void onStart() {
		timer.start();
	}

	@Override
	public void update() {
		assert targetComponent != null;
		targetComponent.setScale(1 + amount * timer.getProgress());
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		// nothing
	}
}