package com.floober.engine.test.gameState;

import com.floober.engine.core.Game;
import com.floober.engine.core.gameState.GameState;
import com.floober.engine.core.gameState.GameStateManager;
import com.floober.engine.core.renderEngine.Screenshot;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.core.util.input.GamepadInput;
import com.floober.engine.core.util.input.KeyInput;

import java.time.ZonedDateTime;

import static org.lwjgl.glfw.GLFW.*;

public class TestState extends GameState {

	private final RectElement rect;

	private float x;
	private float y;
	private float r;

	public TestState(GameStateManager gsm) {
		super(gsm);
		x = Game.centerX();
		y = Game.centerY();
		rect = new RectElement(Colors.RED, x, y, Layers.DEFAULT_LAYER, 100, 100, true);
	}

	@Override
	public void init() {
		// TODO
	}

	@Override
	public void update() {

		// move/rotate based on gamepad input
		float delta = 250 * Game.getFrameTime();
		float rotate = 90 * Game.getFrameTime();

		x += GamepadInput.getAxis(0, GLFW_GAMEPAD_AXIS_LEFT_X) * delta;
		y += GamepadInput.getAxis(0, GLFW_GAMEPAD_AXIS_LEFT_Y) * delta;
		r -= GamepadInput.getAxis(0, GLFW_GAMEPAD_AXIS_RIGHT_X) * rotate;

		rect.setPosition(x, y, Layers.DEFAULT_LAYER);
		rect.setRotation(r);
		rect.transform();

	}

	@Override
	public void render() {
		rect.render();
	}

	@Override
	public void handleInput() {
		// Toggle debug mode: Ctrl + Shift + D
		if (KeyInput.isShift() && KeyInput.isCtrl() && KeyInput.isPressed(KeyInput.D)) {
			Settings.toggleBooleanSetting("debug_mode");
		}
		// Screenshots
		if (KeyInput.isPressed(KeyInput.F2)) {
			String dir = System.getProperty("user.dir");
			String path = dir + "/screenshots/screenshot-" +
					ZonedDateTime.now().toLocalTime().toString().substring(0,8).replace(":", ".") + ".png";
			Screenshot.takeScreenshot(path);
		}
		// TEST: Toggling post-processing effects
		if (KeyInput.isShift()) {
			if (KeyInput.isPressed(KeyInput.C)) { // C for contrast
				MasterRenderer.primaryWindowRenderer.getPostProcessor().toggleStageEnabled("contrast");
			}
			if (KeyInput.isPressed(KeyInput.I)) { // C for invert
				MasterRenderer.primaryWindowRenderer.getPostProcessor().toggleStageEnabled("invertColor");
			}
		}

		// TEST: Gamepad buttons to change colors
		if (GamepadInput.isPressed(0, GLFW_GAMEPAD_BUTTON_A)) {
			rect.setColor(Colors.GREEN);
		}
		if (GamepadInput.isPressed(0, GLFW_GAMEPAD_BUTTON_B)) {
			rect.setColor(Colors.RED);
		}
		if (GamepadInput.isPressed(0, GLFW_GAMEPAD_BUTTON_X)) {
			rect.setColor(Colors.BLUE);
		}
		if (GamepadInput.isPressed(0, GLFW_GAMEPAD_BUTTON_Y)) {
			rect.setColor(Colors.YELLOW);
		}
	}

	@Override
	public void cleanUp() {
		// TODO
	}
}