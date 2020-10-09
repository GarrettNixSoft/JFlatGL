package com.floober.engine.renderEngine.particles.emitters;

import com.floober.engine.renderEngine.particles.ParticleTexture;
import com.floober.engine.renderEngine.particles.behavior.ParticleBehavior;
import com.floober.engine.renderEngine.particles.types.LightParticle;
import com.floober.engine.util.math.RandomUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class LightParticleEmitter extends ParticleEmitter {

	private float minInnerRadius, maxInnerRadius;
	private float minOuterRadius, maxOuterRadius;
	private Vector3f lightColor;
	private int lightMode;
	private float minLightIntensity, maxLightIntensity;
	private float minLightRadius, maxLightRadius;

	public LightParticleEmitter(Vector3f position, ParticleTexture particleTexture, ParticleBehavior particleBehavior) {
		super(position, particleTexture, particleBehavior);
	}

	// INITIALIZERS
	public void initInnerRadius(float minInnerRadius, float maxInnerRadius) {
		this.minInnerRadius = minInnerRadius;
		this.maxInnerRadius = maxInnerRadius;
	}

	public void initOuterRadius(float minOuterRadius, float maxOuterRadius) {
		this.minOuterRadius = minOuterRadius;
		this.maxOuterRadius = maxOuterRadius;
	}

	public void initLightIntensity(float minLightIntensity, float maxLightIntensity) {
		this.minLightIntensity = minLightIntensity;
		this.maxLightIntensity = maxLightIntensity;
	}

	public void initLightRadius(float minLightRadius, float maxLightRadius) {
		this.minLightRadius = minLightRadius;
		this.maxLightRadius = maxLightRadius;
	}

	// GETTERS
	public float getMinInnerRadius() {
		return minInnerRadius;
	}
	public float getMaxInnerRadius() {
		return maxInnerRadius;
	}
	public float getMinOuterRadius() {
		return minOuterRadius;
	}
	public float getMaxOuterRadius() {
		return maxOuterRadius;
	}
	public Vector3f getLightColor() {
		return lightColor;
	}
	public int getLightMode() {
		return lightMode;
	}
	public float getMinLightIntensity() {
		return minLightIntensity;
	}
	public float getMaxLightIntensity() {
		return maxLightIntensity;
	}
	public float getMinLightRadius() {
		return minLightRadius;
	}
	public float getMaxLightRadius() {
		return maxLightRadius;
	}

	// SETTERS
	public void setMinInnerRadius(float minInnerRadius) {
		this.minInnerRadius = minInnerRadius;
	}
	public void setMaxInnerRadius(float maxInnerRadius) {
		this.maxInnerRadius = maxInnerRadius;
	}
	public void setMinOuterRadius(float minOuterRadius) {
		this.minOuterRadius = minOuterRadius;
	}
	public void setMaxOuterRadius(float maxOuterRadius) {
		this.maxOuterRadius = maxOuterRadius;
	}
	public void setLightColor(Vector3f lightColor) {
		this.lightColor = lightColor;
	}
	public void setLightMode(int lightMode) {
		this.lightMode = lightMode;
	}
	public void setMinLightIntensity(float minLightIntensity) {
		this.minLightIntensity = minLightIntensity;
	}
	public void setMaxLightIntensity(float maxLightIntensity) {
		this.maxLightIntensity = maxLightIntensity;
	}
	public void setMinLightRadius(float minLightRadius) {
		this.minLightRadius = minLightRadius;
	}
	public void setMaxLightRadius(float maxLightRadius) {
		this.maxLightRadius = maxLightRadius;
	}

	@Override
	public void generateParticle() {
		// get a position for the particle
		Vector3f particlePosition = generatePositionVector();
		// get the light particle values
		float innerRadius = RandomUtil.getFloat(minInnerRadius, maxInnerRadius);
		float outerRadius = innerRadius + RandomUtil.getFloat(minOuterRadius, maxOuterRadius);
		float lightIntensity = RandomUtil.getFloat(minLightIntensity, maxLightIntensity);
		float lightRadius = RandomUtil.getFloat(minLightRadius, maxLightRadius);
		// convert to relative values
		float fixedInnerRad = innerRadius / lightRadius;
		float fixedOuterRad = outerRadius / lightRadius;
		// generate the particle
		LightParticle particle = new LightParticle(getParticleBehavior(), getParticleTexture(),
				getParticleBehavior().getAppearanceBehavior().getParticleColor(), particlePosition,
				lightRadius * 2, lightRadius * 2, new Vector2f(), fixedInnerRad, fixedOuterRad, lightMode, lightColor,
				lightIntensity, lightRadius, 1, true);
		// init the particle
		getParticleBehavior().initParticle(particle);
		// tell the particle to convert to screen position
		particle.convertScreenPosition();
		// done!
	}

}
