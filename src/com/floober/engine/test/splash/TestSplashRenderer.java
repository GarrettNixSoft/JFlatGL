package com.floober.engine.test.splash;

import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.splash.SplashRenderer;
import com.floober.engine.core.splash.SplashScreen;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.time.Timer;
import org.joml.Vector3f;

public class TestSplashRenderer extends SplashRenderer {

	private final Timer timer = new Timer(-1);

	private GUIText loadText;

	private TextureElement textureElement;

	@Override
	public void init() {

		FontType font = Loader.loadFontConverted("aller");

		loadText = new GUIText("", 5, font, new Vector3f(0, 0.5f, 1), 1, GUIText.Justify.CENTER);
		loadText.setColor(Colors.GREEN);
		loadText.setWidth(0.5f);
		loadText.setEdge(0.2f);
		loadText.show();

		timer.start();

		TextureComponent textureComponent = Loader.loadTextureConverted("default.png");

		textureElement = new TextureElement(textureComponent);
		textureElement.setPosition(Config.SPLASH_WIDTH / 2f, Config.SPLASH_HEIGHT / 2f, 1);
		textureElement.setSize(200, 200);
		SplashScreen.transform(textureElement);
	}

	@Override
	public void render() {
		loadText.replaceText(GameLoader.getLoadString());

		float size = Config.SPLASH_WIDTH * GameLoader.getLoadProgress();
		RectElement redRect = new RectElement(Colors.RED, 0, Config.SPLASH_HEIGHT - 50, Layers.BOTTOM_LAYER + 1, size, 50, false);
		redRect.transform(SplashScreen.splashWindow);
		Render.drawRect(redRect);

		textureElement.renderTransformed();
	}

	@Override
	public void cleanUp() {
		loadText.remove();
	}
}
