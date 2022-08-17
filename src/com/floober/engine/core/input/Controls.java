package com.floober.engine.core.input;

import com.floober.engine.core.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controls {

	// ******************************** INPUT MODES ********************************
	public enum InputMode {
		KEYBOARD_MOUSE, GAMEPAD
	}

	private static InputMode INPUT_MODE = InputMode.KEYBOARD_MOUSE;

	public static InputMode getCurrentInputMode() {
		return INPUT_MODE;
	}

	// ******************************** CONTROL CONTEXTS ********************************
	private static final Map<String, ControlContext> controlContexts = new HashMap<>();
	private static String currentContext;

	public static Map<String, ControlContext> getControls() {
		return new HashMap<>(controlContexts);
	}

	public static String getCurrentContext() {
		return currentContext;
	}

	public static void setCurrentContext(String context) {
		currentContext = context;
	}

	// ******************************** LOADING ********************************
	public static void init() {
		controlContexts.putAll(ControlSchemeParser.loadControls());
		Logger.logLoadControls("Controls loaded! Total control contexts loaded: " + controlContexts.size());
		// this is the dumbest thing I've ever done
		for (String context : controlContexts.keySet()) {
			currentContext = context;
			break;
		}
	}

	// ******************************** AUTO-MODE SWITCHING ********************************
	public static void update() {

		if (INPUT_MODE == InputMode.KEYBOARD_MOUSE) {
			if (GamepadInput.anyInput()) {
				INPUT_MODE = InputMode.GAMEPAD;

				Logger.log("INPUT MODE SWITCHED: Gamepad");
			}
		}
		else if (INPUT_MODE == InputMode.GAMEPAD) {
			if (KeyInput.KEY_PRESSED || MouseInput.anyInput()) {
				INPUT_MODE = InputMode.KEYBOARD_MOUSE;

				Logger.log("INPUT MODE SWITCHED: Keyboard/Mouse");
			}
		}

	}

	// ******************************** CHECKING INPUTS ********************************
	public static boolean inputPresent(String control, int player) {

		// fetch the current context
		ControlContext context = controlContexts.get(currentContext);

		// if the requested control is valid for this context, check it
		if (context.hasControl(control)) {

			Keybind keybind = context.getKeybindForControl(control);
			return keybind.inputPresent(player);

		}
		// otherwise, this input cannot be present
		else return false;

	}

}
