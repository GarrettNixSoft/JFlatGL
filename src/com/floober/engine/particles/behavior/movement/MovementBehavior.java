package com.floober.engine.particles.behavior.movement;

import com.floober.engine.particles.Particle;

public abstract class MovementBehavior {

	public abstract void initParticle(Particle particle);
	public abstract void updateParticle(Particle particle);

}
