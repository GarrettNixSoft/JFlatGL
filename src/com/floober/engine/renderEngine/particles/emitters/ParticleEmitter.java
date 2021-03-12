package com.floober.engine.renderEngine.particles.emitters;

import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.geometry.CircleElement;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.particles.ParticleTexture;
import com.floober.engine.renderEngine.particles.behavior.ParticleBehavior;
import com.floober.engine.renderEngine.particles.types.EmitterParticle;
import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.math.RandomUtil;
import com.floober.engine.util.time.ScaledTimer;
import org.joml.Vector3f;

public class ParticleEmitter {

	private final Vector3f position;
	private ParticleBehavior particleBehavior;
	private ParticleTexture particleTexture;
	private boolean boxMode;

	// particle settings
	private float positionDeltaMin, positionDeltaMax;
	private float positionDeltaVerticalMin, positionDeltaVerticalMax;
	private final ScaledTimer particleTimer;

	protected int batchCount = 1;
	protected int batchVariation;

	// CONSTRUCTOR(S)

	/**
	 * Create a particle emitter.
	 * @param position The initial position.
	 * @param particleTexture The texture to use for particles generated.
	 * @param particleBehavior The defined behavior to control the particles.
	 */
	public ParticleEmitter(Vector3f position, ParticleTexture particleTexture, ParticleBehavior particleBehavior) {
		this.position = position;
		this.particleTexture = particleTexture;
		this.particleBehavior = particleBehavior;
		particleTimer = new ScaledTimer();
	}

	// USING THE EMITTER
	public void update() {
		particleTimer.update();
		// generate particles every so often as specified
		if (particleTimer.finished()) {
			generateParticles();
			particleTimer.restart();
		}
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

	public void initPositionVerticalDelta(float positionDeltaVerticalMin, float positionDeltaVerticalMax) {
		this.positionDeltaVerticalMin = positionDeltaVerticalMin;
		this.positionDeltaVerticalMax = positionDeltaVerticalMax;
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

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
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
	 * false, the min/max distances are treated as radii at a random rotation from the source
	 * position.
	 * @param boxMode Whether to use box mode.
	 */
	public void setBoxMode(boolean boxMode) {
		this.boxMode = boxMode;
	}

	/**
	 * Set the number of particles generated per batch.
	 * @param batchCount The batch size.
	 */
	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}

	/**
	 * Set the amount from which the actual batch size may vary
	 * (less than or greater than) from the batch size.
	 * @param batchVariation The allowed variation.
	 */
	public void setBatchVariation(int batchVariation) {
		this.batchVariation = batchVariation;
	}

	public void setParticleDelay(float particleDelay) {
		particleTimer.reset();
		particleTimer.setTime(particleDelay);
	}

	// GENERATING PARTICLES

	/**
	 * Generate a particle at this source's position, using the current settings
	 * of this source and the particle behavior.
	 */
	public void generateParticles() {
		// generate a particle batch
		int particleCount = RandomUtil.getIntAverage(batchCount, batchVariation);
		for (int i = 0; i < particleCount; ++i) {
			// get a position for the particle
			Vector3f particlePosition = generatePositionVector();
			// create the particle
			EmitterParticle particle = new EmitterParticle(particleBehavior, particleTexture, particlePosition);
			// configure its appearance and movement
			particleBehavior.initParticle(particle);
			// tell the particle to convert to screen position
			particle.convertScreenPosition();
			// done!
		}
	}

	/**
	 * Get a starting position for a particle generated by this emitter.
	 * @return A Vector3f representing a starting position in pixel coordinates.
	 */
	public Vector3f generatePositionVector() {
		// calculate starting position
		Vector3f startingPosition = new Vector3f();
		if (boxMode) {
			float xPos = RandomUtil.getFloat(positionDeltaMin, positionDeltaMax);
			float yPos = RandomUtil.getFloat(positionDeltaVerticalMin, positionDeltaVerticalMax);
			if (RandomUtil.getBoolean()) xPos = -xPos;
			if (RandomUtil.getBoolean()) yPos = -yPos;
			startingPosition.set(xPos, yPos, position.z());
		}
		else {
			float distance = RandomUtil.getFloat(positionDeltaMin, positionDeltaMax);
			startingPosition.set(MathUtil.getCartesian(distance, 0), position.z());
			// use the rotation of the velocity; this ensures than particles always move away from the center
		}
		return new Vector3f(position).add(startingPosition);
	}

	public void renderBounds() {
		if (boxMode) {
			OutlineElement bounds = new OutlineElement(particleBehavior.getAppearanceBehavior().getParticleColor(),
										position.x, position.y, (int) position.z, positionDeltaMax * 2,
									positionDeltaVerticalMax * 2, 2, true);
			Render.drawOutline(bounds);
		}
		else {
			CircleElement bounds = new CircleElement(particleBehavior.getAppearanceBehavior().getParticleColor(),
										position.x, position.y, (int) position.z, positionDeltaMax - 2, positionDeltaMax);
			Render.drawCircle(bounds);
		}
	}

}