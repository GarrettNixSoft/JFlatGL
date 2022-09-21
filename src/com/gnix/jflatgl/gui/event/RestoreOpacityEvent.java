package com.gnix.jflatgl.gui.event;

import com.gnix.jflatgl.gui.component.GUIComponent;
import com.gnix.jflatgl.core.util.time.Timer;

public class RestoreOpacityEvent extends GUIEvent {

	private final Timer timer;
	private float startingOpacity;

	public RestoreOpacityEvent(GUIComponent targetComponent, float time) {
		super(false, targetComponent);
		timer = new Timer(time);
	}

	@Override
	public void onStart() {
		assert targetComponent != null;
		startingOpacity = targetComponent.getOpacity();
		timer.start();
	}

	@Override
	public void update() {
		float delta = 1 - startingOpacity;
		assert targetComponent != null;
		targetComponent.setOpacity(1 + delta * timer.getProgress());
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		assert targetComponent != null;
		targetComponent.setOpacity(1);
	}
}
