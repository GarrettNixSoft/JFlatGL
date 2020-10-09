package com.floober.engine.util.math;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/*
	For utility functions.
 */
public class MathUtil {

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(translation, 0));
		matrix.scale(new Vector3f(scale, 0));
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.scale(new Vector3f(scale, 0));
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale, float rz) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(translation, 0));
		matrix.scale(new Vector3f(scale, 0));
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector2f scale, float rz) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.scale(new Vector3f(scale, 1));
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translation(translation);
		matrix.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0));
		matrix.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0));
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		matrix.scale(scale);
		return matrix;
	}

	public static float smoothstep(float min, float max, float value) {
		if (value < min) return 0;
		else if (value > max) return 1;
		else {
			float delta = max - min;
			float pos = value - min;
			float val = Math.max(0, Math.min(1, pos / delta));
			return val * val * (3.0f - 2.0f * val);
		}
	}

	public static float boundedSmoothstep(float min, float max, float lowBound, float highBound, float value) {
		float boundDelta = highBound - lowBound;
		return lowBound + smoothstep(min, max, value) * boundDelta;
	}

	/**
	 * Get the length of a Cartesian vector with x-component {@code x} and y-component {@code y}.
	 * @param dx The x-component.
	 * @param dy The y-component.
	 * @return The length of the vector.
	 */
	public static float length(float dx, float dy) {
		return (float) (Math.sqrt( Math.pow(dx, 2) + Math.pow(dy, 2) ));
	}

	/**
	 * Return a Vector2f containing a cartesian representation of the given polar coordinates.
	 * @param magnitude The length of the polar coordinate.
	 * @param angle The angle of the polar coordinate.
	 * @return A cartesian representation of the given polar coordinate.
	 */
	public static Vector2f getCartesian(float magnitude, float angle) {
		angle = (float) Math.toRadians(angle);
		float dx = magnitude * (float) Math.cos(angle);
		float dy = magnitude * (float) Math.sin(angle);
		return new Vector2f(dx, dy);
	}

	/**
	 * Return a Vector2f containing a cartesian representation of the given polar coordinates.
	 * @param polarVector A Vector2f representing a polar coordinate.
	 * @return A cartesian representation of the given polar coordinate.
	 */
	public static Vector2f getCartesian(Vector2f polarVector) {
		return getCartesian(polarVector.x(), polarVector.y());
	}

	/**
	 * Get a Vector2f storing the polar representation of the given Cartesian vector.
	 * @param dx The x-component.
	 * @param dy The y-component.
	 * @return A polar vector representation of the Cartesian vector.
	 */
	public static Vector2f getPolar(float dx, float dy) {
		// get angle (range -pi to pi)
		float angleRad = (float) Math.atan2(dy, dx);
		// convert to degrees
		float angleDeg = (float) Math.toDegrees(angleRad);
		// convert to range 0 to 360
		if (angleDeg < 0) {
			float positiveAngle = -angleDeg;
			float past180 = 180 - positiveAngle;
			angleDeg = 180 + past180;
		}
		// get length
		float length = length(dx, dy);
		// return the vector
		return new Vector2f(length, angleDeg);
	}

	/**
	 * Get a Vector2f storing the polar representation of the given Cartesian vector.
	 * @param cartesian The Cartesian vector.
	 * @return A polar vector representation of the Cartesian vector.
	 */
	public static Vector2f getPolar(Vector2f cartesian) {
		return getPolar(cartesian.x(), cartesian.y());
	}

}