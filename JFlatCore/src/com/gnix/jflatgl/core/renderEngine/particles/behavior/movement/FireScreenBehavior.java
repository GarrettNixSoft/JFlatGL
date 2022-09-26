package com.gnix.jflatgl.core.renderEngine.particles.behavior.movement;

import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.particles.systems.FireScreenParticleSystem;
import com.gnix.jflatgl.core.renderEngine.particles.types.EmitterParticle;
import com.gnix.jflatgl.core.util.math.RandomUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FireScreenBehavior extends MovementBehavior {

	private final FireScreenParticleSystem fireSystem;

	public FireScreenBehavior(FireScreenParticleSystem fireSystem) {
		this.fireSystem = fireSystem;
	}

	@Override
	public void initParticle(EmitterParticle particle) {
		float dx = RandomUtil.getFloat(-50, 50);
		float dy = RandomUtil.getCenterBiasedRandom(500);
	}

	@Override
	public void updateParticle(EmitterParticle particle) {
		// interact with fire system
		fireSystem.interact(particle);
		// move it
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
