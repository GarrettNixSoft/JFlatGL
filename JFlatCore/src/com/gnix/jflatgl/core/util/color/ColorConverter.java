package com.gnix.jflatgl.core.util.color;

import com.gnix.jflatgl.core.util.Logger;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * A utility class for converting strings to color vectors.
 */
public class ColorConverter {

	/**
	 * Get a 4-float color vector by name.
	 * @param colorName The name of the color.
	 * @return The color, or a zero vector if no custom color by that name exists.
	 */
	public static Vector4f getColorByName(String colorName) {
		return switch (colorName) {
			case "black" -> new Vector4f(Colors.BLACK);
			case "white" -> new Vector4f(Colors.WHITE);
			case "red" -> new Vector4f(Colors.RED);
			case "orange" -> new Vector4f(Colors.ORANGE);
			case "yellow" -> new Vector4f(Colors.YELLOW);
			case "green" -> new Vector4f(Colors.GREEN);
			case "cyan" -> new Vector4f(Colors.CYAN);
			case "blue" -> new Vector4f(Colors.BLUE);
			case "magenta" -> new Vector4f(Colors.MAGENTA);
			case "purple" -> new Vector4f(Colors.PURPLE);
			case "pink" -> new Vector4f(Colors.PINK);
			case "gray" -> new Vector4f(Colors.GRAY);
			case "lightGray" -> new Vector4f(Colors.LIGHT_GRAY);
			case "darkGray" -> new Vector4f(Colors.DARK_GRAY);
			case "particle_red" -> new Vector4f(Colors.PARTICLE_RED);
			case "particle_orange" -> new Vector4f(Colors.PARTICLE_ORANGE);
			case "particle_yellow" -> new Vector4f(Colors.PARTICLE_YELLOW);
			case "particle_gold" -> new Vector4f(Colors.PARTICLE_GOLD);
			case "particle_green" -> new Vector4f(Colors.PARTICLE_GREEN);
			case "particle_cyan" -> new Vector4f(Colors.PARTICLE_CYAN);
			case "particle_blue" -> new Vector4f(Colors.PARTICLE_BLUE);
			case "particle_purple" -> new Vector4f(Colors.PARTICLE_PURPLE);
			case "particle_magenta" -> new Vector4f(Colors.PARTICLE_MAGENTA);
			case "none" -> new Vector4f(-1);
			default -> getColorByValue(colorName);
		};
	}

	/**
	 * Get a 3-float color vector by name.
	 * @param colorName The name of the color.
	 * @return The color, or a zero vector if no custom color by that name exists.
	 */
	public static Vector3f getColor3ByName(String colorName) {
		return switch (colorName) {
			case "black" -> new Vector3f(Colors.BLACK_3);
			case "white" -> new Vector3f(Colors.WHITE_3);
			case "red" -> new Vector3f(Colors.RED_3);
			case "orange" -> new Vector3f(Colors.ORANGE_3);
			case "yellow" -> new Vector3f(Colors.YELLOW_3);
			case "green" -> new Vector3f(Colors.GREEN_3);
			case "cyan" -> new Vector3f(Colors.CYAN_3);
			case "blue" -> new Vector3f(Colors.BLUE_3);
			case "magenta" -> new Vector3f(Colors.MAGENTA_3);
			case "purple" -> new Vector3f(Colors.PURPLE_3);
			case "pink" -> new Vector3f(Colors.PINK_3);
			case "gray" -> new Vector3f(Colors.GRAY_3);
			case "lightGray" -> new Vector3f(Colors.LIGHT_GRAY_3);
			case "darkGray" -> new Vector3f(Colors.DARK_GRAY_3);
			case "none" -> new Vector3f(-1);
			default -> new Vector3f(0);
		};
	}

	public static Vector4f getColorByValue(String colorData) {
		Vector4f color = new Vector4f(0);
		// validate
		if (colorData.startsWith("(") && colorData.endsWith(")")) {
			// this is a valid color format, so remove the parentheses
			colorData = colorData.substring(1, colorData.length() - 1);
			String[] rgba = colorData.split(",");
			// validate
			if (rgba.length == 4) {
				// this is the correct length, get the color values
				try {
					color.x = Float.parseFloat(rgba[0]);
					color.y = Float.parseFloat(rgba[1]);
					color.z = Float.parseFloat(rgba[2]);
					color.w = Float.parseFloat(rgba[3]);
				}
				catch (Exception e) { Logger.logError("Error parsing color value string"); }
			}
		}
		// return result
		return color;
	}

}
