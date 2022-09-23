package com.gnix.jflatgl.core.util.configuration;

/**
 * Store a value in a pseudo-weakly-typed way.
 * Retrieving the value requires knowing the
 * actual type and calling the correct getter.
 */
public class SettingValue {

	public enum DataType {
		BOOLEAN, INT, LONG, FLOAT, DOUBLE, STRING
	}

	private DataType dataType;
	private Object value;

	public SettingValue(DataType dataType, Object value) {
		this.dataType = dataType;
		this.value = value;
	}

	public DataType getDataType() {
		return dataType;
	}

	public int getAsInt() {
		return (Integer) value;
	}

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
