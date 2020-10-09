package com.floober.engine.renderEngine.particles.behavior.movement;

import com.floober.engine.renderEngine.particles.types.Particle;
import org.joml.Vector2f;

public class StaticBehavior extends MovementBehavior {

	@Override
	public void initParticle(Particle particle) {
		particle.setVelocity(new Vector2f(0));
	}

	@Override
	public void updateParticle(Particle particle) {
		// do nothing
	}

}