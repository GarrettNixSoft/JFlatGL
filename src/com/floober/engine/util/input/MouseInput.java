package com.floober.engine.util.input;

import com.floober.engine.display.Window;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.util.math.Collisions;
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
	public Vector2i mouseLocation = new Vector2i(), prevLocation = new Vector2i();
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public double WHEEL;
	public boolean WHEEL_UP;
	public boolean WHEEL_DOWN;

	private static int DX, DY;
	
	public static void update() {
		// get the target window's mouse adapter and its attributes
		long windowTarget = MasterRenderer.getTargetWindowID();
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
		windowMouseAdapter.mouseLocation = new Vector2i((int) xPos, (int) yPos);
		DX = windowMouseAdapter.mouseLocation.x - windowMouseAdapter.prevLocation.x;
		DY = windowMouseAdapter.mouseLocation.y - windowMouseAdapter.prevLocation.y;
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
	
	public static boolean isPressed(int button) {
		return windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).buttonState[button];
	}
	public static boolean isClicked(int button) {
		return 	windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).buttonState[button] &&
				!windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).prevButtonState[button];
	}
	public static boolean leftClick() {
		return 	windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).buttonState[LEFT] &&
				!windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).prevButtonState[LEFT];
	}
	public static boolean rightClick() {
		return 	windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).buttonState[RIGHT] &&
				!windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).prevButtonState[RIGHT];
	}

	public static Vector2i getMousePosi() {
		return windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).mouseLocation;
	}
	public static Vector2i getPrevPosi() { return windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).prevLocation; }

	public static Vector2f getMousePos() {
		return new Vector2f(windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).mouseLocation);
	}
	public static Vector2f getPrevPos() { return new Vector2f(windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).prevLocation); }
	
	public static int getX() { return windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).mouseLocation.x(); }
	public static int getY() {
		return windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).mouseLocation.y();
	}

	public static int getDX() { return DX; }
	public static int getDY() { return DY; }

	public static boolean mouseOver(Vector4f bounds) {
		return Collisions.contains(bounds, windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).mouseLocation);
	}

	public static boolean wheelUp() {
		return windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).WHEEL_UP;
	}

	public static boolean wheelDown() {
		return windowMouseAdapters.get(MasterRenderer.getTargetWindowID()).WHEEL_DOWN;
	}
	
}