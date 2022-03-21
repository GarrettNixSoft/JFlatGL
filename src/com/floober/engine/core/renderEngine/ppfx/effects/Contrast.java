package com.floober.engine.core.renderEngine.ppfx.effects;

import com.floober.engine.core.renderEngine.ppfx.PPEffect;
import com.floober.engine.core.renderEngine.shaders.ppfx.ContrastShader;
import com.floober.engine.core.util.configuration.Config;

public class Contrast extends PPEffect {

	private float contrastChangeAmount;

	public Contrast(long windowID) {
		super(windowID, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
		shader = new ContrastShader();
		contrastChangeAmount = 0.5f;
	}

	/**
	 * Set the amount of contrast change. Positive values
	 * increase contrast, negative values decrease contrast.
	 * @param contrastChangeAmount the amount of change
	 */
	public void setContrastChangeAmount(float contrastChangeAmount) {
		this.contrastChangeAmount = contrastChangeAmount;
	}

	@Override
	public void init() {
		ContrastShader shader = (ContrastShader) this.shader;
		shader.loadContrastChangeAmount(contrastChangeAmount);
	}
}
