package com.gnix.jflatgl.gui;

import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.gui.component.GUIComponent;

import java.util.HashMap;
import java.util.Optional;

public class GUIManager {

	public static final HashMap<String, GUI> GUI_BANK = new HashMap<>();

	private static GUI activeGUI;
	private static GUI nextGUI;

	/**
	 * Search for a component in the current active GUI.
	 * @param componentID the ID of the component to search for
	 * @return an Optional containing the component, if found.
	 */
	public static Optional<GUIComponent> searchComponentByID(String componentID) {
		GUIComponent component = activeGUI.getComponentByID(componentID);
		return Optional.ofNullable(component);
	}

	public static void setActiveGUI(GUI gui) {
		activeGUI = gui;
		activeGUI.open();
	}

	public static void queueNextGUI(GUI gui) {
		nextGUI = gui;
	}

	public static void closeGUI() {
		// can't close an active GUI if there isn't one
		if (activeGUI == null) return;
		Logger.log("CLOSING GUI ************************************************************************");
		activeGUI.close();
	}

	public static void forceCloseGUI() {
		// can't close an active GUI if there isn't one
		if (activeGUI == null) return;
		GUI_BANK.remove(activeGUI.getId());
		activeGUI.forceClose();
		activeGUI = null;
	}

	public static void update() {
		GUIEventsHandler.handleEvents(); // handle events from last frame
		// only update GUI if one exists
		if (activeGUI != null) {
			String guiID = activeGUI.getId();
			activeGUI.update();
			if (activeGUI == null) {
				Logger.log("GUI " + guiID + " CLOSED!");
				return;
			}
			// if the current GUI is closed,
			if (activeGUI.isClosed()) {
				Logger.log("GUI " + guiID + " CLOSED!");
				// advance to the next if one exists
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
