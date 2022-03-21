package com.floober.engine.core.renderEngine.ppfx.effects;

import com.floober.engine.core.renderEngine.ppfx.PPEffect;
import com.floober.engine.core.renderEngine.shaders.ppfx.GrayscaleShader;
import com.floober.engine.core.util.configuration.Config;

public class Grayscale extends PPEffect {

	public Grayscale(long windowID) {
		super(windowID, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
		shader = new GrayscaleShader();
	}

	@Override
	public void init() {
		// nothing
	}
}