package com.gnix.jflatgl.core.renderEngine.particles.emitters;

import com.gnix.jflatgl.core.renderEngine.particles.ParticleTexture;
import com.gnix.jflatgl.core.renderEngine.particles.behavior.ParticleBehavior;
import com.gnix.jflatgl.core.renderEngine.particles.types.TexturedParticle;
import com.gnix.jflatgl.core.util.math.RandomUtil;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TexturedParticleEmitter extends ParticleEmitter {

	// Set boundaries for where coordinates can be
	private float minX1 = 0, minY1 = 0, maxX1 = 0, maxY1 = 0;
	private float minXRange, minYRange, maxXRange, maxYRange;

	public TexturedParticleEmitter(Vector3f position, ParticleTexture particleTexture, ParticleBehavior particleBehavior) {
		super(position, particleTexture, particleBehavior);
	}

	// INITIALIZERS

	/**
	 * Set the bounds for the upper-left corner of the particles' texture
	 * coordinates, corresponding to the (x,y) values of their coordinate vectors.
	 * 
	 * @param minX1 The minimum upper-left X offset.
	 * @param minY1 The minimum upper-left Y offset.
	 * @param maxX1 The maximum upper-left X offset.
	 * @param maxY1 The maximum upper-left Y offset.
	 *
	 * @throws IllegalArgumentException if any of the given minimums exceed their corresponding
	 * maximum values, or any if of the given values plus their corresponding range exceeds 1.
	 */
	public void initStartOffset(float minX1, float minY1, float maxX1, float maxY1) {
		// ERROR CHECKING
		if (minX1 > maxX1 || minY1 > maxY1 || maxX1 + maxXRange > 1 || maxY1 + maxYRange > 1)
			throw new IllegalArgumentException("Start offset: minimum exceeds maximum or max plus range exceeds 1.");
		// PASSED; SET VALUES
		this.minX1 = minX1;
		this.minY1 = minY1;
		this.maxX1 = maxX1;
		this.maxY1 = maxY1;
	}

	/**
	 * Set the bounds for the range of the bottom-right coordinate.
	 * 	 
	 * @param minXRange The minimum range for the width.
	 * @param minYRange The minimum range for the height
	 * @param maxXRange The maximum range for the width.
	 * @param maxYRange The maximum range for the height.
	 *
	 * @throws IllegalArgumentException if any of the given minimums exceed their corresponding
	 * maximum values, or if any of the ranges may cause coordinates to go out of bounds (exceed 1).
	 */
	public void initEndOffset(float minXRange, float minYRange, float maxXRange, float maxYRange) {
		// RANGE CHECK
		if (minXRange > maxXRange || minYRange > maxYRange || maxX1 + maxXRange > 1 || maxY1 + maxYRange > 1)
			throw new IllegalArgumentException("Maximum value range overlaps with minimum value range.");
		// PASSED; SET VALUES
		this.minXRange = minXRange;
		this.minYRange = minYRange;
		this.maxXRange = maxXRange;
		this.maxYRange = maxYRange;
	}

	// GETTERS
	public float getMinX1() {
		return minX1;
	}
	public float getMinY1() {
		return minY1;
	}
	public float getMaxX1() {
		return maxX1;
	}
	public float getMaxY1() {
		return maxY1;
	}
	public float getMinXRange() {
		return minXRange;
	}
	public float getMinYRange() {
		return minYRange;
	}
	public float getMaxXRange() {
		return maxXRange;
	}
	public float getMaxYRange() {
		return maxYRange;
	}

	// SETTERS
	public void setMinX1(float minX1) {
		this.minX1 = minX1;
	}
	public void setMinY1(float minY1) {
		this.minY1 = minY1;
	}
	public void setMaxX1(float maxX1) {
		this.maxX1 = maxX1;
	}
	public void setMaxY1(float maxY1) {
		this.maxY1 = maxY1;
	}
	public void setMinXRange(float minXRange) {
		this.minXRange = minXRange;
	}
	public void setMinYRange(float minYRange) {
		this.minYRange = minYRange;
	}
	public void setMaxXRange(float maxXRange) {
		this.maxXRange = maxXRange;
	}
	public void setMaxYRange(float maxYRange) {
		this.maxYRange = maxYRange;
	}

	@Override
	public void generateParticles() {
		// get a position for the particle
		Vector3f particlePosition = generatePositionVector();
		// get the texture offset values
		float x1 = RandomUtil.getFloat(minX1, maxX1);
		float y1 = RandomUtil.getFloat(minY1, maxY1);
		float x2 = x1 + RandomUtil.getFloat(minXRange, maxXRange);
		float y2 = y1 + RandomUtil.getFloat(minYRange, maxYRange);
		Vector4f textureCoords = new Vector4f(x1, y1, x2, y2);
		// generate the particle
		TexturedParticle particle = new TexturedParticle(getParticleBehavior(), getParticleTexture(), particlePosition);
		// init the particle
		particle.setTextureCoords(textureCoords);
		getParticleBehavior().initParticle(particle);
		// tell the particle to convert to screen position
		particle.convertScreenPosition();
		// done!
	}

}
