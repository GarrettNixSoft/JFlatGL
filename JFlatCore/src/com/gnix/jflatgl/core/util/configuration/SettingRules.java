package com.gnix.jflatgl.core.util.configuration;

import org.json.JSONArray;
import org.json.JSONObject;

public class SettingRules {

	Class<?> dataType;
	JSONObject rulesObject;

	public SettingRules(JSONObject rulesObject, Class<?> dataType) {
		this.rulesObject = rulesObject;
		this.dataType = dataType;
	}

	public boolean valueIsAllowed(Object value) {

		// Match type
		if (value.getClass() != dataType) return false;

		// Apply option constraints
		if (rulesObject.has("options")) {
			JSONArray options = rulesObject.getJSONArray("options");
			for (int i = 0; i < options.length(); i++) {
				if (options.get(i).equals(value)) return true;
			}
			return false;
		}

		// Apply numeric constraints
		if (rulesObject.has("minValue")) {
			if (dataType == Integer.class) {
				if ((Integer) value < rulesObject.getInt("minValue")) return false;
			}
			else if (dataType == Short.class) {
				if ((Short) value < rulesObject.getInt("minValue")) return false;
			}
			else if (dataType == Long.class) {
				if ((Long) value < rulesObject.getLong("minValue")) return false;
			}
			else if (dataType == Float.class) {
				if ((Float) value < rulesObject.getFloat("minValue")) return false;
			}
			else if (dataType == Double.class) {
				if ((Double) value < rulesObject.getFloat("minValue")) return false;
			}
		}

		if (rulesObject.has("maxValue")) {
			if (dataType == Integer.class) {
				if ((Integer) value > rulesObject.getInt("maxValue")) return false;
			}
			else if (dataType == Short.class) {
				if ((Short) value > rulesObject.getInt("maxValue")) return false;
			}
			else if (dataType == Long.class) {
				if ((Long) value > rulesObject.getLong("maxValue")) return false;
			}
			else if (dataType == Float.class) {
				if ((Float) value > rulesObject.getFloat("maxValue")) return false;
			}
			else if (dataType == Double.class) {
				if ((Double) value > rulesObject.getFloat("maxValue")) return false;
			}
		}

		return true;

	}

	public String getDisplayName(Object value) {

		if (rulesObject.has("options")) {
			JSONArray options = rulesObject.getJSONArray("options");
			for (int i = 0; i < options.length(); i++) {
				if (options.get(i).equals(value)) {
					return options.getJSONObject(i).optString("displayName", value.toString());
				}
			}
		}

		return value.toString();

	}

	public Object getDefaultValue() {

		if (rulesObject.has("defaultValue")) return rulesObject.get("defaultValue");
		else if (dataType == Boolean.class)	return false;
		else if (dataType == Integer.class)	return 0;
		else if (dataType == Short.class)	return 0;
		else if (dataType == Long.class)	return 0;
		else if (dataType == Float.class)	return 0;
		else if (dataType == Double.class)	return 0;
		else if (dataType == String.class)	return "";
		else return new Object();

	}

}
