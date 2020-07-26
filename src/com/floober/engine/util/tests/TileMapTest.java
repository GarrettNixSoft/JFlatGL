package com.floober.engine.util.tests;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.fonts.fontRendering.TextMaster;
import com.floober.engine.loaders.Loader;
import com.floober.engine.loaders.level.TileMapLoader;
import com.floober.engine.main.Game;
import com.floober.engine.particles.ParticleMaster;
import com.floober.engine.renderEngine.MasterRenderer;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.elements.TileElement;
import com.floober.engine.textures.Texture;
import com.floober.engine.textures.TextureAtlas;
import com.floober.engine.tiles.TileMap;
import com.floober.engine.util.Logger;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.time.Sync;
import org.lwjgl.glfw.Callbacks;

import java.util.ArrayList;
import java.util.Objects;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

public class TileMapTest {

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		// Set up OpenAL.
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Set up the frame limiter.
		Sync sync = new Sync();

		// Initialize the game.
		Game game = new Game();
		Loader loader = new Loader();
		game.init(loader);

		TileMapLoader.game = game;

		// Create the renderer.
		MasterRenderer masterRenderer = new MasterRenderer();
		Render.renderer = masterRenderer;

		// initialize master components
		TextMaster.init();
		ParticleMaster.init();

		// TEST

		TextureAtlas tileset = game.getTextureAtlas("test_tilemap");
		TileMap tileMap = TileMapLoader.loadTileMap("sample.map", new ArrayList<>());

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
			//game.update();

			// render game internally
//			game.render();

			// TEST
			tileMap.render();
			// END_TEST

			// render to the screen
			masterRenderer.render();

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
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}