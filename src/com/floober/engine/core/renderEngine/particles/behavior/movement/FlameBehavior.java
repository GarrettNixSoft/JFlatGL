package com.floober.engine.core.renderEngine.particles.behavior.movement;

import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.particles.types.EmitterParticle;
import com.floober.engine.core.util.math.MathUtil;
import com.floober.engine.core.util.math.RandomUtil;
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
	public void initParticle(EmitterParticle particle) {
		float speed = RandomUtil.getFloat(particleSpeedMin, particleSpeedMax);
		float angle = RandomUtil.getFloatAverage(flameDirection, flameAngleVariation);
		particle.setVelocity(MathUtil.getCartesian(speed, angle));
		float startRotation = RandomUtil.getFloat(360);
		particle.setRotation(startRotation);
		particle.setRotationSpeed(0);
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
		particle.setPosition(xPos, yPos, particle.getLayer());
	}
}
