package com.floober.engine.util.tests;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.fonts.fontRendering.TextMaster;
import com.floober.engine.main.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.particles.ParticleMaster;
import com.floober.engine.renderEngine.MasterRenderer;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.textures.Texture;
import com.floober.engine.util.Colors;
import com.floober.engine.util.time.Sync;
import com.floober.engine.util.Logger;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import org.lwjgl.glfw.Callbacks;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;

public class RenderingTest {

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Initialize the game.
		Game game = new Game();
		Loader loader = new Loader();
		game.init(loader);
		// game components
		Sync sync = new Sync();
		MasterRenderer masterRenderer = new MasterRenderer();
		Render.renderer = masterRenderer;
		// master components
		TextMaster.init();
		ParticleMaster.init();

		// TEST

		Texture texture = game.getTexture("default");
		Texture texture2 = game.getTexture("default2");
		TextureElement element1 = new TextureElement(texture, 0, 0, 15, false);
		TextureElement element2 = new TextureElement(texture, 32, 0, 0, false);
		TextureElement element3 = new TextureElement(texture, 0, 32, 0, false);
		TextureElement element4 = new TextureElement(texture, 32, 32, 0, false);
		TextureElement testStackElement = new TextureElement(texture2, 0, 0, 10, false);

		// END_TEST

		// Run the game loop!
		while (!glfwWindowShouldClose(windowID)) {
			// clear window
			masterRenderer.prepare();

			// poll input
			KeyInput.update();
			MouseInput.update();

			DisplayManager.checkToggleFullscreen();

			// run game logic
			game.update();
			ParticleMaster.update();

			// render game internally
			game.render();
			masterRenderer.addTextureElement(element1);
			masterRenderer.addTextureElement(element2);
			masterRenderer.addTextureElement(element3);
			masterRenderer.addTextureElement(element4);
			masterRenderer.addTextureElement(testStackElement);

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