package com.floober.engine.renderEngine.particles.types;

import com.floober.engine.renderEngine.particles.ParticleTexture;
import com.floober.engine.renderEngine.particles.behavior.ParticleBehavior;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * A TexturedParticle is a particle that displays a fragment
 * of a texture on its quad.
 */
public class TexturedParticle extends EmitterParticle {

	private final Vector4f textureCoords = new Vector4f();

	/**
	 * Create a new Textured Particle.
	 * @param behavior The behavior that controls this particle.
	 * @param texture The texture this particle will use.
	 * @param position The position of this particle.
	 */
	public TexturedParticle(ParticleBehavior behavior, ParticleTexture texture, Vector3f position) {
		super(behavior, texture, position);
	}

	/**
	 * Set the coordinates on the texture that this particle will use.
	 * @param coords The coordinates; (x,y) is the top left corner, (z,w) is the bottom right.
	 */
	public void setTextureCoords(Vector4f coords) {
		textureCoords.set(coords);
	}

	/***
	 * Get the texture coordinates used by this particle.
	 * @return The coordinates.
	 */
	public Vector4f getTextureCoords() {
		return textureCoords;
	}

}