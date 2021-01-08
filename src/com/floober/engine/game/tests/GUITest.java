package com.floober.engine.game.tests;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.game.Game;
import com.floober.engine.game.GameFlags;
import com.floober.engine.gui.GUI;
import com.floober.engine.gui.GUIAction;
import com.floober.engine.gui.GUIManager;
import com.floober.engine.gui.component.Button;
import com.floober.engine.gui.component.GUILayer;
import com.floober.engine.gui.component.GUIPanel;
import com.floober.engine.gui.event.*;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.ppfx.PostProcessing;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.util.Logger;
import com.floober.engine.util.color.Colors;
import com.floober.engine.util.configuration.Settings;
import com.floober.engine.util.exception.GUIException;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.time.Sync;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

public class GUITest {

	public static void main(String[] args) throws GUIException {

		// Load user preferences/settings and game flags
		Settings.load();
		GameFlags.init();

		// Set up logging.
		Logger.setLoggerConfig();

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		AudioMaster.init();

		// Initialize the game.
		Game.init();
		// game components
		Sync sync = new Sync();
		// master components
		TextMaster.init();
		ParticleMaster.init();
		PostProcessing.init();

		// TEST GUI CODE

		GUI gui = new GUI("test_gui");
		GUIPanel panel = new GUIPanel("test_panel");
		gui.addPanel(panel);

		GUILayer layer = new GUILayer("test_layer");
		panel.addLayer(layer);

		// create the button
		Button button = new Button("test_button");
		// set the button's parameters
		button.label("Quit").rounded(0.15f).textSize(1.2f)
				.location(Display.center(), MasterRenderer.TOP_LAYER).size(new Vector2f(250, 100))
				.primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(new GUIAction()
						.addPerformActionOnTrigger(() -> button.queueEvent(new ReadyEvent(button))))
				.onClose(new GUIAction()
						.addPerformActionOnTrigger(() -> button.queueEvent(new FadeComponentEvent(button, 0, 0.2f)))
						.addPerformActionOnTrigger(() -> button.queueEvent(new BlockingDelayEvent(0.2f)))
						.addPerformActionOnTrigger(() -> button.queueEvent(new PerformActionEvent(
								new GUIAction().addPerformActionOnTrigger(Game::quit)))))
				.onMouseOver(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("hover")) // play the hover sound
						.addPerformActionOnTrigger(() -> button.queueEvent(new GrowEvent(button, 0.1f, 0.05f))) // grow by 10% over 50ms
						.addPerformActionOnTrigger(() -> button.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1))))
				.onMouseExit(new GUIAction()
						.addPerformActionOnTrigger(() -> button.queueEvent(new RestoreScaleEvent(button, 0.05f))) // return to normal size
						.addPerformActionOnTrigger(() -> button.setPrimaryColor(Colors.WHITE)))
				.onLeftClick(new GUIAction()
						.addPerformActionOnTrigger(GUIManager::closeGUI));

		// add the button to the GUI panel
		layer.addComponent(button);

		Button otherButton = new Button("test_button_2");
		otherButton.label("Jump!").rounded(0.15f).textSize(1.2f)
				.location(new Vector3f(Display.centerX(), Display.centerY() + 120, MasterRenderer.TOP_LAYER))
				.size(new Vector2f(250, 100)).primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(new GUIAction()
						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new ReadyEvent(otherButton))))
				.onClose(new GUIAction()
								.addPerformActionOnTrigger(() -> otherButton.queueEvent(new FadeComponentEvent(otherButton, 0, 0.2f))))
				.onMouseOver(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("hover")) // play the hover sound
						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new GrowEvent(otherButton, 0.1f, 0.05f))) // grow by 10% over 50ms
						.addPerformActionOnTrigger(() -> otherButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1))))
				.onMouseExit(new GUIAction()
						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new RestoreScaleEvent(otherButton, 0.05f))) // return to normal size
						.addPerformActionOnTrigger(() -> otherButton.setPrimaryColor(Colors.WHITE)))
				.onLeftClick(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("jump")));

		layer.addComponent(otherButton);


		GUIManager.setActiveGUI(gui);

		// END_TEST

		while (!(glfwWindowShouldClose(windowID) || Game.closeRequested())) {
			// clear window
			MasterRenderer.prepare();

			// poll input
			KeyInput.update();
			MouseInput.update();

			DisplayManager.checkToggleFullscreen();

			// run game logic
			Game.update();
			ParticleMaster.update();
			GUIManager.update();

			// render game internally
			Game.render();
			GUIManager.render();

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

		Settings.save();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}