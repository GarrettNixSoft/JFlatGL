package com.floober.engine.core.renderEngine.renderers;

import com.floober.engine.core.assets.loaders.ImageLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.particles.ParticleTexture;
import com.floober.engine.core.renderEngine.particles.behavior.ParticleBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.appearance.FadeOutBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.FlameBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.MovementBehavior;
import com.floober.engine.core.renderEngine.particles.emitters.ParticleEmitter;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.Globals;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.core.util.file.FileUtil;
import com.floober.engine.core.util.interpolators.FadeFloat;
import com.floober.engine.core.util.interpolators.FadeInFloat;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONObject;

import static com.floober.engine.core.util.Globals.*;

/**
 * This class handles rendering while the game loads.
 * It calls an {@code init()} method to load the few
 * assets it needs to draw the render progress screen,
 * then the game can begin loading. Progress is reported
 * to a global variable, and after each asset is loaded,
 * the LoadRenderer is called again to update the screen
 * with the current progress.
 */
public class LoadRenderer {

	public static LoadRenderer instance;

	// loading stages
	public static final int FONTS = 0, TEXTURES = 1, ANIMATIONS = 2, SFX = 3, MUSIC = 4, FADE_IN = 5, DONE = 6, FADE_OUT = 7;

	// make it look cool and smooth
	private final FadeInFloat fadeIn;
	private final FadeInFloat fadeInCheck;
	private final FadeFloat fadeOut;

	// screen components
	private TextureElement logoElement;
	private TextureElement checkElement;
	private GUIText progressText;
	private RectElement baseBar;
	private RectElement progressBar;

	private ParticleEmitter particleSource;

	public LoadRenderer() {
		if (instance == null) instance = this;
		fadeIn = new FadeInFloat(1);
		fadeInCheck = new FadeInFloat(0.75f);
		fadeOut = new FadeFloat(1);
	}

	/**
	 * Loads the resources necessary for the loading screen.
	 */
	public LoadRenderer init() {
		// get settings
		String fontName = Config.LOAD_RENDER_SETTINGS.getString("font");
		String logoPath = Config.LOAD_RENDER_SETTINGS.getString("logo");
		String checkPath = Config.LOAD_RENDER_SETTINGS.getString("check");
		// prepare the text master
		TextMaster.init();
		// width and height convenience variables
		int WIDTH = Config.INTERNAL_WIDTH;
		int HEIGHT = Config.INTERNAL_HEIGHT;
		// positions
		float screenCenterX = WIDTH / 2f;
		float screenCenterY = HEIGHT / 2f;
		// load assets
		// loading screen assets
		TextureComponent logo = ImageLoader.loadTexture(logoPath);
		logoElement = new TextureElement(logo, screenCenterX, screenCenterY - 200, Layers.DEFAULT_LAYER, 256, 256, true);
		logoElement.setHasTransparency(true);
		TextureComponent check = Loader.loadTexture(checkPath);
		checkElement = new TextureElement(check, screenCenterX, screenCenterY + 200, Layers.DEFAULT_LAYER, 64, 64, true);
		checkElement.setHasTransparency(true);
		FontType loadingFont = Loader.loadFont(fontName);
		// set colors
		Vector4f baseColor = new Vector4f(1);
		Vector4f barColor = new Vector4f(1, 1, 1, 0);
		Vector4f textColor = new Vector4f(Colors.WHITE);
		// configure text
		// progress
		float lineLength = 0.5f;
		float x = 0.5f - lineLength / 2;
		progressText = new GUIText("Loading...\n0%", 1.5f, loadingFont, new Vector3f(x, 0.55f, 0), lineLength, true);
		progressText.setColor(textColor);
		progressText.setWidth(0.5f);
		progressText.setEdge(0.2f);
		progressText.show();
		// configure rect elements
		int barWidth = 500;
		int barHeight = 10;
		// base bar
		baseBar = new RectElement(baseColor, WIDTH / 2f, HEIGHT / 2f + 30, Layers.DEFAULT_LAYER, barWidth, barHeight, true);
		progressBar = new RectElement(barColor, WIDTH / 2f - barWidth / 2f, HEIGHT / 2f + 30 - barHeight / 2f, Layers.DEFAULT_LAYER + 1, 0, barHeight, false);
		// particle effect
		TextureComponent particleTex = ImageLoader.loadTexture("res/textures/particles/glow_map.png");
		// particle effect
		ParticleTexture particleTexture = new ParticleTexture(particleTex, 1, true);
		Vector3f sourcePosition = new Vector3f(WIDTH / 2f - barWidth / 2f, HEIGHT / 2f + 30 - barHeight / 2f, Layers.TOP_LAYER);
		MovementBehavior movementBehavior = new FlameBehavior(-90, 20);
		movementBehavior.initSpeed(10, 80);
		AppearanceBehavior appearanceBehavior = new FadeOutBehavior(1, 0);
		appearanceBehavior.initSize(5, 40);
		ParticleBehavior particleBehavior = new ParticleBehavior(movementBehavior, appearanceBehavior);
		particleBehavior.initLife(0.1f, 1.0f);
		particleSource = new ParticleEmitter(sourcePosition, particleTexture, particleBehavior);
		particleSource.initPositionDelta(0, 1);
		particleSource.setBoxMode(false);
		particleSource.setParticleDelay(40);
		// reset frame deltas
		DisplayManager.updateDisplay();
		return this;
	}

	public static void reportCurrentAsset(String asset) {
		Globals.currentAsset = asset;
	}

