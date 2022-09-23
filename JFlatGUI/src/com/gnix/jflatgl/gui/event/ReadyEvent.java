package com.gnix.jflatgl.gui.event;

import com.gnix.jflatgl.gui.component.GUIComponent;

public class ReadyEvent extends GUIEvent {

	public ReadyEvent(GUIComponent targetComponent) {
		super(false, targetComponent);
	}

	@Override
	public void onStart() {
		assert targetComponent != null;
		targetComponent.setActive(true);
		complete = true;
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
