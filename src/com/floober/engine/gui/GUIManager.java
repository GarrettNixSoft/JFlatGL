package com.floober.engine.gui;

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
		activeGUI.close();
	}

	public static void update() {
		GUIEventsHandler.handleEvents(); // handle events from last frame
		// TODO check for GUI close done, switch to next GUI
		activeGUI.update();
	}

	public static void render() {
		activeGUI.doTransform();
		activeGUI.render();
	}

}