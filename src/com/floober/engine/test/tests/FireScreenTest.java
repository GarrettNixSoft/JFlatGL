package com.floober.engine.test.tests;

import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.particles.systems.FireScreenParticleSystem;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.time.Sync;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

public class FireScreenTest {

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		// Create the window and set up OpenGL and GLFW.
		DisplayManager.initPrimaryGameWindow();

		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Initialize the game.
		Game.init();
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
			MasterRenderer.primaryWindowRenderer.prepare();

			// poll input
			KeyInput.update();
			MouseInput.update();

			DisplayManager.checkToggleFullscreen();

			// run game logic
			Game.update();

			fireSystem.update();

			ParticleMaster.update();

			// render game internally
			Game.render();

			fireSystem.render();

			float fps = 1.0f / DisplayManager.getFrameTimeRaw();
			fpsDisplay.replaceText("FPS: " + fps + "\nParticles: " + ParticleMaster.getParticleCount());

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render();

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
		ParticleMaster.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(primaryWindowID);
		glfwDestroyWindow(primaryWindowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}