package com.floober.engine.util.color;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Colors {

	// standard colors
	public static final Vector4f WHITE = new Vector4f(1);
	public static final Vector4f BLACK = new Vector4f(0, 0, 0, 1);
	public static final Vector4f GRAY = new Vector4f(0.5f, 0.5f, 0.5f, 1);
	public static final Vector4f LIGHT_GRAY = new Vector4f(0.7f, 0.7f, 0.7f, 1);
	public static final Vector4f DARK_GRAY = new Vector4f(0.3f, 0.3f, 0.3f, 1);
	public static final Vector4f RED = new Vector4f(1, 0, 0, 1);
	public static final Vector4f ORANGE = new Vector4f(1, 0.41f, 0, 1);
	public static final Vector4f RED_ORANGE = new Vector4f(1, 0.21f, 0, 1);
	public static final Vector4f YELLOW = new Vector4f(1, 0.8f, 0, 1);
	public static final Vector4f GOLD = new Vector4f(1, 0.6f, 0, 1);
	public static final Vector4f GREEN = new Vector4f(0, 1, 0, 1);
	public static final Vector4f CYAN = new Vector4f(0, 1, 1, 1);
	public static final Vector4f BLUE = new Vector4f(0, 0, 1, 1);
	public static final Vector4f PURPLE = new Vector4f(0.3f, 0, 1, 1);
	public static final Vector4f PINK = new Vector4f(1, 0.69f, 0.69f, 1);
	public static final Vector4f MAGENTA = new Vector4f(1, 0, 0.9f, 1);

	// standard colors (Vec3)
	public static final Vector3f WHITE_3 = new Vector3f(1);
	public static final Vector3f BLACK_3 = new Vector3f(0, 0, 0);
	public static final Vector3f GRAY_3 = new Vector3f(0.5f, 0.5f, 0.5f);
	public static final Vector3f LIGHT_GRAY_3 = new Vector3f(0.7f, 0.7f, 0.7f);
	public static final Vector3f DARK_GRAY_3 = new Vector3f(0.3f, 0.3f, 0.3f);
	public static final Vector3f RED_3 = new Vector3f(1, 0, 0);
	public static final Vector3f ORANGE_3 = new Vector3f(1, 0.41f, 0);
	public static final Vector3f RED_ORANGE_3 = new Vector3f(1, 0.21f, 0);
	public static final Vector3f YELLOW_3 = new Vector3f(1, 0.8f, 0);
	public static final Vector3f GOLD_3 = new Vector3f(1, 0.6f, 0);
	public static final Vector3f GREEN_3 = new Vector3f(0, 1, 0);
	public static final Vector3f CYAN_3 = new Vector3f(0, 1, 1);
	public static final Vector3f BLUE_3 = new Vector3f(0, 0, 1);
	public static final Vector3f PURPLE_3 = new Vector3f(0.3f, 0, 1);
	public static final Vector3f PINK_3 = new Vector3f(1, 0.69f, 0.69f);
	public static final Vector3f MAGENTA_3 = new Vector3f(1, 0, 0.9f);

	// other colors
	public static final Vector4f DARK_GREEN = new Vector4f(0, 0.5f, 0.1f, 1);

	// particle colors
	public static final Vector4f PARTICLE_RED = new Vector4f(1, 0.1f, 0.1f, 1);
	public static final Vector4f PARTICLE_ORANGE = new Vector4f(1, 0.41f, 0.1f, 1);
	public static final Vector4f PARTICLE_RED_ORANGE = new Vector4f(1, 0.21f, 0.1f, 1);
	public static final Vector4f PARTICLE_YELLOW = new Vector4f(1, 0.8f, 0.1f, 1);
	public static final Vector4f PARTICLE_GOLD = new Vector4f(1, 0.6f, 0.1f, 1);
	public static final Vector4f PARTICLE_GREEN = new Vector4f(0.1f, 1, 0.1f, 1);
	public static final Vector4f PARTICLE_CYAN = new Vector4f(0.1f, 1, 1, 1);
	public static final Vector4f PARTICLE_BLUE = new Vector4f(0.1f, 0.1f, 1, 1);
	public static final Vector4f PARTICLE_PURPLE = new Vector4f(0.3f, 0.1f, 1, 1);
	public static final Vector4f PARTICLE_MAGENTA = new Vector4f(1, 0.1f, 0.9f, 1);

	/**
	 * Invert a Color vector. Returns a vector whose components are
	 * equal to one minus the given vector's corresponding component.
	 * @param color The color to invert.
	 * @return The inverted color vector.
	 */
	public static Vector4f invert(Vector4f color) {
		return new Vector4f(1 - color.x(), 1 - color.y(), 1 - color.z(), 1 - color.w());
	}

	/**
	 * Get a Color vector that represents a mixture of two other colors.
	 * {@code a} may be a value between 0 and 1 and represents the
	 * percentage of the resulting color that will be contributed by
	 * the mix color, with the rest coming from the base color.
	 * @param baseColor The base color.
	 * @param mixColor The color to mix in.
	 * @param mixPercentage The percentage of the mix color to use.
	 * @return The mixed color.
	 */
	public static Vector4f mix(Vector4f baseColor, Vector4f mixColor, float mixPercentage) {
		return new Vector4f(
				baseColor.x * (1f - mixPercentage) + mixColor.x * mixPercentage,
				baseColor.y * (1f - mixPercentage) + mixColor.y * mixPercentage,
				baseColor.z * (1f - mixPercentage) + mixColor.z * mixPercentage,
				baseColor.w * (1f - mixPercentage) + mixColor.w * mixPercentage
		);
	}
}