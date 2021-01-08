package com.floober.engine.util.tests;

import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.loaders.ImageLoader;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.particles.ParticleTexture;
import com.floober.engine.renderEngine.particles.behavior.*;
import com.floober.engine.renderEngine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.renderEngine.particles.behavior.appearance.FadeOutBehavior;
import com.floober.engine.renderEngine.particles.behavior.movement.FlameBehavior;
import com.floober.engine.renderEngine.particles.emitters.ParticleEmitter;
import com.floober.engine.renderEngine.ppfx.PostProcessing;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.util.Logger;
import com.floober.engine.util.color.Colors;
import com.floober.engine.util.configuration.Config;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.time.Sync;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;

public class ParticleTest {

	// generating particles
	static ParticleEmitter particleSource;
	static ParticleEmitter lightParticleSource;
	static ParticleBehavior particleBehavior;
	static boolean useExplosionTexture;
	// particle textures
	static ParticleTexture explosionParticleTex;
	static ParticleTexture glowParticleTex;
	// GUI text
	static FontType guiFont;
	static GUIText particleCountDisplay;
	static GUIText performanceDisplay;
	static GUIText mousePositionDisplay;
	static GUIText colorDisplay;
	static GUIText particleSettingsDisplay;

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		Config.WINDOW_TITLE += " (Particle Test)";

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		// Initialize the game.
		// game.init();
		// game components
		Sync sync = new Sync();
		MasterRenderer masterRenderer = new MasterRenderer();
		// master components
		TextMaster.init();
		ParticleMaster.init();
		PostProcessing.init();

		// TEST
		// Use a GUIText to display the number of particles currently in existence.
		guiFont = Loader.loadFont("aller");
		Vector4f textColor = new Vector4f(1);
		Vector4f shadowColor = new Vector4f(0);
		particleCountDisplay = new GUIText("Particles: 0", 1.5f, guiFont, new Vector3f(0), 1, false);
		particleCountDisplay.setColor(textColor);
		particleCountDisplay.setWidth(0.5f);
		particleCountDisplay.setEdge(0.2f);
		particleCountDisplay.setOutlineColor(shadowColor);
		particleCountDisplay.setBorderWidth(0.7f);
		particleCountDisplay.setBorderEdge(0.08f);
		particleCountDisplay.setShadowOffset(0.007f, 0.007f);
		performanceDisplay = new GUIText("Tick: ... Frame: ...", 1f, guiFont, new Vector3f(0, 0.05f, 0), 0.12f, false);
		performanceDisplay.setColor(textColor);
		performanceDisplay.setWidth(0.5f);
		performanceDisplay.setEdge(0.1f);
		performanceDisplay.setOutlineColor(shadowColor);
		performanceDisplay.setBorderWidth(0.7f);
		performanceDisplay.setBorderEdge(0.08f);
		performanceDisplay.setShadowOffset(0.007f, 0.007f);
		mousePositionDisplay = new GUIText("Mouse position: (...)", 1f, guiFont, new Vector3f(0, 0.11f, 0), 0.35f, false);
		mousePositionDisplay.setColor(textColor);
		mousePositionDisplay.setWidth(0.5f);
		mousePositionDisplay.setEdge(0.1f);
		mousePositionDisplay.setOutlineColor(shadowColor);
		mousePositionDisplay.setBorderWidth(0.7f);
		mousePositionDisplay.setBorderEdge(0.08f);
		mousePositionDisplay.setShadowOffset(0.007f, 0.007f);
		colorDisplay = new GUIText("Color", 1f, guiFont, new Vector3f(0, 0.17f, 0), 0.2f, false);
		colorDisplay.setColor(textColor);
		colorDisplay.setWidth(0.5f);
		colorDisplay.setEdge(0.1f);
		colorDisplay.setOutlineColor(shadowColor);
		colorDisplay.setBorderWidth(0.7f);
		colorDisplay.setBorderEdge(0.08f);
		colorDisplay.setShadowOffset(0.007f, 0.007f);
		particleSettingsDisplay = new GUIText("Particle Settings: ...", 1f, guiFont, new Vector3f(0, 0.25f, 0), 0.3f, false);
		particleSettingsDisplay.setColor(textColor);
		particleSettingsDisplay.setWidth(0.5f);
		particleSettingsDisplay.setEdge(0.1f);
		particleSettingsDisplay.setOutlineColor(shadowColor);
		particleSettingsDisplay.setBorderWidth(0.7f);
		particleSettingsDisplay.setBorderEdge(0.08f);
		particleSettingsDisplay.setShadowOffset(0.007f, 0.007f);

