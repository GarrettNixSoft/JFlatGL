package com.gnix.jflatgl.core.renderEngine.lights;

import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

public record Light(Vector2f position, Vector4f color, float intensity, float innerRadius, float outerRadius, float maxRadius) {

	/**
	 * Set a new position for this Light.
	 * @param newPosition A position in world-space pixel coordinates, which will be automatically
	 *                    converted to screen-space coordinates.
	 */
	public void setPosition(Vector2f newPosition) {
		position.set(DisplayManager.convertToDisplayPosition2D(newPosition));
	}

}
