package com.floober.engine.util;

import java.io.PrintStream;

public class Logger {
	
	// print stream
	public static PrintStream outStream = System.out;
	
	// SETTINGS
	// main
	public static boolean logAnything;
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
		Logger.logErrors = true;
		// startup
		Logger.logLoaders = true;
		Logger.logLoadSuccess = true;
		Logger.logLoadErrors = true;
		Logger.logLoadGeneral = true;
		// in-game
		Logger.logEntityDebug = true;
		Logger.logEnemyDebug = false;
		Logger.logPlayerDebug = true;
		// event
		Logger.logEvents = true;
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

	public static void logAudio(String message) {
		if (!logAudioStart) return;
		outStream.println("[AUDIO] " + message);
	}
	
	// error print
	public static void logError(String error) {
		if (!logErrors) return;
		outStream.println("*** [ERROR] " + error);
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