package com.floober.engine.test.tests;

import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.systems.FireScreenParticleSystem;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.time.Sync;
import com.floober.engine.test.gameState.TestGameStateManager;
import com.floober.engine.test.splash.TestSplashRenderer;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

public class FireScreenTest {

	public static void main(String[] args) {

		Config.load();
		Settings.load();

		TestSplashRenderer splashRenderer = new TestSplashRenderer();

		// Initialize the game.
		Game.init(splashRenderer, new TestGameStateManager(1));
		// game components
		Sync sync = new Sync();

		GUIText fpsDisplay = new GUIText("FPS: ", 0.5f, Game.getFont("menu"),
				new Vector3f(0, 0, 1), 1, false);
		fpsDisplay.setColor(Colors.GREEN);
		fpsDisplay.setWidth(0.5f);
		fpsDisplay.setEdge(0.2f);
		fpsDisplay.show();

		// TEST

		FireScreenParticleSystem fireSystem = new FireScreenParticleSystem();

		// END_TEST

		// Run the game loop!
		while (!glfwWindowShouldClose(primaryWindowID)) {
			// clear window
			MasterRenderer.primaryWindowRenderer.prepare(true);

			// poll input
			KeyInput.update();
			MouseInput.update();

			DisplayManager.checkToggleFullscreen();

			// run game logic
			Game.update();

			fireSystem.update();

			// render game internally
			Game.render();

			fireSystem.render();

			float fps = 1.0f / DisplayManager.getFrameTimeRaw();
			fpsDisplay.replaceText("FPS: " + fps + "\nParticles: " + MasterRenderer.getParticleCount());

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render(true);

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
			sync.sync(DisplayManager.FPS_CAP);
		}

		// Clean up when done.

		// game.cleanUp();
		Loader.cleanUp();
		MasterRenderer.cleanUp();
		TextMaster.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(primaryWindowID);
		glfwDestroyWindow(primaryWindowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}