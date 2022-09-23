package com.gnix.jflatgl.gui.event;

import com.gnix.jflatgl.gui.GUIAction;

public class PerformActionEvent extends GUIEvent {

	private final GUIAction action;

	public PerformActionEvent(GUIAction action) {
		super(false);
		this.action = action;
	}

	@Override
	public void onStart() {
		action.onTrigger();
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
