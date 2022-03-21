package com.floober.engine.core.renderEngine.particles.behavior.movement;

import com.floober.engine.core.renderEngine.particles.types.EmitterParticle;
import org.joml.Vector2f;

public class StaticBehavior extends MovementBehavior {

	@Override
	public void initParticle(EmitterParticle particle) {
		particle.setVelocity(new Vector2f(0));
	}

	@Override
	public void updateParticle(EmitterParticle particle) {
		// do nothing
	}

}