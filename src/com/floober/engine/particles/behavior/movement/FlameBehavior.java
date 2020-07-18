package com.floober.engine.particles.behavior.movement;

import com.floober.engine.particles.Particle;
import org.joml.Vector2f;

public class FlameBehavior extends MovementBehavior {

	private Vector2f flameDirection;
	private float averageSpeed;
	private float speedVariation;

	public FlameBehavior(Vector2f flameDirection, float averageSpeed, float speedVariation) {
		this.flameDirection = flameDirection;
		this.averageSpeed = averageSpeed;
		this.speedVariation = speedVariation;
	}

	@Override
	public void updateParticle(Particle particle) {
		//
	}
}
