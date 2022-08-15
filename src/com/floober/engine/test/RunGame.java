package com.floober.engine.test;

import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.configuration.Settings;
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
import com.floober.engine.test.gameState.TestGameStateManager;
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
		Game.loadConfig();

		// TESTING GAME
		Settings.setSettingValue("fullscreen", false);
		Settings.setSettingValue("show_fps", true);
		// FOR EFFICIENCY IN RUNNING MULTIPLE TIMES TO CHECK THINGS

		// game components
//		Sync sync = new Sync(); // this is optional; it's meant to sync framerates to a constant speed but is rather buggy

		// Update the display once to get the timings set
		DisplayManager.updateDisplay();

		// Set up a splash renderer
		TestSplashRenderer splashRenderer = new TestSplashRenderer();

		// load the game assets
		Game.init(splashRenderer, new TestGameStateManager(1));

		// Run the game loop!
		Game.runGameLoop();

		// Clean up when done.
		Game.cleanUp();

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