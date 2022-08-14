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

}
