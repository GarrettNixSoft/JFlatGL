package com.floober.engine.renderEngine.particles.behavior.appearance;

import com.floober.engine.renderEngine.particles.types.Particle;
import com.floober.engine.util.math.MathUtil;

public class FadeOutBehavior extends AppearanceBehavior {

	private final float initialAlpha, fadeDelay;

	public FadeOutBehavior(float initialAlpha, float fadeDelay) {
		this.initialAlpha = initialAlpha;
		this.fadeDelay = fadeDelay;
	}

	@Override
	public void initParticle(Particle particle) {
		particle.setAlpha(initialAlpha);
	}

	@Override
	public void updateParticle(Particle particle) {
		float time = particle.getElapsedTime();
		if (time > fadeDelay) {
			float life = particle.getLifeLength();
			float progress = (time - fadeDelay) / (life - fadeDelay);
			float percentage = initialAlpha - MathUtil.smoothstep(0, initialAlpha, progress);
			particle.setAlpha(initialAlpha * percentage);
		}
	}
}