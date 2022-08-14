package com.floober.engine.core.util.input;

import org.lwjgl.glfw.GLFWGamepadState;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickGUID;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;

public class Gamepad {

	// identifiers
	private final String name;
	private final String guid;
	private final int jid;

	// button, axis states
	private final GLFWGamepadState gamepadState;

	public Gamepad(int jid) {
		this.jid = jid;
		this.name = glfwGetJoystickName(jid);
		this.guid = glfwGetJoystickGUID(jid);
		this.gamepadState = GLFWGamepadState.malloc();
	}

	// ******************************** GETTERS ********************************
	public String getName() {
		return name;
	}

	public String getGUID() {
		return guid;
	}

	public int getJID() {
		return jid;
	}

	// ******************************** METHODS ********************************
	public void update() {
		// TODO
	}

	public void free() {
		gamepadState.free();
	}

}
