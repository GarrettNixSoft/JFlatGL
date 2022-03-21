package com.floober.engine.gui;

import com.floober.engine.gui.event.GUIEvent;
import com.floober.engine.core.util.data.Queue;

public class GUIEventsHandler {

	private static final Queue<GUIEvent> events = new Queue<>();

	public static void sendEvent(GUIEvent event) {
		events.push(event);
	}

	public static void handleEvents() {
		while (!events.isEmpty()) {
			GUIEvent event = events.poll();
			// handle the event
			//
		}
	}

}