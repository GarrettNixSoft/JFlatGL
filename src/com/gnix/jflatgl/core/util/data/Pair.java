package com.gnix.jflatgl.core.util.data;

import org.joml.Vector2f;

public record Pair<T1, T2>(T1 data1, T2 data2) {

	/**
	 * Convert this Integer Pair to a Vector2f.
	 * <br>
	 * This method is provided for convenience only. There are
	 * no type safety checks in place, so only use it if you
	 * are certain the calling Pair contains Integer objects.
	 *
	 * @return a Vector2f representation of this Pair.
	 */
	public Vector2f pixelsToVec2f() {

		Integer d1 = (Integer) data1;
		Integer d2 = (Integer) data2;

		float x = (float) d1;
		float y = (float) d2;

		return new Vector2f(x, y);

	}

}
