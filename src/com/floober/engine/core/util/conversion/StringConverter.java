package com.floober.engine.core.util.conversion;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

/*
 * @author Floober
 * 
 * Use a StringConverter object to handle String conversions.
 * 
 */
public class StringConverter {

	public static String vectorToString(Object vecObj) {
		return switch (vecObj) {
			case Vector4f v4f -> vec4fToString(v4f);
			case Vector2f v2f -> vec2fToString(v2f);
			case Vector2i v2i -> vec2iToString(v2i);
			default -> vecObj.toString();
		};
	}

	public static String vec4fToString(Vector4f vector) {
		return "Vector4f(" + vector.x() + ", " + vector.y() + ", " + vector.z() + ", " + vector.w() + ")";
	}

	public static String vec3fToString(Vector3f vector) {
		return "Vector3f(" + vector.x() + ", " + vector.y() + ", " + vector.z() + ")";
	}

	public static String vec2fToString(Vector2f vector) {
		return "Vector4f(" + vector.x() + ", " + vector.y() + ")";
	}

	public static String vec2iToString(Vector2i vector) {
		return "Vector4f(" + vector.x() + ", " + vector.y() + ")";
	}
	
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

	public static String byteArrayToString(byte[] arr) {
		StringBuilder result = new StringBuilder();
		result.append('{');
		for (byte b : arr) {
			result.append(b);
			result.append(", ");
		}
		result.setLength(result.length() - 2);
		result.append('}');
		return result.toString();
	}

	public static String shortArrayToString(short[] arr) {
		StringBuilder result = new StringBuilder();
		result.append('{');
		for (short s : arr) {
			result.append(s);
			result.append(", ");
		}
		result.setLength(result.length() - 2);
		result.append('}');
		return result.toString();
	}

	public static String intArrayToString(int[] arr) {
		StringBuilder result = new StringBuilder();
		result.append('{');
		for (int i : arr) {
			result.append(i);
			result.append(", ");
		}
		result.setLength(result.length() - 2);
		result.append('}');
		return result.toString();
	}

	public static String longArrayToString(long[] arr) {
		StringBuilder result = new StringBuilder();
		result.append('{');
		for (long l : arr) {
			result.append(l);
			result.append(", ");
		}
		result.setLength(result.length() - 2);
		result.append('}');
		return result.toString();
	}

	public static String floatArrayToString(float[] arr) {
		StringBuilder result = new StringBuilder();
		result.append('{');
		for (float f : arr) {
			result.append(f);
			result.append(", ");
		}
		result.setLength(result.length() - 2);
		result.append('}');
		return result.toString();
	}

	public static String doubleArrayToString(double[] arr) {
		StringBuilder result = new StringBuilder();
		result.append('{');
		for (double d : arr) {
			result.append(d);
			result.append(", ");
		}
		result.setLength(result.length() - 2);
		result.append('}');
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