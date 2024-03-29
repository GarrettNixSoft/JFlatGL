package com.gnix.jflatgl.core.renderEngine.particles.behavior.movement;

import com.gnix.jflatgl.core.renderEngine.particles.types.EmitterParticle;

public abstract class MovementBehavior implements Cloneable {

	protected float particleSpeedMin, particleSpeedMax;

	public abstract void initParticle(EmitterParticle particle);
	public abstract void updateParticle(EmitterParticle particle);

	// INITIALIZERS

	/**
	 * Set initial boundary values for the particle speed. This
	 * function ignores greater than or less than checks.
	 * @param particleSpeedMin The minimum particle speed.
	 * @param particleSpeedMax The maximum particle speed.
	 */
	public void initSpeed(float particleSpeedMin, float particleSpeedMax) {
		this.particleSpeedMin = particleSpeedMin;
		this.particleSpeedMax = particleSpeedMax;
	}

	// GETTERS

	/**
	 * @return The minimum particle speed, in pixels per second.
	 */
	public float getParticleSpeedMin() {
		return particleSpeedMin;
	}

	/**
	 * @return The maximum particle speed, in pixels per second.
	 */
	public float getParticleSpeedMax() {
		return particleSpeedMax;
	}

	// SETTERS

	/**
	 * Set the minimum speed of particles generated by this source. The speed
	 * may be zero. If the specified speed is less than 0, or if it is greater
	 * than the current maximum speed, this call is ignored.
	 * @param particleSpeedMin The minimum particle speed, in pixels per second.
	 */
	public void setParticleSpeedMin(float particleSpeedMin) {
		if (particleSpeedMin >= 0 && particleSpeedMin <= particleSpeedMax)
			this.particleSpeedMin = particleSpeedMin;
	}

	/**
	 * Set the maximum speed of particles generated by this source. If the
	 * specified speed is less than the current minimum speed, this call is
	 * ignored.
	 * @param particleSpeedMax The maximum particle speed, in pixels per second.
	 */
	public void setParticleSpeedMax(float particleSpeedMax) {
		if (particleSpeedMax >= particleSpeedMin)
			this.particleSpeedMax = particleSpeedMax;
	}

	public MovementBehavior clone() {
		try {
			return (MovementBehavior) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
