package com.gnix.jflatgl.core.renderEngine.particles.types;

import com.gnix.jflatgl.core.renderEngine.particles.ParticleTexture;
import com.gnix.jflatgl.core.renderEngine.particles.behavior.ParticleBehavior;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LightParticle extends EmitterParticle {

	// size of the actual particle
	private final float innerRadius; // radius to keep at full color
	private final float outerRadius; // edge radius where color reaches zero

	// light
	public static final int LINEAR = 0, SMOOTH = 1;
	private final int lightMode; // 0 = Linear, 1 = Smooth
	private final Vector3f lightColor = new Vector3f();
	private final float lightIntensity;
	private final float lightRadius;

	public LightParticle(ParticleBehavior behavior, ParticleTexture texture, Vector4f color,
						 Vector3f position, float width, float height, Vector2f velocity,
						 float innerRadius, float outerRadius, int lightMode, Vector3f lightColor,
						 float lightIntensity, float lightRadius, float lifeLength, boolean fadeOut) {
		super(behavior, texture, color, position, width, height, velocity, lifeLength, 0, 0, fadeOut);
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.lightMode = lightMode;
		this.lightColor.set(lightColor);
		this.lightIntensity = lightIntensity;
		this.lightRadius = lightRadius;
	}

	// GETTERS
	public float getInnerRadius() {
		return innerRadius;
	}
	public float getOuterRadius() {
		return outerRadius;
	}
	public int getLightMode() { return lightMode; }
	public Vector3f getLightColor() { return lightColor; }
	public float getLightIntensity() { return lightIntensity; }
	public float getLightRadius() { return lightRadius; }

}
