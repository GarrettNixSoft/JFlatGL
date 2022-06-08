package com.floober.engine.test.tests;

import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.math.RandomUtil;
import com.floober.engine.core.util.time.Sync;

import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glGetIntegerv;

public class MultiWindowTest {

	public static void main(String[] args) {

		Config.load();

		Config.FULLSCREEN = false;

		// Initialize the game.
		Game.init();

		// Run the game loop!
		while (!glfwWindowShouldClose(primaryWindowID)) {

			// clear window and make window context current
			MasterRenderer.primaryWindowRenderer.prepare(true);

			// poll input
			KeyInput.update();
			MouseInput.update();

			if (KeyInput.isPressed(KeyInput.T)) Logger.log("Key press detected on primary window");
			if (MouseInput.leftClick()) Logger.log("Left click detected on primary window");

			// run game logic
			Game.update();

			// TEST: Create a new window
			if (KeyInput.isPressed(KeyInput.W)) {
				int windowWidth = RandomUtil.getInt(100, 400);
				int windowHeight = RandomUtil.getInt(100, 400);
				int windowX = RandomUtil.getInt(100, 2060);
				int windowY = RandomUtil.getInt(100, 940);
				Window newWindow = DisplayManager.createNewWindow(windowWidth, windowHeight, windowX, windowY);
				Logger.log("Successfully created a new window with title \"" + newWindow.getWindowTitle() + "\"!");
			}

			// render game internally
			Game.render();

			// Test manually rendering
			Render.drawRect(Colors.RED, 1920/2f, 1080/2f, 5, 300, 300, true);
			Render.drawRect(Colors.GREEN, MouseInput.getX(), MouseInput.getY(), 5, 80, 80, true);

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render(true);

			// Render to all other windows
			for (Window window : DisplayManager.getWindows()) {
				if (window.getWindowID() != primaryWindowID) {

					MasterRenderer windowRenderer = window.getWindowRenderer();

					windowRenderer.prepare(true);

					// re-compute mouse position for this window
					MouseInput.update();
					KeyInput.update();

					if (KeyInput.isPressed(KeyInput.T)) Logger.log("Key press detected on window #" + window.getWindowID());
					if (MouseInput.leftClick()) Logger.log("Left click detected on window #" + window.getWindowID());

//					glClearColor(1, 0, 0, 1);
//					glClear(GL_COLOR_BUFFER_BIT);

					// Test manually rendering
					Render.fillScreen(Colors.RED);
					Render.drawRect(Colors.GREEN, 0, 0, 0, 80, 80, true);
					Render.drawRect(Colors.GREEN, 100, 100, 0, 80, 80, true);
					Render.drawRect(Colors.GREEN, MouseInput.getX(), MouseInput.getY(), 0, 80, 80, true);

					if (KeyInput.isPressed(KeyInput.P)) {
						int[] viewport = new int[4];
						glGetIntegerv(GL_VIEWPORT, viewport);
						Logger.log("Viewport size = " + viewport[2] + "x" + viewport[3]);
					}

					// render to the screen
					windowRenderer.render(true);
					// Post-processing is now handled automatically in the render call, no need to call it!
				}
			}

			// update displays
			DisplayManager.updateDisplay();

			// sync time
//			sync.sync(DisplayManager.FPS_CAP);
		}

		// Clean up when done.

		// game.cleanUp();
		Loader.cleanUp();
		MasterRenderer.cleanUp();
		TextMaster.cleanUp();

		// Clean up GLFW
		DisplayManager.cleanUp();


		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}
