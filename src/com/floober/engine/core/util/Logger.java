package com.floober.engine.core.util;

import com.floober.engine.core.util.conversion.StringConverter;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.io.PrintStream;

public class Logger {
	
	// print stream
	public static PrintStream outStream = System.out;

	// error severity levels
	public static final int LOW = 0;
	public static final int MEDIUM = 1;
	public static final int HIGH = 2;
	public static final int CRITICAL = 3;
	
	// SETTINGS
	// main
	public static boolean logAnything;
	public static boolean logWarnings;
	public static boolean logErrors;
	// startup
	public static boolean logLoaders;
	public static boolean logLoadSuccess;
	public static boolean logLoadErrors;
	public static boolean logLoadGeneral;
	// in-game
	public static boolean logEntityDebug;
	public static boolean logEnemyDebug;
	public static boolean logPlayerDebug;
	// UI
	public static boolean logUIEvents;
	public static boolean logUIInteractions;
	// audio
	public static boolean logAudioStart;
	// event systems
	public static boolean logEvents;
	public static boolean logCutscenes;

	public static void setLoggerConfig() {
		// main
		Logger.logAnything = true;
		Logger.logWarnings = false;
		Logger.logErrors = true;
		// startup
		Logger.logLoaders = false;
		Logger.logLoadSuccess = false;
		Logger.logLoadErrors = true;
		Logger.logLoadGeneral = false;
		// in-game
		Logger.logEntityDebug = true;
		Logger.logEnemyDebug = false;
		Logger.logPlayerDebug = false;
		// event
		Logger.logEvents = false;
		Logger.logCutscenes = true;
		// UI
		Logger.logUIEvents = true;
		Logger.logUIInteractions = true;
		// audio
		Logger.logAudioStart = true;
		// set all to false if log anything is disabled
		Logger.init();
	}

	public static void init() {
		if (!logAnything) {
			logErrors = logLoaders = logLoadSuccess = logLoadGeneral
			= logEntityDebug = logEnemyDebug = logPlayerDebug
			= logAudioStart = logEvents = logCutscenes = false;
		}
	}

	// standard print
	public static void log(String message) {
		if (!logAnything) return;
		outStream.println("[MESSAGE] " + message);
	}

	public static void log(Object object) {
		if (!logAnything) return;
		outStream.println("[MESSAGE] " + object.toString());
	}

	public static void log(Vector4f vec4f) {
		if (!logAnything) return;
		outStream.println("[MESSAGE] " + StringConverter.vec4fToString(vec4f));
	}

	public static void log(Vector2f vec2f) {
		if (!logAnything) return;
		outStream.println("[MESSAGE] " + StringConverter.vec2fToString(vec2f));
	}

	public static void log(Vector2i vec2i) {
		if (!logAnything) return;
		outStream.println("[MESSAGE] " + StringConverter.vec2iToString(vec2i));
	}

	public static void logAudio(String message) {
		if (!logAudioStart) return;
		outStream.println("[AUDIO] " + message);
	}

	// warning print
	public static void logWarning(String warning) {
		if (!logWarnings) return;
		outStream.println("[WARNING] " + warning);
	}
	
	// error print
	public static void logError(String error) {
		if (!logErrors) return;
		outStream.println("*** [ERROR] (No severity specified) " + error);
	}

	/**
	 * Log an error.
	 * @param error The error message.
	 * @param severity The severity level of the error.
	 *                 <br>
	 *                 <br>
	 *                 LOW - Essentially a warning
	 *                 <br>
	 *                 MEDIUM - Not something that needs to be fixed immediately, but should be fixed in any release builds
	 *                 <br>
	 *                 HIGH - Should be dealt with immediately.
	 *                 <br>
	 *                 CRITICAL - The highest severity errors that prevent the game from running. Raising a Critical Error message will terminate the game immediately.
	 */
	public static void logError(String error, int severity) {
		if (!logErrors && severity != CRITICAL) return;
		String message = "*** [ERROR] (Severity: ";
		switch (severity) {
			case LOW -> message = message + " LOW) ";
			case MEDIUM -> message = message + " MEDIUM) ";
			case HIGH -> message = message + " HIGH) ";
			case CRITICAL -> message = "*** [CRITICAL ERROR] ";
		}
		outStream.println(message + error);
		if (severity == CRITICAL) System.exit(-1); // Critical errors force crashes.
	}

	public static void logError(String error, int severity, Exception e) {
		if (!logErrors && severity != CRITICAL) return;
		String message = "*** [ERROR] (Severity: ";
		switch (severity) {
			case LOW -> message = message + " LOW) ";
			case MEDIUM -> message = message + " MEDIUM) ";
			case HIGH -> message = message + " HIGH) ";
			case CRITICAL -> message = "*** [CRITICAL ERROR] ";
		}
		outStream.println(message + error + " (Exception message: " + e.getMessage() + ")");
		if (severity == CRITICAL) System.exit(-1); // Critical errors force crashes.
	}
	
	public static void logLoadComplete(String message) {
		if (!logLoadGeneral) return;
		outStream.println("[LOAD COMPLETE] " + message);
	}
	
	public static void logLoadError(String error) {
		if (!logLoadErrors) return;
		outStream.println("*** [LOAD ERROR] " + error);
	}

	public static void logLoadSuccess(String path) {
		if (!logLoadSuccess) return;
		outStream.println("[LOAD SUCCESS] Successfully loaded: " + path);
	}
	
	// game load debug
	public static void logLoad(String message) {
		if (!logLoaders) return;
		outStream.println("[LOAD] " + message);
	}

	public static void logEvent(String message) {
		if (!logEvents) return;
		outStream.println("[EVENT] " + message);
	}

	public static void logCutscene(String message) {
		if (!logCutscenes) return;
		outStream.println("[CUTSCENE] " + message);
	}

	// UI debug
	public static void logUIEvent(String message) {
		if (!logUIEvents) return;
		outStream.println("[UI EVENT] " + message);
	}

	public static void logUIInteraction(String message) {
		if (!logUIInteractions) return;
		outStream.println("[UI INTERACTION] " + message);
	}
	
	// entity debug
	public static void logEntity(String message) {
		if (!logEntityDebug) return;
		outStream.println("[ENTITY] " + message);
	}
	
	public static void logEnemy(String message) {
		if (!logEnemyDebug) return;
		outStream.println("[ENEMY] " + message);
	}
	
	public static void logPlayer(String message) {
		if (!logPlayerDebug) return;
		outStream.println("[PLAYER] " + message);
	}
	
}