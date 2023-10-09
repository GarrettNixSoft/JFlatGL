package com.gnix.jflatgl.core.util.configuration;

/**
 * Store a value in a pseudo-weakly-typed way.
 * Retrieving the value requires knowing the
 * actual type and calling the correct getter.
 */
public class SettingValue {

	private Object value;

	public SettingValue(Object value) {
		this.value = value;
	}

	public int getAsInt() {
		return (Integer) value;
	}

	public short getAsShort() { return (Short) value; }

	public long getAsLong() {
		return (Long) value;
	}

	public float getAsFloat() {
		return (Float) value;
	}

	public double getAsDouble() {
		return (Double) value;
	}

	public boolean getAsBoolean() {
		return (Boolean) value;
	}

	public String getAsString() {
		return (String) value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
