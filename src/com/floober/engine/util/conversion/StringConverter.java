package com.floober.engine.util.conversion;

import org.joml.Vector2f;

import java.util.List;

/*
 * @author Floober
 * 
 * Use a StringConverter object to handle String conversions.
 * 
 */
public class StringConverter {
	
	public StringConverter() {}
	
	/*
	 * Given a list of strings, return them all combined
	 * into a single String object.
	 */
	public static String combineAll(List<String> strings) {
		StringBuilder result = new StringBuilder();
		for (String str : strings) {
			result.append(str).append("\n");
		}
		return result.toString();
	}

	public static String listToString(List<?> list) {
		StringBuilder result = new StringBuilder();
		result.append("List[");
		for (Object o : list) {
			result.append(o);
			result.append(" ");
		}
		// remove last space
		result.deleteCharAt(result.length() - 1);
		result.append("]");
		return result.toString();
	}

	/**
	 * Get a pair of floats representing a range of values.
	 * @param rangeStr The range, formatted as "value1->value2" where value1 and value2 are float values.
	 * @return A Vector2f containing the range values.
	 */
	public static Vector2f getFloatRange(String rangeStr) {
		String[] tokens = rangeStr.split("->");
		if (tokens.length != 2) throw new NumberFormatException("Too many values in range (expected 2, found " + tokens.length + ")");
		return new Vector2f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
	}

	/**
	 * Get the value from a string formatted as follows:
	 * {@code *=*}, where {@code *} is any sequence of characters.
	 * Returns the right value.
	 * @param valueStr The string to extract a value from.
	 * @return The value represented on the right side of the equals character.
	 */
	public static String getValue(String valueStr) {
		return valueStr.substring(valueStr.indexOf("=") + 1);
	}
	
}