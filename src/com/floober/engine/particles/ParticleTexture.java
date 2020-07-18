package com.floober.engine.particles;

import com.floober.engine.textures.TextureAtlas;

public class ParticleTexture extends TextureAtlas {

	private final boolean useAdditiveBlend;

	public ParticleTexture(int textureID, int numRows, boolean useAdditiveBlend) {
		super(textureID, numRows);
		this.useAdditiveBlend = useAdditiveBlend;
	}
	public boolean useAdditiveBlend() { return useAdditiveBlend; }
}
