package com.gnix.jflatgl.gui.event;

import com.gnix.jflatgl.gui.component.GUIComponent;

public class ClosedEvent extends GUIEvent {

	public ClosedEvent(GUIComponent targetComponent) {
		super(false, targetComponent);
		setMustWait(true);
	}

	@Override
	public void onStart() {
		// nothing
	}

	@Override
	public void update() {
		assert targetComponent != null;
		if (!targetComponent.hasPendingEvents(this)) {
			targetComponent.setActive(false);
			complete = true;
		}
	}

	@Override
	public void onFinish() {
		// nothing
	}
}
