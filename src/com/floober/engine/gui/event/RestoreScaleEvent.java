package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.core.util.time.Timer;

/**
 * The RestoreScaleEvent will revert its target component's
 * scale back to 1 over a period of time, regardless of what
 * the component's current scale is.
 */
public class RestoreScaleEvent extends GUIEvent {

	private final Timer timer;
	private float startingScale;

	public RestoreScaleEvent(GUIComponent targetComponent, float time) {
		super(false, targetComponent);
		timer = new Timer(time);
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
		float delta = startingScale - 1;
		targetComponent.setScale(1 + delta * (1 - timer.getProgress()));
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		assert targetComponent != null;
		targetComponent.setScale(1); // ensure scale is always exactly 1 when finished
	}
}
