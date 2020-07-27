package com.floober.engine.lights;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Light {

	private final Vector2f position;
	private final Vector4f color;
	private float intensity;
	private float innerRadius;
	private float outerRadius;
	private float maxRadius;

	public Light(Vector2f position, Vector4f color, float outerRadius, float maxRadius) {
		this.position = position;
		this.color = color;
		this.intensity = 1;
		this.outerRadius = outerRadius;
		this.maxRadius = maxRadius;
	}

	public Light(Vector2f position, Vector4f color, float innerRadius, float outerRadius, float maxRadius) {
		this.position = position;
		this.color = color;
		this.intensity = 1;
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.maxRadius = maxRadius;
	}

	public Light(Vector2f position, Vector4f color, float intensity, float innerRadius, float outerRadius, float maxRadius) {
		this.position = position;
		this.color = color;
		this.intensity = intensity;
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.maxRadius = maxRadius;
	}

	// GETTERS
	public Vector2f getPosition() {
		return position;
	}
	public Vector4f getColor() {
		return color;
	}
	public float getIntensity() {
		return intensity;
	}
	public float getInnerRadius() {
		return innerRadius;
	}
	public float getOuterRadius() {
		return outerRadius;
	}
	public float getMaxRadius() { return maxRadius; }

	// SETTERS
	public void setPosition(Vector2f position) {
		this.position.set(position);
	}
	public void setColor(Vector4f color) {
		this.color.set(color);
	}
	public void setIntensity(float intensity) { this.intensity = intensity; }
	public void setInnerRadius(float innerRadius) {
		this.innerRadius = innerRadius;
	}
	public void setOuterRadius(float outerRadius) {
		this.outerRadius = outerRadius;
	}
	public void setMaxRadius(float maxRadius) { this.maxRadius = maxRadius; }
}