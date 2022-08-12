package com.floober.engine.core.util.math;

import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.renderEngine.display.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;

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

//	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector2f scale) {
//		Matrix4f matrix = new Matrix4f();
//		matrix.translate(translation);
//		matrix.scale(new Vector3f(scale, 0));
//		return matrix;
//	}

	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale, float rz) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(translation, 0));
		matrix.scale(new Vector3f(scale, 0));
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		return matrix;
	}

//	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector2f scale, float rz) {
//		Matrix4f matrix = new Matrix4f();
//		matrix.translation(translation);
//		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
//		matrix.scale(new Vector3f(scale, 1));
//		return matrix;
//	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translation(translation);
		matrix.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0));
		matrix.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0));
		matrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		matrix.scale(scale);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector2f scale, float rz) {
//		Logger.log("Translation: " + translation);
		// create a projection matrix
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00(1.0f / Config.INTERNAL_WIDTH);
		projectionMatrix.m11(1.0f / Config.INTERNAL_HEIGHT);
//		projectionMatrix.translation(translation);
//		projectionMatrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
//		projectionMatrix.scale(new Vector3f(scale, 1));
		projectionMatrix.m30(translation.x);
		projectionMatrix.m31(translation.y);
		projectionMatrix.m32(translation.z);
		// model matrix it
		Matrix4f modelMatrix = new Matrix4f();
//		modelMatrix.translation(translation);
		modelMatrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		modelMatrix.scale(new Vector3f(scale, 1));
		// multiply them
		return projectionMatrix.mul(modelMatrix);
	}

	public static Matrix4f createTransformationMatrix(Window window, Vector3f translation, Vector2f scale, float rz) {
//		Logger.log("Translation: " + translation);
		// create a projection matrix
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00(1.0f / window.getWidth());
		projectionMatrix.m11(1.0f / window.getHeight());
//		projectionMatrix.translation(translation);
//		projectionMatrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
//		projectionMatrix.scale(new Vector3f(scale, 1));
		projectionMatrix.m30(translation.x);
		projectionMatrix.m31(translation.y);
		projectionMatrix.m32(translation.z);
		// model matrix it
		Matrix4f modelMatrix = new Matrix4f();
//		modelMatrix.translation(translation);
		modelMatrix.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1));
		modelMatrix.scale(new Vector3f(scale, 1));
		// multiply them
		return projectionMatrix.mul(modelMatrix);
	}

	public static float interpolateBounded(float min, float max, float value) {
		if (value < min) return 0;
		else if (value > max) return 1;
		else {
			float delta = max - min;
			float pos = value - min;
			return pos / delta;
		}
	}

	public static float boundedSmoothstep(float min, float max, float lowBound, float highBound, float value) {
		float boundDelta = highBound - lowBound;
		return lowBound + interpolateBounded(min, max, value) * boundDelta;
	}

	/**
	 * Get a linerarly interpolated value between two given bounds.
	 * Note that the minimum bound may be greater than the maximum,
	 * and the results will still be valid in that case; the names
	 * are just to give an idea of how this method might be used.
	 * @param min The minimum bound.
	 * @param max The maximum bound.
	 * @param percent The relative position between the two bounds.
	 * @return The interpolated value.
	 */
	public static float interpolate(float min, float max, float percent) {
		return min + (max - min) * percent;
	}

	/**
	 * Round a float value to the nearest {@code interval}.
	 * @param value the value to round
	 * @param interval the rounding interval
	 * @return an approximation of the nearest interval for the given value
	 */
	public static float roundToNearest(float value, float interval) {

		float between = value % interval;

		if (between < interval / 2f) {
			return value - between;
		}
		else {
			float rest = interval - between;
			return value + rest;
		}

	}

	/**
	 * Round an int value to the nearest {@code interval}.
	 * @param value the value to round
	 * @param interval the rounding interval
	 * @return the nearest interval for the given value
	 */
	public static int roundToNearest(int value, int interval) {

		int between = value % interval;

		if (between < interval / 2f) {
			return value - between;
		}
		else {
			int rest = interval - between;
			return value + rest;
		}

	}

	/**
	 * Get the distance between two points.
	 * @param x1 The x-coordinate of Point 1
	 * @param y1 The y-coordinate of Point 1
	 * @param x2 The x-coordinate of Point 2
	 * @param y2 The y-coordinate of Point 2
	 * @return The Euclidean distance between the points.
	 */
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
	}

	/**
	 * Get the distance between two points.
	 * @param v1 Point 1
	 * @param v2 Point 2
	 * @return The Euclidean distance between the points.
	 */
	public static float distance(Vector2f v1, Vector2f v2) {
		double x1 = v1.x();
		double y1 = v1.y();
		double x2 = v2.x();
		double y2 = v2.y();
		float xDelta = (float) Math.abs(x1 - x2);
		float yDelta = (float) Math.abs(y1 - y2);
		return (float) Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
	}

	/**
	 * Get a position along the line between two points that is
	 * {@code progress}% of the length of the line away from Point 1.
	 * @param start Point 1
	 * @param finish Point 2
	 * @param progress How far along the line from Point 1 to calculate
	 * @return A point on the line
	 */
	public static Vector2f positionBetween(Vector2f start, Vector2f finish, float progress) {
		float dx = (finish.x - start.x) * progress;
		float dy = (finish.y - start.y) * progress;
		return new Vector2f(start.x + dx, start.y + dy);
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
	 * @param angle The rotation of the polar coordinate.
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
		// get rotation (range -pi to pi)
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

	/**
	 * Get a Vector2f representing the center point of this Rectangle.
	 * @param r The rectangle to get the center of.
	 * @return A Vector2f representing the center.
	 */
	public static Vector2f center(Rectangle r) {
		return new Vector2f((float) (r.getX() + (r.getWidth() / 2.0)), (float) (r.getY() + (r.getHeight() / 2.0)));
	}

	/**
	 * Get the angle, in degrees, from the horizontal formed by
	 * the line passing through the two given points.
	 * @param x1 Point 1 x position
	 * @param y1 Point 1 y position
	 * @param x2 Point 2 x position
	 * @param y2 Point 2 y position
	 * @return the angle
	 */
	public static float getAngle(float x1, float y1, float x2, float y2) {
		double xDelta = x2 - x1;
		double yDelta = y1 - y2;
		double angle = Math.atan2(xDelta, yDelta);
		angle -= Math.PI / 2;
		return (float) Math.toDegrees(angle);
	}

	/**
	 * Get the angle, in degrees, from the horizontal formed by
	 * the line passing through the two given points.
	 * @param pointA the first point
	 * @param pointB the second point
	 * @return the angle
	 */
	public static float getAngle(Vector2f pointA, Vector2f pointB) {
		double xDelta = pointB.x - pointA.x;
		double yDelta = pointB.y - pointA.y;
		double angle = Math.atan2(xDelta, yDelta);
		angle -= Math.PI / 2;
		return (float) Math.toDegrees(angle);
	}

	public static boolean vec4Equal(Vector4f a, Vector4f b) {
		return a.x == b.x && a.y == b.y && a.z == b.z && a.w == b.w;
	}

	public static boolean vec4Different(Vector4f a, Vector4f b) {
		return !vec4Equal(a, b);
	}

}