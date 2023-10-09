package com.gnix.jflatgl.core.util.configuration;

import com.gnix.jflatgl.core.util.Logger;

public class Setting {

	String name, displayName;
	Class<?> dataType;
	SettingValue value;
	SettingRules rules;

	public Setting(String name, String displayName, Class<?> dataType, SettingValue value, SettingRules rules) {
		this.name = name;
		this.displayName = displayName;
		this.dataType = dataType;
		this.value = value;
		this.rules = rules;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public SettingValue getValue() {
		return value;
	}

	public SettingRules getRules() {
		return rules;
	}

	public Object getDefaultValue() {
		return rules.getDefaultValue();
	}

	public void setValue(Object value) {
		if (rules.valueIsAllowed(value)) {
			this.value.setValue(value);

		}
		else Logger.logError("Value " + value + " is not allowed for setting " + name);
	}

}
