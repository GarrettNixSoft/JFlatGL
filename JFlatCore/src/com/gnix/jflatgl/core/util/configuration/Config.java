package com.gnix.jflatgl.core.util.configuration;

import com.gnix.jflatgl.core.assets.loaders.ImageLoader;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.conversion.DisplayScale;
import com.gnix.jflatgl.core.util.data.Pair;
import com.gnix.jflatgl.core.util.file.FileUtil;
import org.joml.Vector4f;
import org.json.JSONObject;

import java.awt.image.BufferedImage;

/**
	The Config class contains all configurable settings that should be
	customized for any project built using this engine, such as the
	path to the game's window icon and default window size.
 */
public class Config {

	public static String WINDOW_TITLE;

	// preferences nodes
	public static String CONFIG_NODE;
	public static String SETTINGS_NODE;
	public static String FLAGS_NODE;

	// where the controls file should be stored
	public static String DEFAULT_CONTROLS_DIR;
	public static String CONTROLS_PATH;

	public static boolean ENABLE_GAMEPAD_CURSOR;
	public static String GAMEPAD_CURSOR_PATH;

	public static boolean DEBUG_MODE; // development use

	public static boolean CRASH_ON_MISSING_SHADER_UNIFORM;

	// 3 image files for the game window icons
	public static String ICON_PATH_64;
	public static String ICON_PATH_48;
	public static String ICON_PATH_32;
	public static BufferedImage ICON_IMAGE;

	// The internal resolution at which the game is rendered;
	// for game logic purposes, these are the bounds of the window
	public static int INTERNAL_WIDTH;
	public static int INTERNAL_HEIGHT;

	// The default size of the game window.
	// The game will be stretched to this size.
	public static int DEFAULT_WIDTH;
	public static int DEFAULT_HEIGHT;

	// Splash screen settings
	public static int SPLASH_WIDTH;
	public static int SPLASH_HEIGHT;
	public static boolean SCALE_SPLASH_SCREEN;
	public static boolean SPLASH_TRANSPARENT;
	public static float SPLASH_FAKE_LATENCY;
	public static boolean USE_SPLASH_SCREEN;

	// Control scheme settings
	public static boolean SUPPORT_INPUT_SWITCHING;

	// Debug settings
	public static JSONObject LOGGER_SETTINGS;

	public static void load() {
		JSONObject configJSON = FileUtil.getResourceDataJSON("/config/config.json");

		CONFIG_NODE = configJSON.getString("config_node");
		SETTINGS_NODE = configJSON.getString("settings_node");
		FLAGS_NODE = configJSON.getString("flags_node");

		DEFAULT_CONTROLS_DIR = configJSON.getString("default_controls");
		CONTROLS_PATH = configJSON.getString("controls_path");

		ENABLE_GAMEPAD_CURSOR = configJSON.optBoolean("enable_gamepad_cursor", false);
		GAMEPAD_CURSOR_PATH = configJSON.optString("gamepad_cursor", "");

		WINDOW_TITLE = configJSON.getString("window_title");

		DEBUG_MODE = configJSON.getBoolean("debug_mode");

		CRASH_ON_MISSING_SHADER_UNIFORM = configJSON.getBoolean("crash_on_missing_shader_uniform");

		ICON_PATH_64 = configJSON.getString("icon_path_64");
		ICON_PATH_48 = configJSON.getString("icon_path_48");
		ICON_PATH_32 = configJSON.getString("icon_path_32");

		ICON_IMAGE = ImageLoader.loadBufferedImage(ICON_PATH_64);

		INTERNAL_WIDTH = configJSON.getInt("internal_width");
		INTERNAL_HEIGHT = configJSON.getInt("internal_height");

		DEFAULT_WIDTH = configJSON.getInt("default_width");
		DEFAULT_HEIGHT = configJSON.getInt("default_height");

		SCALE_SPLASH_SCREEN = configJSON.getBoolean("scale_splash_screen");
		SPLASH_WIDTH = configJSON.getInt("splash_width");
		SPLASH_HEIGHT = configJSON.getInt("splash_height");

		if (SCALE_SPLASH_SCREEN) {
			Pair<Integer, Integer> dimensions = DisplayScale.getDimensionsScaledFor1080p(SPLASH_WIDTH, SPLASH_HEIGHT);
			SPLASH_WIDTH = dimensions.data1();
			SPLASH_HEIGHT = dimensions.data2();
		}

		SPLASH_TRANSPARENT = configJSON.getBoolean("splash_transparent");
		SPLASH_FAKE_LATENCY = configJSON.getFloat("splash_screen_fake_latency");
		USE_SPLASH_SCREEN = configJSON.getBoolean("use_splash_screen");

		SUPPORT_INPUT_SWITCHING = configJSON.optBoolean("support_input_switching", false);

		LOGGER_SETTINGS = configJSON.getJSONObject("logger_settings");

		Logger.setLoggerConfig();
	}

	public static Vector4f getScreenBounds() {
		return new Vector4f(0, 0, INTERNAL_WIDTH, INTERNAL_HEIGHT);
	}

}
