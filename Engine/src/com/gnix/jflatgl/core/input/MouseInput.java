package com.gnix.jflatgl.core.input;

import com.gnix.jflatgl.core.renderEngine.display.Window;
import com.gnix.jflatgl.core.renderEngine.renderers.MasterRenderer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

	// convenience aliases
	public static final int NUM_BUTTONS = 10;
	public static final int BUTTON_1 = GLFW_MOUSE_BUTTON_1;
	public static final int BUTTON_2 = GLFW_MOUSE_BUTTON_2;
	public static final int BUTTON_3 = GLFW_MOUSE_BUTTON_3;
	public static final int BUTTON_4 = GLFW_MOUSE_BUTTON_4;
	public static final int BUTTON_5 = GLFW_MOUSE_BUTTON_5;
	public static final int BUTTON_6 = GLFW_MOUSE_BUTTON_6;
	public static final int BUTTON_7 = GLFW_MOUSE_BUTTON_7;
	public static final int BUTTON_8 = GLFW_MOUSE_BUTTON_8;
	public static final int WHEEL_UP = BUTTON_8 + 1;
	public static final int WHEEL_DOWN = BUTTON_8 + 2;
	public static final int AXIS_X = BUTTON_8 + 3;
	public static final int AXIS_Y = BUTTON_8 + 4;

	// most used
	public static final int LEFT = GLFW_MOUSE_BUTTON_LEFT;
	public static final int RIGHT = GLFW_MOUSE_BUTTON_RIGHT;

	public static boolean[] buttonState = new boolean[NUM_BUTTONS];
	public static boolean[] prevButtonState = new boolean[NUM_BUTTONS];

	public static double WHEEL;
	public static boolean WHEEL_MOVED_UP;
	public static boolean WHEEL_MOVED_DOWN;

	// determining input types

	/**
	 * Determine whether a given input value corresponds to a mouse axis.
	 * <br>
	 * Note: Moving the mouse wheel up or down is considered a button input as it is
	 * essentially tracked as a boolean value.
	 * @param input the input to check
	 * @return {@code true} if the given value corresponds to a mouse axis
	 */
	public static boolean isAxis(int input) {
		return input == AXIS_X || input == AXIS_Y;
	}

	/**
	 * Determine whether a given input value corresponds to a mouse button.
	 * <br>
	 * Note: Moving the mouse wheel up or down is considered a button input as it is
	 * essentially tracked as a boolean value.
	 * @param input the input to check
	 * @return {@code true} if the given value corresponds to a mouse button
	 */
	public static boolean isButton(int input) {
		return	input == BUTTON_1 ||
				input == BUTTON_2 ||
				input == BUTTON_3 ||
				input == BUTTON_4 ||
				input == BUTTON_5 ||
				input == BUTTON_6 ||
				input == BUTTON_7 ||
				input == BUTTON_8 ||
				input == WHEEL_UP ||
				input == WHEEL_DOWN;
	}
	
	public static void update() {
		// process mouse events on the target window
		System.arraycopy(buttonState, 0, prevButtonState, 0, NUM_BUTTONS);
		Window currentWindow = MasterRenderer.getTargetWindow();
		// update button states
		for (int i = GLFW_MOUSE_BUTTON_1; i <= GLFW_MOUSE_BUTTON_LAST; i++) {
			buttonState[i] = glfwGetMouseButton(currentWindow.getWindowID(), i) == GLFW_PRESS;
		}
		// wheel state
		WHEEL_MOVED_UP = WHEEL > 0;
		WHEEL_MOVED_DOWN = WHEEL < 0;
		WHEEL = 0;
		// wheel state as buttons
		buttonState[WHEEL_UP] = WHEEL_MOVED_UP;
		buttonState[WHEEL_DOWN] = WHEEL_MOVED_DOWN;
	}
	
	public static boolean isHeld(int button) {
		return buttonState[button];
	}
	public static boolean isPressed(int button) {
		return 	buttonState[button] &&
				!prevButtonState[button];
	}
	public static boolean leftClick() {
		return 	buttonState[LEFT] &&
				!prevButtonState[LEFT];
	}
	public static boolean rightClick() {
		return 	buttonState[RIGHT] &&
				!prevButtonState[RIGHT];
	}

	public static boolean wheelMoved() {
		return wheelUp() || wheelDown();
	}

	public static boolean wheelUp() {
		return WHEEL_MOVED_UP;
	}

	public static boolean wheelDown() {
		return WHEEL_MOVED_DOWN;
	}

	public static float getAxis(int axis) {
		if (axis == MouseInput.AXIS_X) return Cursor.getDX();
		else if (axis == MouseInput.AXIS_Y) return Cursor.getDY();
		else return 0;
	}

	public static boolean newMotionOnAxis(int axis) {
		if (axis == MouseInput.AXIS_X) return Cursor.getDX() != 0 && Cursor.getPrevDX() == 0;
		else if (axis == MouseInput.AXIS_Y) return Cursor.getDY() != 0 && Cursor.getPrevDY() == 0;
		else return false;
	}

	public static boolean anyButtonHeld() {
		for (boolean state : buttonState) {
			if (state) return true;
		}
		return false;
	}

	public static boolean anyInput() {
		return wheelMoved() || anyButtonHeld();
	}

}
