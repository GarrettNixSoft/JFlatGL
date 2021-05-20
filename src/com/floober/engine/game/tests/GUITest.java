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
import com.floober.engine.gui.component.TabbedPanel;
import com.floober.engine.gui.event.*;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.renderEngine.fonts.fontRendering.FontRenderer;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.ppfx.PostProcessing;
import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.renderEngine.renderers.TextureRenderer;
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

public class GUITest {

	private static GUIText fpsDisplay;

	public static void main(String[] args) throws GUIException {

		// Load user preferences/settings and game flags
		Settings.load();
		GameFlags.init();

		// Set up logging.
		Logger.setLoggerConfig();

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		// Audio master has to be initialized before game loads
		AudioMaster.init();

		// Initialize the game. (Also loads assets)
		Game.init();

		// game components
		TextMaster.init();
		ParticleMaster.init();
		PostProcessing.init();
//		Sync sync = new Sync();

		// SET UP DEBUG TEXT
		fpsDisplay = new GUIText("FPS: ", 0.5f, Game.getFont("menu"),
				new Vector3f(0, 0, 1), 1, false);
		fpsDisplay.setColor(Colors.GREEN);
		fpsDisplay.setWidth(0.5f);
		fpsDisplay.setEdge(0.2f);
		fpsDisplay.show();

		// TEST GUI CODE

		GUI gui = new GUI("test_gui");
		GUILayer layer = new GUILayer("test_layer");

		TabbedPanel tabbedPanel = new TabbedPanel("tabbed_panel_test");
		tabbedPanel.listPosition(TabbedPanel.ListPosition.TOP).borderPadding(10)
				.buttonSize(new Vector2f(120, 120)).buttonSpacing(50)
				.closeTime(0.3f)
				.location(new Vector3f(Display.center(), MasterRenderer.TOP_LAYER))
				.primaryColor(Colors.WHITE).secondaryColor(Colors.RED)
				.onClose(new GUIAction()
						.addPerformActionOnTrigger(() -> tabbedPanel.queueEvent(new FadeComponentEvent(tabbedPanel, 0, 0.2f)))
						.addPerformActionOnTrigger(() -> tabbedPanel.queueEvent(new ScaleEvent(tabbedPanel, -0.2f, 0.2f))));

		layer.addPanel(tabbedPanel);
		gui.addLayer(layer);

		// create main test tab
		TabbedPanel.TabContentPanel tab = tabbedPanel.generateTab("tab_1", Game.getTexture("default"));

		// create second test tab
		TabbedPanel.TabContentPanel tab2 = tabbedPanel.generateTab("tab_2", Game.getTexture("default2"));

		// finalize the panel's layout
		tabbedPanel.finalizeLayout();

		// create the button
		Button button = new Button("quit_button");
		// set the button's parameters
		button.label("Quit").rounded(0.15f).textSize(1.2f)
				.location(new Vector3f(Display.centerX(), Display.centerY() + 120, MasterRenderer.TOP_LAYER))
				.size(new Vector2f(250, 100)).primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(new GUIAction()
						.addPerformActionOnTrigger(() -> button.queueEvent(new RestoreOpacityEvent(button, 0.05f)))
						.addPerformActionOnTrigger(() -> button.queueEvent(new RestoreScaleEvent(button, 0.05f))))
				.onClose(new GUIAction()
						.addPerformActionOnTrigger(() -> button.queueEvent(new FadeComponentEvent(button, 0, 0.2f)))
						.addPerformActionOnTrigger(() -> button.queueEvent(new ScaleEvent(button, -0.2f, 0.2f))))
				.onMouseOver(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("hover2")) // play the hover sound
//						.addPerformActionOnTrigger(() -> button.queueEvent(new ScaleEvent(button, 0.1f, 0.05f))) // grow by 10% over 50ms
						.addPerformActionOnTrigger(() -> button.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1)))
						.addPerformActionOnTrigger(() -> button.queueEvent(new OffsetPositionEvent(button, new Vector2f(30, 0), 0.05f))))
				.onMouseExit(new GUIAction()
//						.addPerformActionOnTrigger(() -> button.queueEvent(new RestoreScaleEvent(button, 0.05f))) // return to normal size
						.addPerformActionOnTrigger(() -> button.setPrimaryColor(Colors.WHITE))
						.addPerformActionOnTrigger(() -> button.queueEvent(new RestoreOffsetEvent(button, 0.05f))))
				.onLeftClick(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("select"))
						.addPerformActionOnTrigger(GUIManager::closeGUI)
						.addPerformActionOnTrigger(tabbedPanel::resetCloseAction)
						.addPerformActionOnTrigger(() -> button.queueEvent(new BlockingDelayEvent(0.3f)))
						.addPerformActionOnTrigger(() -> button.queueEvent(new PerformActionEvent(
								new GUIAction().addPerformActionOnTrigger(Game::quit)))));

		// add the button to the GUI panel
		tab.addComponent(button);

		Button otherButton = new Button("click_button");
		otherButton.label("Click").rounded(0.15f).textSize(1.2f)
				.location(Display.center(), MasterRenderer.TOP_LAYER).size(new Vector2f(250, 100))
				.primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(new GUIAction()
						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new RestoreOpacityEvent(otherButton, 0.05f)))
						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new RestoreScaleEvent(otherButton, 0.05f))))
				.onClose(new GUIAction()
								.addPerformActionOnTrigger(() -> otherButton.queueEvent(new FadeComponentEvent(otherButton, 0, 0.2f)))
								.addPerformActionOnTrigger(() -> otherButton.queueEvent(new ScaleEvent(otherButton, -0.2f, 0.2f))))
				.onMouseOver(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("hover2")) // play the hover sound
