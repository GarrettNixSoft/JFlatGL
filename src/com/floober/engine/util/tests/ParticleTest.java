package com.floober.engine.util.tests;

import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.fonts.fontMeshCreator.FontType;
import com.floober.engine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.fonts.fontRendering.TextMaster;
import com.floober.engine.loaders.ImageLoader;
import com.floober.engine.loaders.Loader;
import com.floober.engine.particles.ParticleFactory;
import com.floober.engine.particles.ParticleMaster;
import com.floober.engine.particles.ParticleSource;
import com.floober.engine.particles.ParticleTexture;
import com.floober.engine.particles.behavior.*;
import com.floober.engine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.particles.behavior.appearance.FadeOutBehavior;
import com.floober.engine.particles.behavior.movement.ConstantVelocityBehavior;
import com.floober.engine.particles.behavior.movement.MovementBehavior;
import com.floober.engine.renderEngine.MasterRenderer;
import com.floober.engine.util.Colors;
import com.floober.engine.util.Logger;
import com.floober.engine.util.data.Config;
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

public class ParticleTest {

	// generating particles
	static ParticleSource particleSource;
	static ParticleFactory particleFactory;
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
		Loader loader = new Loader();
		Sync sync = new Sync();
		MasterRenderer masterRenderer = new MasterRenderer();
		// master components
		TextMaster.init();
		ParticleMaster.init();

		// TEST
		// Use a GUIText to display the number of particles currently in existence.
//		FontType guiFont = new FontType(loader.loadFontAtlas("aller/aller.png"), StringConverter.combineAll(FileUtil.getFileData("/fonts/aller/aller.fnt")));
		guiFont = loader.loadFont("aller");
		Vector4f textColor = new Vector4f(1);
		Vector4f shadowColor = new Vector4f(0);
		particleCountDisplay = new GUIText("Particles: 0", 1.5f, guiFont, new Vector2f(0, 0f), 1, false);
		particleCountDisplay.setColor(textColor);
		particleCountDisplay.setWidth(0.5f);
		particleCountDisplay.setEdge(0.2f);
		particleCountDisplay.setOutlineColor(shadowColor);
		particleCountDisplay.setBorderWidth(0.7f);
		particleCountDisplay.setBorderEdge(0.08f);
		particleCountDisplay.setShadowOffset(0.007f, 0.007f);
		performanceDisplay = new GUIText("Tick: ... Frame: ...", 1f, guiFont, new Vector2f(0, 0.05f), 0.12f, false);
		performanceDisplay.setColor(textColor);
		performanceDisplay.setWidth(0.5f);
		performanceDisplay.setEdge(0.1f);
		performanceDisplay.setOutlineColor(shadowColor);
		performanceDisplay.setBorderWidth(0.7f);
		performanceDisplay.setBorderEdge(0.08f);
		performanceDisplay.setShadowOffset(0.007f, 0.007f);
		mousePositionDisplay = new GUIText("Mouse position: (...)", 1f, guiFont, new Vector2f(0, 0.11f), 0.35f, false);
		mousePositionDisplay.setColor(textColor);
		mousePositionDisplay.setWidth(0.5f);
		mousePositionDisplay.setEdge(0.1f);
		mousePositionDisplay.setOutlineColor(shadowColor);
		mousePositionDisplay.setBorderWidth(0.7f);
		mousePositionDisplay.setBorderEdge(0.08f);
		mousePositionDisplay.setShadowOffset(0.007f, 0.007f);
		colorDisplay = new GUIText("Color", 1f, guiFont, new Vector2f(0, 0.17f), 0.2f, false);
		colorDisplay.setColor(textColor);
		colorDisplay.setWidth(0.5f);
		colorDisplay.setEdge(0.1f);
		colorDisplay.setOutlineColor(shadowColor);
		colorDisplay.setBorderWidth(0.7f);
		colorDisplay.setBorderEdge(0.08f);
		colorDisplay.setShadowOffset(0.007f, 0.007f);
		particleSettingsDisplay = new GUIText("Particle Settings: ...", 1f, guiFont, new Vector2f(0, 0.25f), 0.3f, false);
		particleSettingsDisplay.setColor(textColor);
		particleSettingsDisplay.setWidth(0.5f);
		particleSettingsDisplay.setEdge(0.1f);
		particleSettingsDisplay.setOutlineColor(shadowColor);
		particleSettingsDisplay.setBorderWidth(0.7f);
		particleSettingsDisplay.setBorderEdge(0.08f);
		particleSettingsDisplay.setShadowOffset(0.007f, 0.007f);

