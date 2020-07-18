package com.floober.engine.util.data;

/*
	The Config class contains all configurable settings that should be
	customized for any project built using this engine, such as the
	path to the game's window icon and default window size.
 */
public class Config {

	public static String WINDOW_TITLE = "FlooberEngine Dev 0.1.0";

	public static final String ICON_PATH_64 = "icon/icon64.png";
	public static final String ICON_PATH_48 = "icon/icon48.png";
	public static final String ICON_PATH_32 = "icon/icon32.png";

	public static final int DEFAULT_RESOLUTION_WIDTH = 1920;
	public static final int DEFAULT_RESOLUTION_HEIGHT = 1080;

	public static final int DEFAULT_WIDTH = 1600;
	public static final int DEFAULT_HEIGHT = 900;

	public static final int NEAR_CLIP = 0;
	public static final int FAR_CLIP = 100;

}