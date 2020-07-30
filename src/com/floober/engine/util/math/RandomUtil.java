package com.floober.engine.util.math;

import java.util.Random;

public class RandomUtil {

	private static Random random = new Random();

	/**
	 * Set the seed to use when generating values.
	 * @param seed The new seed.
	 */
	public static void setSeed(long seed) {
		random = new Random(seed);
	}

	/**
	 * Get a random float from 0 to 1.
	 * @return A random float from 0 to 1.
	 */
	public static float getFloat() {
		return random.nextFloat();
	}

	/**
	 * Get a random float from 0 to {@code max}.
	 * @param max The upper bound.
	 * @return A random float from 0 to {@code max}.
	 */
	public static float getFloat(float max) {
		return random.nextFloat() * max;
	}

	/**
	 * Get a random float from {@code min} to {@code max}.
	 * @param min The lower bound.
	 * @param max The upper bound.
	 * @return A random float from {@code min} to {@code max}.
	 */
	public static float getFloat(float min, float max) {
		float delta = max - min;
		return random.nextFloat() * delta + min;
	}

	/**
	 * Get a random float from {@code min} to {@code max}.
	 * @param min The lower bound.
	 * @param max The upper bound.
	 * @return A random float from {@code min} to {@code max}.
	 */
	public static float getFloat(double min, double max) {
		double delta = max - min;
		return (float) (random.nextFloat() * delta + min);
	}

	/**
	 * Get a random float, ranging + or - {@code variation} from {@code average}.
	 * @param average The average value, treated as the center of the range of possible return values.
	 * @param variation The allowed variation from the average, in both the positive and negative directions.
	 * @return A random value in the range [{@code average - variation}, {@code average + variation}]
	 */
	public static float getFloatAverage(float average, float variation) {
		return getFloat(average - variation, average + variation);
	}

	/**
	 * Get a random int from 0 to {@code max}.
	 * @param max The upper bound.
	 * @return A random int from 0 to {@code max}.
	 */
	public static int getInt(int max) {
		return random.nextInt(max);
	}

	/**
	 * Get a random int from {@code min} to {@code max}.
	 * @param min The lower bound.
	 * @param max The upper bound.
	 * @return A random int from {@code min} to {@code max}.
	 */
	public static int getInt(int min, int max) {
		int delta = max - min;
		return random.nextInt(delta) + min;
	}

	/**
	 * Get a random boolean value.
	 * @return A random boolean value.
	 */
	public static boolean getBoolean() {
		return random.nextBoolean();
	}

	static String charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789`~!@#$%^&*()-=_+[]{}\\|;:'\",<.>/?";

	/**
	 * Get a random char, from the set defined in RandomUtil.
	 * @return A random character.
	 */
	public static char getChar() {
		return charSet.charAt(getInt(charSet.length()));
	}

	/**
	 * Get a random string.
	 * @param length The length of the string.
	 * @return A string of {@code length} random characters.
	 */
	public static String getRandomString(int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			builder.append(getChar());
		}
		return builder.toString();
	}

}