	public void render() {
		// prepare
		MasterRenderer.primaryWindowRenderer.prepare(false);
		// invert colors?
		boolean invertColors = false;
		// render white background
		Render.fillScreen(invertColors ? Colors.BLACK : Colors.WHITE);
		// bar size
		int barWidth = 500;
		// get current load info
		String stageName;
		int currCount, total;
		float complete, progress, portion;
		// complete = bar already done;
		// progress = percentage of current stage;
		// portion = how much this stage counts toward total bar
		// BAR OPACITY CONTROL
		float baseAlpha = invertColors ? 0.5f : 0.25f;
		float infoAlpha = 1f;
		float checkAlpha = 0f;
		// configure this pass
		switch (LOAD_STAGE) {
			case FONTS:
				stageName = "fonts...";
				currCount = fontCount;
				total = fontTotal;
				complete = 0;
				portion = 0.05f;
				break;
			case TEXTURES:
				stageName = "textures...";
				currCount = texCount;
				total = texTotal;
				complete = 0.05f;
				portion = 0.45f;
				break;
			case ANIMATIONS:
				stageName = "animations...";
				currCount = animationCount;
				total = animationTotal;
				complete = 0.50f;
				portion = 0.08f;
				break;
			case SFX:
				stageName = "sfx...";
				currCount = sfxCount;
				total = sfxTotal;
				complete = 0.58f;
				portion = 0.12f;
				break;
			case MUSIC:
				stageName = "music...";
				currCount = musicCount;
				total = musicTotal;
				complete = 0.7f;
				portion = 0.3f;
				break;
			case FADE_IN:
				if (!fadeIn.isRunning()) fadeIn.start();
				fadeIn.update();
				stageName = "";
				currCount = 0;
				total = 1;
				complete = 0;
				portion = 1;
				baseAlpha *= fadeIn.getValue();
				infoAlpha = fadeIn.getValue();
				break;
			case DONE:
				if (!fadeInCheck.isRunning()) fadeInCheck.start();
				fadeInCheck.update();
				stageName = "Complete!";
				currCount = 0;
				total = 1;
				complete = 1;
				portion = 1;
				checkAlpha = fadeInCheck.getValue();
				break;
			case FADE_OUT:
				if (!fadeOut.isRunning()) fadeOut.start();
				fadeOut.update();
				stageName = "Complete!";
				currCount = 0;
				total = 1;
				complete = 1;
				portion = 1;
				infoAlpha = checkAlpha = fadeOut.getValue();
				baseAlpha *= fadeOut.getValue();
				break;
			case -1: // load sequence hasn't even started yet
				return;
			default:
				stageName = "null";
				currCount = 0;
				total = 1;
				complete = 0;
				portion = 1;
				break;
		}

		if (total == 0) total = 1;
		progress = ((float) currCount / total);
		String progressStr = "";
		// if loading, report what stage; if debug enabled, include ID of current asset
		if (!(stageName.equals("Complete!") || stageName.equals("")))
			progressStr = Settings.showLoadDebug ?
					currentAsset + " [" + currCount + "/" + total + "]" :
					" [" + currCount + "/" + total + "]";

		// invert colors?
		if (invertColors) {
			infoAlpha = 1 - infoAlpha;
			baseAlpha = 1 - baseAlpha;
		}


		// render base bar
		Vector4f baseColor = new Vector4f(baseAlpha);
		if (invertColors) baseColor = Colors.invert(baseColor);
		baseBar.setColor(baseColor);
		baseBar.transform();
		Render.drawRect(baseBar);
		// draw bar
		Vector4f infoColor = new Vector4f(1 - infoAlpha);
		float totalWidth = (barWidth * complete) + (barWidth * (portion * progress));
		progressBar.setWidth(totalWidth);
		progressBar.transform();
		Vector4f copy = new Vector4f(infoColor);
		if (!invertColors) copy.w = 1;
		progressBar.getColor().set(copy);
		Render.drawRect(progressBar);
		// update particle source position, generate some particles
		particleSource.setPosition(progressBar.getX() + progressBar.getWidth(), progressBar.getY() + progressBar.getHeight() / 2, Layers.TOP_LAYER);
//		particleSource.update(); // TODO decide if this looks better without the particles (it probably does; less jittery)
		// draw text, report percentage
		float percentage = complete + portion * progress;
		int roundedPercent = (int) (percentage * 100.0);
		String text = "Loading " + stageName + progressStr + "\n" + roundedPercent + "%";
		progressText.replaceText(text);
		progressText.setColor(copy);
		progressText.update();
		// draw logo
		logoElement.setAlpha(invertColors ? 1 - infoAlpha : infoAlpha);
		Render.drawImage(logoElement);
		// fade particles
//		particleSource.getParticleBehavior().setParticleLifeMin(0.1f * (1 - infoAlpha));
//		particleSource.getParticleBehavior().setParticleLifeMax(1 - infoAlpha);
//		particleSource.getParticleBehavior().getAppearanceBehavior().getParticleColor().w = 1 - infoAlpha;
		// draw check if possible
		if (checkAlpha != 0) {
			checkElement.setAlpha(checkAlpha);
			Render.drawImage(checkElement);
		}

		// update the screen
		renderFrame();
		DisplayManager.updateDisplay();
	}

	private void renderFrame() {
		/*
			- Target window is correct
			- Buffers are swapping (?)
			- prepare is called before this
			- updateDisplay is called after this
			- is post-processing working?

			I HAD FORGOTTEN THAT I CHANGED HOW TRANSFORMATION MATRICES ARE GENERATED
			ALL THE QUADS WEREN'T BEING SCALED TO SCREEN SPACE COORDINATES
			THEY WERE BEING RENDERED LIKE 5000 TIMES THE SIZE OF THE SCREEN
		 */
		MasterRenderer.primaryWindowRenderer.render(true);
		MasterRenderer.getTargetWindow().swapBuffers();
	}

	/**
	 * Removes any components from the renderer.
	 */
	public void cleanUp() {
		progressText.remove();
	}

}