//						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new ScaleEvent(otherButton, 0.1f, 0.05f))) // grow by 10% over 50ms
						.addPerformActionOnTrigger(() -> otherButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1)))
						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new OffsetPositionEvent(otherButton, new Vector2f(30, 0), 0.05f))))
				.onMouseExit(new GUIAction()
//						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new RestoreScaleEvent(otherButton, 0.05f))) // return to normal size
						.addPerformActionOnTrigger(() -> otherButton.setPrimaryColor(Colors.WHITE))
						.addPerformActionOnTrigger(() -> otherButton.queueEvent(new RestoreOffsetEvent(otherButton, 0.05f))))
				.onLeftClick(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("select")));

		tab.addComponent(otherButton);

		// create a sample button for the second test tab
		Button testButton = new Button("test_button");
		testButton.label("Test").rounded(0.15f).textSize(1.2f)
				.location(Display.center().add(300 - 300, 0), MasterRenderer.TOP_LAYER).size(new Vector2f(250, 100))
				.primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(new GUIAction()
						.addPerformActionOnTrigger(() -> testButton.queueEvent(new RestoreScaleEvent(testButton, 0.05f)))
						.addPerformActionOnTrigger(() -> testButton.queueEvent(new RestoreOpacityEvent(testButton, 0.05f))))
				.onClose(new GUIAction()
						.addPerformActionOnTrigger(() -> testButton.queueEvent(new FadeComponentEvent(testButton, 0, 0.2f)))
						.addPerformActionOnTrigger(() -> testButton.queueEvent(new ScaleEvent(testButton, -0.2f, 0.2f))))
				.onMouseOver(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("hover2")) // play the hover sound
						.addPerformActionOnTrigger(() -> testButton.queueEvent(new ScaleEvent(testButton, 0.1f, 0.05f))) // grow by 10% over 50ms
						.addPerformActionOnTrigger(() -> testButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1))))
				.onMouseExit(new GUIAction()
						.addPerformActionOnTrigger(() -> testButton.queueEvent(new RestoreScaleEvent(testButton, 0.05f))) // return to normal size
						.addPerformActionOnTrigger(() -> testButton.setPrimaryColor(Colors.WHITE)))
				.onLeftClick(new GUIAction()
						.addPerformActionOnTrigger(() -> Game.playSfx("select")));

		tab2.addComponent(testButton);

		// tab buttons
		for (TabbedPanel.TabButton tabButton : tabbedPanel.getTabButtons()) {
			tabButton.onMouseOver(new GUIAction()
					.addPerformActionOnTrigger(() -> Game.playSfx("hover2")) // play the hover sound
							.addPerformActionOnTrigger(() -> tabButton.queueEvent(new ScaleEvent(tabButton, 0.1f, 0.05f))) // grow by 10% over 50ms
							.addPerformActionOnTrigger(() -> tabButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1))))
					.onClose(new GUIAction()
							.addPerformActionOnTrigger(() -> tabButton.queueEvent(new FadeComponentEvent(tabButton, 0, 0.2f)))
							.addPerformActionOnTrigger(() -> tabButton.queueEvent(new ScaleEvent(tabButton, -0.2f, 0.2f))))
					.onMouseExit(new GUIAction()
									.addPerformActionOnTrigger(() -> tabButton.queueEvent(new RestoreScaleEvent(tabButton, 0.05f))) // return to normal size
									.addPerformActionOnTrigger(() -> tabButton.setPrimaryColor(Colors.WHITE)));
		}


		// activate the GUI
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

			checkInput();

			// render game internally
			Game.render();
			GUIManager.render();

			// Debug!
			float fps = 1.0f / DisplayManager.getFrameTimeRaw();
			fpsDisplay.replaceText("FPS: " + fps +
					"\nGeom: " + GeometryRenderer.ELEMENT_COUNT +
					"\nTxtr: " + TextureRenderer.ELEMENT_COUNT +
					"\nText: " + FontRenderer.ELEMENT_COUNT);

			// render to the screen
			MasterRenderer.render();

			// Post processing
			PostProcessing.doPostProcessing(MasterRenderer.getSceneBuffer().getColorTexture());

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
//			if (Settings.capFramerate) sync.sync(Settings.fpsOptions[Settings.maxFramerateIndex]);

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

	private static void checkInput() {
		// TEST: Toggling post-processing effects
		if (KeyInput.isShift()) {
			if (KeyInput.isPressed(KeyInput.C)) { // C for contrast
				PostProcessing.setStageEnabled("contrast", !PostProcessing.isStageEnabled("contrast"));
			}
			if (KeyInput.isPressed(KeyInput.I)) { // I for invert
				PostProcessing.setStageEnabled("invertColor", !PostProcessing.isStageEnabled("invertColor"));
			}
			if (KeyInput.isPressed(KeyInput.B)) { // B for gaussian blur
				PostProcessing.setStageEnabled("gaussianBlur", !PostProcessing.isStageEnabled("gaussianBlur"));
			}
			if (KeyInput.isPressed(KeyInput.G)) { // G for grayscale
				PostProcessing.toggleStage("grayscale");
//				PostProcessing.setStageEnabled("grayscale", !PostProcessing.isStageEnabled("grayscale"));
			}
		}
	}

}