package com.floober.engine.util.conversion;

import com.floober.engine.util.Logger;
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

	public static Vector2f parseCoords(String coordStr) {
		try {
			// strip leading non-digits
			while (!Character.isDigit(coordStr.charAt(0))) {
				coordStr = coordStr.substring(1);
			}
			// strip trailing non-digits
			while (!Character.isDigit(coordStr.charAt(coordStr.length() - 1))) {
				coordStr = coordStr.substring(0, coordStr.length() - 1);
			}
			// split values
			String[] tokens = coordStr.split(",");
			// get values
			float x = Float.parseFloat(tokens[0]);
			float y = Float.parseFloat(tokens[1]);
			// return vector
			return new Vector2f(x, y);
		} catch (Exception e) {
			Logger.logError("Error occurred while parsing coordinate string " + coordStr + ": " + e.getMessage());
			return new Vector2f();
		}
	}
	
}