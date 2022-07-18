package com.floober.engine.test.tests;

import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.Game;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.core.util.conversion.StringConverter;
import com.floober.engine.gui.GUI;
import com.floober.engine.gui.GUIManager;
import com.floober.engine.gui.component.Button;
import com.floober.engine.gui.component.GUILayer;
import com.floober.engine.gui.component.tab.TabButton;
import com.floober.engine.gui.component.tab.TabContentPanel;
import com.floober.engine.gui.component.tab.TabbedPanel;
import com.floober.engine.gui.event.*;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.fonts.fontRendering.FontRenderer;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.renderers.TextureRenderer;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.exception.GUIException;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.test.gameState.TestGameStateManager;
import com.floober.engine.test.splash.TestSplashRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

public class GUITest {

	public static void main(String[] args) throws GUIException {

		Game.loadConfig();

		Settings.setSettingValue("fullscreen", false);

		TestSplashRenderer splashRenderer = new TestSplashRenderer();

		// Initialize the game.
		Game.init(splashRenderer, new TestGameStateManager(1));

		// SET UP DEBUG TEXT
		GUIText fpsDisplay = new GUIText("FPS: ", 0.5f, Game.getFont("menu"),
				new Vector3f(0, 0, 1), 1, false);
		fpsDisplay.setColor(Colors.GREEN);
		fpsDisplay.setWidth(0.5f);
		fpsDisplay.setEdge(0.2f);
		fpsDisplay.show();

		// TEST GUI CODE
		Window gameWindow = DisplayManager.getPrimaryGameWindow();

		GUI gui = new GUI("test_gui");
		GUILayer layer = new GUILayer("test_layer", gui);

		TabbedPanel tabbedPanel = new TabbedPanel("tabbed_panel_test", gui);
		tabbedPanel.listPosition(TabbedPanel.ListPosition.TOP).borderPadding(10)
				.buttonSize(new Vector2f(120, 120)).buttonSpacing(50)
				.closeTime(0.3f)
				.location(new Vector3f(gameWindow.center(), Layers.TOP_LAYER))
				.primaryColor(Colors.WHITE).secondaryColor(Colors.RED)
				.onClose(() -> {
					tabbedPanel.queueEvent(new FadeComponentEvent(tabbedPanel, 0, 0.2f));
					tabbedPanel.queueEvent(new ScaleEvent(tabbedPanel, -0.2f, 0.2f));
				});

		layer.addComponent(tabbedPanel);
		gui.storeLayer(layer);

		// create main test tab
		TabContentPanel tab = tabbedPanel.generateTab("tab_1", Game.getTexture("default"));

		// create second test tab
		TabContentPanel tab2 = tabbedPanel.generateTab("tab_2", Game.getTexture("default2"));

		// finalize the panel's layout
		tabbedPanel.finalizeLayout();

		// create the button
		Button quitButton = new Button("quit_button", gui);
		// set the button's parameters
		quitButton.label("Quit").rounded(0.15f).textSize(1.2f)
				.location(new Vector3f(gameWindow.centerX(), gameWindow.centerY() + 120, Layers.TOP_LAYER))
				.size(new Vector2f(250, 100)).primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(() -> {
					quitButton.queueEvent(new RestoreOpacityEvent(quitButton, 0.05f));
					quitButton.queueEvent(new RestoreScaleEvent(quitButton, 0.05f));
				})
				.onClose(() -> {
					quitButton.queueEvent(new FadeComponentEvent(quitButton, 0, 0.2f));
					quitButton.queueEvent(new ScaleEvent(quitButton, -0.2f, 0.2f));
				})
				.onMouseOver(() -> {
					Game.playSfx("hover2");
//					button.queueEvent(new ScaleEvent(button, 0.1f, 0.05f)); // grow by 10% over 50ms
					quitButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1));
					quitButton.queueEvent(new OffsetPositionEvent(quitButton, new Vector2f(30, 0), 0.05f));
				})
				.onMouseExit(() -> {
					quitButton.queueEvent(new RestoreScaleEvent(quitButton, 0.05f));// return to normal size
					quitButton.setPrimaryColor(Colors.WHITE);
					quitButton.queueEvent(new RestoreOffsetEvent(quitButton, 0.05f));
				})
				.onLeftClick(() -> {
					Game.playSfx("select");
					tabbedPanel.resetCloseAction();
					quitButton.queueEvent(new BlockingDelayEvent(0.3f));
					quitButton.queueEvent(new PerformActionEvent(Game::quit));
					GUIManager.closeGUI();
				});

		// add the button to the GUI panel
		tab.addComponent(quitButton);

		Button otherButton = new Button("click_button", gui);
		otherButton.label("Click").rounded(0.15f).textSize(1.2f)
				.location(gameWindow.center(), Layers.TOP_LAYER).size(new Vector2f(250, 100))
				.primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(() -> {
						otherButton.queueEvent(new RestoreOpacityEvent(otherButton, 0.05f));
						otherButton.queueEvent(new RestoreScaleEvent(otherButton, 0.05f));
				})
				.onClose(() -> {
								otherButton.queueEvent(new FadeComponentEvent(otherButton, 0, 0.2f));
								otherButton.queueEvent(new ScaleEvent(otherButton, -0.2f, 0.2f));
				})
				.onMouseOver(() -> {
						Game.playSfx("hover2");// play the hover sound
//						otherButton.queueEvent(new ScaleEvent(otherButton, 0.1f, 0.05f))) // grow by 10% over 50ms
						otherButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1));
						otherButton.queueEvent(new OffsetPositionEvent(otherButton, new Vector2f(30, 0), 0.05f));
				})
				.onMouseExit(() -> {
//						otherButton.queueEvent(new RestoreScaleEvent(otherButton, 0.05f))) // return to normal size
						otherButton.setPrimaryColor(Colors.WHITE);
						otherButton.queueEvent(new RestoreOffsetEvent(otherButton, 0.05f));
				})
				.onLeftClick(() -> Game.playSfx("select"));

		tab.addComponent(otherButton);

		// create a sample button for the second test tab
		Button testButton = new Button("test_button", gui);
		testButton.label("Test").rounded(0.15f).textSize(1.2f)
				.location(gameWindow.center().add(300, 0), Layers.TOP_LAYER).size(new Vector2f(250, 100))
				.primaryColor(Colors.WHITE).secondaryColor(Colors.BLACK)
				.onOpen(() -> {
						testButton.queueEvent(new RestoreScaleEvent(testButton, 0.05f));
						testButton.queueEvent(new RestoreOpacityEvent(testButton, 0.05f));
				})
				.onClose(() -> {
						testButton.queueEvent(new FadeComponentEvent(testButton, 0, 0.2f));
						testButton.queueEvent(new ScaleEvent(testButton, -0.2f, 0.2f));
				})
				.onMouseOver(() -> {
						Game.playSfx("hover2"); // play the hover sound
						testButton.queueEvent(new ScaleEvent(testButton, 0.1f, 0.05f)); // grow by 10% over 50ms
						testButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1));
				})
				.onMouseExit(() -> {
						testButton.queueEvent(new RestoreScaleEvent(testButton, 0.05f)); // return to normal size
						testButton.setPrimaryColor(Colors.WHITE);
				})
				.onLeftClick(() -> Game.playSfx("select"));

		tab2.addComponent(testButton);

		// tab buttons
		for (TabButton tabButton : tabbedPanel.getTabButtons()) {
			tabButton.onMouseOver(() -> {
					Game.playSfx("hover2");// play the hover sound
							tabButton.queueEvent(new ScaleEvent(tabButton, 0.1f, 0.05f)); // grow by 10% over 50ms
							tabButton.setPrimaryColor(new Vector4f(1, 0.9f, 0.7f, 1));
					})
					.onClose(() -> {
							tabButton.queueEvent(new FadeComponentEvent(tabButton, 0, 0.2f));
							tabButton.queueEvent(new ScaleEvent(tabButton, -0.2f, 0.2f));
					})
					.onMouseExit(() -> {
							tabButton.queueEvent(new RestoreScaleEvent(tabButton, 0.05f));// return to normal size
							tabButton.setPrimaryColor(Colors.WHITE);
					});
		}

		gui.stackAndLock(layer);

		// activate the GUI
		GUIManager.setActiveGUI(gui);

		// END_TEST

		while (!(glfwWindowShouldClose(primaryWindowID) || Game.closeRequested())) {
			// clear window
			MasterRenderer.primaryWindowRenderer.prepare(true);

			DisplayManager.checkToggleFullscreen();

			// run game logic
			Game.update();

			checkInput();

			// render game internally
			Game.render();

			// Debug!
			float fps = 1.0f / DisplayManager.getFrameTimeRaw();
			fpsDisplay.replaceText("FPS: " + fps +
					"\nGeom: " + GeometryRenderer.ELEMENT_COUNT +
					"\nTxtr: " + TextureRenderer.ELEMENT_COUNT +
					"\nText: " + FontRenderer.ELEMENT_COUNT +
					"\nmPos: " + StringConverter.vec2fToString(MouseInput.getMousePos()));

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render(true);
			MasterRenderer.getTargetWindow().swapBuffers();

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

		Settings.save();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(primaryWindowID);
		glfwDestroyWindow(primaryWindowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

	private static void checkInput() {
		// TEST: Toggling post-processing effects
		if (KeyInput.isShift()) {
			if (KeyInput.isPressed(KeyInput.C)) { // C for contrast
				MasterRenderer.primaryWindowRenderer.getPostProcessor().toggleStageEnabled("contrast");
			}
			if (KeyInput.isPressed(KeyInput.I)) { // I for invert
				MasterRenderer.primaryWindowRenderer.getPostProcessor().toggleStageEnabled("invertColor");
			}
			if (KeyInput.isPressed(KeyInput.G)) { // G for gaussian blur
				MasterRenderer.primaryWindowRenderer.getPostProcessor().toggleStageEnabled("gaussianBlur");
			}
		}
		// TEST: Mouse being weird?
		if (MouseInput.leftClick()) Logger.log("Left click!");
	}

}