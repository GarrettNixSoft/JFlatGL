package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.core.util.time.Timer;
import org.joml.Vector2f;

public class OffsetPositionEvent extends GUIEvent {

	private final Timer timer;
	private final Vector2f offset;

	public OffsetPositionEvent(GUIComponent targetComponent, Vector2f offset, float time) {
		super(false, targetComponent);
		this.offset = offset;
		this.timer = new Timer(time);
	}

	@Override
	public void onStart() {
		timer.start();
	}

	@Override
	public void update() {
		Vector2f currentOffset = new Vector2f(offset).mul(timer.getProgress());
		assert targetComponent != null;
		targetComponent.setOffsetPosition(currentOffset);
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		assert targetComponent != null;
		targetComponent.setOffsetPosition(offset);
	}

}