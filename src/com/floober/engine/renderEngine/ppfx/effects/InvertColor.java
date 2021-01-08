package com.floober.engine.renderEngine.ppfx.effects;

import com.floober.engine.renderEngine.ppfx.PPEffect;
import com.floober.engine.renderEngine.shaders.ppfx.InvertColorShader;
import com.floober.engine.util.configuration.Config;

public class InvertColor extends PPEffect {

	public InvertColor() {
		super(Config.DEFAULT_RESOLUTION_WIDTH, Config.DEFAULT_RESOLUTION_HEIGHT);
		shader = new InvertColorShader();
	}

	@Override
	public void init() {
		// nothing
	}
}
