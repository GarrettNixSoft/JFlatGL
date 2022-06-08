package com.floober.engine.core.util.configuration;

import com.floober.engine.core.util.file.FileUtil;
import org.joml.Vector4f;
import org.json.JSONObject;

/**
	The Config class contains all configurable settings that should be
	customized for any project built using this engine, such as the
	path to the game's window icon and default window size.
 */
public class Config {

	public static String WINDOW_TITLE;

	public static boolean DEBUG_MODE; // development use

	// 3 image files for the game window icons
	public static String ICON_PATH_64;
	public static String ICON_PATH_48;
	public static String ICON_PATH_32;

	// The internal resolution at which the game is rendered;
	// for game logic purposes, these are the bounds of the window
	public static int INTERNAL_WIDTH;
	public static int INTERNAL_HEIGHT;

	// The default size of the game window.
	// The game will be stretched to this size.
	public static int DEFAULT_WIDTH;
	public static int DEFAULT_HEIGHT;

	public static boolean USE_LOAD_RENDERER;

	public static boolean FULLSCREEN;

	public static JSONObject LOAD_RENDER_SETTINGS;

	public static void load() {
		JSONObject configJSON = FileUtil.getJSON("/config/config.json");

		WINDOW_TITLE = configJSON.getString("window_title");

		DEBUG_MODE = configJSON.getBoolean("debug_mode");

		ICON_PATH_64 = configJSON.getString("icon_path_64");
		ICON_PATH_48 = configJSON.getString("icon_path_48");
		ICON_PATH_32 = configJSON.getString("icon_path_32");

		INTERNAL_WIDTH = configJSON.getInt("internal_width");
		INTERNAL_HEIGHT = configJSON.getInt("internal_height");

		DEFAULT_WIDTH = configJSON.getInt("default_width");
		DEFAULT_HEIGHT = configJSON.getInt("default_height");

		USE_LOAD_RENDERER = configJSON.getBoolean("use_load_renderer");

		FULLSCREEN = configJSON.getBoolean("fullscreen");

		LOAD_RENDER_SETTINGS = configJSON.getJSONObject("load_render_settings");
	}

	public static Vector4f getScreenBounds() {
		return new Vector4f(0, 0, INTERNAL_WIDTH, INTERNAL_HEIGHT);
	}

}