package gametitle.entity.misc;

import com.gnix.jflatgl.core.camera.Camera;
import com.gnix.jflatgl.core.entity.Entity;
import com.gnix.jflatgl.core.renderEngine.particles.emitters.ParticleEmitter;
import org.joml.Vector3f;

public class ParticleEmitterEntity extends Entity {

	private final ParticleEmitter particleEmitter;

	public ParticleEmitterEntity(ParticleEmitter particleEmitter) {
		super();
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
	public void render(Camera camera) {
		//
	}
}
