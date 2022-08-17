package com.floober.engine.core.input;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;

/**
 * KeyNames contains an array with String names for every key constant defined in KeyInput.
 *
 * @author Floober
 */
public class KeyNames {

	protected static final String[] KEY_NAMES = new String[KeyInput.NUM_KEYS];

	protected static final Map<String, Integer> KEY_VALUES = new HashMap<>();

	static {
		// ASSIGN NAMES
		// letters
		KEY_NAMES[KeyInput.A] = "a";
		KEY_NAMES[KeyInput.B] = "b";
		KEY_NAMES[KeyInput.C] = "c";
		KEY_NAMES[KeyInput.D] = "d";
		KEY_NAMES[KeyInput.E] = "e";
		KEY_NAMES[KeyInput.F] = "f";
		KEY_NAMES[KeyInput.G] = "g";
		KEY_NAMES[KeyInput.H] = "h";
		KEY_NAMES[KeyInput.I] = "i";
		KEY_NAMES[KeyInput.J] = "j";
		KEY_NAMES[KeyInput.K] = "k";
		KEY_NAMES[KeyInput.L] = "l";
		KEY_NAMES[KeyInput.M] = "m";
		KEY_NAMES[KeyInput.N] = "n";
		KEY_NAMES[KeyInput.O] = "o";
		KEY_NAMES[KeyInput.P] = "p";
		KEY_NAMES[KeyInput.Q] = "q";
		KEY_NAMES[KeyInput.R] = "r";
		KEY_NAMES[KeyInput.S] = "s";
		KEY_NAMES[KeyInput.T] = "t";
		KEY_NAMES[KeyInput.U] = "u";
		KEY_NAMES[KeyInput.V] = "v";
		KEY_NAMES[KeyInput.W] = "w";
		KEY_NAMES[KeyInput.X] = "x";
		KEY_NAMES[KeyInput.Y] = "y";
		KEY_NAMES[KeyInput.Z] = "z";
		// arrows
		KEY_NAMES[KeyInput.LEFT] = "left_arrow";
		KEY_NAMES[KeyInput.RIGHT] = "right_arrow";
		KEY_NAMES[KeyInput.UP] = "up_arrow";
		KEY_NAMES[KeyInput.DOWN] = "down_arrow";
		// util keys
		KEY_NAMES[KeyInput.SPACE] 		= "space";
		KEY_NAMES[KeyInput.ENTER] 		= "enter";
		KEY_NAMES[KeyInput.LSHIFT] 		= "left_shift";
		KEY_NAMES[KeyInput.RSHIFT] 		= "right_shift";
		KEY_NAMES[KeyInput.LCONTROL] 	= "left_control";
		KEY_NAMES[KeyInput.RCONTROL] 	= "right_control";
		KEY_NAMES[KeyInput.ESC] 		= "escape";
		KEY_NAMES[KeyInput.LBRACKET] 	= "left_bracket";
		KEY_NAMES[KeyInput.RBRACKET] 	= "right_bracket";
		KEY_NAMES[KeyInput.BACKSLASH] 	= "backslash";
		KEY_NAMES[KeyInput.SEMICOLON] 	= "semicolon";
		KEY_NAMES[KeyInput.APOSTROPHE] 	= "apostrophe";
		KEY_NAMES[KeyInput.COMMA] 		= "comma";
		KEY_NAMES[KeyInput.PERIOD] 		= "period";
		KEY_NAMES[KeyInput.SLASH] 		= "slash";
		KEY_NAMES[KeyInput.TAB] 		= "tab";
		// numbers
		KEY_NAMES[KeyInput.KEY_1] = "1";
		KEY_NAMES[KeyInput.KEY_2] = "2";
		KEY_NAMES[KeyInput.KEY_3] = "3";
		KEY_NAMES[KeyInput.KEY_4] = "4";
		KEY_NAMES[KeyInput.KEY_5] = "5";
		KEY_NAMES[KeyInput.KEY_6] = "6";
		KEY_NAMES[KeyInput.KEY_7] = "7";
		KEY_NAMES[KeyInput.KEY_8] = "8";
		KEY_NAMES[KeyInput.KEY_9] = "9";
		KEY_NAMES[KeyInput.KEY_0] = "0";
		// function keys
		KEY_NAMES[KeyInput.F1] = "f1";
		KEY_NAMES[KeyInput.F2] = "f2";
		KEY_NAMES[KeyInput.F3] = "f3";
		KEY_NAMES[KeyInput.F4] = "f4";
		KEY_NAMES[KeyInput.F5] = "f5";
		KEY_NAMES[KeyInput.F6] = "f6";
		KEY_NAMES[KeyInput.F7] = "f7";
		KEY_NAMES[KeyInput.F8] = "f8";
		KEY_NAMES[KeyInput.F9] = "f9";
		KEY_NAMES[KeyInput.F10] = "f10";
		KEY_NAMES[KeyInput.F11] = "f11";
		KEY_NAMES[KeyInput.F12] = "f12";
		// top row util keys
		KEY_NAMES[KeyInput.GRAVE] = "grave";
		KEY_NAMES[KeyInput.PLUS] = "plus";
		KEY_NAMES[KeyInput.MINUS] = "minus";
		KEY_NAMES[KeyInput.BACKSPACE] = "backspace";
		// that 3x3 of util keys to the right of the main keys
		KEY_NAMES[KeyInput.PRINT_SCREEN] = "print_screen";
		KEY_NAMES[KeyInput.SCROLL_LOCK] = "scroll_lock";
		KEY_NAMES[KeyInput.PAUSE_BREAK] = "pause_break";
		KEY_NAMES[KeyInput.INSERT] = "insert";
		KEY_NAMES[KeyInput.DELETE] = "delete";
		KEY_NAMES[KeyInput.HOME] = "home";
		KEY_NAMES[KeyInput.END] = "end";
		KEY_NAMES[KeyInput.PAGE_UP] = "page_up";
		KEY_NAMES[KeyInput.PAGE_DOWN] = "page_down";
		// idk where this is (Windows key?)
		KEY_NAMES[KeyInput.MENU] = "menu";
		// keypad keys
		KEY_NAMES[KeyInput.KP_ENTER] = "kp_enter";
		KEY_NAMES[KeyInput.KP_0] = "kp_0";
		KEY_NAMES[KeyInput.KP_1] = "kp_1";
		KEY_NAMES[KeyInput.KP_2] = "kp_2";
		KEY_NAMES[KeyInput.KP_3] = "kp_3";
		KEY_NAMES[KeyInput.KP_4] = "kp_4";
		KEY_NAMES[KeyInput.KP_5] = "kp_5";
		KEY_NAMES[KeyInput.KP_6] = "kp_6";
		KEY_NAMES[KeyInput.KP_7] = "kp_7";
		KEY_NAMES[KeyInput.KP_8] = "kp_8";
		KEY_NAMES[KeyInput.KP_9] = "kp_9";
		KEY_NAMES[KeyInput.KP_PLUS] = "kp_plus";
		KEY_NAMES[KeyInput.KP_MINUS] = "kp_minus";
		KEY_NAMES[KeyInput.KP_ASTERISK] = "kp_asterisk";
		KEY_NAMES[KeyInput.KP_SLASH] = "kp_slash";
		KEY_NAMES[KeyInput.KP_DECIMAL] = "kp_decimal";
		// ASSIGN VALUES
		// letters
		KEY_VALUES.put("a", KeyInput.A);
		KEY_VALUES.put("b", KeyInput.B);
		KEY_VALUES.put("c", KeyInput.C);
		KEY_VALUES.put("d", KeyInput.D);
		KEY_VALUES.put("e", KeyInput.E);
		KEY_VALUES.put("f", KeyInput.F);
		KEY_VALUES.put("g", KeyInput.G);
		KEY_VALUES.put("h", KeyInput.H);
		KEY_VALUES.put("i", KeyInput.I);
		KEY_VALUES.put("j", KeyInput.J);
		KEY_VALUES.put("k", KeyInput.K);
		KEY_VALUES.put("l", KeyInput.L);
		KEY_VALUES.put("m", KeyInput.M);
		KEY_VALUES.put("n", KeyInput.N);
		KEY_VALUES.put("o", KeyInput.O);
		KEY_VALUES.put("p", KeyInput.P);
		KEY_VALUES.put("q", KeyInput.Q);
		KEY_VALUES.put("r", KeyInput.R);
		KEY_VALUES.put("s", KeyInput.S);
		KEY_VALUES.put("t", KeyInput.T);
		KEY_VALUES.put("u", KeyInput.U);
		KEY_VALUES.put("v", KeyInput.V);
		KEY_VALUES.put("w", KeyInput.W);
		KEY_VALUES.put("x", KeyInput.X);
		KEY_VALUES.put("y", KeyInput.Y);
		KEY_VALUES.put("z", KeyInput.Z);
		// arrows
		KEY_VALUES.put("left_arrow", KeyInput.LEFT);
		KEY_VALUES.put("right_arrow", KeyInput.RIGHT);
		KEY_VALUES.put("up_arrow", KeyInput.UP);
		KEY_VALUES.put("down_arrow", KeyInput.DOWN);
		// util keys
		KEY_VALUES.put("space", KeyInput.SPACE);
		KEY_VALUES.put("enter", KeyInput.ENTER);
		KEY_VALUES.put("left_shift", KeyInput.LSHIFT);
		KEY_VALUES.put("right_shift", KeyInput.RSHIFT);
		KEY_VALUES.put("left_control", KeyInput.LCONTROL);
		KEY_VALUES.put("right_control", KeyInput.RCONTROL);
		KEY_VALUES.put("escape", KeyInput.ESC);
		KEY_VALUES.put("left_bracket", KeyInput.LBRACKET);
		KEY_VALUES.put("right_bracket", KeyInput.RBRACKET);
		KEY_VALUES.put("backslash", KeyInput.BACKSLASH);
		KEY_VALUES.put("semicolon", KeyInput.SEMICOLON);
		KEY_VALUES.put("apostrophe", KeyInput.APOSTROPHE);
		KEY_VALUES.put("comma", KeyInput.COMMA);
		KEY_VALUES.put("period", KeyInput.PERIOD);
		KEY_VALUES.put("slash", KeyInput.SLASH);
		KEY_VALUES.put("tab", KeyInput.TAB);
		// numbers
		KEY_VALUES.put("1", KeyInput.KEY_1);
		KEY_VALUES.put("2", KeyInput.KEY_2);
		KEY_VALUES.put("3", KeyInput.KEY_3);
		KEY_VALUES.put("4", KeyInput.KEY_4);
		KEY_VALUES.put("5", KeyInput.KEY_5);
		KEY_VALUES.put("6", KeyInput.KEY_6);
		KEY_VALUES.put("7", KeyInput.KEY_7);
		KEY_VALUES.put("8", KeyInput.KEY_8);
		KEY_VALUES.put("9", KeyInput.KEY_9);
		KEY_VALUES.put("0", KeyInput.KEY_0);
		// function keys
		KEY_VALUES.put("f1", KeyInput.F1);
		KEY_VALUES.put("f2", KeyInput.F2);
		KEY_VALUES.put("f3", KeyInput.F3);
		KEY_VALUES.put("f4", KeyInput.F4);
		KEY_VALUES.put("f5", KeyInput.F5);
		KEY_VALUES.put("f6", KeyInput.F6);
		KEY_VALUES.put("f7", KeyInput.F7);
		KEY_VALUES.put("f8", KeyInput.F8);
		KEY_VALUES.put("f9", KeyInput.F9);
		KEY_VALUES.put("f10", KeyInput.F10);
		KEY_VALUES.put("f11", KeyInput.F11);
		KEY_VALUES.put("f12", KeyInput.F12);
		// top row util keys
		KEY_VALUES.put("grave", KeyInput.GRAVE);
		KEY_VALUES.put("plus", KeyInput.PLUS);
		KEY_VALUES.put("minus", KeyInput.MINUS);
		KEY_VALUES.put("backspace", KeyInput.BACKSPACE);
		// that 3x3 of util keys to the right of the main keys
		KEY_VALUES.put("print_screen", KeyInput.PRINT_SCREEN);
		KEY_VALUES.put("scroll_lock", KeyInput.SCROLL_LOCK);
		KEY_VALUES.put("pause_break", KeyInput.PAUSE_BREAK);
		KEY_VALUES.put("insert", KeyInput.INSERT);
		KEY_VALUES.put("delete", KeyInput.DELETE);
		KEY_VALUES.put("home", KeyInput.HOME);
		KEY_VALUES.put("end", KeyInput.END);
		KEY_VALUES.put("page_up", KeyInput.PAGE_UP);
		KEY_VALUES.put("page_down", KeyInput.PAGE_DOWN);
		// idk where this is (Windows key?)
		KEY_VALUES.put("menu", KeyInput.MENU);
		// keypad keys
		KEY_VALUES.put("kp_enter", KeyInput.KP_ENTER);
		KEY_VALUES.put("kp_0", KeyInput.KP_0);
		KEY_VALUES.put("kp_1", KeyInput.KP_1);
		KEY_VALUES.put("kp_2", KeyInput.KP_2);
		KEY_VALUES.put("kp_3", KeyInput.KP_3);
		KEY_VALUES.put("kp_4", KeyInput.KP_4);
		KEY_VALUES.put("kp_5", KeyInput.KP_5);
		KEY_VALUES.put("kp_6", KeyInput.KP_6);
		KEY_VALUES.put("kp_7", KeyInput.KP_7);
		KEY_VALUES.put("kp_8", KeyInput.KP_8);
		KEY_VALUES.put("kp_9", KeyInput.KP_9);
		KEY_VALUES.put("kp_plus", KeyInput.KP_PLUS);
		KEY_VALUES.put("kp_minus", KeyInput.KP_MINUS);
		KEY_VALUES.put("kp_asterisk", KeyInput.KP_ASTERISK);
		KEY_VALUES.put("kp_slash", KeyInput.KP_SLASH);
		KEY_VALUES.put("kp_decimal", KeyInput.KP_DECIMAL);
	}

}
