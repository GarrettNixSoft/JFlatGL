package com.floober.engine.core.renderEngine.particles.behavior.appearance;

import com.floober.engine.core.renderEngine.particles.types.EmitterParticle;

public class FadeInOutBehavior extends AppearanceBehavior {

	private final float peakAlpha, fadeInPortion, fadeOutPortion;

	public FadeInOutBehavior(float peakAlpha, float fadeInPortion, float fadeOutPortion) {
		this.peakAlpha = peakAlpha;
		this.fadeInPortion = fadeInPortion;
		this.fadeOutPortion = fadeOutPortion;
	}

	@Override
	public void initParticle(EmitterParticle particle) {
		particle.setAlpha(0);
		float fadeInTime = particle.getLifeLength() * fadeInPortion;
		float fadeOutTime = particle.getLifeLength() * fadeOutPortion;
		particle.addValue("fadeInTime", fadeInTime);
		particle.addValue("fadeDelay", particle.getLifeLength() - fadeOutTime);
	}

	@Override
	public void updateParticle(EmitterParticle particle) {
		float time = particle.getElapsedTime();
		float value;
		if (time < (value = particle.getValue("fadeInTime"))) { // fading in
			float progress = time / value;
			particle.setAlpha(peakAlpha * progress);
		}
		else if (time > (value = particle.getValue("fadeDelay"))) { // fading out
			float progress = (time - value) / (particle.getLifeLength() - value);
			particle.setAlpha(peakAlpha - (peakAlpha * progress));
		}
		else { // peak alpha stage
			particle.setAlpha(peakAlpha);
		}
	}
}