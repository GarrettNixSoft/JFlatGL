package com.floober.engine.renderEngine.ppfx.effects;

import com.floober.engine.renderEngine.ppfx.PPEffect;
import com.floober.engine.renderEngine.shaders.ppfx.InvertColorShader;
import com.floober.engine.util.configuration.Config;

public class InvertColor extends PPEffect {

	public InvertColor() {
		super(Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
		shader = new InvertColorShader();
	}

	@Override
	public void init() {
		// nothing
	}
}
