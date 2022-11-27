package com.gnix.jflatgl.core.util.conversion;

import com.gnix.jflatgl.core.util.data.Pair;

import java.awt.*;

public class DisplayScale {

	public enum AspectRatio {
		D_16_9, D_16_10, D_4_3,
		NONE
	}

	public static int DISPLAY_WIDTH;
	public static int DISPLAY_HEIGHT;
	public static double SCALE_W;
	public static double SCALE_H;

	static {

		// fetch the display dimensions
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DISPLAY_WIDTH = gd.getDisplayMode().getWidth();
		DISPLAY_HEIGHT = gd.getDisplayMode().getHeight();

		// get 1080p scaling values
		SCALE_W = DISPLAY_WIDTH / 1920.0;
		SCALE_H = DISPLAY_HEIGHT / 1080.0;
	}

	/**
	 * Get a pair of integers scaled to the user's current display. The Pair
	 * returned represents the dimensions for the user's current display
	 * resolution that will result in a physical size equivalent to the given
	 * dimensions on an equally sized 1080p monitor.
	 * <br>
	 * For example, if the user is running the game on a 4K display, the pair
	 * returned will be scaled up by a factor of 2 on both axes.
	 *
	 * @param width the desired pixel width on a 1080p display
	 * @param height the desired pixel height on a 1080p display
	 * @return a Pair of integers scaled to the user's display resolution
	 */
	public static Pair<Integer, Integer> getDimensionsScaledFor1080p(int width, int height) {

		int w = (int) (width * SCALE_W);
		int h = (int) (height * SCALE_H);

		return new Pair<>(w, h);

	}

	public static Pair<Integer, Integer> getDimensionsScaledForResolution(int width, int height, double resolutionWidth, double resolutionHeight) {

		double scaleX = DISPLAY_WIDTH / resolutionWidth;
		double scaleY = DISPLAY_HEIGHT / resolutionHeight;

		int w = (int) (width * scaleX);
		int h = (int) (height * scaleY);

		return new Pair<>(w, h);

	}

	public static Pair<Integer, Integer> getDimensionsScaledToMonitor(double width, AspectRatio aspectRatio) {

		int w = (int) (width * SCALE_W);

		int h = switch (aspectRatio) {
			case D_16_9 -> w * 9 / 16;
			case D_16_10 -> w * 10 / 16;
			case D_4_3 -> w * 3 / 4;
			case NONE -> w;
			case null -> throw new RuntimeException();
		};

		return new Pair<>(w, h);

	}

}