		// Particles

		explosionParticleTex = new ParticleTexture(ImageLoader.loadTexture("textures/particles/explosion.png").getId(), 4, true);
		glowParticleTex = new ParticleTexture(ImageLoader.loadTexture("textures/particles/glow.png").getId(), 1, true);
		MovementBehavior movementBehavior = new ConstantVelocityBehavior();
		AppearanceBehavior appearanceBehavior = new FadeOutBehavior(1, 0);
		ParticleBehavior behavior = new ParticleBehavior(movementBehavior, appearanceBehavior);
		particleFactory = new ParticleFactory(glowParticleTex, new Vector4f(1));
		particleSource = new ParticleSource(new Vector3f(), behavior, particleFactory);

		// particle settings
		particleFactory.initSize(6, 64);
		particleFactory.initSpeed(5, 200);
		particleFactory.initLife(0.5f, 1f);
		particleFactory.initPositionDelta(0, 0);
		particleFactory.setBoxMode(false);
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
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // why?

	}

	private static void runParticleTest() {
		// color selection
		if (KeyInput.isPressed(KeyInput.KEY_1)) 	 particleFactory.setParticleColor(Colors.PARTICLE_RED);
		else if (KeyInput.isPressed(KeyInput.KEY_2)) particleFactory.setParticleColor(Colors.PARTICLE_ORANGE);
		else if (KeyInput.isPressed(KeyInput.KEY_3)) particleFactory.setParticleColor(Colors.PARTICLE_YELLOW);
		else if (KeyInput.isPressed(KeyInput.KEY_4)) particleFactory.setParticleColor(Colors.PARTICLE_GREEN);
		else if (KeyInput.isPressed(KeyInput.KEY_5)) particleFactory.setParticleColor(Colors.DARK_GREEN);
		else if (KeyInput.isPressed(KeyInput.KEY_6)) particleFactory.setParticleColor(Colors.PARTICLE_CYAN);
		else if (KeyInput.isPressed(KeyInput.KEY_7)) particleFactory.setParticleColor(Colors.PARTICLE_BLUE);
		else if (KeyInput.isPressed(KeyInput.KEY_8)) particleFactory.setParticleColor(Colors.PARTICLE_PURPLE);
		else if (KeyInput.isPressed(KeyInput.KEY_9)) particleFactory.setParticleColor(Colors.PARTICLE_MAGENTA);
		else if (KeyInput.isPressed(KeyInput.KEY_0)) particleFactory.setParticleColor(Colors.WHITE);
		else if (KeyInput.isPressed(KeyInput.ESC)) particleFactory.setRandomColor(true);

		if (KeyInput.isPressed(KeyInput.SPACE)){
			useExplosionTexture = !useExplosionTexture;
			particleFactory.setParticleTexture(useExplosionTexture ? explosionParticleTex : glowParticleTex);
		}

		// ADJUSTING PARTICLE SETTINGS
		// UP and DOWN arrows to increase/decrease particle size
		if (KeyInput.isDown(KeyInput.UP)) {
			if (KeyInput.isShift())
				particleFactory.setParticleSizeMin(particleFactory.getParticleSizeMin() + 1);
			else
				particleFactory.setParticleSizeMax(particleFactory.getParticleSizeMax() + 1);
		}
		if (KeyInput.isDown(KeyInput.DOWN)) {
			if (KeyInput.isShift())
				particleFactory.setParticleSizeMin(particleFactory.getParticleSizeMin() - 1);
			else
				particleFactory.setParticleSizeMax(particleFactory.getParticleSizeMax() - 1);
		}
		// LEFT and RIGHT keys to increase/decrease speed
		if (KeyInput.isDown(KeyInput.RIGHT)) {
			if (KeyInput.isShift())
				particleFactory.setParticleSpeedMin(particleFactory.getParticleSpeedMin() + 1);
			else
				particleFactory.setParticleSpeedMax(particleFactory.getParticleSpeedMax() + 1);
		}
		if (KeyInput.isDown(KeyInput.LEFT)) {
			if (KeyInput.isShift())
				particleFactory.setParticleSpeedMin(particleFactory.getParticleSpeedMin() - 1);
			else
				particleFactory.setParticleSpeedMax(particleFactory.getParticleSpeedMax() - 1);
		}
		// SCROLL WHEEL to increase/decrease life duration
		if (MouseInput.WHEEL_UP) {
			if (KeyInput.isShift())
				particleFactory.setParticleLifeMin(particleFactory.getParticleLifeMin() + 0.1f);
			else
				particleFactory.setParticleLifeMax(particleFactory.getParticleLifeMax() + 0.1f);
		}
		else if (MouseInput.WHEEL_DOWN) {
			if (KeyInput.isShift())
				particleFactory.setParticleLifeMin(particleFactory.getParticleLifeMin() - 0.1f);
			else
				particleFactory.setParticleLifeMax(particleFactory.getParticleLifeMax() - 0.1f);
		}
		// RIGHT CLICK to increase/decrease starting position delta
		if (MouseInput.isPressed(MouseInput.RIGHT)) {
			if (KeyInput.isCtrl()) {
				if (KeyInput.isShift())
					particleFactory.setPositionDeltaMin(particleFactory.getPositionDeltaMin() - 1);
				else
					particleFactory.setPositionDeltaMax(particleFactory.getPositionDeltaMax() - 1);
			}
			else {
				if (KeyInput.isShift())
					particleFactory.setPositionDeltaMin(particleFactory.getPositionDeltaMin() + 1);
				else
					particleFactory.setPositionDeltaMax(particleFactory.getPositionDeltaMax() + 1);
			}

		}

		// generating particles
		if (MouseInput.isPressed(MouseInput.LEFT)) {
			particleSource.setPosition(new Vector3f(MouseInput.getMousePos(), 0));
			int particleBatchCount = 4;
			for (int i = 0; i < particleBatchCount; ++i) {
				particleSource.generateParticle();
			}
		}

		// updating GUI display
		performanceDisplay.updateText("Tick: " + DisplayManager.getCurrentFrameDeltaRaw() + "ms            FPS: " + (1.0f / DisplayManager.getFrameTimeRaw()));
		mousePositionDisplay.updateText("Mouse position: (" + MouseInput.getX() + ", " + MouseInput.getY() + ") - Scaling: [" + MouseInput.xPosRatio + ", " + MouseInput.yPosRatio + "]");
		Vector4f color = particleFactory.getParticleColor();
		colorDisplay.setColor(color.x(), color.y(), color.z(), color.w());
		colorDisplay.updateText(particleFactory.isRandomColor() ? "Random" : "Color");
		particleCountDisplay.updateText("Particles: " + ParticleMaster.getParticleCount());
		particleSettingsDisplay.updateText("Particle Settings:\n"
				+ "Min. Size: " + particleFactory.getParticleSizeMin() + "\n"
				+ "Max. Size: " + particleFactory.getParticleSizeMax() + "\n"
				+ "Min. Life: " + particleFactory.getParticleLifeMin() + "\n"
				+ "Max. Life: " + particleFactory.getParticleLifeMax() + "\n"
				+ "Min. Speed: " + particleFactory.getParticleSpeedMin() + "\n"
				+ "Max. Speed: " + particleFactory.getParticleSpeedMax() + "\n"
				+ "Min. Delta: " + particleFactory.getPositionDeltaMin() + "\n"
				+ "Max. Delta: " + particleFactory.getPositionDeltaMax()
		);
	}

}
