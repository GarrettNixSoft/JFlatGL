package com.floober.engine.particles.behavior.movement;

import com.floober.engine.display.Display;
import com.floober.engine.particles.Particle;
import com.floober.engine.util.Logger;
import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.math.RandomUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FlameBehavior extends MovementBehavior {

	private final float flameDirection;
	private final float flameAngleVariation;

	public FlameBehavior(float flameDirection, float flameAngleVariation) {
		this.flameDirection = flameDirection;
		this.flameAngleVariation = flameAngleVariation;
	}

	@Override
	public void initParticle(Particle particle) {
		float speed = RandomUtil.getFloat(particleSpeedMin, particleSpeedMax);
		float angle = RandomUtil.getFloatAverage(flameDirection, flameAngleVariation);
		particle.setVelocity(MathUtil.getCartesian(speed, angle));
		float startRotation = RandomUtil.getFloat(360);
		particle.setRotation(startRotation);
		particle.setRotationSpeed(0);
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
