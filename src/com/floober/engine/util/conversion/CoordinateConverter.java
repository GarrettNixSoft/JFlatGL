package com.floober.engine.util.conversion;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.Window;
import com.floober.engine.util.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CoordinateConverter {

	public static Vector3f convertToCoordinateVector(String str) {
		switch (str) {
			case "screen_center" -> { return new Vector3f(DisplayManager.getPrimaryGameWindow().centerX(), DisplayManager.getPrimaryGameWindow().centerY(), 0.5f); }
			case "screen_top_left" -> { return new Vector3f(0, 0, 0); }
			default -> { return new Vector3f(parseCoords(str), 0); }
		}
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