package com.floober.engine.core.renderEngine.particles.types;

import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.particles.ParticleTexture;

public class GravityParticle extends Particle {

	private final float gravity;
	private final float dx;
	private float dy;

	/**
	 * Generate a new particle.
	 * @param texture The particle texture.
	 */
	public GravityParticle(ParticleTexture texture, float x, float y, float z, float width, float height, float dx, float dy, float gravity) {
		super(texture, x, y, z, width, height, 1);
		this.x = x;
		this.dx = dx;
		this.dy = dy;
		this.gravity = gravity;
	}

	@Override
	public boolean update() {
		float time = DisplayManager.getCurrentFrameDelta();
		this.dy += gravity * time;
		this.x += dx * time;
		this.y += dy * time;
		setPosition(DisplayManager.convertToDisplayPosition(x, y, layer, width, height, true));
		return super.update();
	}

}
