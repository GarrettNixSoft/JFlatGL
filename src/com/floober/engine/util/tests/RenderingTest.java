package com.floober.engine.util.tests;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.util.Logger;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.time.Sync;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

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
		game.init();
		// game components
		Sync sync = new Sync();
		MasterRenderer masterRenderer = new MasterRenderer();
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

		TextureElement testCropElement = new TextureElement(texture, Display.WIDTH / 2f, Display.HEIGHT / 2f, 0, 64, 64, true);
		testCropElement.setTextureOffset(new Vector4f(0.25f, 0.25f, 0.75f, 0.75f));

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
			masterRenderer.addTextureElement(testCropElement);

			// render to the screen
			masterRenderer.render();

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
			sync.sync(Display.FPS_CAP);
		}

		// Clean up when done.
		Loader.cleanUp();
		masterRenderer.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}