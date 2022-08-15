package com.floober.engine.core.util.input;

import org.lwjgl.glfw.GLFWGamepadState;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Gamepad {

	// identifiers
	private final String name;
	private final String guid;
	private final int jid;
	private final int index;

	// button, axis states
	private final GLFWGamepadState gamepadState;
	private final GLFWGamepadState prevState;

	private byte[] buttons = new byte[100];
	private float[] axes = new float[100];

	private byte[] prevButtons = new byte[100];
	private float[] prevAxes = new float[100];

	public Gamepad(int jid, int index) {
		this.jid = jid;
		this.index = index;
		this.name = glfwGetJoystickName(jid);
		this.guid = glfwGetJoystickGUID(jid);
		this.gamepadState = GLFWGamepadState.malloc();
		this.prevState = GLFWGamepadState.malloc();
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

	public int getIndex() {
		return index;
	}

	protected GLFWGamepadState getGamepadState() {
		return gamepadState;
	}

	public boolean isHeld(int button) {
		return buttons[button] == GLFW_PRESS;
	}

	public boolean isPressed(int button) {
		return prevButtons[button] == GLFW_PRESS && isHeld(button);
	}

	public float getAxis(int axis) {
		return axes[axis];
	}

	// ******************************** METHODS ********************************
	public void update() {

		// the previous state is now whatever the current state was
		prevState.set(gamepadState);
		prevButtons = buttons;
		prevAxes = axes;

		// ask GLFW what's going on with this gamepad
		glfwGetGamepadState(jid, gamepadState);

		ByteBuffer buttonBytes = gamepadState.buttons();
		FloatBuffer axisBytes = gamepadState.axes();

		buttons = new byte[buttonBytes.capacity()];
		buttonBytes.get(buttons);

		axes = new float[axisBytes.capacity()];
		axisBytes.get(axes);

	}

	public void free() {
		gamepadState.free();
	}

}
