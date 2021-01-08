package com.floober.engine.renderEngine.particles.behavior.movement;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.particles.types.EmitterParticle;
import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.math.RandomUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ConstantVelocityBehavior extends MovementBehavior {

	protected final float minAngle, maxAngle;

	public ConstantVelocityBehavior(float minAngle, float maxAngle) {
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
	}

	@Override
	public void initParticle(EmitterParticle particle) {
		float speed = RandomUtil.getFloat(particleSpeedMin, particleSpeedMax);
		float angle = RandomUtil.getFloat(minAngle, maxAngle);
		particle.setVelocity(MathUtil.getCartesian(speed, angle));
		float startRotation = RandomUtil.getFloat(360);
		float rotationSpeed = 0;
		particle.setRotation(startRotation);
		particle.setRotationSpeed(rotationSpeed);
	}

	@Override
	public void updateParticle(EmitterParticle particle) {
		Vector2f velocity = particle.getVelocity();
		Vector3f position = particle.getPosition();
		float time = DisplayManager.getFrameTimeSeconds();
		float dx = velocity.x() * time;
		float dy = velocity.y() * time;
		float xPos = position.x() + dx;
		float yPos = position.y() + dy;
		// set map position
		particle.setPosition(xPos, yPos, particle.getZ());
	}
}
