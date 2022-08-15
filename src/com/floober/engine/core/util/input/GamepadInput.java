package com.floober.engine.core.util.input;

import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.time.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class GamepadInput {

	// track all controllers connected
	private static final List<Gamepad> connectedGamepads = new ArrayList<>();
	private static final Map<Integer, Gamepad> gamepadMap = new HashMap<>();

	// timer to poll for whether gamepads are still connected
	private static Timer connectionPollTimer;

	// stick dead zones
	private static float innerDeadZone = 0.1f;
	private static float outerDeadZone = 0;

	// ******************************** SETTINGS ********************************
	public static void setInnerDeadZone(float innerDeadZone) {
		GamepadInput.innerDeadZone = innerDeadZone;
	}

	public static void setOuterDeadZone(float outerDeadZone) {
		GamepadInput.outerDeadZone = outerDeadZone;
	}

	// ******************************** SETUP ********************************
	public static void init() {

		// create the poll timer
		connectionPollTimer = new Timer(1); // poll every second
		connectionPollTimer.start();

		// connect all gamepads that are already connected
		for (int jid = GLFW_JOYSTICK_1; jid <= GLFW_JOYSTICK_LAST; jid++) {
			if (glfwJoystickPresent(jid)) {
				connectGamepad(jid);
			}
		}

		// set the joystick callback to detect new connections and disconnections
		glfwSetJoystickCallback((int jid, int event) -> {
			// only react to gamepad connections
			if (glfwJoystickIsGamepad(jid)) {
				if (event == GLFW_CONNECTED) {
					connectGamepad(jid);
				} else if (event == GLFW_DISCONNECTED) {
					disconnectGamepad(jid);
				}
			}
		});

	}

	public static void update() {

		// check whether to poll controllers
		if (connectionPollTimer.finished()) {
			pollControllerConnections();
			connectionPollTimer.restart();
		}

		// TODO update all connected controllers
		for (Gamepad gamepad : connectedGamepads) {
			gamepad.update();
		}

	}

	private static void pollControllerConnections() {

//		Logger.logGamepad("POLLING GAMEPAD CONNECTIONS...");

		for (int i = 0; i < connectedGamepads.size(); i++) {

			Gamepad gamepad = connectedGamepads.get(i);
			int jid = gamepad.getJID();

			if (!glfwJoystickPresent(jid)) {
				disconnectGamepad(jid);
				i--;
			}

		}

	}

	private static void connectGamepad(int jid) {

		Gamepad gamepad = new Gamepad(jid);
		connectedGamepads.add(gamepad);
		gamepadMap.put(jid, gamepad);

		Logger.logGamepadConnection(gamepad, true);

	}

	private static void disconnectGamepad(int jid) {

		Gamepad gamepad = gamepadMap.get(jid);
		gamepad.free();

		gamepadMap.remove(jid);
		connectedGamepads.remove(gamepad);

		Logger.logGamepadConnection(gamepad, false);

	}

	// ******************************** GETTING INPUT VALUES ********************************
	public static boolean isPressed(int gamepadIndex, int button) {

		// return false for invalid index values
		if (gamepadIndex < 0 || gamepadIndex >= connectedGamepads.size()) return false;

		// fetch the button state of the requested gamepad
		return connectedGamepads.get(gamepadIndex).isPressed(button);

	}

	public static boolean isHeld(int gamepadIndex, int button) {

		// return false for invalid index values
		if (gamepadIndex < 0 || gamepadIndex >= connectedGamepads.size()) return false;

		// fetch the button state of the requested gamepad
		return connectedGamepads.get(gamepadIndex).isHeld(button);

	}

	public static float getAxis(int gamepadIndex, int axis) {

		// return false for invalid index values
		if (gamepadIndex < 0 || gamepadIndex >= connectedGamepads.size()) return 0;

		// fetch the axis state of the requested gamepad
		float stick = connectedGamepads.get(gamepadIndex).getAxis(axis);

		// apply dead zones
		if (Math.abs(stick) < innerDeadZone) stick = 0;
		else if (stick > 1 - outerDeadZone) stick = 1 - outerDeadZone;
		else if (stick < -1 + outerDeadZone) stick = -1 + outerDeadZone;

		// return the stick value
		return stick;

	}

}
