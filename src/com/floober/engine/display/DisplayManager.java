package com.floober.engine.display;

import com.floober.engine.util.Logger;
import com.floober.engine.util.configuration.Config;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.time.TimeScale;
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

	public static void start() {
		gameStartTime = System.nanoTime();
	}

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
				glfwSetWindowMonitor(windowID, glfwGetPrimaryMonitor(), 0, 0, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT, Display.FPS_CAP);
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
	/**
	 * Get the time since the last frame completion in seconds.
	 * @return The frame time, scaled by the current value in {@code TimeScale}.
	 */
	public static float getFrameTimeSeconds() {
		return delta * TimeScale.getTimeScale();
	}

	public static float getFrameTimeRaw() {
		return delta;
	}

	// frame time in nanoseconds
	public static float getCurrentFrameDelta() {
		return currentFrameDelta * TimeScale.getTimeScale();
	}
	public static long getCurrentFrameDeltaLong() {
		return (long) (currentFrameDelta * TimeScale.getTimeScale());
	}
	public static long getCurrentFrameDeltaRaw() {
		return currentFrameDelta;
	}

	public static float getGameTime() {
		return (System.nanoTime() - gameStartTime) / 1_000_000f / 1000f;
	}

	public static long getGameTimeMS() {
		return (System.nanoTime() - gameStartTime) / 1_000_000;
	}

	// used as timer
	public static long getCurrentTime() {
		return (long) (glfwGetTime() * 1000);
	}

}