		// Particles

		Texture explosionTexture = ImageLoader.loadTexture("textures/particles/explosion.png");
		Texture glowTexture = ImageLoader.loadTexture("textures/particles/glow.png");

		explosionParticleTex = new ParticleTexture(explosionTexture, 4, true);
		glowParticleTex = new ParticleTexture(glowTexture, 1, true);
		AppearanceBehavior appearanceBehavior = new FadeOutBehavior(1, 0);
		FlameBehavior flameBehavior = new FlameBehavior(-90, 15);
		particleBehavior = new ParticleBehavior(flameBehavior, appearanceBehavior);
		particleSource = new ParticleEmitter(new Vector3f(), glowParticleTex, particleBehavior);

		// particle settings
		particleBehavior.getAppearanceBehavior().initSize(6, 200);
		particleBehavior.getMovementBehavior().initSpeed(100, 120);
		particleBehavior.initLife(0.3f, 0.8f);
		particleSource.initPositionDelta(0, 0);
		particleSource.setBoxMode(false);
		particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_ORANGE);
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
			// game.update();
			ParticleMaster.update();

			// color selection
			runParticleTest();

			// render game internally
			// game.render();


			// render to the screen
			masterRenderer.render();

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
			sync.sync(Display.FPS_CAP);
		}

		// Clean up when done.

		// game.cleanUp();
		Loader.cleanUp();
		masterRenderer.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // why?

	}

	private static void runParticleTest() {
		// color selection
		if (KeyInput.isPressed(KeyInput.KEY_1)) 	particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_RED);
		else if (KeyInput.isPressed(KeyInput.KEY_2)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_ORANGE);
		else if (KeyInput.isPressed(KeyInput.KEY_3)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_YELLOW);
		else if (KeyInput.isPressed(KeyInput.KEY_4)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_GREEN);
		else if (KeyInput.isPressed(KeyInput.KEY_5)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.DARK_GREEN);
		else if (KeyInput.isPressed(KeyInput.KEY_6)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_CYAN);
		else if (KeyInput.isPressed(KeyInput.KEY_7)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_BLUE);
		else if (KeyInput.isPressed(KeyInput.KEY_8)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_PURPLE);
		else if (KeyInput.isPressed(KeyInput.KEY_9)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_MAGENTA);
		else if (KeyInput.isPressed(KeyInput.KEY_0)) particleBehavior.getAppearanceBehavior().setParticleColor(Colors.WHITE);
		else if (KeyInput.isPressed(KeyInput.ESC)) particleBehavior.getAppearanceBehavior().setRandomColor(true);

		if (KeyInput.isPressed(KeyInput.SPACE)){
			useExplosionTexture = !useExplosionTexture;
			particleSource.setParticleTexture(useExplosionTexture ? explosionParticleTex : glowParticleTex);
		}

		// ADJUSTING PARTICLE SETTINGS
		// UP and DOWN arrows to increase/decrease particle size
		if (KeyInput.isHeld(KeyInput.UP)) {
			if (KeyInput.isShift())
				particleBehavior.getAppearanceBehavior().setParticleSizeMin(particleBehavior.getAppearanceBehavior().getParticleSizeMin() + 1);
			else
				particleBehavior.getAppearanceBehavior().setParticleSizeMax(particleBehavior.getAppearanceBehavior().getParticleSizeMax() + 1);
		}
		if (KeyInput.isHeld(KeyInput.DOWN)) {
			if (KeyInput.isShift())
				particleBehavior.getAppearanceBehavior().setParticleSizeMin(particleBehavior.getAppearanceBehavior().getParticleSizeMin() - 1);
			else
				particleBehavior.getAppearanceBehavior().setParticleSizeMax(particleBehavior.getAppearanceBehavior().getParticleSizeMax() - 1);
		}
		// LEFT and RIGHT keys to increase/decrease speed
		if (KeyInput.isHeld(KeyInput.RIGHT)) {
			if (KeyInput.isShift())
				particleBehavior.getMovementBehavior().setParticleSpeedMin(particleBehavior.getMovementBehavior().getParticleSpeedMin() + 1);
			else
				particleBehavior.getMovementBehavior().setParticleSpeedMax(particleBehavior.getMovementBehavior().getParticleSpeedMax() + 1);
		}
		if (KeyInput.isHeld(KeyInput.LEFT)) {
			if (KeyInput.isShift())
				particleBehavior.getMovementBehavior().setParticleSpeedMin(particleBehavior.getMovementBehavior().getParticleSpeedMin() - 1);
			else
				particleBehavior.getMovementBehavior().setParticleSpeedMax(particleBehavior.getMovementBehavior().getParticleSpeedMax() - 1);
		}
		// SCROLL WHEEL to increase/decrease life duration
		if (MouseInput.WHEEL_UP) {
			if (KeyInput.isShift())
				particleBehavior.setParticleLifeMin(particleBehavior.getParticleLifeMin() + 0.1f);
			else
				particleBehavior.setParticleLifeMax(particleBehavior.getParticleLifeMax() + 0.1f);
		}
		else if (MouseInput.WHEEL_DOWN) {
			if (KeyInput.isShift())
				particleBehavior.setParticleLifeMin(particleBehavior.getParticleLifeMin() - 0.1f);
			else
				particleBehavior.setParticleLifeMax(particleBehavior.getParticleLifeMax() - 0.1f);
		}
		// RIGHT CLICK to increase/decrease starting position delta
		if (MouseInput.isPressed(MouseInput.RIGHT)) {
			if (KeyInput.isCtrl()) {
				if (KeyInput.isShift())
					particleSource.setPositionDeltaMin(particleSource.getPositionDeltaMin() - 1);
				else
					particleSource.setPositionDeltaMax(particleSource.getPositionDeltaMax() - 1);
			}
			else {
				if (KeyInput.isShift())
					particleSource.setPositionDeltaMin(particleSource.getPositionDeltaMin() + 1);
				else
					particleSource.setPositionDeltaMax(particleSource.getPositionDeltaMax() + 1);
			}

		}

		// generating particles
		if (MouseInput.isPressed(MouseInput.LEFT)) {
			particleSource.setPosition(new Vector3f(MouseInput.getMousePosi(), 0));
			int particleBatchCount = 4;
			for (int i = 0; i < particleBatchCount; ++i) {
				particleSource.generateParticles();
			}
		}

		// updating GUI display
		performanceDisplay.replaceText("Tick: " + DisplayManager.getCurrentFrameDeltaRaw() + "ms            FPS: " + (1.0f / DisplayManager.getFrameTimeRaw()));
		mousePositionDisplay.replaceText("Mouse position: (" + MouseInput.getX() + ", " + MouseInput.getY() + ") - Scaling: [" + MouseInput.xPosRatio + ", " + MouseInput.yPosRatio + "]");
		Vector4f color = particleBehavior.getAppearanceBehavior().getParticleColor();
		colorDisplay.setColor(color.x(), color.y(), color.z(), color.w());
		colorDisplay.replaceText(particleBehavior.getAppearanceBehavior().isRandomColor() ? "Random" : "Color");
		particleCountDisplay.replaceText("Particles: " + ParticleMaster.getParticleCount());
		particleSettingsDisplay.replaceText("Particle Settings:\n"
				+ "Min. Size: " + particleBehavior.getAppearanceBehavior().getParticleSizeMin() + "\n"
				+ "Max. Size: " + particleBehavior.getAppearanceBehavior().getParticleSizeMax() + "\n"
				+ "Min. Life: " + particleBehavior.getParticleLifeMin() + "\n"
				+ "Max. Life: " + particleBehavior.getParticleLifeMax() + "\n"
				+ "Min. Speed: " + particleBehavior.getMovementBehavior().getParticleSpeedMin() + "\n"
				+ "Max. Speed: " + particleBehavior.getMovementBehavior().getParticleSpeedMax() + "\n"
				+ "Min. Delta: " + particleSource.getPositionDeltaMin() + "\n"
				+ "Max. Delta: " + particleSource.getPositionDeltaMax()
		);
	}

}
