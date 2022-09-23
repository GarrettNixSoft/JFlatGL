package com.gnix.jflatgl.core.input;

import java.util.HashMap;
import java.util.Map;

/**
 * MouseNames contains an array with String names for every mouse constant defined in MouseInput.
 *
 * @author Floober
 */
public class MouseNames {

	protected static final String[] MOUSE_NAMES = new String[MouseInput.NUM_BUTTONS + 4]; // add wheel up and down and X/Y axes

	protected static final Map<String, Integer> MOUSE_VALUES = new HashMap<>();

	static {
		// ASSIGN NAMES
		// buttons
		MOUSE_NAMES[MouseInput.BUTTON_1] = "mouse_left";
		MOUSE_NAMES[MouseInput.BUTTON_2] = "mouse_right";
		MOUSE_NAMES[MouseInput.BUTTON_3] = "button_3";
		MOUSE_NAMES[MouseInput.BUTTON_4] = "button_4";
		MOUSE_NAMES[MouseInput.BUTTON_5] = "button_5";
		MOUSE_NAMES[MouseInput.BUTTON_6] = "button_6";
		MOUSE_NAMES[MouseInput.BUTTON_7] = "button_7";
		MOUSE_NAMES[MouseInput.BUTTON_8] = "button_8";
		MOUSE_NAMES[MouseInput.WHEEL_UP] = "mouse_wheel_up";
		MOUSE_NAMES[MouseInput.WHEEL_DOWN] = "mouse_wheel_down";
		// axes
		MOUSE_NAMES[MouseInput.AXIS_X] = "mouse_axis_x";
		MOUSE_NAMES[MouseInput.AXIS_Y] = "mouse_axis_y";
		// ASSIGN VALUES
		// buttons
		MOUSE_VALUES.put("mouse_left", MouseInput.BUTTON_1);
		MOUSE_VALUES.put("mouse_right", MouseInput.BUTTON_2);
		MOUSE_VALUES.put("button_3", MouseInput.BUTTON_3);
		MOUSE_VALUES.put("button_4", MouseInput.BUTTON_4);
		MOUSE_VALUES.put("button_5", MouseInput.BUTTON_5);
		MOUSE_VALUES.put("button_6", MouseInput.BUTTON_6);
		MOUSE_VALUES.put("button_7", MouseInput.BUTTON_7);
		MOUSE_VALUES.put("button_8", MouseInput.BUTTON_8);
		MOUSE_VALUES.put("mouse_wheel_up", MouseInput.WHEEL_UP);
		MOUSE_VALUES.put("mouse_wheel_down", MouseInput.WHEEL_DOWN);
		// axes
		MOUSE_VALUES.put("mouse_axis_x", MouseInput.AXIS_X);
		MOUSE_VALUES.put("mouse_axis_y", MouseInput.AXIS_Y);
	}

}
