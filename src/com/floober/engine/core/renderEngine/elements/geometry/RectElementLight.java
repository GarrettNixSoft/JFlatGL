package com.floober.engine.core.renderEngine.elements.geometry;

import com.floober.engine.core.util.configuration.Config;
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
		super(new Vector4f(0), 0, 0, layer, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT, centered);
		this.lightColor = color;
		this.lightPosition = lightPosition;
		this.ambientLight = ambientLight;
		this.lightIntensity = lightIntensity;
		this.lightRadius = lightRadius / Config.INTERNAL_HEIGHT;
		this.lightInnerRadius = lightInnerRadius / Config.INTERNAL_HEIGHT;
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
