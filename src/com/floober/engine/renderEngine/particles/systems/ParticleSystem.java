package com.floober.engine.renderEngine.particles.systems;

import com.floober.engine.renderEngine.particles.types.Particle;

public abstract class ParticleSystem {

	public abstract void update();

	public abstract void interact(Particle particle);

}