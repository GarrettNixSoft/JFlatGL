package com.floober.engine.core.input;

import com.floober.engine.core.util.Logger;
import org.lwjgl.glfw.GLFWGamepadState;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Gamepad {

	// convenience aliases
	public static final int AXIS_LEFT_X = GLFW_GAMEPAD_AXIS_LEFT_X;
	public static final int AXIS_LEFT_Y = GLFW_GAMEPAD_AXIS_LEFT_Y;
	public static final int AXIS_RIGHT_X = GLFW_GAMEPAD_AXIS_RIGHT_X;
	public static final int AXIS_RIGHT_Y = GLFW_GAMEPAD_AXIS_RIGHT_Y;
	public static final int AXIS_TRIGGER_LEFT = GLFW_GAMEPAD_AXIS_LEFT_TRIGGER;
	public static final int AXIS_TRIGGER_RIGHT = GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER;

	public static final int BUTTON_X = GLFW_GAMEPAD_BUTTON_X;
	public static final int BUTTON_Y = GLFW_GAMEPAD_BUTTON_Y;
	public static final int BUTTON_A = GLFW_GAMEPAD_BUTTON_A;
	public static final int BUTTON_B = GLFW_GAMEPAD_BUTTON_B;
	public static final int BUTTON_BACK = GLFW_GAMEPAD_BUTTON_BACK;
	public static final int BUTTON_START = GLFW_GAMEPAD_BUTTON_START;
	public static final int BUTTON_GUIDE = GLFW_GAMEPAD_BUTTON_GUIDE;
	public static final int BUMPER_LEFT = GLFW_GAMEPAD_BUTTON_LEFT_BUMPER;
	public static final int BUMPER_RIGHT = GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER;
	public static final int THUMB_LEFT = GLFW_GAMEPAD_BUTTON_LEFT_THUMB;
	public static final int THUMB_RIGHT = GLFW_GAMEPAD_BUTTON_RIGHT_THUMB;

	public static final int[] AXIS_IDS = {
			GLFW_GAMEPAD_AXIS_LEFT_X,
			GLFW_GAMEPAD_AXIS_LEFT_Y,
			GLFW_GAMEPAD_AXIS_RIGHT_X,
			GLFW_GAMEPAD_AXIS_RIGHT_Y,
			GLFW_GAMEPAD_AXIS_LEFT_TRIGGER,
			GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER
	};

	public static final int[] BUTTON_IDS = {
			GLFW_GAMEPAD_BUTTON_X,
			GLFW_GAMEPAD_BUTTON_Y,
			GLFW_GAMEPAD_BUTTON_A,
			GLFW_GAMEPAD_BUTTON_B,
			GLFW_GAMEPAD_BUTTON_BACK,
			GLFW_GAMEPAD_BUTTON_START,
			GLFW_GAMEPAD_BUTTON_GUIDE,
			GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,
			GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,
			GLFW_GAMEPAD_BUTTON_LEFT_THUMB,
			GLFW_GAMEPAD_BUTTON_RIGHT_THUMB
	};

	public static final String[] AXIS_NAMES = {
			"Left Stick X",
			"Left Stick Y",
			"Right Stick X",
			"Right Stick Y",
			"Left Trigger",
			"Right Trigger"
	};

	public static final String[] BUTTON_NAMES = {
			"X",
			"Y",
			"A",
			"B",
			"Back",
			"Start",
			"Guide",
			"Left Bumper",
			"Right Bumper",
			"Left Thumb",
			"Right Thumb"
	};

	// check what type of input a given binding is
	public static boolean isAxis(String input) {
		return GamepadNames.AXIS_NAMES.contains(input);
	}

	public static boolean isDirectional(String input) {
		return GamepadNames.AXIS_DIRECTION_NAMES.contains(input);
	}

	public static boolean isButton(String input) {
		return GamepadNames.BUTTON_NAMES.contains(input);
	}

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
		return prevButtons[button] != GLFW_PRESS && isHeld(button);
	}

	public float getAxis(int axis) {
		return axes[axis];
	}

	public boolean axisNewInput(int axis) {

		if (GamepadInput.deadZoneApplies(axis)) {

			float deadZone = GamepadInput.getInnerDeadZone();
			return Math.abs(prevAxes[axis]) <= deadZone && Math.abs(axes[axis]) > deadZone;

		}
		else {

			float restValue = -1.0f;
			return prevAxes[axis] == restValue && getAxis(axis) != restValue;

		}

	}

	public boolean anyInput() {

		// true if any button is pressed this frame
		for (int button : BUTTON_IDS) {
			if (isHeld(button)) {
				Logger.logGamepad("Button press: " + BUTTON_NAMES[button]);
				return true;
			}
		}

		// true if any axis is not at rest this frame

		// triggers rest at -1.0
		if (getAxis(AXIS_TRIGGER_LEFT) > -1) return true;
		if (getAxis(AXIS_TRIGGER_RIGHT) > -1) return true;

		// sticks rest at [ -innerDeadZone, innerDeadZone ]
		float deadZone = GamepadInput.getInnerDeadZone();

		if (Math.abs(getAxis(AXIS_LEFT_X)) > deadZone) return true;
		if (Math.abs(getAxis(AXIS_LEFT_Y)) > deadZone) return true;
		if (Math.abs(getAxis(AXIS_RIGHT_X)) > deadZone) return true;
		if (Math.abs(getAxis(AXIS_RIGHT_Y)) > deadZone) return true;


		// false if none of the above
		return false;

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
