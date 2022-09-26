package com.gnix.jflatgl.core.renderEngine.particles.behavior.appearance;

import com.gnix.jflatgl.core.renderEngine.particles.types.EmitterParticle;
import com.gnix.jflatgl.core.util.math.MathUtil;

public class FadeOutBehavior extends AppearanceBehavior {

	private final float initialAlpha, fadeDelay;

	public FadeOutBehavior(float initialAlpha, float fadeDelay) {
		this.initialAlpha = initialAlpha;
		this.fadeDelay = fadeDelay;
	}

	@Override
	public void initParticle(EmitterParticle particle) {
		particle.setAlpha(initialAlpha);
	}

	@Override
	public void updateParticle(EmitterParticle particle) {
		float time = particle.getElapsedTime();
		if (time > fadeDelay) {
			float life = particle.getLifeLength();
			float progress = (time - fadeDelay) / (life - fadeDelay);
			float percentage = initialAlpha - MathUtil.interpolateBounded(0, initialAlpha, progress);
			particle.setAlpha(initialAlpha * percentage);
		}
	}
}
