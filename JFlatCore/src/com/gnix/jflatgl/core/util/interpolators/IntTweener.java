package com.gnix.jflatgl.core.util.interpolators;

public class IntTweener {

	public static int getIntBetween(int a, int b, double between) {
		int delta = b - a;
		int tween = (int) (delta * between);
		return a + tween;
	}

}
