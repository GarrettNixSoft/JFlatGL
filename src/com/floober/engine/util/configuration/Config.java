package com.floober.engine.util.configuration;

import org.joml.Vector4f;

/**
	The Config class contains all configurable settings that should be
	customized for any project built using this engine, such as the
	path to the game's window icon and default window size.
 */
public class Config {

	public static String WINDOW_TITLE = "FlooberEngine Development Version";

	// 3 image files for the game window icons
	public static final String ICON_PATH_64 = "icon/icon64.png";
	public static final String ICON_PATH_48 = "icon/icon48.png";
	public static final String ICON_PATH_32 = "icon/icon32.png";

	// The internal resolution at which the game is rendered;
	// for game logic purposes, these are the bounds of the window
	public static final int INTERNAL_WIDTH = 1920;
	public static final int INTERNAL_HEIGHT = 1080;

	// The default size of the game window.
	// The game will be stretched to this size.
	public static final int DEFAULT_WIDTH = 1600;
	public static final int DEFAULT_HEIGHT = 900;

	public static Vector4f getScreenBounds() {
		return new Vector4f(0, 0, INTERNAL_WIDTH, INTERNAL_HEIGHT);
	}

}