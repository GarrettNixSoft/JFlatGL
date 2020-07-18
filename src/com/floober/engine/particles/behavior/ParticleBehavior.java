package com.floober.engine.particles.behavior;

import com.floober.engine.particles.Particle;
import com.floober.engine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.particles.behavior.movement.MovementBehavior;

public class ParticleBehavior {

	private final MovementBehavior movementBehavior;
	private final AppearanceBehavior appearanceBehavior;

	public ParticleBehavior(MovementBehavior movementBehavior, AppearanceBehavior appearanceBehavior) {
		this.movementBehavior = movementBehavior;
		this.appearanceBehavior = appearanceBehavior;
	}

	// GETTERS
	public MovementBehavior getMovementBehavior() {
		return movementBehavior;
	}
	public AppearanceBehavior getAppearanceBehavior() {
		return appearanceBehavior;
	}

	// GENERATING PARTICLES
	public void initParticle(Particle particle) {
		//
	}

	// UPDATE
	public void updateParticle(Particle particle) {
		movementBehavior.updateParticle(particle);
		appearanceBehavior.updateParticle(particle);
	}

}