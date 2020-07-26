package com.floober.engine.particles;

import com.floober.engine.textures.TextureAtlas;

public class ParticleTexture extends TextureAtlas {

	private final boolean useAdditiveBlend;

	public ParticleTexture(int textureID, int width, int height, int numRows, boolean useAdditiveBlend) {
		super(textureID, width, height, numRows);
		this.useAdditiveBlend = useAdditiveBlend;
	}
	public boolean useAdditiveBlend() { return useAdditiveBlend; }
}
