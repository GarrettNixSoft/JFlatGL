package com.floober.engine.display;

import com.floober.engine.util.data.Config;
import com.floober.engine.util.math.MathUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Display {

	public static int WIDTH = Config.DEFAULT_RESOLUTION_WIDTH;
	public static int HEIGHT = Config.DEFAULT_RESOLUTION_HEIGHT;
	public static int FPS_CAP = 144;

	public static boolean fullscreen = false;

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
		float displayZ = MathUtil.smoothstep(Config.NEAR_CLIP, Config.FAR_CLIP, z);
		// return the result
		return new Vector3f(displayX, displayY, displayZ);
	}

	public static Vector2f convertToDisplayScale(float width, float height) {
		float displayWidth = width / WIDTH;
		float displayHeight = height / HEIGHT;
		return new Vector2f(displayWidth, displayHeight);
	}

	public static float convertToScreenSize(float pixels) {
		return pixels / WIDTH;
	}

}