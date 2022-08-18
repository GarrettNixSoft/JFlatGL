package com.floober.engine.core.input;

import com.floober.engine.core.assets.loaders.ImageLoader;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.textures.RawTextureData;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.conversion.VectorConverter;
import com.floober.engine.core.util.math.Collisions;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

public class Cursor {

	// ******************************** MODE SWITCHING ********************************
	public enum CursorMode {
		RAW_MOUSE, GAMEPAD
	}

	// default to raw mouse input
	private static CursorMode CURSOR_MODE = CursorMode.RAW_MOUSE;

	// a custom cursor for gamepad mode
	private static long cursorObj;

	public static void init() {
		GLFWImage cursorImage = GLFWImage.malloc();
		RawTextureData cursorImageData = ImageLoader.loadImageRaw(Config.GAMEPAD_CURSOR_PATH);
		cursorImage.set(cursorImageData.width, cursorImageData.height, cursorImageData.buffer);
		cursorObj = glfwCreateCursor(cursorImage, cursorImageData.width / 2, cursorImageData.height / 2);
//		cursorObj = glfwCreateCursor(cursorImage, 0, 0);

		if (cursorObj == MemoryUtil.NULL) {
			PointerBuffer pointerBuffer = PointerBuffer.allocateDirect(4096);
			glfwGetError(pointerBuffer);
			String errorMessage = pointerBuffer.getStringUTF8();
			throw new RuntimeException("Failed to create cursor! Error: " + errorMessage);
		}
	}

	public static void setCursorMode(CursorMode mode) {

		// assign the mode
		CURSOR_MODE = mode;

		// handle GLFW cursor settings
		if (CURSOR_MODE == CursorMode.GAMEPAD) {

			// hide the mouse when over this window
//			glfwSetInputMode(DisplayManager.primaryWindowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

			// set the cursor to the custom one
			glfwSetCursor(primaryWindowID, cursorObj);

		}
		else {
//			glfwSetInputMode(DisplayManager.primaryWindowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			glfwSetCursor(primaryWindowID, MemoryUtil.NULL);
		}

	}

	public static CursorMode getCursorMode(CursorMode mode) {
		return CURSOR_MODE;
	}

	// ******************************** GAMEPAD STICK SETTINGS ********************************
	public enum CursorStick {
		LEFT, RIGHT
	}

	// default to the right stick
	private static CursorStick CURSOR_STICK = CursorStick.RIGHT;

	public static CursorStick getCursorStick() {
		return CURSOR_STICK;
	}

	public static void setCursorStick(CursorStick cursorStick) {
		Cursor.CURSOR_STICK = cursorStick;
	}

	// ******************************** GAMEPAD SENSITIVITY ********************************
	private static double sensitivity = 1;
	private static final double defaultSpeed = 5;

	public static double getSensitivity() {
		return sensitivity;
	}

	public static void setSensitivity(double sensitivity) {
		Cursor.sensitivity = sensitivity;
	}

	// ******************************** TRACKING ********************************
	private static int x;
	private static int y;
	private static int prevX;
	private static int prevY;
	public static Vector2f mouseLocation = new Vector2f(), prevLocation = new Vector2f();

	private static float DX, DY;
	private static float prevDX, prevDY;

	private static boolean GAMEPAD_MOVED_CURSOR;

	public static void update() {

		// remove the gamepad moved cursor flag
		GAMEPAD_MOVED_CURSOR = false;

		// store last position
		prevX = x;
		prevY = y;

		// move the delta values to the previous slots
		prevLocation = mouseLocation;
		prevDX = DX;
		prevDY = DY;

		// update mouse buttons and the wheel
		MouseInput.update();

		// get the game window
		Window currentWindow = MasterRenderer.getTargetWindow();

		// get the mouse position from GLFW
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(primaryWindowID, xBuffer, yBuffer);
		double xRaw = xBuffer.get();
		double yRaw = yBuffer.get();
		double xPos = (xRaw + currentWindow.getMouseXOffset()) * currentWindow.getMouseXRatio();
		double yPos = (yRaw + currentWindow.getMouseYOffset()) * currentWindow.getMouseYRatio();

		// when in gamepad mode, move according to the gamepad input
		if (CURSOR_MODE == CursorMode.GAMEPAD) {

			// get the gamepad stick axes
			int xAxis = CURSOR_STICK == CursorStick.LEFT ? GamepadNames.GAMEPAD_VALUES.get("left_stick_x") : GamepadNames.GAMEPAD_VALUES.get("right_stick_x");
			int yAxis = CURSOR_STICK == CursorStick.LEFT ? GamepadNames.GAMEPAD_VALUES.get("left_stick_y") : GamepadNames.GAMEPAD_VALUES.get("right_stick_y");
			float xa = GamepadInput.getAxis(0, xAxis);
			float ya = GamepadInput.getAxis(0, yAxis);

			if (xa != 0 || ya != 0) {

				GAMEPAD_MOVED_CURSOR = true;

				double dx = xa * sensitivity * defaultSpeed;
				double dy = ya * sensitivity * defaultSpeed;

				x += (int) dx;
				y += (int) dy;

				DX = (float) dx;
				DY = (float) dy;

				glfwSetCursorPos(primaryWindowID, x, y);

//				Logger.log("Gamepad moved cursor by (%.2f, %.2f)", dx, dy);
			}

		}

		if (!GAMEPAD_MOVED_CURSOR) {

			// the gamepad wasn't moved, so allow the mouse itself to move

			// store the new location
			mouseLocation = new Vector2f((float) xPos, (float) yPos);
			x = (int) xPos;
			y = (int) yPos;

			// update the delta values
			DX = (float) xPos - prevLocation.x;
			DY = (float) yPos - prevLocation.y;
		}

	}

	public static boolean gamepadMovedCursor() {
		return GAMEPAD_MOVED_CURSOR;
	}

	// ******************************** FETCHING POSITION INFO ********************************
	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	public static int getDX() {
		return x - prevX;
	}

	public static int getDY() {
		return y - prevY;
	}

	public static float getPrevDX() {
		return prevDX;
	}

	public static float getPrevDY() {
		return prevDY;
	}

	public static Vector2i getCursorPosInt() {
		return VectorConverter.vec2fToVec2i(mouseLocation);
	}

	public static Vector2i getPrevPosInt() {
		return VectorConverter.vec2fToVec2i(prevLocation);
	}

	public static Vector2f getCursorPos() {
		return new Vector2f(mouseLocation);
	}

	public static Vector2f getPrevPos() {
		return new Vector2f(prevLocation);
	}

	public static int getScreenX() {
		return DisplayManager.getPrimaryGameWindow().getX() + getX();
	}

	public static int getScreenY() {
		return DisplayManager.getPrimaryGameWindow().getY() + getY();
	}

	public static boolean mouseOver(Vector4f bounds) {
		return Collisions.contains(bounds, mouseLocation);
	}


	// ******************************** STATE CHANGE INFO ********************************
	public static boolean anyInput() {
		return cursorMovedByMouse() || MouseInput.wheelMoved() || GAMEPAD_MOVED_CURSOR;
	}

	public static boolean anyMouseInput() {
		return cursorMovedByMouse() || MouseInput.anyInput();
	}

	public static boolean cursorMovedByMouse() {
		return (getDX() != 0 || getDY() != 0) && !gamepadMovedCursor();
	}

}
