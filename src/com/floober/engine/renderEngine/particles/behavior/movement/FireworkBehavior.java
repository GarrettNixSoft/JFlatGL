package com.floober.engine.renderEngine.particles.behavior.movement;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.particles.types.EmitterParticle;
import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.math.RandomUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class FireworkBehavior extends MovementBehavior {

	private final float gravity;

	public FireworkBehavior(float gravity) {
		this.gravity = gravity;
	}

	@Override
	public void initParticle(EmitterParticle particle) {
		float speed = RandomUtil.getFloat(particleSpeedMin, particleSpeedMax);
		float angle = RandomUtil.getFloat(360);
		particle.setVelocity(MathUtil.getCartesian(speed, angle));
		float rotationSpeed = 0;
		particle.setRotation(angle);
		particle.setRotationSpeed(rotationSpeed);
	}

	@Override
	public void updateParticle(EmitterParticle particle) {
		// get time
		float time = DisplayManager.getFrameTimeSeconds();
		// get current state
		Vector3f position = particle.getPosition();
		Vector2f velocity = particle.getVelocity();
		// slow it down
		Vector2f inverse = new Vector2f(velocity).negate();
		velocity.add(inverse.mul(time * 2.5f));
		// gravity it
		velocity.y += gravity * time;
		// TEST
		boolean output = false;
//		if (Math.random() < 0.05) output = true;
		// END_TEST* (also output condition of print statements below)
		// do the movement
		float dx = velocity.x() * time;
		float dy = velocity.y() * time;
		float xPos = position.x() + dx;
		float yPos = position.y() + dy;
		// set the new position
		Vector3f start = new Vector3f(), end = new Vector3f();
		if (output) System.out.println("Particle old position: " +  (start = new Vector3f(particle.getPosition())));
		particle.setPosition(xPos, yPos, particle.getLayer());
		if (output) System.out.println("Particle new position: " + (end = new Vector3f(particle.getPosition())));
		if (output) System.out.println("Velocity magnitude: " + velocity.length());
		if (output) System.out.println("Actual change in magnitude: " + start.sub(end).length());
	}
}
