package com.floober.engine.core.input;

import com.floober.engine.core.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_AXIS_LAST;

/**
 * GamepadNames contains an array with String names for every gamepad constant defined in Gamepad.
 *
 * @author Floober
 */
public class GamepadNames {

	protected static final String[] GAMEPAD_NAMES = new String[Gamepad.AXIS_NAMES.length + Gamepad.BUTTON_NAMES.length];
	private static final int BUTTON_START = GLFW_GAMEPAD_AXIS_LAST + 1;

	protected static final Map<String, Integer> GAMEPAD_VALUES = new HashMap<>();

	protected static final List<String> AXIS_NAMES = new ArrayList<>();
	protected static final List<String> BUTTON_NAMES = new ArrayList<>();

	static {
		// DIFFERENTIATE
		// axes
		AXIS_NAMES.add("left_stick_x");
		AXIS_NAMES.add("left_stick_y");
		AXIS_NAMES.add("right_stick_x");
		AXIS_NAMES.add("right_stick_y");
		AXIS_NAMES.add("left_trigger");
		AXIS_NAMES.add("right_trigger");
		// buttons
		BUTTON_NAMES.add("x");
		BUTTON_NAMES.add("y");
		BUTTON_NAMES.add("a");
		BUTTON_NAMES.add("b");
		BUTTON_NAMES.add("back");
		BUTTON_NAMES.add("start");
		BUTTON_NAMES.add("guide");
		BUTTON_NAMES.add("left_bumper");
		BUTTON_NAMES.add("right_bumper");
		BUTTON_NAMES.add("left_thumb");
		BUTTON_NAMES.add("right_thumb");
		// ASSIGN NAMES
		// axes
		GAMEPAD_NAMES[Gamepad.AXIS_LEFT_X] 			= "left_stick_x";
		GAMEPAD_NAMES[Gamepad.AXIS_LEFT_Y] 			= "left_stick_y";
		GAMEPAD_NAMES[Gamepad.AXIS_RIGHT_X] 		= "right_stick_x";
		GAMEPAD_NAMES[Gamepad.AXIS_RIGHT_Y] 		= "right_stick_y";
		GAMEPAD_NAMES[Gamepad.AXIS_TRIGGER_LEFT] 	= "left_trigger";
		GAMEPAD_NAMES[Gamepad.AXIS_TRIGGER_RIGHT] 	= "right_trigger";
		// buttons
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUTTON_X] 		= "x";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUTTON_Y] 		= "y";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUTTON_A] 		= "a";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUTTON_B] 		= "b";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUTTON_BACK] 	= "back";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUTTON_START] 	= "start";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUTTON_GUIDE] 	= "guide";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUMPER_LEFT] 	= "left_bumper";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.BUMPER_RIGHT] 	= "right_bumper";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.THUMB_LEFT]	= "left_thumb";
		GAMEPAD_NAMES[BUTTON_START + Gamepad.THUMB_RIGHT]	= "right_thumb";
		// ASSIGN VALUES
		GAMEPAD_VALUES.put("left_stick_x", Gamepad.AXIS_LEFT_X);
		GAMEPAD_VALUES.put("left_stick_y", Gamepad.AXIS_LEFT_Y);
		GAMEPAD_VALUES.put("right_stick_x", Gamepad.AXIS_RIGHT_X);
		GAMEPAD_VALUES.put("right_stick_y", Gamepad.AXIS_RIGHT_Y);
		GAMEPAD_VALUES.put("left_trigger", Gamepad.AXIS_TRIGGER_LEFT);
		GAMEPAD_VALUES.put("right_trigger", Gamepad.AXIS_TRIGGER_RIGHT);
		GAMEPAD_VALUES.put("x", Gamepad.BUTTON_X);
		GAMEPAD_VALUES.put("y", Gamepad.BUTTON_Y);
		GAMEPAD_VALUES.put("a", Gamepad.BUTTON_A);
		GAMEPAD_VALUES.put("b", Gamepad.BUTTON_B);
		GAMEPAD_VALUES.put("back", Gamepad.BUTTON_BACK);
		GAMEPAD_VALUES.put("start", Gamepad.BUTTON_START);
		GAMEPAD_VALUES.put("guide", Gamepad.BUTTON_GUIDE);
		GAMEPAD_VALUES.put("left_bumper", Gamepad.BUMPER_LEFT);
		GAMEPAD_VALUES.put("right_bumper", Gamepad.BUMPER_RIGHT);
		GAMEPAD_VALUES.put("left_thumb", Gamepad.THUMB_LEFT);
		GAMEPAD_VALUES.put("right_thumb", Gamepad.THUMB_RIGHT);
	}

}
