package com.floober.engine.game;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.gui.GUIManager;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.Screenshot;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.renderEngine.fonts.fontRendering.FontRenderer;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.ppfx.PostProcessing;
import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.renderEngine.renderers.LoadRenderer;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.renderEngine.renderers.TextureRenderer;
import com.floober.engine.renderEngine.textures.TextureOutliner;
import com.floober.engine.util.Logger;
import com.floober.engine.util.color.Colors;
import com.floober.engine.util.configuration.Config;
import com.floober.engine.util.configuration.Settings;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.time.TimeScale;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;

import java.time.ZonedDateTime;
import java.util.Objects;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Run the game! The {@code main()} function here contains
 * all initial game setup and the game loop, as well as clean-up
 * code after the game loop.
 */
public class RunGame {

	public static LoadRenderer loadRenderer = new LoadRenderer();

	private static GUIText fpsDisplay;

	public static void main(String[] args) {

		// Load user preferences/settings and game flags
		Settings.load();
		GameFlags.init();

		// Set up logging.
		Logger.setLoggerConfig();

		// TESTING GAME
		Config.FULLSCREEN = false;
		// FOR EFFICIENCY IN RUNNING MULTIPLE TIMES TO CHECK THINGS

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		// Set up OpenAL.
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Set up post-processing
		PostProcessing.init();

		// game components
//		Sync sync = new Sync(); // this is optional; it's meant to sync framerates to a constant speed but is rather buggy

		// master components
		TextMaster.init();
		ParticleMaster.init();

		// Update the display once to get the timings set
		DisplayManager.updateDisplay();

		// load the game assets
		Game.init();

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

		// Run the game loop!
		while (!(glfwWindowShouldClose(windowID) || Game.closeRequested())) {

			// poll input
			KeyInput.update();
			MouseInput.update();

			// time
			TimeScale.update();

			// run game logic
			Game.update();
			ParticleMaster.update();
			GUIManager.update();

			// clear window
			MasterRenderer.prepare();

			// render game internally
			Game.render();
			GUIManager.render();

			// Debug!
			float fps = 1.0f / DisplayManager.getFrameTimeRaw();
			fpsDisplay.replaceText("FPS: " + fps +
					"\nGeom: " + GeometryRenderer.ELEMENT_COUNT +
					"\nTxtr: " + TextureRenderer.ELEMENT_COUNT +
					"\nText: " + FontRenderer.ELEMENT_COUNT +
					"\nPart: " + ParticleMaster.numParticles);

			// handle top-level universal inputs
			handleInput();

			// render to the screen
			MasterRenderer.render();

			// do post processing
			PostProcessing.doPostProcessing(MasterRenderer.getSceneBuffer().getColorTexture());

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
//			sync.sync(Display.FPS_CAP);
// 			TODO decide if this is worth it; for me, it's smoother without (but rare hitching occurs for like 0.1s)
		}

		// Clean up when done.
		Loader.cleanUp();
		MasterRenderer.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();
		AudioMaster.cleanUp();
		TextureOutliner.cleanUp();
		PostProcessing.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

		// save user settings/preferences and game flags
		Settings.save();
		GameFlags.save();

	}

	private static void handleInput() {
		// F3 to show/hide FPS and/or debug info
		if (KeyInput.isPressed(KeyInput.F3)) {
			Settings.showFps = !Settings.showFps;
			if (!Settings.showFps) fpsDisplay.hide();
			else fpsDisplay.show();
		}
		// Toggle debug mode: Ctrl + Shift + D
		if (KeyInput.isShift() && KeyInput.isCtrl() && KeyInput.isPressed(KeyInput.D)) {
			Settings.debugMode = !Settings.debugMode;
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
				PostProcessing.setStageEnabled("contrast", !PostProcessing.isStageEnabled("contrast"));
			}
			if (KeyInput.isPressed(KeyInput.I)) { // C for invert
				PostProcessing.setStageEnabled("invertColor", !PostProcessing.isStageEnabled("invertColor"));
			}
		}
	}

}