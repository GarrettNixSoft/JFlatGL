package com.floober.engine.test.splash;

import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.splash.SplashRenderer;
import com.floober.engine.core.splash.SplashScreen;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.time.Timer;

public class TestSplashRenderer extends SplashRenderer {

	private final Timer timer = new Timer(-1);

	@Override
	public void init() {
		// nothing
	}

	@Override
	public void render() {
		float size = Config.SPLASH_WIDTH * timer.getTimeElapsedSeconds() / 5;
		RectElement redRect = new RectElement(Colors.RED, 0, Config.SPLASH_HEIGHT - 50, Layers.BOTTOM_LAYER + 1, size, 50, false);
		redRect.transform(SplashScreen.splashWindow);
		Render.drawRect(redRect);
	}

	@Override
	public void cleanUp() {
		// nothing
	}
}
