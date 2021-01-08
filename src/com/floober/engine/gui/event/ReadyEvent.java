package com.floober.engine.gui.event;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.util.Logger;

public class ReadyEvent extends GUIEvent {

	public ReadyEvent(GUIComponent targetComponent) {
		super(false, targetComponent);
	}

	@Override
	public void onStart() {
		assert targetComponent != null;
		targetComponent.setReady();
		Logger.log("Component " + targetComponent + " has been set to ready");
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
