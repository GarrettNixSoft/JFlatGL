package com.floober.engine.util.configuration;

import com.floober.engine.util.Logger;

import java.util.prefs.Preferences;

/**
	Use this class to store and modify game settings.
 */
public class Settings {
	
	// SETTINGS
	// volume
	public static float musicVolume;
	public static float sfxVolume;

	// display
	public static boolean fullscreen;
	public static boolean capFramerate;
	public static int maxFramerateIndex;

	public static final int[] fpsOptions = {30, 60, 90, 120, 144, 165, 240};

	// dialogue
	public static int dialogueCharDelay; // default: 25

	// debug
	public static boolean showFps;
	public static boolean showPlayerDebug;
	public static boolean showDialogueDebug;
	public static boolean showLoadDebug;

	// all-purpose flag for testing features
	public static boolean debugMode = true;

	// KEYS
	// volume
	private static final String musicVolKey = "music_vol";
	private static final String sfxVolKey = "sfx_vol";
	// display
	private static final String fullscreenKey = "fullscreen";
	private static final String capFramerateKey = "cap_framerate";
	private static final String maxFramerateIndexKey = "max_framerate_index";
	// dialogue
	private static final String dialogueCharDelayKey = "dialogue_char_delay";
	// debug
	private static final String debugModeKey = "debug_mode";

	private static final String showFpsKey = "show_fps";
	private static final String showPlayerDebugKey = "show_player_debug";
	private static final String showDialogueDebugKey = "show_dialogue_debug";
	private static final String showLoadDebugKey = "show_load_debug";

	// preferences interface
	private static Preferences prefs;
	private static final String PREF_PATH = "galactichorizon/settings"; // <-- customize this for your project!


	// load (run this on startup)
	public static void load() {
		// connect to stored preferences
		prefs = Preferences.userRoot().node(PREF_PATH);
		// pull all settings from storage
		// volume
		musicVolume = prefs.getFloat(musicVolKey, 0.25f);
		sfxVolume = prefs.getFloat(sfxVolKey, 0.25f);
		// dialogue
		dialogueCharDelay = prefs.getInt(dialogueCharDelayKey, 25);
		// display
		fullscreen = prefs.getBoolean(fullscreenKey, false);
		capFramerate = prefs.getBoolean(capFramerateKey, true);
		maxFramerateIndex = prefs.getInt(maxFramerateIndexKey, 1);
		// debug
		debugMode = prefs.getBoolean(debugModeKey, false);
		showFps = prefs.getBoolean(showFpsKey, false);
		showPlayerDebug = prefs.getBoolean(showPlayerDebugKey, false);
		showDialogueDebug = prefs.getBoolean(showDialogueDebugKey, false);
		showLoadDebug = prefs.getBoolean(showLoadDebugKey, false);
		// TEST
		showLoadDebug = false;
		showPlayerDebug = false;
		// END_TEST
	}

	// save (run this on exit)
	public static void save() {
		// store all settings
		prefs.putFloat(musicVolKey, musicVolume);
		prefs.putFloat(sfxVolKey, sfxVolume);
		prefs.putBoolean(fullscreenKey, fullscreen);
		prefs.putBoolean(debugModeKey, debugMode);
		prefs.putBoolean(showFpsKey, showFps);
		prefs.putBoolean(showPlayerDebugKey, showPlayerDebug);
		prefs.putBoolean(showDialogueDebugKey, showDialogueDebug);
		prefs.putBoolean(showLoadDebugKey, showLoadDebug);
		Logger.log("Settings saved.");
	}
	
}