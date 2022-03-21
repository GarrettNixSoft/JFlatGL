package com.floober.engine.core.scenario;

import com.floober.engine.core.util.Globals;

import java.util.HashMap;
import java.util.prefs.Preferences;

/**
 * The GameFlags class stores all boolean flags to store
 * on the player's system. This allows the game's progress
 * to be saved, loaded, and updated.
 */

public class GameFlags {

	private static HashMap<String, Boolean> gameFlags;

	// preferences API
	private static Preferences prefs;
	private static final String PREF_PATH = "engine/flags";

	public static void init() {
		// connect to stored preferences
		prefs = Preferences.userRoot().node(PREF_PATH);
		// load and store all flags
		gameFlags = new HashMap<>();
		// ... for now, only one flag exists
		gameFlags.put("prologue_shown", prefs.getBoolean("prologue_shown", false));
	}

	public static void storeFlagValue(String flagKey, boolean value) {
		gameFlags.put(flagKey, value);
	}

	public static boolean getFlagValue(String flagKey) {
		return gameFlags.get(flagKey);
	}

	/**
	 * Checks the current level value in {@code Globals.currentLevel} to
	 * determine if a flag should be set, and if so, sets that flag.
	 * Should be called at every instance of the GameState changing.
	 */
	public static void updateOnLevelComplete() {
		if (Globals.currentLevel.equals("misc/prologue")) {
			storeFlagValue("prologue_shown", true);
		}
	}

	/**
	 * Save all game flags to the user's computer.
	 */
	public static void save() {
		// save all flags
		for (String key : gameFlags.keySet()) {
			prefs.putBoolean(key, gameFlags.get(key));
		}
	}

}