package com.floober.engine.util.input;

import com.floober.engine.display.GameWindow;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInput {
	
	public static final int NUM_KEYS = 100;
	
	public static boolean[] keyState = new boolean[NUM_KEYS];
	public static boolean[] prevKeyState = new boolean[NUM_KEYS];

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
	public static final int UP = 27;
	public static final int DOWN = 28;
	public static final int LEFT = 29;
	public static final int RIGHT = 30;
	public static final int SPACE = 31;
	public static final int ENTER = 32;
	public static final int LSHIFT = 33;
	public static final int RSHIFT = 34;
	public static final int LCONTROL = 35;
	public static final int RCONTROL = 36;
	public static final int ESC = 37;
	public static final int KEY_1 = 38;
	public static final int KEY_2 = 39;
	public static final int KEY_3 = 40;
	public static final int KEY_4 = 41;
	public static final int KEY_5 = 42;
	public static final int KEY_6 = 43;
	public static final int KEY_7 = 44;
	public static final int KEY_8 = 45;
	public static final int KEY_9 = 46;
	public static final int BACKSPACE = 47;
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
	public static final int PLUS = 60;
	public static final int MINUS = 61;
	public static final int KP_ENTER = 62;
	public static final int KEY_0 = 63;

	public static void update() {
		System.arraycopy(keyState, 0, prevKeyState, 0, NUM_KEYS);
		keyState[A]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_A) == GLFW_PRESS;
		keyState[B]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_B) == GLFW_PRESS;
		keyState[C]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_C) == GLFW_PRESS;
		keyState[D]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_D) == GLFW_PRESS;
		keyState[E]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_E) == GLFW_PRESS;
		keyState[F]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_F) == GLFW_PRESS;
		keyState[G]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_G) == GLFW_PRESS;
		keyState[H]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_H) == GLFW_PRESS;
		keyState[I]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_I) == GLFW_PRESS;
		keyState[J]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_J) == GLFW_PRESS;
		keyState[K]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_K) == GLFW_PRESS;
		keyState[L]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_L) == GLFW_PRESS;
		keyState[M]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_M) == GLFW_PRESS;
		keyState[N]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_N) == GLFW_PRESS;
		keyState[O]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_O) == GLFW_PRESS;
		keyState[P]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_P) == GLFW_PRESS;
		keyState[Q]	       = glfwGetKey(GameWindow.windowID, GLFW_KEY_Q) == GLFW_PRESS;
		keyState[R]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_R) == GLFW_PRESS;
		keyState[S]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_S) == GLFW_PRESS;
		keyState[T]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_T) == GLFW_PRESS;
		keyState[U]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_U) == GLFW_PRESS;
		keyState[V]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_V) == GLFW_PRESS;
		keyState[W]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_W) == GLFW_PRESS;
		keyState[X]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_X) == GLFW_PRESS;
		keyState[Y]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_Y) == GLFW_PRESS;
		keyState[Z]        = glfwGetKey(GameWindow.windowID, GLFW_KEY_Z) == GLFW_PRESS;
		keyState[UP]       = glfwGetKey(GameWindow.windowID, GLFW_KEY_UP) == GLFW_PRESS;
		keyState[DOWN]     = glfwGetKey(GameWindow.windowID, GLFW_KEY_DOWN) == GLFW_PRESS;
		keyState[LEFT]     = glfwGetKey(GameWindow.windowID, GLFW_KEY_LEFT) == GLFW_PRESS;
		keyState[RIGHT]    = glfwGetKey(GameWindow.windowID, GLFW_KEY_RIGHT) == GLFW_PRESS;
		keyState[SPACE]    = glfwGetKey(GameWindow.windowID, GLFW_KEY_SPACE) == GLFW_PRESS;
		keyState[ENTER]    = glfwGetKey(GameWindow.windowID, GLFW_KEY_ENTER) == GLFW_PRESS;
		keyState[LSHIFT]   = glfwGetKey(GameWindow.windowID, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS;
		keyState[RSHIFT]   = glfwGetKey(GameWindow.windowID, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS;
		keyState[LCONTROL] = glfwGetKey(GameWindow.windowID, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS;
		keyState[RCONTROL] = glfwGetKey(GameWindow.windowID, GLFW_KEY_RIGHT_CONTROL) == GLFW_PRESS;
		keyState[ESC] = glfwGetKey(GameWindow.windowID, GLFW_KEY_ESCAPE) == GLFW_PRESS;
		keyState[KEY_1] = glfwGetKey(GameWindow.windowID, GLFW_KEY_1) == GLFW_PRESS;
		keyState[KEY_2] = glfwGetKey(GameWindow.windowID, GLFW_KEY_2) == GLFW_PRESS;
		keyState[KEY_3] = glfwGetKey(GameWindow.windowID, GLFW_KEY_3) == GLFW_PRESS;
		keyState[KEY_4] = glfwGetKey(GameWindow.windowID, GLFW_KEY_4) == GLFW_PRESS;
		keyState[KEY_5] = glfwGetKey(GameWindow.windowID, GLFW_KEY_5) == GLFW_PRESS;
		keyState[KEY_6] = glfwGetKey(GameWindow.windowID, GLFW_KEY_6) == GLFW_PRESS;
		keyState[KEY_7] = glfwGetKey(GameWindow.windowID, GLFW_KEY_7) == GLFW_PRESS;
		keyState[KEY_8] = glfwGetKey(GameWindow.windowID, GLFW_KEY_8) == GLFW_PRESS;
		keyState[KEY_9] = glfwGetKey(GameWindow.windowID, GLFW_KEY_9) == GLFW_PRESS;
		keyState[KEY_0] = glfwGetKey(GameWindow.windowID, GLFW_KEY_0) == GLFW_PRESS;
		keyState[BACKSPACE] = glfwGetKey(GameWindow.windowID, GLFW_KEY_BACKSPACE) == GLFW_PRESS;
		keyState[F1] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F1) == GLFW_PRESS;
		keyState[F2] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F2) == GLFW_PRESS;
		keyState[F3] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F3) == GLFW_PRESS;
		keyState[F4] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F4) == GLFW_PRESS;
		keyState[F5] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F5) == GLFW_PRESS;
		keyState[F6] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F6) == GLFW_PRESS;
		keyState[F7] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F7) == GLFW_PRESS;
		keyState[F8] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F8) == GLFW_PRESS;
		keyState[F9] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F9) == GLFW_PRESS;
		keyState[F10] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F10) == GLFW_PRESS;
		keyState[F11] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F11) == GLFW_PRESS;
		keyState[F12] = glfwGetKey(GameWindow.windowID, GLFW_KEY_F12) == GLFW_PRESS;
		keyState[PLUS] = glfwGetKey(GameWindow.windowID, GLFW_KEY_KP_ADD) == GLFW_PRESS;
		keyState[MINUS] = glfwGetKey(GameWindow.windowID, GLFW_KEY_KP_SUBTRACT) == GLFW_PRESS;
		keyState[KP_ENTER] = glfwGetKey(GameWindow.windowID, GLFW_KEY_KP_ENTER) == GLFW_PRESS;
	}

	// CHECK IF A KEY IS HELD DOWN
	public static boolean isHeld(int key) {
		return keyState[key];
	}

	public static boolean isHeld(int... keys) {
		for (int i : keys) {
			if (keyState[i]) return true;
		}
		return false;
	}

	// CHECK FOR NEW KEY PRESS
	public static boolean isPressed(int key) {
		return keyState[key] && !prevKeyState[key];
	}

	public static boolean isPressed(int... keys) {
		for (int i : keys) {
			if (keyState[i] && !prevKeyState[i]) return true;
		}
		return false;
	}

	// CHECK FOR HOLDING SHIFT
	public static boolean isShift() {
		return keyState[LSHIFT] || keyState[RSHIFT];
	}

	// CHECK FOR HOLDING CONTROL
	public static boolean isCtrl() {
		return keyState[LCONTROL] || keyState[RCONTROL];
	}
}