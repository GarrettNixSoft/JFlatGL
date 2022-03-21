package com.floober.engine.core.renderEngine.ppfx.effects;

import com.floober.engine.core.renderEngine.ppfx.PPEffect;
import com.floober.engine.core.renderEngine.shaders.ppfx.ToScreenShader;

/**
 * The ToScreen effect is the last stage in the Post Processing pipeline.
 * It exists only to render the contents of the last frame buffer used
 * onto the screen to ensure that, in all cases, the scene makes it to
 * the player's display.
 */
public class ToScreen extends PPEffect {

	public ToScreen(long windowID) {
		super(windowID);
		shader = new ToScreenShader();
	}

	@Override
	public void init() {
		// nothing
	}

}
