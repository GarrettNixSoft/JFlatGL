package com.gnix.jflatgl.core.renderEngine.ppfx.effects;

import com.gnix.jflatgl.core.renderEngine.ppfx.PPEffect;
import com.gnix.jflatgl.core.renderEngine.shaders.ppfx.InvertColorShader;
import com.gnix.jflatgl.core.util.configuration.Config;

public class InvertColor extends PPEffect {

	public InvertColor(long windowID) {
		super(windowID, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
		shader = new InvertColorShader();
	}

	@Override
	public void init() {
		// nothing
	}
}
