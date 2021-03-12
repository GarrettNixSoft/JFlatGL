package com.floober.engine.game.tests;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.particles.systems.FireScreenParticleSystem;
import com.floober.engine.renderEngine.ppfx.PostProcessing;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.util.Logger;
import com.floober.engine.util.color.Colors;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.time.Sync;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;

public class FireScreenTest {

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Initialize the game.
		Game.init();
		// game components
		Sync sync = new Sync();
		// master components
		TextMaster.init();
		ParticleMaster.init();
		PostProcessing.init();

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
		while (!glfwWindowShouldClose(windowID)) {
			// clear window
			MasterRenderer.prepare();

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
			MasterRenderer.render();

			// Post processing
			PostProcessing.doPostProcessing(MasterRenderer.getSceneBuffer().getColorTexture());

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
			sync.sync(Display.FPS_CAP);
		}

		// Clean up when done.

		// game.cleanUp();
		Loader.cleanUp();
		MasterRenderer.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}