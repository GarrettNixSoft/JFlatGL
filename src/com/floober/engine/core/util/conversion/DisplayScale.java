package com.floober.engine.core.util.conversion;

import com.floober.engine.core.util.data.Pair;

import java.awt.*;

public class DisplayScale {

	public static int DISPLAY_WIDTH;
	public static int DISPLAY_HEIGHT;
	public static double SCALE_W;
	public static double SCALE_H;

	static {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		DISPLAY_WIDTH = gd.getDisplayMode().getWidth();
		DISPLAY_HEIGHT = gd.getDisplayMode().getHeight();

		SCALE_W = DISPLAY_WIDTH / 1920.0;
		SCALE_H = DISPLAY_HEIGHT / 1080.0;
	}

	/**
	 * Get a pair of integers scaled to the user's current display. The Pair
	 * returned represents the dimensions for the user's current display
	 * resolution that will result in a physical size equivalent to the given
	 * dimensions on an equally sized 1080p monitor.
	 *
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

}
