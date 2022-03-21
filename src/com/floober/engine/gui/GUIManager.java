package com.floober.engine.gui;

import com.floober.engine.core.util.Logger;

public class GUIManager {

	private static GUI activeGUI;
	private static GUI nextGUI;

	public static void setActiveGUI(GUI gui) {
		activeGUI = gui;
		activeGUI.open();
	}

	public static void queueNextGUI(GUI gui) {
		nextGUI = gui;
	}

	public static void closeGUI() {
		Logger.log("CLOSING GUI ************************************************************************");
		activeGUI.close();
	}

	public static void update() {
		GUIEventsHandler.handleEvents(); // handle events from last frame
		// only update GUI if one exists
		if (activeGUI != null) {
			activeGUI.update();
			if (activeGUI.isClosed()) {
				if (nextGUI != null) {
					activeGUI = nextGUI;
					nextGUI = null;
				}
				else {
					activeGUI = null;
				}
			}
		}

	}

	public static void render() {
		if (activeGUI != null) {
			activeGUI.doTransform();
			activeGUI.render();
		}
	}

}