package com.floober.engine.display;

import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.util.configuration.Config;
import com.floober.engine.util.math.MathUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.glViewport;

public class Display {

	// internal resolution
	public static int WIDTH = Config.INTERNAL_WIDTH;
	public static int HEIGHT = Config.INTERNAL_HEIGHT;
	public static int WINDOW_WIDTH, WINDOW_HEIGHT;
	public static int FPS_CAP = 144;
	public static final Vector2f SCREEN_RATIO = new Vector2f();
	public static boolean fullscreen = false;

	public static int X_OFFSET = 0;
	public static int Y_OFFSET = 0;
	public static int VIEWPORT_WIDTH;
	public static int VIEWPORT_HEIGHT;

	public static void setViewport(int x, int y, int width, int height) {
		X_OFFSET = x;
		Y_OFFSET = y;
		VIEWPORT_WIDTH = width;
		VIEWPORT_HEIGHT = height;
		glViewport(x, y, width, height);
	}

	/**
	 * Reset the display to the viewport described by the current
	 * values for {@code X_OFFSET}, {@code Y_OFFSET}, {@code WIDTH}, and {@code HEIGHT}.
	 */
	public static void setViewport() {
		glViewport(X_OFFSET, Y_OFFSET, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
	}

	public static float centerX() {
		return WIDTH / 2f;
	}
	public static float centerY() {
		return HEIGHT / 2f;
	}

	public static Vector2f center() {
		return new Vector2f(centerX(), centerY());
	}

	public static Vector2f dimensions() {
		return new Vector2f(Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
	}

	/**
	 * Convert the position and scale values given from pixel units to screen units,
	 * and return the results in a Vector3f that can be passed to a vertex shader.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @param width The width of the element
	 * @param height The height of the element
	 * @param centered Whether to use the width and height values to center the new position on the given (x,y) position
	 * @return A Vector3f representing the given pixel coordinates in screen coordinates.
	 */
	public static Vector3f convertToDisplayPosition(float x, float y, float z, float width, float height, boolean centered) {
		// translate to top left
		if (!centered) {
			x += width / 2;
			y += height / 2;
		}
		// invert y axis
		y = HEIGHT - y;
		// convert pixel coordinates to OpenGL coordinates
		float displayX = -1 + (2f / Display.WIDTH) * x;
		float displayY = -1 + (2f / Display.HEIGHT) * y;
		// convert Z position to [0 ... 1]
		float displayZ = MathUtil.interpolateBounded(0, MasterRenderer.TOP_LAYER, z);
		// return the result
		return new Vector3f(displayX, displayY, displayZ);
	}

	public static Vector2f convertToDisplayPosition2D(Vector2f position) {
		// invert y axis
		float y = HEIGHT - position.y;
		// convert pixel coordinates to OpenGL coordinates
		float displayX = -1 + (2f / Display.WIDTH) * position.x;
		float displayY = -1 + (2f / Display.HEIGHT) * y;
		// return the result
		return new Vector2f(displayX, displayY);
	}

	public static Vector2f convertToScreenPos(Vector2f position) {
		// invert y axis
//		float y = HEIGHT - position.y;
		// convert pixel coordinates to OpenGL coordinates
		float displayX = (1f / Display.WIDTH) * position.x;
		float displayY = (1f / Display.HEIGHT) * position.y;
		// return the result
		return new Vector2f(displayX, displayY);
	}

	/**
	 * Convert the dimensions given from pixel units to screen units, and
	 * return the result in a Vector2f that can be passed to a vertex shader.
	 * @param width The width of the element
	 * @param height The height of the element
	 * @return A Vector2f representing the dimensions of the element on the screen.
	 */
	public static Vector2f convertToDisplayScale(float width, float height) {
		float displayWidth = width / WIDTH;
		float displayHeight = height / HEIGHT;
		return new Vector2f(displayWidth, displayHeight);
	}

	public static float convertToScreenSize(float pixels) {
		return pixels / WIDTH;
	}

}