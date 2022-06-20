package com.floober.engine.core.util.configuration;

import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.file.FileUtil;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import static com.floober.engine.core.util.configuration.SettingValue.DataType.*;

public class Settings {

	private static Preferences prefs;
	private static Map<String, SettingValue> settings;

	private static JSONObject defaults;

	public static boolean getSettingBoolean(String key) {
		return settings.get(key).getAsBoolean();
	}

	public static int getSettingInt(String key) {
		return settings.get(key).getAsInt();
	}

	public static long getSettingLong(String key) {
		return settings.get(key).getAsLong();
	}

	public static float getSettingFloat(String key) {
		return settings.get(key).getAsFloat();
	}

	public static double getSettingDouble(String key) {
		return settings.get(key).getAsDouble();
	}

	public static String getSettingString(String key) {
		return settings.get(key).getAsString();
	}

	public static void setSettingValue(String key, Object value) {
		settings.get(key).setValue(value);
	}

	public static void toggleBooleanSetting(String key) {
		SettingValue setting = settings.get(key);
		if (setting.getDataType() != BOOLEAN) throw new IllegalArgumentException("Tried to toggle non-boolean setting " + key);
		// set its value to NOT the current value
		setting.setValue(!getSettingBoolean(key));
	}

	public static void load() {
		// load preferences from the registry
		prefs = Preferences.userRoot().node(Config.SETTINGS_NODE);
		// load settings JSON metadata
		JSONObject settingsJSON = FileUtil.getResourceDataJSON("/config/settings.json");
		defaults = settingsJSON.getJSONObject("defaults");
		// get all settings
		settings = new HashMap<>();
		for (String key : settingsJSON.keySet()) {
			if (!key.equals("defaults")) {
				String dataType = settingsJSON.getString(key);
				switch (dataType.toLowerCase()) {
					case "boolean" -> fetchAndHoldBoolean(key);
					case "int" -> fetchAndHoldInt(key);
					case "long" -> fetchAndHoldLong(key);
					case "float" -> fetchAndHoldFloat(key);
					case "double" -> fetchAndHoldDouble(key);
					case "string" -> fetchAndHoldString(key);
					default -> throw new RuntimeException("Invalid data type in settings.json: key \"" + key + "\", type \"" + dataType + "\"");
				}
			}
		}
		// log success
		Logger.logLoad("Settings loaded.");
	}

	private static void fetchAndHoldBoolean(String key) {
		boolean defaultValue = defaults.optBoolean(key, false);
		boolean value = prefs.getBoolean(key, defaultValue);
		settings.put(key, new SettingValue(BOOLEAN, value));
	}

	private static void fetchAndHoldInt(String key) {
		int defaultValue = defaults.optInt(key, 0);
		int value = prefs.getInt(key, defaultValue);
		settings.put(key, new SettingValue(INT, value));
	}

	private static void fetchAndHoldLong(String key) {
		long defaultValue = defaults.optLong(key, 0);
		long value = prefs.getLong(key, defaultValue);
		settings.put(key, new SettingValue(LONG, value));
	}

	private static void fetchAndHoldFloat(String key) {
		float defaultValue = defaults.optFloat(key, 0);
		float value = prefs.getFloat(key, defaultValue);
		settings.put(key, new SettingValue(FLOAT, value));
	}

	private static void fetchAndHoldDouble(String key) {
		double defaultValue = defaults.optDouble(key, 0);
		double value = prefs.getDouble(key, defaultValue);
		settings.put(key, new SettingValue(DOUBLE, value));
	}

	private static void fetchAndHoldString(String key) {
		byte[] defaultValue = defaults.optString(key, "").getBytes(StandardCharsets.UTF_8);
		String value = new String(prefs.getByteArray(key, defaultValue), StandardCharsets.UTF_8);
		settings.put(key, new SettingValue(STRING, value));
	}

	public static void save() {
		// for every setting value, save it to the registry
		for (String key : settings.keySet()) {
			SettingValue value = settings.get(key);
			switch (value.getDataType()) {
				case BOOLEAN -> storeBoolean(key, value);
				case INT -> storeInt(key, value);
				case LONG -> storeLong(key, value);
				case FLOAT -> storeFloat(key, value);
				case DOUBLE -> storeDouble(key, value);
				case STRING -> storeString(key, value);
			}
		}
		// log success
		Logger.logLoad("Settings saved.");
	}

	private static void storeBoolean(String key, SettingValue value) {
		boolean booleanValue = value.getAsBoolean();
		prefs.putBoolean(key, booleanValue);
	}

	private static void storeInt(String key, SettingValue value) {
		int intValue = value.getAsInt();
		prefs.putInt(key, intValue);
	}

	private static void storeLong(String key, SettingValue value) {
		long longValue = value.getAsLong();
		prefs.putLong(key, longValue);
	}

	private static void storeFloat(String key, SettingValue value) {
		float floatValue = value.getAsFloat();
		prefs.putFloat(key, floatValue);
	}

	private static void storeDouble(String key, SettingValue value) {
		double doubleValue = value.getAsDouble();
		prefs.putDouble(key, doubleValue);
	}

	private static void storeString(String key, SettingValue value) {
		byte[] stringValue = value.getAsString().getBytes(StandardCharsets.UTF_8);
		prefs.putByteArray(key, stringValue);
	}

}
