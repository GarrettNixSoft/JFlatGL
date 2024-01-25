package com.gnix.jflatgl.core.util.configuration;

import com.gnix.jflatgl.core.assets.loaders.ImageLoader;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.conversion.DisplayScale;
import com.gnix.jflatgl.core.util.data.Pair;
import com.gnix.jflatgl.core.util.file.FileUtil;
import org.joml.Vector4f;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;

/**
	The Config class contains all configurable settings that should be
	customized for any project built using this engine, such as the
	path to the game's window icon and default window size.
 */
public class Config {

	public static String WINDOW_TITLE;
	public static String GAME_TITLE_ON_DISK;

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
	public static DisplayScale.AspectRatio ASPECT_RATIO;

	// The default size of the game window.
	// The game will be stretched to this size.
	public static int DEFAULT_WIDTH;
	public static int DEFAULT_HEIGHT;
	public static boolean SCALE_TO_MONITOR;

	// Splash screen settings
	public static int SPLASH_WIDTH;
	public static int SPLASH_HEIGHT;
	public static boolean SCALE_SPLASH_SCREEN;
	public static boolean SPLASH_TRANSPARENT;
	public static float SPLASH_FAKE_LATENCY;
	public static boolean USE_SPLASH_SCREEN;

	// Control scheme settings
	public static boolean SUPPORT_INPUT_SWITCHING;

	// Advanced window settings
	public static boolean WINDOW_DECORATION;
	public static boolean WINDOW_TRANSPARENCY;
	public static boolean WINDOW_ON_TOP;
	public static Vector4f WINDOW_CLEAR_COLOR;

	// Debug settings
	public static JSONObject LOGGER_SETTINGS;

	public static void load() {
		JSONObject configJSON = FileUtil.getOrCreateResourceDataJSON("/config/config.json", getDefaultConfig());

		CONFIG_NODE = configJSON.optString("config_node", "my_game/config");
		SETTINGS_NODE = configJSON.optString("settings_node", "my_game/settings");
		FLAGS_NODE = configJSON.optString("flags_node", "my_game/flags");

		DEFAULT_CONTROLS_DIR = configJSON.optString("default_controls", "config/controls");
		CONTROLS_PATH = configJSON.optString("controls_path", "/controls");

		ENABLE_GAMEPAD_CURSOR = configJSON.optBoolean("enable_gamepad_cursor", false);
		GAMEPAD_CURSOR_PATH = configJSON.optString("gamepad_cursor", "");

		WINDOW_TITLE = configJSON.optString("window_title", "A JFlatGL Game");
		GAME_TITLE_ON_DISK = configJSON.optString("game_title_on_disk", "A JFlatGL Game");

		DEBUG_MODE = configJSON.optBoolean("debug_mode", true);

		CRASH_ON_MISSING_SHADER_UNIFORM = configJSON.optBoolean("crash_on_missing_shader_uniform", true);

		ICON_PATH_64 = configJSON.optString("icon_path_64", "icon/icon64.png");
		ICON_PATH_48 = configJSON.optString("icon_path_48", "icon/icon48.png");
		ICON_PATH_32 = configJSON.optString("icon_path_32", "icon/icon32.png");

		ICON_IMAGE = ImageLoader.loadBufferedImageOptional(ICON_PATH_64, 64, 64);

		INTERNAL_WIDTH = configJSON.optInt("internal_width", 1920);
		INTERNAL_HEIGHT = configJSON.optInt("internal_height", 1080);

		ASPECT_RATIO = switch (configJSON.optString("aspect_ratio", "none")) {
			case "16:9" -> DisplayScale.AspectRatio.D_16_9;
			case "16:10", "8:5" -> DisplayScale.AspectRatio.D_16_10;
			case "4:3" -> DisplayScale.AspectRatio.D_4_3;
			default -> DisplayScale.AspectRatio.NONE;
		};

		DEFAULT_WIDTH = configJSON.optInt("default_width", 1280);
		DEFAULT_HEIGHT = configJSON.optInt("default_height", 720);
		SCALE_TO_MONITOR = configJSON.optBoolean("scale_to_monitor", false);

		USE_SPLASH_SCREEN = configJSON.optBoolean("use_splash_screen", false);

		SCALE_SPLASH_SCREEN = configJSON.optBoolean("scale_splash_screen", true);
		SPLASH_WIDTH = configJSON.optInt("splash_width", 500);
		SPLASH_HEIGHT = configJSON.optInt("splash_height", 300);

		if (SCALE_SPLASH_SCREEN) {
			Pair<Integer, Integer> dimensions = DisplayScale.getDimensionsScaledFor1080p(SPLASH_WIDTH, SPLASH_HEIGHT);
			SPLASH_WIDTH = dimensions.data1();
			SPLASH_HEIGHT = dimensions.data2();
		}

		SPLASH_TRANSPARENT = configJSON.optBoolean("splash_transparent", false);
		SPLASH_FAKE_LATENCY = configJSON.optFloat("splash_screen_fake_latency", 0);

		SUPPORT_INPUT_SWITCHING = configJSON.optBoolean("support_input_switching", false);

		WINDOW_DECORATION = configJSON.optBoolean("decorate_window", true);
		WINDOW_TRANSPARENCY = configJSON.optBoolean("transparent_window", false);
		WINDOW_ON_TOP = configJSON.optBoolean("always_on_top", false);

		JSONArray clearColorArr = configJSON.optJSONArray("clear_color");
		if (clearColorArr != null) {
			WINDOW_CLEAR_COLOR = new Vector4f();
			WINDOW_CLEAR_COLOR.x = clearColorArr.getFloat(0);
			WINDOW_CLEAR_COLOR.y = clearColorArr.getFloat(1);
			WINDOW_CLEAR_COLOR.z = clearColorArr.getFloat(2);
			WINDOW_CLEAR_COLOR.w = clearColorArr.getFloat(3);
		}
		else WINDOW_CLEAR_COLOR = new Vector4f(0, 0, 0, 1);

		LOGGER_SETTINGS = configJSON.optJSONObject("logger_settings");
		if (LOGGER_SETTINGS == null) LOGGER_SETTINGS = getDefaultLoggerSettings();

		Logger.setLoggerConfig();
	}

