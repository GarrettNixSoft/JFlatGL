package com.gnix.jflatgl.core.renderEngine.ppfx.effects;

import com.gnix.jflatgl.core.renderEngine.ppfx.PPEffect;
import com.gnix.jflatgl.core.renderEngine.shaders.ppfx.GrayscaleShader;
import com.gnix.jflatgl.core.util.configuration.Config;

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
