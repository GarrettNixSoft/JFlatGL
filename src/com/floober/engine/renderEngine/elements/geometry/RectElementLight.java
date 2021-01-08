package com.floober.engine.renderEngine.elements.geometry;

import com.floober.engine.util.configuration.Config;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RectElementLight extends RectElement {

	private final Vector3f lightColor;
	private final float ambientLight;
	private final float lightIntensity;
	private final float lightRadius;
	private final float lightInnerRadius;
	private final Vector2f lightPosition;

	public RectElementLight(Vector3f color, Vector2f lightPosition, int layer, float ambientLight, float lightIntensity, float lightRadius, float lightInnerRadius, boolean centered) {
		super(new Vector4f(0), 0, 0, layer, Config.DEFAULT_RESOLUTION_WIDTH, Config.DEFAULT_RESOLUTION_HEIGHT, centered);
		this.lightColor = color;
		this.lightPosition = lightPosition;
		this.ambientLight = ambientLight;
		this.lightIntensity = lightIntensity;
		this.lightRadius = lightRadius / Config.DEFAULT_RESOLUTION_HEIGHT;
		this.lightInnerRadius = lightInnerRadius / Config.DEFAULT_RESOLUTION_HEIGHT;
	}

	public Vector3f getLightColor() {
		return lightColor;
	}
	public Vector2f getLightPosition() { return lightPosition; }
	public float getAmbientLight() {
		return ambientLight;
	}
	public float getLightIntensity() {
		return lightIntensity;
	}
	public float getLightRadius() {
		return lightRadius;
	}
	public float getLightInnerRadius() {
		return lightInnerRadius;
	}

}
