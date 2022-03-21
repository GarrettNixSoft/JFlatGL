package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.core.util.time.Timer;
import org.joml.Vector2f;

public class RestoreOffsetEvent extends GUIEvent {

	private final Timer timer;
	private Vector2f startingOffset;

	public RestoreOffsetEvent(GUIComponent targetComponent, float time) {
		super(false, targetComponent);
		timer = new Timer(time);
	}

	@Override
	public void onStart() {
		assert targetComponent != null;
		startingOffset = targetComponent.getOffset();
		timer.start();
	}

	@Override
	public void update() {
		Vector2f offset = new Vector2f(startingOffset).mul(1 - timer.getProgress());
		assert targetComponent != null;
		targetComponent.setOffsetPosition(offset);
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		assert targetComponent != null;
		targetComponent.resetPosition();
	}

}