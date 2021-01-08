package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;

public class ClosedEvent extends GUIEvent {

	public ClosedEvent(GUIComponent targetComponent) {
		super(false, targetComponent);
	}

	@Override
	public void onStart() {
		assert targetComponent != null;
		targetComponent.setClosed();
	}

	@Override
	public void update() {
		// nothing
	}

	@Override
	public void onFinish() {
		// nothing
	}
}
