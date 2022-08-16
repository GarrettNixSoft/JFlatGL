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
	
	public static final int NUM_BUTTONS = 2;

	public boolean[] buttonState = new boolean[NUM_BUTTONS];
	public boolean[] prevButtonState = new boolean[NUM_BUTTONS];
	public Vector2f mouseLocation = new Vector2f(), prevLocation = new Vector2f();
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public double WHEEL;
	public boolean WHEEL_UP;
	public boolean WHEEL_DOWN;

	private float DX, DY;
	
	public static void update() {
		// get the target window's mouse adapter and its attributes
		long windowTarget = DisplayManager.primaryWindowID;
		MouseInput windowMouseAdapter = windowMouseAdapters.get(windowTarget);
		boolean[] buttonState = windowMouseAdapter.buttonState;
		boolean[] prevButtonState = windowMouseAdapter.prevButtonState;
		// process mouse events on the target window
		System.arraycopy(buttonState, 0, prevButtonState, 0, NUM_BUTTONS);
		Window currentWindow = MasterRenderer.getTargetWindow();
		buttonState[LEFT] = glfwGetMouseButton(currentWindow.getWindowID(), GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
		buttonState[RIGHT] = glfwGetMouseButton(currentWindow.getWindowID(), GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS;
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
		windowMouseAdapter.WHEEL_UP = windowMouseAdapter.WHEEL > 0;
		windowMouseAdapter.WHEEL_DOWN = windowMouseAdapter.WHEEL < 0;
		windowMouseAdapter.WHEEL = 0;

		// get velocity
//		DX = Mouse.getDX();
//		DY = -Mouse.getDY(); // Disabled until I come up with a better solution.
	}

	public static boolean anyInput() {
		return mouseMoved() || wheelMoved();
	}
	
	public static boolean isPressed(int button) {
		return windowMouseAdapters.get(DisplayManager.primaryWindowID).buttonState[button];
	}
	public static boolean isClicked(int button) {
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
	
	public static int getX() { return (int) windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation.x(); }
	public static int getY() { return (int) windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation.y(); }

	public static float getDX() { return windowMouseAdapters.get(DisplayManager.primaryWindowID).DX; }
	public static float getDY() { return windowMouseAdapters.get(DisplayManager.primaryWindowID).DY; }

	public static boolean mouseOver(Vector4f bounds) {
		return Collisions.contains(bounds, windowMouseAdapters.get(DisplayManager.primaryWindowID).mouseLocation);
	}

	public static boolean wheelMoved() { return wheelUp() || wheelDown(); }

	public static boolean wheelUp() {
		return windowMouseAdapters.get(DisplayManager.primaryWindowID).WHEEL_UP;
	}

	public static boolean wheelDown() {
		return windowMouseAdapters.get(DisplayManager.primaryWindowID).WHEEL_DOWN;
	}

}