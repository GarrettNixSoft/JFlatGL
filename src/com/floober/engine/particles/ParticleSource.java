package com.floober.engine.particles;

import com.floober.engine.particles.behavior.ParticleBehavior;
import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.math.RandomUtil;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ParticleSource {

	private final Vector3f position;
	private ParticleBehavior particleBehavior;
	private ParticleTexture particleTexture;
	private boolean boxMode;

	// particle settings
	private float positionDeltaMin, positionDeltaMax;

	public ParticleSource(Vector3f position, ParticleTexture particleTexture, ParticleBehavior particleBehavior) {
		this.position = position;
		this.particleTexture = particleTexture;
		this.particleBehavior = particleBehavior;
	}

	// INITIALIZERS

	/**
	 * Set initial boundary values for the particle starting position delta.
	 * This function ignores greater than or less than checks.
	 * @param positionDeltaMin The minimum position delta.
	 * @param positionDeltaMax The maximum position delta.
	 */
	public void initPositionDelta(float positionDeltaMin, float positionDeltaMax) {
		this.positionDeltaMin = positionDeltaMin;
		this.positionDeltaMax = positionDeltaMax;
	}

	// GETTERS

	/**
	 * @return The position of this source.
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return The particle behavior configuration for this source.
	 */
	public ParticleBehavior getParticleBehavior() { return particleBehavior; }

	/**
	 * @return The particle texture for this source.
	 */
	public ParticleTexture getParticleTexture() {
		return particleTexture;
	}

	/**
	 * @return The minimum distance from the source's position that a particle can appear.
	 */
	public float getPositionDeltaMin() {
		return positionDeltaMin;
	}

	/**
	 * @return The maximum distance from the source's position that a particle can appear.
	 */
	public float getPositionDeltaMax() {
		return positionDeltaMax;
	}

	/**
	 * @return Whether box mode is active for particle position ranges. If true, the
	 * position delta values are treated as a box surrounding the source's position.
	 * If false, they are treated as the radius of a circle surrounding the source's
	 * position.
	 */
	public boolean isBoxMode() {
		return boxMode;
	}

	// SETTERS
	/**
	 * Set the position of the particle source.
	 * @param position The new position.
	 */
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public void setParticleBehavior(ParticleBehavior particleBehavior) {
		this.particleBehavior = particleBehavior;
	}

	public void setParticleTexture(ParticleTexture particleTexture) {
		this.particleTexture = particleTexture;
	}

	/**
	 * Set the minimum distance from the source position that a particle may
	 * appear. If the specified distance is less than zero, or if it is greater
	 * than the current maximum distance, this call is ignored.
	 * @param positionDeltaMin The minimum distance, in pixels at the default resolution.
	 */
	public void setPositionDeltaMin(float positionDeltaMin) {
		if (positionDeltaMin > 0 && positionDeltaMin < positionDeltaMax)
			this.positionDeltaMin = positionDeltaMin;
	}

	/**
	 * Set the maximum distance from the source position that a particle may
	 * appear. If the specified distance is less than the current minimum, this
	 * call is ignored.
	 * @param positionDeltaMax The maximum distance, in pixels at the default resolution.
	 */
	public void setPositionDeltaMax(float positionDeltaMax) {
		if (positionDeltaMax >= positionDeltaMin)
			this.positionDeltaMax = positionDeltaMax;
	}

	/**
	 * Set whether initial position bounds are treated as a bounding square or a circle.
	 * If true, the min/max distances are treated as horizontal and vertical ranges. If
	 * false, the min/max distances are treated as radii at a random angle from the source
	 * position.
	 * @param boxMode Whether to use box mode.
	 */
	public void setBoxMode(boolean boxMode) {
		this.boxMode = boxMode;
	}

	// GENERATING PARTICLES
	/**
	 * Generate a particle at this source's position, using the current settings
	 * of this source and the particle behavior.
	 */
	public void generateParticle() {
		// get a position for the particle
		Vector3f particlePosition = generatePositionVector();
		// create the particle
		Particle particle = new Particle(particleBehavior, particleTexture, particlePosition);
		// configure its appearance and movement
		particleBehavior.initParticle(particle);
		// tell the particle to convert to screen position
		particle.convertScreenPosition();
		// done!
	}

	private Vector3f generatePositionVector() {
		// calculate starting position
		Vector3f startingPosition = new Vector3f();
		if (boxMode) {
			float xPos = RandomUtil.getFloat(positionDeltaMin, positionDeltaMax);
			float yPos = RandomUtil.getFloat(positionDeltaMin, positionDeltaMax);
			if (RandomUtil.getBoolean()) xPos = -xPos;
			if (RandomUtil.getBoolean()) yPos = -yPos;
			startingPosition.set(xPos, yPos, position.z());
		}
		else {
			float distance = RandomUtil.getFloat(positionDeltaMin, positionDeltaMax);
			startingPosition.set(MathUtil.getCartesian(distance, 0), position.z());
			// use the angle of the velocity; this ensures than particles always move away from the center
		}
		return new Vector3f(position).add(startingPosition);
	}

}