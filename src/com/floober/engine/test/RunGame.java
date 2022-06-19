package com.floober.engine.test;

import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.ImageLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.assets.loaders.RawTexture;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.gui.GUIManager;
import com.floober.engine.core.renderEngine.Screenshot;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.fonts.fontRendering.FontRenderer;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.renderers.TextureRenderer;
import com.floober.engine.core.renderEngine.textures.TextureOutliner;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.time.TimeScale;
import com.floober.engine.test.splash.TestSplashRenderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;

import java.time.ZonedDateTime;
import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Run the game! The {@code main()} function here contains
 * all initial game setup and the game loop, as well as clean-up
 * code after the game loop.
 */
public class RunGame {

	private static GUIText fpsDisplay;

	public static void main(String[] args) {

		// Load user preferences/settings and game flags
		Config.load();
		Settings.load();

		// TESTING GAME
		Settings.setSettingValue("fullscreen", false);
		// FOR EFFICIENCY IN RUNNING MULTIPLE TIMES TO CHECK THINGS

		// game components
//		Sync sync = new Sync(); // this is optional; it's meant to sync framerates to a constant speed but is rather buggy

		// Update the display once to get the timings set
		DisplayManager.updateDisplay();

		// Set up a splash renderer
		TestSplashRenderer splashRenderer = new TestSplashRenderer();

		// load the game assets
		Game.init(splashRenderer);

		// with assets now loaded, have the system masters set their global shortcuts
		ParticleMaster.initGlobals();

		// finish load render
//		loadRenderer.cleanUp();

		// SET UP DEBUG TEXT
		fpsDisplay = new GUIText("FPS: ", 0.5f, Game.getFont("menu"), new Vector3f(0, 0, 0), 1, false);
		fpsDisplay.setColor(Colors.GREEN);
		fpsDisplay.setWidth(0.5f);
		fpsDisplay.setEdge(0.2f);
		fpsDisplay.show();

		RawTexture fontRaw = ImageLoader.loadTexture("res/fonts/aller/aller.png");
//		TextureComponent testComponent = new TextureComponent(Game.getFont("menu").getTextureAtlas(), 1024, 1024, true);
		TextureComponent testComponent = fontRaw.convertToOpenGLTexture();

		TextureElement testElement = new TextureElement(Game.getTexture("default"));
		testElement.setPosition(100, 100, Layers.DEFAULT_LAYER);
		testElement.setSize(200, 200);
		testElement.setCentered(false);

		// Run the game loop!
		while (!(glfwWindowShouldClose(primaryWindowID) || Game.closeRequested())) {

			// poll input
			KeyInput.update();
			MouseInput.update();

			// time
			TimeScale.update();

			// run game logic
			Game.update();
			GUIManager.update();

			// clear window
			MasterRenderer.primaryWindowRenderer.prepare(true);

			// render game internally
			Game.render();
			GUIManager.render();

//			Render.drawRect(Colors.RED, Window.mainCenterX(), Window.mainCenterY(), Layers.DEFAULT_LAYER, 200, 200, true);
			testElement.renderTransformed();

			// Debug!
			float fps = 1.0f / DisplayManager.getFrameTimeRaw();
			fpsDisplay.replaceText("FPS: " + fps +
					"\nGeom: " + GeometryRenderer.ELEMENT_COUNT +
					"\nTxtr: " + TextureRenderer.ELEMENT_COUNT +
					"\nText: " + FontRenderer.ELEMENT_COUNT +
					"\nPart: " + MasterRenderer.getParticleCount());

			// handle top-level universal inputs
			handleInput();

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render(true);
			MasterRenderer.getTargetWindow().swapBuffers();

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
//			sync.sync(Display.FPS_CAP);
// 			decide if this is worth it; for me, it's smoother without (but rare hitching occurs for like 0.1s)
		}

		// Clean up when done.
		Loader.cleanUp();
		MasterRenderer.cleanUp();
		TextMaster.cleanUp();
		AudioMaster.cleanUp();
		TextureOutliner.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(primaryWindowID);
		glfwDestroyWindow(primaryWindowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

		// save user settings/preferences and game flags
		Settings.save();

	}

	private static void handleInput() {
		// F3 to show/hide FPS and/or debug info
		if (KeyInput.isPressed(KeyInput.F3)) {
			Settings.toggleBooleanSetting("show_fps");
			if (!Settings.getSettingBoolean("show_fps")) fpsDisplay.hide();
			else fpsDisplay.show();
		}
		// Toggle debug mode: Ctrl + Shift + D
		if (KeyInput.isShift() && KeyInput.isCtrl() && KeyInput.isPressed(KeyInput.D)) {
			Settings.toggleBooleanSetting("debug_mode");
		}
		// Screenshots
		if (KeyInput.isPressed(KeyInput.F2)) {
			String dir = System.getProperty("user.dir");
			String path = dir + "/screenshots/screenshot-" +
					ZonedDateTime.now().toLocalTime().toString().substring(0,8).replace(":", ".") + ".png";
			Screenshot.takeScreenshot(path);
		}
		// TEST: Toggling post-processing effects
		if (KeyInput.isShift()) {
			if (KeyInput.isPressed(KeyInput.C)) { // C for contrast
				MasterRenderer.primaryWindowRenderer.getPostProcessor().toggleStageEnabled("contrast");
			}
			if (KeyInput.isPressed(KeyInput.I)) { // C for invert
				MasterRenderer.primaryWindowRenderer.getPostProcessor().toggleStageEnabled("invertColor");
			}
		}
	}

}