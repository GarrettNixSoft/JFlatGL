package com.floober.engine.renderEngine.particles.types;

import com.floober.engine.display.Window;
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
public class EmitterParticle extends Particle {

	// APPEARANCE
	private final float startAlpha;

	// BEHAVIOR
	private float rotationSpeed;
	private boolean fadeOut;

	// CONTROL
	private final ParticleBehavior behavior;

	public EmitterParticle(ParticleBehavior behavior, ParticleTexture texture, Vector3f position) {
		super(texture, position.x(), position.y(), position.z(), 0, 0, 1);
		this.position.set(position);
		this.behavior = behavior;
		this.startAlpha = 1;
		ParticleMaster.addParticle(this);
	}

	public EmitterParticle(ParticleBehavior behavior, ParticleTexture texture, Vector4f color, Vector3f position, float width, float height, Vector2f velocity, float lifeLength, float initialRotation, float rotationSpeed, boolean fadeOut) {
		super(texture, position.x(), position.y(), position.z(), width, height, lifeLength);
		this.behavior = behavior;
		this.color.set(color);
		startAlpha = color.w();
		this.velocity.set(velocity);
		this.rotation = initialRotation;
		this.rotationSpeed = rotationSpeed;
		this.fadeOut = fadeOut;
		convertScreenPosition();
		ParticleMaster.addParticle(this);
	}

	// GETTERS
	public float getRotationSpeed() {
		return rotationSpeed;
	}
	public boolean isFadeOut() {
		return fadeOut;
	}
	public float getStartAlpha() { return startAlpha; }

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
	public void setSize(float size) {
		this.width = this.height = size;
	}

	private void setMapPosition() {
		//
	}

	@Override
	public void convertScreenPosition() {
		setMapPosition();
		screenPosition.set(DisplayManager.convertToDisplayPosition(x, y, layer, width, height, true));
//		scaleVec.set(Display.convertToDisplayScale(width, height));
		scaleVec.set(width, height);
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
		int stageCount = texture.numTextures();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1.0f;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.numRows();
		int row = index / texture.numRows();
		offset.x = (float) column / texture.numRows();
		offset.y = (float) row / texture.numRows();
	}
}