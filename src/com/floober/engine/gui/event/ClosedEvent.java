package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.util.Logger;

public class ClosedEvent extends GUIEvent {

	public ClosedEvent(GUIComponent targetComponent) {
		super(false, targetComponent);
		setMustWait(true);
	}

	@Override
	public void onStart() {
		// nothing
		Logger.log("ClosedEvent targeting component " + targetComponent.getComponentID() + " has been started");
	}

	@Override
	public void update() {
		assert targetComponent != null;
		if (!targetComponent.hasPendingEvents()) {
			targetComponent.setActive(false);
			complete = true;
			Logger.log("Component " + targetComponent.getComponentID() + " close complete triggered via " + this);
		}
		else Logger.log("Cannot close, events still being processed");
	}

	@Override
	public void onFinish() {
		// nothing
	}
}
