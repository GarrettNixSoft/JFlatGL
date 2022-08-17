package com.floober.engine.core.input;

import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.util.conversion.VectorConverter;
import com.floober.engine.core.util.math.Collisions;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

	public static HashMap<Long, MouseInput> windowMouseAdapters = new HashMap<>();

	// convenience aliases
	public static final int NUM_BUTTONS = 8;
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

	public boolean[] buttonState = new boolean[NUM_BUTTONS];
	public boolean[] prevButtonState = new boolean[NUM_BUTTONS];
	public Vector2f mouseLocation = new Vector2f(), prevLocation = new Vector2f();

	public double WHEEL;
	public boolean WHEEL_MOVED_UP;
	public boolean WHEEL_MOVED_DOWN;

	private float DX, DY;
	private float prevDX, prevDY;

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
		// get the target window's mouse adapter and its attributes
		long windowTarget = DisplayManager.primaryWindowID;
		MouseInput windowMouseAdapter = windowMouseAdapters.get(windowTarget);
		boolean[] buttonState = windowMouseAdapter.buttonState;
		boolean[] prevButtonState = windowMouseAdapter.prevButtonState;
		// process mouse events on the target window
		System.arraycopy(buttonState, 0, prevButtonState, 0, NUM_BUTTONS);
		Window currentWindow = MasterRenderer.getTargetWindow();
		// update button states
		for (int i = GLFW_MOUSE_BUTTON_1; i <= GLFW_MOUSE_BUTTON_LAST; i++) {
			buttonState[i] = glfwGetMouseButton(currentWindow.getWindowID(), i) == GLFW_PRESS;
		}
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(currentWindow.getWindowID(), xBuffer, yBuffer);
		double xRaw = xBuffer.get();
		double yRaw = yBuffer.get();
		double xPos = (xRaw + currentWindow.getMouseXOffset()) * currentWindow.getMouseXRatio();
		double yPos = (yRaw + currentWindow.getMouseYOffset()) * currentWindow.getMouseYRatio();
//		if (currentWindow.getWindowID() != MasterRenderer.primaryWindowRenderer.getTargetWindow().getWindowID())
//			Logger.log("Position in window: (" + xRaw + ", " + yRaw + ")");
		windowMouseAdapter.prevLocation = windowMouseAdapter.mouseLocation;
		windowMouseAdapter.mouseLocation = new Vector2f((float) xPos, (float) yPos);
		// move the delta values to the previous slots
		windowMouseAdapter.prevDX = windowMouseAdapter.DX;
		windowMouseAdapter.prevDY = windowMouseAdapter.DY;
		// update the delta values
		windowMouseAdapter.DX = (float) xPos - windowMouseAdapter.prevLocation.x;
		windowMouseAdapter.DY = (float) yPos - windowMouseAdapter.prevLocation.y;
		// TEST
//		if (leftClick()) {
//			xBuffer = BufferUtils.createDoubleBuffer(1);
//			yBuffer = BufferUtils.createDoubleBuffer(1);
//			glfwGetCursorPos(DisplayManager.primaryWindowID, xBuffer, yBuffer);
//			System.out.println("Raw position: (" + xBuffer.get() + ", " + yBuffer.get() + "); Adjusted position: (" + xPos + ", " + yPos + ")");
//		}
		// END_TEST
		windowMouseAdapter.WHEEL_MOVED_UP = windowMouseAdapter.WHEEL > 0;
		windowMouseAdapter.WHEEL_MOVED_DOWN = windowMouseAdapter.WHEEL < 0;
		windowMouseAdapter.WHEEL = 0;

		// get velocity
//		DX = Mouse.getDX();
//		DY = -Mouse.getDY(); // Disabled until I come up with a better solution.
	}

	public static boolean anyInput() {
		return mouseMoved() || wheelMoved();
	}
	
	public static boolean isHeld(int button) {
		return windowMouseAdapters.get(DisplayManager.primaryWindowID).buttonState[button];
	}
	public static boolean isPressed(int button) {
		return 	windowMouseAdapters.get(DisplayManager.primaryWindowID).buttonState[button] &&
				!windowMouseAdapters.get(DisplayManager.primaryWindowID).prevButtonState[button];
	}
	public static boolean leftClick() {
		return 	windowMouseAdapters.get(DisplayManager.primaryWindowID).buttonState[LEFT] &&
				!windowMouseAdapters.get(DisplayManager.primaryWindowID).prevButtonState[LEFT];
	}
	public static boolean rightClick() {
		return 	windowMouseAdapters.get(DisplayManager.primaryWindowID).buttonState[RIGHT] &&
				!windowMouseAdapters.get(DisplayManager.primaryWindowID).prevButtonState[RIGHT];
	}

	public static Vector2i getMousePosi() {
		return VectorConverter.vec2fToVec2i(windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation);
	}

	public static Vector2i getPrevPosi() {
		return VectorConverter.vec2fToVec2i(windowMouseAdapters.get(DisplayManager.primaryWindowID).prevLocation);
	}

	public static Vector2f getMousePos() {
		return new Vector2f(windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation);
	}

	public static Vector2f getPrevPos() {
		return new Vector2f(windowMouseAdapters.get(DisplayManager.primaryWindowID).prevLocation);
	}

	public static boolean mouseMoved() {
		return getDX() != 0 || getDY() != 0;
	}

	public static boolean newMotionOnAxis(int axis) {
		if (axis == AXIS_X) return getDX() != 0 && getPrevDX() == 0;
		else if (axis == AXIS_Y) return getDY() != 0 && getPrevDY() == 0;
		else return false;
	}
	
	public static int getX() { return (int) windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation.x(); }
	public static int getY() { return (int) windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation.y(); }

	public static float getDX() { return windowMouseAdapters.get(DisplayManager.primaryWindowID).DX; }
	public static float getDY() { return windowMouseAdapters.get(DisplayManager.primaryWindowID).DY; }

	public static float getPrevDX() { return windowMouseAdapters.get(DisplayManager.primaryWindowID).prevDX; }
	public static float getPrevDY() { return windowMouseAdapters.get(DisplayManager.primaryWindowID).prevDY; }

	public static float getAxis(int axis) {
		if (axis == AXIS_X) return getDX();
		else if (axis == AXIS_Y) return getDY();
		else return 0;
	}

	public static boolean mouseOver(Vector4f bounds) {
		return Collisions.contains(bounds, windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation);
	}

	public static boolean wheelMoved() { return wheelUp() || wheelDown(); }

	public static boolean wheelUp() {
		return windowMouseAdapters.get(DisplayManager.primaryWindowID).WHEEL_MOVED_UP;
	}

	public static boolean wheelDown() {
		return windowMouseAdapters.get(DisplayManager.primaryWindowID).WHEEL_MOVED_DOWN;
	}

}