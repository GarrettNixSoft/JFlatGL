package com.gnix.jflatgl.core.util.save;

import com.gnix.jflatgl.core.util.configuration.Config;
import com.gnix.jflatgl.core.util.conversion.StringConverter;
import com.gnix.jflatgl.core.util.file.FileUtil;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveSettings {

	private static File settingsFile;
	private static JSONObject settingsJSON;

	private static final String SEP = System.getProperty("file.separator");

	/**
	 * Initialize access to the settings save file(s).
	 * Creates any necessary files if they do not yet exist,
	 * then opens them for read/write access as needed.
	 */
	public static void init() {
		// Determine path to use for save file
		String filePath = System.getProperty("user.home") + SEP + Config.GAME_TITLE_ON_DISK;
		String filePathAndName = filePath + "settings.json";
		// Create a hook to the file
		settingsFile = new File(filePathAndName);
		// If the file does not exist, create it
		try {
			if (settingsFile.createNewFile()) {
				PrintWriter tempWriter = new PrintWriter(settingsFile);
				tempWriter.write("{}");
				tempWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Load the file's contents as JSON
		String fileDataCombined = StringConverter.combineAll(FileUtil.getFileData(settingsFile));
		settingsJSON = new JSONObject(fileDataCombined);
	}

	/**
	 * Save the current state of all settings to disk.
	 */
	public static void save() {
		try {
			PrintWriter writer = new PrintWriter(settingsFile);
			writer.write(settingsJSON.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void modifySettingInt(String settingKey, int value) {
		settingsJSON.put(settingKey, value);
	}

	public static void modifySettingFloat(String settingKey, float value) {
		settingsJSON.put(settingKey, value);
	}

	public static void modifySettingBoolean(String settingKey, boolean value) {
		settingsJSON.put(settingKey, value);
	}

	public static void modifySettingString(String settingKey, String value) {
		settingsJSON.put(settingKey, value);
	}

}
