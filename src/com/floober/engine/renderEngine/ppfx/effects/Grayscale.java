package com.floober.engine.renderEngine.ppfx.effects;

import com.floober.engine.renderEngine.ppfx.PPEffect;
import com.floober.engine.renderEngine.shaders.ppfx.GrayscaleShader;
import com.floober.engine.util.configuration.Config;

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