package com.floober.engine.core.renderEngine.ppfx.effects;

import com.floober.engine.core.renderEngine.ppfx.PPEffect;
import com.floober.engine.core.renderEngine.shaders.ppfx.InvertColorShader;
import com.floober.engine.core.util.configuration.Config;

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