	public static Vector4f getScreenBounds() {
		return new Vector4f(0, 0, INTERNAL_WIDTH, INTERNAL_HEIGHT);
	}

	private static JSONObject getDefaultLoggerSettings() {
		JSONObject logger = new JSONObject();

		logger.put("log_anything", true);
		logger.put("log_warnings", true);
		logger.put("log_errors", true);
		logger.put("log_loaders", true);
		logger.put("log_load_success", true);
		logger.put("log_load_errors", true);
		logger.put("log_load_general", true);
		logger.put("log_entity_debug", true);
		logger.put("log_enemy_debug", true);
		logger.put("log_player_debug", true);
		logger.put("log_events", true);
		logger.put("log_cutscenes", true);
		logger.put("log_gui_events", true);
		logger.put("log_gui_interactions", true);
		logger.put("log_audio_start", true);

		return logger;
	}

	private static JSONObject getDefaultConfig() {
		JSONObject config = new JSONObject();

		config.put("config_node", "my_game/config");
		config.put("settings_node", "my_game/settings");
		config.put("flags_node", "my_game/flags");

		config.put("default_controls", "config/controls");
		config.put("controls_path", "/controls");

		config.put("enable_gamepad_cursor", false);
		config.put("gamepad_cursor", "");

		config.put("window_title", "A JFlatGL Game");
		config.put("game_title_on_disk", "A JFlatGL Game");

		config.put("debug_mode", true);
		config.put("crash_on_missing_shader_uniform", true);

		config.put("icon_path_64", "icon/icon64.png");
		config.put("icon_path_48", "icon/icon48.png");
		config.put("icon_path_32", "icon/icon32.png");

		config.put("internal_width", 1920);
		config.put("internal_height", 1080);
		config.put("aspect_ratio", "16:9");

		config.put("default_width", 1280);
		config.put("default_height", 720);
		config.put("scale_to_monitor", false);

		config.put("use_splash_screen", false);
		config.put("scale_splash_screen", true);
		config.put("splash_width", 500);
		config.put("splash_height", 300);
		config.put("splash_transparent", false);
		config.put("splash_screen_fake_latency", 0);

		config.put("support_input_switching", false);

		config.put("logger_settings", getDefaultLoggerSettings());

		return config;
	}

}
