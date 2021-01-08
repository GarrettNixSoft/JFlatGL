package com.floober.engine.entity.misc;

import com.floober.engine.entity.core.Entity;
import com.floober.engine.entity.util.EntityHandler;
import com.floober.engine.renderEngine.particles.emitters.ParticleEmitter;
import org.joml.Vector3f;

public class ParticleEmitterEntity extends Entity {

	private final ParticleEmitter particleEmitter;

	public ParticleEmitterEntity(EntityHandler entityHandler, ParticleEmitter particleEmitter) {
		super(entityHandler);
		this.particleEmitter = particleEmitter;
	}

	@Override
	public void setPosition(Vector3f position) {
		super.setPosition(position);
		particleEmitter.setPosition(position);
	}

	@Override
	public void update() {
		particleEmitter.update();
	}

	@Override
	public void render() {
		//
	}
}
