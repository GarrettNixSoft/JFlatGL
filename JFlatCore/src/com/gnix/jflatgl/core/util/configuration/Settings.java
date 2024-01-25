package com.gnix.jflatgl.core.util.configuration;

import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.file.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class Settings {

	private static Preferences prefs;
	private static Map<String, Setting> settings;

	public static boolean getSettingBoolean(String key) {
		return settings.get(key).getValue().getAsBoolean();
	}

	public static int getSettingInt(String key) {
		return settings.get(key).getValue().getAsInt();
	}

	public static short getSettingShort(String key) { return settings.get(key).getValue().getAsShort(); }

	public static long getSettingLong(String key) {
		return settings.get(key).getValue().getAsLong();
	}

	public static float getSettingFloat(String key) {
		return settings.get(key).getValue().getAsFloat();
	}

	public static double getSettingDouble(String key) {
		return settings.get(key).getValue().getAsDouble();
	}

	public static String getSettingString(String key) {
		return settings.get(key).getValue().getAsString();
	}

	public static void setSettingValue(String key, Object value) {
		settings.get(key).getValue().setValue(value);
	}

	public static void toggleBooleanSetting(String key) {
		Setting setting = settings.get(key);
		if (setting.getDataType() != Boolean.class) throw new IllegalArgumentException("Tried to toggle non-boolean setting " + key);
		// set its value to NOT the current value
		setting.setValue(!getSettingBoolean(key));
	}

	public static void load() {
		// load preferences from the registry
		prefs = Preferences.userRoot().node(Config.SETTINGS_NODE);
		// load settings JSON metadata
		JSONObject settingsJSON = FileUtil.getOrCreateResourceDataJSON("/config/settings.json", getDefaultSettings());
		if (!settingsJSON.has("settings")) Logger.logError("Settings JSON contains: " + settingsJSON.toString(4));
		JSONArray settingsArray = settingsJSON.getJSONArray("settings");
		// get all settings
		settings = new HashMap<>();
		for (int i = 0; i < settingsArray.length(); i++) {
			JSONObject setting = settingsArray.getJSONObject(i);
			String name = setting.getString("name");
			String displayName = setting.optString("displayName", name);
			String dataTypeStr = setting.getString("type");
			Class<?> dataType = getDataType(dataTypeStr);
			JSONObject rulesObj = setting.optJSONObject("rules");
			if (rulesObj == null) rulesObj = new JSONObject();
			SettingRules rules = new SettingRules(rulesObj, dataType);
			SettingValue value = fetchStoredValueIfPresent(name, dataType, rules.getDefaultValue());
			if (value == null) value = new SettingValue(rules.getDefaultValue());
			settings.put(name, new Setting(name, displayName, dataType, value, rules));
		}
		// log success
		Logger.logLoad("Settings loaded.");
	}

	private static SettingValue fetchStoredValueIfPresent(String key, Class<?> dataType, Object defaultValue) {
		// Fail fast if we don't have any setting with this key
		if (!settings.containsKey(key))
			return null;

		if (dataType == Boolean.class)	{
			boolean value = prefs.getBoolean(key, (Boolean) defaultValue);
			return new SettingValue(value);
		}
		else if (dataType == Integer.class)	{
			int value = prefs.getInt(key, (Integer) defaultValue);
			return new SettingValue(value);
		}
		else if (dataType == Short.class) {
			short value = (short) prefs.getInt(key, (Short) defaultValue);
			return new SettingValue(value);
		}
		else if (dataType == Long.class) {
			long value = prefs.getLong(key, (Long) defaultValue);
			return new SettingValue(value);
		}
		else if (dataType == Float.class) {
			float value = prefs.getFloat(key, (Float) defaultValue);
			return new SettingValue(value);
		}
		else if (dataType == Double.class) {
			double value = prefs.getDouble(key, (Double) defaultValue);
			return new SettingValue(value);
		}
		else if (dataType == String.class) {
			byte[] valueBytes = ((String) settings.get(key).getDefaultValue()).getBytes(StandardCharsets.UTF_8);
			String value = new String(prefs.getByteArray(key, valueBytes), StandardCharsets.UTF_8);
			return new SettingValue(value);
		}
		else {
			return new SettingValue(new Object());
		}
	}

	public static void save() {
		// for every setting value, save it to the registry
		for (String key : settings.keySet()) {
			Setting setting = settings.get(key);
			if 		(setting.getDataType() == Boolean.class)	storeBoolean(key, setting.getValue().getAsBoolean());
			else if (setting.getDataType() == Integer.class)	storeInt(key, setting.getValue().getAsInt());
			else if (setting.getDataType() == Short.class)		storeShort(key, setting.getValue().getAsShort());
			else if (setting.getDataType() == Long.class)		storeLong(key, setting.getValue().getAsLong());
			else if (setting.getDataType() == Float.class)		storeFloat(key, setting.getValue().getAsFloat());
			else if (setting.getDataType() == Double.class)		storeDouble(key, setting.getValue().getAsDouble());
			else if (setting.getDataType() == String.class)		storeString(key, setting.getValue().getAsString());
		}
		// log success
		Logger.logLoad("Settings saved.");
	}

	private static void storeBoolean(String key, Boolean value) {
		prefs.putBoolean(key, value);
	}

	private static void storeInt(String key, Integer value) {
		prefs.putInt(key, value);
	}

	private static void storeShort(String key, Short value) {
		prefs.putInt(key, value);
	}

	private static void storeLong(String key, Long value) {
		prefs.putLong(key, value);
	}

	private static void storeFloat(String key, Float value) {
		prefs.putFloat(key, value);
	}

	private static void storeDouble(String key, Double value) {
		prefs.putDouble(key, value);
	}

	private static void storeString(String key, String value) {
		byte[] stringValue = value.getBytes(StandardCharsets.UTF_8);
		prefs.putByteArray(key, stringValue);
	}



	private static Class<?> getDataType(String type) {
		return switch (type) {
			case "boolean" -> Boolean.class;
			case "int" -> Integer.class;
			case "short" -> Short.class;
			case "long" -> Long.class;
			case "float" -> Float.class;
			case "double" -> Double.class;
			case "string" -> String.class;
			default -> Object.class;
		};
	}

	private static JSONObject getDefaultSettings() {
		JSONObject settingsObj = new JSONObject();

		JSONArray settings = new JSONArray();

		// ================ fullscreen ================ // TODO change to display type and modify DisplayManager to check it during initGameWindow()
		JSONObject displayMode = new JSONObject();
		displayMode.put("name", "display");
		displayMode.put("displayName", "Display Mode");
		displayMode.put("type", "string");
		JSONObject displayRulesObj = new JSONObject();
		JSONArray displayRules = new JSONArray();
		JSONObject fullscreen = new JSONObject();
		fullscreen.put("value", "fullscreen");
		fullscreen.put("displayValue", "Fullscreen");
		JSONObject windowed = new JSONObject();
		windowed.put("value", "windowed");
		windowed.put("displayValue", "Windowed");
		JSONObject windowedFS = new JSONObject();
		windowedFS.put("value", "windowed_fullscreen");
		windowedFS.put("displayValue", "Windowed Fullscreen");
		displayRules.put(fullscreen);
		displayRules.put(windowed);
		displayRules.put(windowedFS);
		displayRulesObj.put("options", displayRules);
		displayMode.put("rules", displayRulesObj);
		settings.put(displayMode);

		// ================ master_volume ================
		JSONObject masterVolume = new JSONObject();
		masterVolume.put("name", "master_volume");
		masterVolume.put("displayName", "Master Volume");
		masterVolume.put("type", "int");

		JSONObject masterVolumeRules = new JSONObject();
		masterVolumeRules.put("minValue", 0);
		masterVolumeRules.put("maxValue", 100);
		masterVolumeRules.put("defaultValue", 50);

		masterVolume.put("rules", masterVolumeRules);
		settings.put(masterVolume);

		// ================ music_volume ================
		JSONObject musicVolume = new JSONObject();
		musicVolume.put("name", "music_volume");
		musicVolume.put("displayName", "Music");
		musicVolume.put("type", "int");

		JSONObject musicVolumeRules = new JSONObject();
		musicVolumeRules.put("minValue", 0);
		musicVolumeRules.put("maxValue", 100);
		musicVolumeRules.put("defaultValue", 100);

		musicVolume.put("rules", musicVolumeRules);
		settings.put(musicVolume);

		// ================ sfx_volume ================
		JSONObject sfxVolume = new JSONObject();
		sfxVolume.put("name", "sfx_volume");
		sfxVolume.put("displayName", "Sound Effects");
		sfxVolume.put("type", "int");

		JSONObject sfxVolumeRules = new JSONObject();
		sfxVolumeRules.put("minValue", 0);
		sfxVolumeRules.put("maxValue", 100);
		sfxVolumeRules.put("defaultValue", 100);

		sfxVolume.put("rules", sfxVolumeRules);
		settings.put(sfxVolume);

		// ================ show_fps ================
		JSONObject showFPS = new JSONObject();
		showFPS.put("name", "show_fps");
		showFPS.put("displayName", "Show FPS");
		showFPS.put("type", "boolean");
		settings.put(showFPS);

		// TODO build more defaults

		settingsObj.put("settings", settings);

		return settingsObj;
	}

}
