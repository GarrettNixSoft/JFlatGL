package com.floober.engine.core.util.configuration;

import com.floober.engine.core.splash.SplashRenderer;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.file.FileUtil;
import org.joml.Vector4f;
import org.json.JSONObject;

/**
	The Config class contains all configurable settings that should be
	customized for any project built using this engine, such as the
	path to the game's window icon and default window size.
 */
public class Config {

	public static String CONFIG_NODE;
	public static String SETTINGS_NODE;

	public static String WINDOW_TITLE;

	public static boolean DEBUG_MODE; // development use

	public static boolean CRASH_ON_MISSING_SHADER_UNIFORM;

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

	// Splash screen dimensions
	public static int SPLASH_WIDTH;
	public static int SPLASH_HEIGHT;
	public static boolean SPLASH_TRANSPARENT;

	public static float SPLASH_FAKE_LATENCY;

	public static boolean USE_SPLASH_SCREEN;

	public static JSONObject LOAD_RENDER_SETTINGS;
	public static JSONObject LOGGER_SETTINGS;

	public static void load() {
		JSONObject configJSON = FileUtil.getResourceDataJSON("/config/config.json");

		CONFIG_NODE = configJSON.getString("config_node");
		SETTINGS_NODE = configJSON.getString("settings_node");

		WINDOW_TITLE = configJSON.getString("window_title");

		DEBUG_MODE = configJSON.getBoolean("debug_mode");

		CRASH_ON_MISSING_SHADER_UNIFORM = configJSON.getBoolean("crash_on_missing_shader_uniform");

		ICON_PATH_64 = configJSON.getString("icon_path_64");
		ICON_PATH_48 = configJSON.getString("icon_path_48");
		ICON_PATH_32 = configJSON.getString("icon_path_32");

		INTERNAL_WIDTH = configJSON.getInt("internal_width");
		INTERNAL_HEIGHT = configJSON.getInt("internal_height");

		DEFAULT_WIDTH = configJSON.getInt("default_width");
		DEFAULT_HEIGHT = configJSON.getInt("default_height");

		SPLASH_WIDTH = configJSON.getInt("splash_width");
		SPLASH_HEIGHT = configJSON.getInt("splash_height");
		SPLASH_TRANSPARENT = configJSON.getBoolean("splash_transparent");

		SPLASH_FAKE_LATENCY = configJSON.getFloat("splash_screen_fake_latency");

		USE_SPLASH_SCREEN = configJSON.getBoolean("use_splash_screen");

		LOAD_RENDER_SETTINGS = configJSON.getJSONObject("load_render_settings");

		LOGGER_SETTINGS = configJSON.getJSONObject("logger_settings");

		Logger.setLoggerConfig();
	}

	public static Vector4f getScreenBounds() {
		return new Vector4f(0, 0, INTERNAL_WIDTH, INTERNAL_HEIGHT);
	}

}