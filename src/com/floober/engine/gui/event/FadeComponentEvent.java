package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.core.util.time.Timer;

public class FadeComponentEvent extends GUIEvent {

	private final Timer timer;
	private final float targetOpacity;
	private float startOpacity;

	public FadeComponentEvent(GUIComponent targetComponent, float targetOpacity, float time) {
		super(false, targetComponent);
		this.targetOpacity = targetOpacity;
		timer = new Timer(time);
	}

	@Override
	public void onStart() {
		assert targetComponent != null;
		startOpacity = targetComponent.getOpacity();
		timer.start();
	}

	@Override
	public void update() {
		float delta = targetOpacity - startOpacity;
		assert targetComponent != null;
		targetComponent.setOpacity(startOpacity + delta * timer.getProgress());
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		assert targetComponent != null;
		targetComponent.setOpacity(targetOpacity);
	}
}
