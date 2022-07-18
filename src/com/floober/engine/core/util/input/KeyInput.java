package com.floober.engine.core.util.input;

import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.util.data.Queue;
import com.floober.engine.core.util.time.TimeScale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInput {

	public static HashMap<Long, KeyInput> windowKeyboardAdapters = new HashMap<>();

	public static final int NUM_KEYS = 100;

	public boolean[] keyState = new boolean[NUM_KEYS];
	public boolean[] prevKeyState = new boolean[NUM_KEYS];
	public long[] keyPressTime = new long[NUM_KEYS];

	public static final int KEY_HOLD_DELAY = 500;

	public Queue<Character> characterQueue = new Queue<>();
	public List<Character> textInput = new ArrayList<>();

	// LETTER KEYS
	public static final int A = 1;
	public static final int B = 2;
	public static final int C = 3;
	public static final int D = 4;
	public static final int E = 5;
	public static final int F = 6;
	public static final int G = 7;
	public static final int H = 8;
	public static final int I = 9;
	public static final int J = 10;
	public static final int K = 11;
	public static final int L = 12;
	public static final int M = 13;
	public static final int N = 14;
	public static final int O = 15;
	public static final int P = 16;
	public static final int Q = 17;
	public static final int R = 18;
	public static final int S = 19;
	public static final int T = 20;
	public static final int U = 21;
	public static final int V = 22;
	public static final int W = 23;
	public static final int X = 24;
	public static final int Y = 25;
	public static final int Z = 26;

	// ARROW KEYS
	public static final int UP = 27;
	public static final int DOWN = 28;
	public static final int LEFT = 29;
	public static final int RIGHT = 30;

	// LETTER-AREA UTILITY KEYS
	public static final int SPACE = 31;
	public static final int ENTER = 32;
	public static final int LSHIFT = 33;
	public static final int RSHIFT = 34;
	public static final int LCONTROL = 35;
	public static final int RCONTROL = 36;
	public static final int ESC = 37;
	public static final int LBRACKET = 64;
	public static final int RBRACKET = 65;
	public static final int BACKSLASH = 66;
	public static final int SEMICOLON = 85;
	public static final int APOSTROPHE = 86;
	public static final int COMMA = 87;
	public static final int PERIOD = 88;
	public static final int SLASH = 89;
	public static final int TAB = 90;

	// NUMBER KEYS (ABOVE LETTERS)
	public static final int KEY_1 = 38;
	public static final int KEY_2 = 39;
	public static final int KEY_3 = 40;
	public static final int KEY_4 = 41;
	public static final int KEY_5 = 42;
	public static final int KEY_6 = 43;
	public static final int KEY_7 = 44;
	public static final int KEY_8 = 45;
	public static final int KEY_9 = 46;
	public static final int KEY_0 = 63;

	// FUNCTION KEYS
	public static final int F1 = 48;
	public static final int F2 = 49;
	public static final int F3 = 50;
	public static final int F4 = 51;
	public static final int F5 = 52;
	public static final int F6 = 53;
	public static final int F7 = 54;
	public static final int F8 = 55;
	public static final int F9 = 56;
	public static final int F10 = 57;
	public static final int F11 = 58;
	public static final int F12 = 59;

	// KEYS NEXT TO NUMBER KEYS
	public static final int GRAVE = 67;
	public static final int PLUS = 60;
	public static final int MINUS = 61;
	public static final int BACKSPACE = 47;

	// 3x3 UTILITY KEYS
	public static final int PRINT_SCREEN = 91;
	public static final int SCROLL_LOCK = 92;
	public static final int PAUSE_BREAK = 93;
	public static final int INSERT = 68;
	public static final int DELETE = 69;
	public static final int HOME = 94;
	public static final int END = 95;
	public static final int PAGE_UP = 96;
	public static final int PAGE_DOWN = 97;
	public static final int MENU = 98;

	// KEYPAD KEYS
	public static final int KP_ENTER = 62;
	public static final int KP_0 = 70;
	public static final int KP_1 = 71;
	public static final int KP_2 = 72;
	public static final int KP_3 = 73;
	public static final int KP_4 = 74;
	public static final int KP_5 = 75;
	public static final int KP_6 = 76;
	public static final int KP_7 = 77;
	public static final int KP_8 = 78;
	public static final int KP_9 = 79;
	public static final int KP_PLUS = 80;
	public static final int KP_MINUS = 81;
	public static final int KP_ASTERISK = 82;
	public static final int KP_SLASH = 83;
	public static final int KP_DECIMAL = 84;

	public static void update() {
		// get window target
		long windowTarget = MasterRenderer.getTargetWindowID();
		KeyInput keyboardAdapter = windowKeyboardAdapters.get(windowTarget);
		// flush text input to the list, and clear the queue
		keyboardAdapter.textInput = keyboardAdapter.characterQueue.getElements();
		keyboardAdapter.characterQueue.clear();
		// get key state arrays
		boolean[] keyState = keyboardAdapter.keyState;
		boolean[] prevKeyState = keyboardAdapter.prevKeyState;
		// update key states
		System.arraycopy(keyState, 0, prevKeyState, 0, NUM_KEYS);
		keyState[A] = glfwGetKey(windowTarget, GLFW_KEY_A) == GLFW_PRESS;
		keyState[B] = glfwGetKey(windowTarget, GLFW_KEY_B) == GLFW_PRESS;
		keyState[C] = glfwGetKey(windowTarget, GLFW_KEY_C) == GLFW_PRESS;
		keyState[D] = glfwGetKey(windowTarget, GLFW_KEY_D) == GLFW_PRESS;
		keyState[E] = glfwGetKey(windowTarget, GLFW_KEY_E) == GLFW_PRESS;
		keyState[F] = glfwGetKey(windowTarget, GLFW_KEY_F) == GLFW_PRESS;
		keyState[G] = glfwGetKey(windowTarget, GLFW_KEY_G) == GLFW_PRESS;
		keyState[H] = glfwGetKey(windowTarget, GLFW_KEY_H) == GLFW_PRESS;
		keyState[I] = glfwGetKey(windowTarget, GLFW_KEY_I) == GLFW_PRESS;
		keyState[J] = glfwGetKey(windowTarget, GLFW_KEY_J) == GLFW_PRESS;
		keyState[K] = glfwGetKey(windowTarget, GLFW_KEY_K) == GLFW_PRESS;
		keyState[L] = glfwGetKey(windowTarget, GLFW_KEY_L) == GLFW_PRESS;
		keyState[M] = glfwGetKey(windowTarget, GLFW_KEY_M) == GLFW_PRESS;
		keyState[N] = glfwGetKey(windowTarget, GLFW_KEY_N) == GLFW_PRESS;
		keyState[O] = glfwGetKey(windowTarget, GLFW_KEY_O) == GLFW_PRESS;
		keyState[P] = glfwGetKey(windowTarget, GLFW_KEY_P) == GLFW_PRESS;
		keyState[Q] = glfwGetKey(windowTarget, GLFW_KEY_Q) == GLFW_PRESS;
		keyState[R] = glfwGetKey(windowTarget, GLFW_KEY_R) == GLFW_PRESS;
		keyState[S] = glfwGetKey(windowTarget, GLFW_KEY_S) == GLFW_PRESS;
		keyState[T] = glfwGetKey(windowTarget, GLFW_KEY_T) == GLFW_PRESS;
		keyState[U] = glfwGetKey(windowTarget, GLFW_KEY_U) == GLFW_PRESS;
		keyState[V] = glfwGetKey(windowTarget, GLFW_KEY_V) == GLFW_PRESS;
		keyState[W] = glfwGetKey(windowTarget, GLFW_KEY_W) == GLFW_PRESS;
		keyState[X] = glfwGetKey(windowTarget, GLFW_KEY_X) == GLFW_PRESS;
		keyState[Y] = glfwGetKey(windowTarget, GLFW_KEY_Y) == GLFW_PRESS;
		keyState[Z] = glfwGetKey(windowTarget, GLFW_KEY_Z) == GLFW_PRESS;
		keyState[UP] = glfwGetKey(windowTarget, GLFW_KEY_UP) == GLFW_PRESS;
		keyState[DOWN] = glfwGetKey(windowTarget, GLFW_KEY_DOWN) == GLFW_PRESS;
		keyState[LEFT] = glfwGetKey(windowTarget, GLFW_KEY_LEFT) == GLFW_PRESS;
		keyState[RIGHT] = glfwGetKey(windowTarget, GLFW_KEY_RIGHT) == GLFW_PRESS;
		keyState[SPACE] = glfwGetKey(windowTarget, GLFW_KEY_SPACE) == GLFW_PRESS;
		keyState[ENTER] = glfwGetKey(windowTarget, GLFW_KEY_ENTER) == GLFW_PRESS;
		keyState[LSHIFT] = glfwGetKey(windowTarget, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS;
		keyState[RSHIFT] = glfwGetKey(windowTarget, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS;
		keyState[LCONTROL] = glfwGetKey(windowTarget, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS;
		keyState[RCONTROL] = glfwGetKey(windowTarget, GLFW_KEY_RIGHT_CONTROL) == GLFW_PRESS;
		keyState[ESC] = glfwGetKey(windowTarget, GLFW_KEY_ESCAPE) == GLFW_PRESS;
		keyState[KEY_1] = glfwGetKey(windowTarget, GLFW_KEY_1) == GLFW_PRESS;
		keyState[KEY_2] = glfwGetKey(windowTarget, GLFW_KEY_2) == GLFW_PRESS;
		keyState[KEY_3] = glfwGetKey(windowTarget, GLFW_KEY_3) == GLFW_PRESS;
		keyState[KEY_4] = glfwGetKey(windowTarget, GLFW_KEY_4) == GLFW_PRESS;
		keyState[KEY_5] = glfwGetKey(windowTarget, GLFW_KEY_5) == GLFW_PRESS;
		keyState[KEY_6] = glfwGetKey(windowTarget, GLFW_KEY_6) == GLFW_PRESS;
		keyState[KEY_7] = glfwGetKey(windowTarget, GLFW_KEY_7) == GLFW_PRESS;
		keyState[KEY_8] = glfwGetKey(windowTarget, GLFW_KEY_8) == GLFW_PRESS;
		keyState[KEY_9] = glfwGetKey(windowTarget, GLFW_KEY_9) == GLFW_PRESS;
		keyState[KEY_0] = glfwGetKey(windowTarget, GLFW_KEY_0) == GLFW_PRESS;
		keyState[BACKSPACE] = glfwGetKey(windowTarget, GLFW_KEY_BACKSPACE) == GLFW_PRESS;
		keyState[F1] = glfwGetKey(windowTarget, GLFW_KEY_F1) == GLFW_PRESS;
		keyState[F2] = glfwGetKey(windowTarget, GLFW_KEY_F2) == GLFW_PRESS;
		keyState[F3] = glfwGetKey(windowTarget, GLFW_KEY_F3) == GLFW_PRESS;
		keyState[F4] = glfwGetKey(windowTarget, GLFW_KEY_F4) == GLFW_PRESS;
		keyState[F5] = glfwGetKey(windowTarget, GLFW_KEY_F5) == GLFW_PRESS;
		keyState[F6] = glfwGetKey(windowTarget, GLFW_KEY_F6) == GLFW_PRESS;
		keyState[F7] = glfwGetKey(windowTarget, GLFW_KEY_F7) == GLFW_PRESS;
		keyState[F8] = glfwGetKey(windowTarget, GLFW_KEY_F8) == GLFW_PRESS;
		keyState[F9] = glfwGetKey(windowTarget, GLFW_KEY_F9) == GLFW_PRESS;
		keyState[F10] = glfwGetKey(windowTarget, GLFW_KEY_F10) == GLFW_PRESS;
		keyState[F11] = glfwGetKey(windowTarget, GLFW_KEY_F11) == GLFW_PRESS;
		keyState[F12] = glfwGetKey(windowTarget, GLFW_KEY_F12) == GLFW_PRESS;
		keyState[PLUS] = glfwGetKey(windowTarget, GLFW_KEY_KP_ADD) == GLFW_PRESS;
		keyState[MINUS] = glfwGetKey(windowTarget, GLFW_KEY_KP_SUBTRACT) == GLFW_PRESS;
		keyState[KP_ENTER] = glfwGetKey(windowTarget, GLFW_KEY_KP_ENTER) == GLFW_PRESS;
		keyState[LBRACKET] = glfwGetKey(windowTarget, GLFW_KEY_LEFT_BRACKET) == GLFW_PRESS;
		keyState[RBRACKET] = glfwGetKey(windowTarget, GLFW_KEY_RIGHT_BRACKET) == GLFW_PRESS;
		keyState[BACKSLASH] = glfwGetKey(windowTarget, GLFW_KEY_BACKSLASH) == GLFW_PRESS;
		keyState[GRAVE] = glfwGetKey(windowTarget, GLFW_KEY_GRAVE_ACCENT) == GLFW_PRESS;
		keyState[INSERT] = glfwGetKey(windowTarget, GLFW_KEY_INSERT) == GLFW_PRESS;
		keyState[DELETE] = glfwGetKey(windowTarget, GLFW_KEY_DELETE) == GLFW_PRESS;
		keyState[KP_0] = glfwGetKey(windowTarget, GLFW_KEY_KP_0) == GLFW_PRESS;
		keyState[KP_1] = glfwGetKey(windowTarget, GLFW_KEY_KP_1) == GLFW_PRESS;
		keyState[KP_2] = glfwGetKey(windowTarget, GLFW_KEY_KP_2) == GLFW_PRESS;
		keyState[KP_3] = glfwGetKey(windowTarget, GLFW_KEY_KP_3) == GLFW_PRESS;
		keyState[KP_4] = glfwGetKey(windowTarget, GLFW_KEY_KP_4) == GLFW_PRESS;
		keyState[KP_5] = glfwGetKey(windowTarget, GLFW_KEY_KP_5) == GLFW_PRESS;
		keyState[KP_6] = glfwGetKey(windowTarget, GLFW_KEY_KP_6) == GLFW_PRESS;
		keyState[KP_7] = glfwGetKey(windowTarget, GLFW_KEY_KP_7) == GLFW_PRESS;
		keyState[KP_8] = glfwGetKey(windowTarget, GLFW_KEY_KP_8) == GLFW_PRESS;
		keyState[KP_9] = glfwGetKey(windowTarget, GLFW_KEY_KP_9) == GLFW_PRESS;
		keyState[KP_PLUS] = glfwGetKey(windowTarget, GLFW_KEY_KP_ADD) == GLFW_PRESS;
		keyState[KP_MINUS] = glfwGetKey(windowTarget, GLFW_KEY_KP_SUBTRACT) == GLFW_PRESS;
		keyState[KP_ASTERISK] = glfwGetKey(windowTarget, GLFW_KEY_KP_MULTIPLY) == GLFW_PRESS;
		keyState[KP_SLASH] = glfwGetKey(windowTarget, GLFW_KEY_KP_DIVIDE) == GLFW_PRESS;
		keyState[KP_DECIMAL] = glfwGetKey(windowTarget, GLFW_KEY_KP_DECIMAL) == GLFW_PRESS;
		keyState[SEMICOLON] = glfwGetKey(windowTarget, GLFW_KEY_SEMICOLON) == GLFW_PRESS;
		keyState[APOSTROPHE] = glfwGetKey(windowTarget, GLFW_KEY_APOSTROPHE) == GLFW_PRESS;
		keyState[COMMA] = glfwGetKey(windowTarget, GLFW_KEY_COMMA) == GLFW_PRESS;
		keyState[PERIOD] = glfwGetKey(windowTarget, GLFW_KEY_PERIOD) == GLFW_PRESS;
		keyState[SLASH] = glfwGetKey(windowTarget, GLFW_KEY_SLASH) == GLFW_PRESS;
		keyState[TAB] = glfwGetKey(windowTarget, GLFW_KEY_TAB) == GLFW_PRESS;
		keyState[PRINT_SCREEN] = glfwGetKey(windowTarget, GLFW_KEY_PRINT_SCREEN) == GLFW_PRESS;
		keyState[SCROLL_LOCK] = glfwGetKey(windowTarget, GLFW_KEY_SCROLL_LOCK) == GLFW_PRESS;
		keyState[PAUSE_BREAK] = glfwGetKey(windowTarget, GLFW_KEY_PAUSE) == GLFW_PRESS;
		keyState[HOME] = glfwGetKey(windowTarget, GLFW_KEY_HOME) == GLFW_PRESS;
		keyState[END] = glfwGetKey(windowTarget, GLFW_KEY_END) == GLFW_PRESS;
		keyState[PAGE_UP] = glfwGetKey(windowTarget, GLFW_KEY_PAGE_UP) == GLFW_PRESS;
		keyState[PAGE_DOWN] = glfwGetKey(windowTarget, GLFW_KEY_PAGE_DOWN) == GLFW_PRESS;
		keyState[MENU] = glfwGetKey(windowTarget, GLFW_KEY_MENU) == GLFW_PRESS;

		// update press timings
		long[] keyPressTime = keyboardAdapter.keyPressTime;
		long time = System.nanoTime();
		for (int i = 0; i < keyPressTime.length; i++) {
			if (isPressed(i)) keyPressTime[i] = time;
			else if (!isHeld(i)) keyPressTime[i] = -1;
		}
	}

	private static KeyInput getInstance() {
		return windowKeyboardAdapters.get(MasterRenderer.getTargetWindowID());
	}

	// CHECK IF A KEY IS HELD DOWN
	public static boolean isHeld(int key) {
		return getInstance().keyState[key];
	}

	public static boolean isHeld(int... keys) {
		for (int i : keys) {
			if (getInstance().keyState[i]) return true;
		}
		return false;
	}

	public static boolean isHeldDelay(int key) {
		if (isHeld(key)) {
			long start = getInstance().keyPressTime[key];
			long time = TimeScale.getRawTime(start);
			return time >= KEY_HOLD_DELAY;
		} else return false;
	}

	public static boolean isReleased(int key) {
		return !getInstance().keyState[key] &&
				getInstance().prevKeyState[key];
	}

	public static boolean isReleased(int... keys) {
		for (int i : keys) {
			if (isReleased(i)) return true;
		}
		return false;
	}

	// CHECK FOR NEW KEY PRESS
	public static boolean isPressed(int key) {
		return getInstance().keyState[key] &&
				!getInstance().prevKeyState[key];
	}

	public static boolean isPressed(int... keys) {
		for (int i : keys) {
			if (isPressed(i)) return true;
		}
		return false;
	}

	// CHECK FOR HOLDING SHIFT
	public static boolean isShift() {
		return getInstance().keyState[LSHIFT] ||
				getInstance().keyState[RSHIFT];
	}

	// CHECK FOR HOLDING CONTROL
	public static boolean isCtrl() {
		return getInstance().keyState[LCONTROL] ||
				getInstance().keyState[RCONTROL];
	}

	// CHECK FOR TEXT INPUT
	public static List<Character> getCharacterInput() {
		return getInstance().textInput;
	}
}

