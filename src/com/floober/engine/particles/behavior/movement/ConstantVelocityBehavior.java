package com.floober.engine.particles.behavior.movement;

import com.floober.engine.display.Display;
import com.floober.engine.particles.Particle;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ConstantVelocityBehavior extends MovementBehavior {

	private float minVelocity, maxVelocity;
	private float minAngle, maxAngle;

	public ConstantVelocityBehavior(float minVelocity, float maxVelocity, float minAngle, float maxAngle) {
		this.minVelocity = minVelocity;
		this.maxVelocity = maxVelocity;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
	}

	@Override
	public void initParticle(Particle particle) {
		//
	}

	@Override
	public void updateParticle(Particle particle) {
		Vector2f velocity = particle.getVelocity();
		Vector3f initialPosition = particle.getInitialPosition();
		float time = particle.getElapsedTime();
		float dx = velocity.x() * time;
		float dy = velocity.y() * time;
		float xPos = initialPosition.x() + dx;
		float yPos = initialPosition.y() + dy;
		// set map position
		particle.setPosition(Display.convertToDisplayPosition(xPos, yPos, particle.getZ(), particle.getWidth(), particle.getHeight(), true));
	}
}
