package com.floober.engine.particles.behavior.appearance;

import com.floober.engine.particles.Particle;

public abstract class AppearanceBehavior {

	public abstract void initParticle(Particle particle);
	public abstract void updateParticle(Particle particle);

}