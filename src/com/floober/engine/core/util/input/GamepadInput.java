package com.floober.engine.core.util.input;

import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.time.Timer;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class GamepadInput {

	// track all controllers connected
	private static final Gamepad[] connectedGamepads = new Gamepad[GLFW_JOYSTICK_LAST];
	private static final Map<Integer, Gamepad> gamepadMap = new HashMap<>();

	// timer to poll for whether gamepads are still connected
	private static Timer connectionPollTimer;

	// stick dead zones
	private static float innerDeadZone = 0.08f;
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
			if (gamepad != null) gamepad.update();
		}

	}

	private static void pollControllerConnections() {

//		Logger.logGamepad("POLLING GAMEPAD CONNECTIONS...");

		for (int i = 0; i < connectedGamepads.length; i++) {

			Gamepad gamepad = connectedGamepads[i];
			if (gamepad == null) continue;

			int jid = gamepad.getJID();

			if (!glfwJoystickPresent(jid)) {
				disconnectGamepad(jid);
				i--;
			}

		}

	}

	private static void connectGamepad(int jid) {

		// find first available gamepad slot
		int index = 0;
		while (index < connectedGamepads.length && connectedGamepads[index] != null) {
			index++;
		}

		// if no slots are available, log it
		if (index == connectedGamepads.length) {
			Logger.logGamepad("Could not connect gamepad (" + glfwGetGamepadName(jid) + "), max gamepad connections reached");
			return;
		}

		Gamepad gamepad = new Gamepad(jid, index);
		connectedGamepads[index] = gamepad;
		gamepadMap.put(jid, gamepad);

		Logger.logGamepadConnection(gamepad, true);

	}

	private static void disconnectGamepad(int jid) {

		Gamepad gamepad = gamepadMap.get(jid);
		gamepad.free();

		gamepadMap.remove(jid);
		connectedGamepads[gamepad.getIndex()] = null;

		Logger.logGamepadConnection(gamepad, false);

	}

	public static void cleanUp() {

		for (Gamepad gamepad : connectedGamepads) {
			if (gamepad != null) gamepad.free();
		}
	}

	// ******************************** CHECKING STATUS ********************************
	public static boolean isConnected(int gamepadIndex) {
		return connectedGamepads[gamepadIndex] != null;
	}

	// ******************************** GETTING INPUT VALUES ********************************
	public static boolean isPressed(int gamepadIndex, int button) {

		// return false for invalid index values
		if (gamepadIndex < 0 || gamepadIndex >= connectedGamepads.length || connectedGamepads[gamepadIndex] == null) return false;

		// fetch the button state of the requested gamepad
		return connectedGamepads[gamepadIndex].isPressed(button);

	}

	public static boolean isHeld(int gamepadIndex, int button) {

		// return false for invalid index values
		if (gamepadIndex < 0 || gamepadIndex >= connectedGamepads.length || connectedGamepads[gamepadIndex] == null) return false;

		// fetch the button state of the requested gamepad
		return connectedGamepads[gamepadIndex].isHeld(button);

	}

	public static float getAxis(int gamepadIndex, int axis) {

		// return false for invalid index values
		if (gamepadIndex < 0 || gamepadIndex >= connectedGamepads.length || connectedGamepads[gamepadIndex] == null) return 0;

		// fetch the axis state of the requested gamepad
		float stick = connectedGamepads[gamepadIndex].getAxis(axis);

		// apply dead zones
		if (deadZoneApplies(axis)) {
			if (Math.abs(stick) < innerDeadZone) stick = 0;
			else if (stick > 1 - outerDeadZone) stick = 1 - outerDeadZone;
			else if (stick < -1 + outerDeadZone) stick = -1 + outerDeadZone;
		}

		// return the stick value
		return stick;

	}

	private static boolean deadZoneApplies(int axis) {

		return	axis == GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER ||
				axis == GLFW_GAMEPAD_AXIS_LEFT_TRIGGER ||
				axis == GLFW_GAMEPAD_AXIS_LEFT_X ||
				axis == GLFW_GAMEPAD_AXIS_LEFT_Y ||
				axis == GLFW_GAMEPAD_AXIS_RIGHT_X ||
				axis == GLFW_GAMEPAD_AXIS_RIGHT_Y;

	}

}
