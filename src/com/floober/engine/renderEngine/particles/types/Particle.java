package com.floober.engine.renderEngine.particles.types;

import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.particles.ParticleTexture;
import com.floober.engine.renderEngine.particles.behavior.ParticleBehavior;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/*
	Represents an individual particle to be rendered.
 */
public class Particle implements Comparable<Particle> {

	// pixel position and size
	private float x, y, z;
	private float width, height;

	// APPEARANCE
	// texture
	private final ParticleTexture texture;
	private final Vector2f texOffset1 = new Vector2f();
	private final Vector2f texOffset2 = new Vector2f();
	private float blend;

	// color modifiers
	private final Vector4f color = new Vector4f();
	private final float startAlpha;

	// BEHAVIOR
	private final Vector3f initialPosition = new Vector3f();
	private final Vector3f position = new Vector3f();
	private final Vector2f velocity = new Vector2f();
	private final Vector2f scaleVec = new Vector2f();
	private float rotation;
	private float rotationSpeed;
	private boolean fadeOut;

	private float lifeLength;
	private float elapsedTime;

	// CONTROL
	private final ParticleBehavior behavior;

	public Particle(ParticleBehavior behavior, ParticleTexture texture, Vector3f position) {
		this.behavior = behavior;
		this.texture = texture;
		this.x = position.x();
		this.y = position.y();
		this.z = position.z();
		this.initialPosition.set(position);
		this.startAlpha = 1;
		this.lifeLength = 1;
		ParticleMaster.addParticle(this);
	}

	public Particle(ParticleBehavior behavior, ParticleTexture texture, Vector4f color, Vector3f position, float width, float height, Vector2f velocity, float lifeLength, float initialRotation, float rotationSpeed, boolean fadeOut) {
		this.behavior = behavior;
		this.texture = texture;
		this.color.set(color);
		startAlpha = color.w();
		this.x = position.x();
		this.y = position.y();
		this.z = position.z();
		this.initialPosition.set(position);
		this.width = width;
		this.height = height;
		this.velocity.set(velocity);
		this.lifeLength = lifeLength;
		this.rotation = initialRotation;
		this.rotationSpeed = rotationSpeed;
		this.fadeOut = fadeOut;
		convertScreenPosition();
		ParticleMaster.addParticle(this);
	}

	// GETTERS
	// position and size
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
	public Vector3f getInitialPosition() { return initialPosition; }
	public Vector3f getPosition() {
		return position;
	}
	public Vector2f getScaleVec() {
		return scaleVec;
	}

	// motion
	public Vector2f getVelocity() {
		return velocity;
	}
	public float getRotation() {
		return rotation;
	}
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	// texture
	public ParticleTexture getTexture() {
		return texture;
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

	// appearance
	public Vector4f getColor() {
		return color;
	}
	public boolean isFadeOut() {
		return fadeOut;
	}

	// lifespan
	public float getLifeLength() {
		return lifeLength;
	}
	public float getElapsedTime() {
		return elapsedTime;
	}
	public float getStartAlpha() { return startAlpha; }

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
	public void setPosition(Vector3f position) { this.position.set(position); }
	public void setVelocity(Vector2f velocity) { this.velocity.set(velocity); }
	public void setScaleVec(Vector2f scaleVec) { this.scaleVec.set(scaleVec); }
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * Set the rotation speed of this particle.
	 * @param rotationSpeed The rotation speed, in degrees per second.
	 */
	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public void setFadeOut(boolean fadeOut) {
		this.fadeOut = fadeOut;
	}
	public void setLifeLength(float lifeLength) {
		this.lifeLength = lifeLength;
	}
	public void setInitialPosition(float x, float y, float z) {
		this.initialPosition.set(x, y, z);
	}
	public void setInitialPosition(Vector3f position) {
		this.initialPosition.set(position);
	}
	public void setSize(float size) {
		this.width = this.height = size;
	}

	// UPDATING THE PARTICLE
	public void convertScreenPosition() {
		position.set(Display.convertToDisplayPosition(x, y, z, width, height, true));
		scaleVec.set(Display.convertToDisplayScale(width, height));
	}

	// move
	public boolean update() {
		behavior.updateParticle(this);
		// update time
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		// movement and appearance implemented
		behavior.updateParticle(this);
		// update texture coords
		updateTextureCoordInfo();
		return elapsedTime < lifeLength;
	}

	private void updateTextureCoordInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumRows() * texture.getNumRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1.0f;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumRows();
		int row = index / texture.getNumRows();
		offset.x = (float) column / texture.getNumRows();
		offset.y = (float) row / texture.getNumRows();
	}

	@Override
	public int compareTo(Particle o) {
		return Float.compare(o.z, z);
	}
}