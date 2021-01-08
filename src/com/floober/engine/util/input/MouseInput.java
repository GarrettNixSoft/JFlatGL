package com.floober.engine.util.input;

import com.floober.engine.display.Display;
import com.floober.engine.display.GameWindow;
import com.floober.engine.util.configuration.Config;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
	
	public static final int NUM_BUTTONS = 2;

	public static boolean[] buttonState = new boolean[NUM_BUTTONS];
	public static boolean[] prevButtonState = new boolean[NUM_BUTTONS];
	public static Vector2i mouseLocation = new Vector2i(), prevLocation = new Vector2i();
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public static double WHEEL;
	public static boolean WHEEL_UP;
	public static boolean WHEEL_DOWN;

	public static double xPosRatio = 1;
	public static double yPosRatio = 1;
	public static int xOffset = 0;
	public static int yOffset = 0;

//	private static int DX, DY;
	
	public static void update() {
		System.arraycopy(buttonState, 0, prevButtonState, 0, NUM_BUTTONS);
		buttonState[LEFT] = glfwGetMouseButton(GameWindow.windowID, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
		buttonState[RIGHT] = glfwGetMouseButton(GameWindow.windowID, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS;
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(GameWindow.windowID, xBuffer, yBuffer);
		double xPos = (xBuffer.get() + xOffset) * xPosRatio;
		double yPos = (yBuffer.get() + yOffset) * yPosRatio;
		prevLocation = mouseLocation;
		mouseLocation = new Vector2i((int) xPos, (int) yPos);
		// TEST
//		if (leftClick()) {
//			xBuffer = BufferUtils.createDoubleBuffer(1);
//			yBuffer = BufferUtils.createDoubleBuffer(1);
//			glfwGetCursorPos(GameWindow.windowID, xBuffer, yBuffer);
//			System.out.println("Raw position: (" + xBuffer.get() + ", " + yBuffer.get() + "); Adjusted position: (" + xPos + ", " + yPos + ")");
//		}
		// END_TEST
		WHEEL_UP = WHEEL > 0;
		WHEEL_DOWN = WHEEL < 0;
		WHEEL = 0;

		// get velocity
//		DX = Mouse.getDX();
//		DY = -Mouse.getDY(); // Disabled until I come up with a better solution.
	}

	// UTIL: window resize support
	public static void updateRatio(double width, double height) {
		xPosRatio = Config.DEFAULT_RESOLUTION_WIDTH / width;
		yPosRatio = Config.DEFAULT_RESOLUTION_HEIGHT / height;
		Display.SCREEN_RATIO.set(xPosRatio, yPosRatio);
	}

	public static void setOffset(int x, int y) {
		xOffset = x;
		yOffset = y;
	}
	
	public static boolean isPressed(int button) {
		return buttonState[button];
	}
	public static boolean isClicked(int button) {
		return buttonState[button] && !prevButtonState[button];
	}
	public static boolean leftClick() {
		return buttonState[LEFT] && !prevButtonState[LEFT];
	}
	public static boolean rightClick() {
		return buttonState[RIGHT] && !prevButtonState[RIGHT];
	}

	public static Vector2i getMousePosi() {
		return mouseLocation;
	}
	public static Vector2i getPrevPosi() { return prevLocation; }

	public static Vector2f getMousePos() {
		return new Vector2f(mouseLocation);
	}
	public static Vector2f getPrevPos() { return new Vector2f(prevLocation); }
	
	public static int getX() { return mouseLocation.x(); }
	public static int getY() {
		return mouseLocation.y();
	}
	
	public static boolean mouseOver(Rectangle rect) {
		return rect.contains(new Point(mouseLocation.x, mouseLocation.y));
	}
	
}