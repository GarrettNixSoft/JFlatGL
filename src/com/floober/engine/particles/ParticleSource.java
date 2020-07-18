package com.floober.engine.particles;

import com.floober.engine.particles.behavior.ParticleBehavior;
import com.floober.engine.util.math.MathUtil;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ParticleSource {

	private final Vector3f position;
	private final ParticleBehavior particleBehavior;
	private final ParticleFactory particleFactory;

	// particle settings

	public ParticleSource(Vector3f position, ParticleBehavior particleBehavior, ParticleFactory particleFactory) {
		this.position = position;
		this.particleBehavior = particleBehavior;
		this.particleFactory = particleFactory;
	}

	// GETTERS

	/**
	 * @return The position of this source.
	 */
	public Vector3f getPosition() {
		return position;
	}

	// SETTERS
	/**
	 * Set the position of the particle source.
	 * @param position The new position.
	 */
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	// GENERATING PARTICLES

	/**
	 * Generate a particle at this source's position, using the current settings
	 * of this source.
	 *
	 * Note: angle is in degrees, counterclockwise starting from the right side.
	 */
	public void generateParticle() {
		Vector3f particlePosition = new Vector3f();
		float xPos = position.x();
		float yPos = position.y();
		float zPos = position.z();
		particlePosition.set(xPos, yPos, zPos);
		particleFactory.generateParticle(particleBehavior, particlePosition);
	}

}