package com.floober.engine.renderEngine.particles.types;

import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.particles.ParticleTexture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Particle implements Comparable<Particle> {

	// pixel position and size
	protected float x, y, z;
	protected float width, height;

	// screen values
	protected final Vector3f position = new Vector3f();
	protected final Vector2f velocity = new Vector2f();
	protected final Vector2f scaleVec = new Vector2f();
	protected float rotation;

	protected final Vector3f screenPosition = new Vector3f();

	// APPEARANCE
	protected final ParticleTexture texture;
	protected final Vector4f color = new Vector4f();
	protected final Vector2f texOffset1 = new Vector2f();
	protected final Vector2f texOffset2 = new Vector2f();
	protected float blend;

	// Life
	protected float lifeLength;
	protected float elapsedTime;

	/**
	 * Generate a new particle.
	 * @param texture The particle texture.
	 */
	public Particle(ParticleTexture texture, float x, float y, float z, float width, float height, float lifeLength) {
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.z = z;
		this.position.set(x, y, z);
		this.width = width;
		this.height = height;
		this.lifeLength = lifeLength;
	}

	public boolean update() {
		// update time
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		return elapsedTime < lifeLength;
	}

	// GETTERS
	public ParticleTexture getTexture() {
		return texture;
	}
	public Vector4f getColor() {
		return color;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public Vector3f getPosition() {
		return position;
	}
	public Vector3f getScreenPosition() { return screenPosition; }
	public Vector2f getVelocity() {
		return velocity;
	}
	public Vector2f getScaleVec() {
		return scaleVec;
	}
	public float getRotation() {
		return rotation;
	}
	public Vector2f getTexOffset1() {
		return texOffset1;
	}
	public Vector2f getTexOffset2() {
		return texOffset2;
	}
	public float getBlend() {
		return blend;
	}
	public float getLifeLength() {
		return lifeLength;
	}
	public float getElapsedTime() {
		return elapsedTime;
	}

	// SETTERS
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public void setVelocity(Vector2f velocity) { this.velocity.set(velocity); }
	public void setScaleVec(Vector2f scaleVec) { this.scaleVec.set(scaleVec); }
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	public void setTexOffset1(Vector2f texOffset1) {
		this.texOffset1.set(texOffset1);
	}
	public void setTexOffset2(Vector2f texOffset2) {
		this.texOffset2.set(texOffset2);
	}
	public void setBlend(float blend) {
		this.blend = blend;
	}
	public void setColor(Vector4f color) {
		this.color.set(color);
	}
	public void setColor(float r, float g, float b, float a) { this.color.set(r, g, b, a); }
	public void setAlpha(float a) { this.color.w = a; }

	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.position.set(x, y, z);
		convertScreenPosition();
	}

	public void setPosition(Vector3f position) {
		setPosition(position.x(), position.y(), position.z());
	}

	// UPDATING THE PARTICLE
	public void convertScreenPosition() {
		screenPosition.set(Display.convertToDisplayPosition(x, y, z, width, height, true));
		scaleVec.set(Display.convertToDisplayScale(width, height));
	}

	@Override
	public int compareTo(Particle o) {
		return Float.compare(o.z, z);
	}

}