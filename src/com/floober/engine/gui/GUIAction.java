package com.floober.engine.gui;

import com.floober.engine.gui.event.GUIEvent;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GUIAction {

	public interface Action {
		void onTrigger();
	}

	private final List<Action> actions = new ArrayList<>();
	private final List<GUIEvent> events = new ArrayList<>();

	/**
	 * Add an action to this GUIAction.
	 * @param action the action to be performed
	 */
	public GUIAction addPerformActionOnTrigger(Action action) {
		actions.add(action);
		return this;
	}

	public GUIAction addSendEventOnTrigger(GUIEvent event) {
		events.add(event);
		return this;
	}

	/**
	 * Trigger all actions and events associated with this GUIAction.
	 */
	public void trigger() {
//		Logger.log("Triggered (timestamp = " + System.nanoTime() + ")");
		// perform all actions
		for (Action action : actions) {
			action.onTrigger();
		}
		// send all events
		for (GUIEvent event : events) {
			GUIEventsHandler.sendEvent(event);
		}
	}

}