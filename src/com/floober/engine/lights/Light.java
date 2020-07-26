package com.floober.engine.lights;

import org.joml.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f color;
	private float brightness;

	public Light(Vector3f position, Vector3f color, float brightness) {
		this.position = position;
		this.color = color;
		this.brightness = brightness;
	}

	// GETTERS
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getColor() {
		return color;
	}

	public float getBrightness() {
		return brightness;
	}

	// SETTERS
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
	}
}