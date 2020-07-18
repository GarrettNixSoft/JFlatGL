package com.floober.engine.display;

import com.floober.engine.util.data.Config;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.time.TimeScale;
import com.floober.engine.util.Logger;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;

public class DisplayManager {

	// timings
	private static long lastFrameTime;
	private static long currentFrameDelta;
	private static float delta;
	private static long gameStartTime;

	public static void updateDisplay() {
		glfwSwapBuffers(windowID);
		glfwPollEvents();
		// update time values
		long currentFrameTime = getCurrentTime();
		currentFrameDelta = currentFrameTime - lastFrameTime;
		delta = currentFrameDelta / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static void checkToggleFullscreen() {
		if (KeyInput.isPressed(KeyInput.F11)) {
			Display.fullscreen = !Display.fullscreen;
			Logger.log("Attempting to toggle full screen");
			if (Display.fullscreen) {
				glfwSetWindowMonitor(windowID, glfwGetPrimaryMonitor(), 0, 0, Config.DEFAULT_RESOLUTION_WIDTH, Config.DEFAULT_RESOLUTION_HEIGHT, Display.FPS_CAP);
			}
			else {
				glfwSetWindowMonitor(windowID, MemoryUtil.NULL, 0, 0, Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT, GLFW_DONT_CARE);
				centerWindow();
			}
		}
	}

	public static void centerWindow() {
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		if (vidMode != null)
			glfwSetWindowPos(windowID, (vidMode.width() - Config.DEFAULT_WIDTH) / 2, (vidMode.height() - Config.DEFAULT_HEIGHT) / 2);
		else
			Logger.logError("COULD NOT CENTER WINDOW: glfwGetVideoMode() returned NULL");
	}

	// TIME METHODS
	// frame time in seconds
	public static float getFrameTimeSeconds() {
		return delta * TimeScale.getTimeScale();
	}
	public static float getFrameTimeRaw() {
		return delta;
	}

	// frame time in nanoseconds
	public static long getCurrentFrameDelta() {
		return (long) (currentFrameDelta * TimeScale.getTimeScale());
	}
	public static float getCurrentFrameDeltaFloat() {
		return currentFrameDelta * TimeScale.getTimeScale();
	}
	public static long getCurrentFrameDeltaRaw() {
		return currentFrameDelta;
	}

	public static long getGameRunningTimeMS() { return (System.nanoTime() - gameStartTime) / 1000000; }

	// used as timer
	public static long getCurrentTime() {
		return (long) (glfwGetTime() * 1000);
	}

}