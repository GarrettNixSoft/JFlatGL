package com.floober.engine.core.renderEngine.particles.systems;

import com.floober.engine.core.renderEngine.particles.types.Particle;

public abstract class ParticleSystem {

	public abstract void update();

	public abstract void interact(Particle particle);

}