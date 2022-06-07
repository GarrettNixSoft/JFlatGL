package com.floober.engine.core.util.conversion;

import org.joml.Vector2f;
import org.joml.Vector2i;

public class VectorConverter {

	public static Vector2i vec2fToVec2i(Vector2f vector) {
		return new Vector2i((int) vector.x, (int) vector.y);
	}

}
