package com.floober.engine.main;

import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.fonts.fontRendering.TextMaster;
import com.floober.engine.loaders.Loader;
import com.floober.engine.models.ModelLoader;
import com.floober.engine.particles.ParticleMaster;
import com.floober.engine.renderEngine.MasterRenderer;
import com.floober.engine.util.time.Sync;
import com.floober.engine.util.Logger;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import org.lwjgl.glfw.Callbacks;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;

public class RunGame {

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		// Initialize the game.
		// game.init();
		// game components
		Loader loader = new Loader();
		Sync sync = new Sync();
		MasterRenderer masterRenderer = new MasterRenderer();
		// master components
		TextMaster.init();
		ParticleMaster.init();

		// TEST
		// ...
		// END_TEST

		// Run the game loop!
		while (!glfwWindowShouldClose(windowID)) {
			// clear window
			masterRenderer.prepare();

			// poll input
			KeyInput.update();
			MouseInput.update();

			// run game logic
			// game.update();
			ParticleMaster.update();

			// render game internally
			// game.render();

			// render to the screen
			masterRenderer.render();
			ParticleMaster.renderParticles();
			TextMaster.render();

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
			sync.sync(Display.FPS_CAP);
		}

		// Clean up when done.

		// game.cleanUp();
		loader.cleanUp();
		masterRenderer.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);

		glfwTerminate();
		glfwSetErrorCallback(null).free();

	}

}