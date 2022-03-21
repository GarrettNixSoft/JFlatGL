package com.floober.engine.test.tests;

import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.assets.loaders.ImageLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.particles.ParticleTexture;
import com.floober.engine.core.renderEngine.particles.behavior.ParticleBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.appearance.FadeOutBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.ConstantVelocityBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.FlameBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.MovementBehavior;
import com.floober.engine.core.renderEngine.particles.emitters.LightParticleEmitter;
import com.floober.engine.core.renderEngine.particles.emitters.ParticleEmitter;
import com.floober.engine.core.renderEngine.particles.emitters.TexturedParticleEmitter;
import com.floober.engine.core.renderEngine.particles.types.LightParticle;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.time.Sync;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static com.floober.engine.core.util.input.KeyInput.S;
import static org.lwjgl.glfw.GLFW.*;

public class ParticleTest {

	// generating particles
	static ParticleEmitter particleEmitter;
	static ParticleEmitter particleEmitter1;
	static ParticleEmitter particleEmitter2;
	static ParticleEmitter particleEmitter3;
	static ParticleEmitter particleEmitter4;
	static int sourceSwitch;
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
		Config.FULLSCREEN = false;

		// Create the window and set up OpenGL and GLFW.
		DisplayManager.initPrimaryGameWindow();

		// Initialize the game.
		// game.init();
		// game components
		Sync sync = new Sync();
		// master components
		TextMaster.init();
		ParticleMaster.init();

		Logger.log("initialization completed");

		// TEST
		// Use a GUIText to display the number of particles currently in existence.
//		FontType guiFont = new FontType(loader.loadFontAtlas("aller/aller.png"), StringConverter.combineAll(FileUtil.getFileData("/fonts/aller/aller.fnt")));
		guiFont = Loader.loadFont("aller");
		Vector4f textColor = new Vector4f(1,1,1,0.8f);
		Vector4f shadowColor = new Vector4f(0);
		particleCountDisplay = new GUIText("Particles: 0", 1.5f, guiFont, new Vector3f(0), 1, false);
		particleCountDisplay.setColor(textColor);
		particleCountDisplay.setWidth(0.5f);
		particleCountDisplay.setEdge(0.2f);
		particleCountDisplay.setOutlineColor(shadowColor);
		particleCountDisplay.setBorderWidth(0.7f);
		particleCountDisplay.setBorderEdge(0.08f);
		particleCountDisplay.setShadowOffset(0.007f, 0.007f);
		particleCountDisplay.show();
		performanceDisplay = new GUIText("Tick: ... Frame: ...", 1f, guiFont, new Vector3f(0, 0.05f, 0), 0.12f, false);
		performanceDisplay.setColor(textColor);
		performanceDisplay.setWidth(0.5f);
		performanceDisplay.setEdge(0.1f);
		performanceDisplay.setOutlineColor(shadowColor);
		performanceDisplay.setBorderWidth(0.7f);
		performanceDisplay.setBorderEdge(0.08f);
		performanceDisplay.setShadowOffset(0.007f, 0.007f);
		performanceDisplay.show();
		mousePositionDisplay = new GUIText("Mouse position: (...)", 1f, guiFont, new Vector3f(0, 0.11f, 0), 0.35f, false);
		mousePositionDisplay.setColor(textColor);
		mousePositionDisplay.setWidth(0.5f);
		mousePositionDisplay.setEdge(0.1f);
		mousePositionDisplay.setOutlineColor(shadowColor);
		mousePositionDisplay.setBorderWidth(0.7f);
		mousePositionDisplay.setBorderEdge(0.08f);
		mousePositionDisplay.setShadowOffset(0.007f, 0.007f);
		mousePositionDisplay.show();
		colorDisplay = new GUIText("Color", 1f, guiFont, new Vector3f(0, 0.17f, 0), 0.2f, false);
		colorDisplay.setColor(textColor);
		colorDisplay.setWidth(0.5f);
		colorDisplay.setEdge(0.1f);
		colorDisplay.setOutlineColor(shadowColor);
		colorDisplay.setBorderWidth(0.7f);
		colorDisplay.setBorderEdge(0.08f);
		colorDisplay.setShadowOffset(0.007f, 0.007f);
		colorDisplay.show();
		particleSettingsDisplay = new GUIText("Particle Settings: ...", 1f, guiFont, new Vector3f(0, 0.25f, 0), 0.3f, false);
		particleSettingsDisplay.setColor(textColor);
		particleSettingsDisplay.setWidth(0.5f);
		particleSettingsDisplay.setEdge(0.1f);
		particleSettingsDisplay.setOutlineColor(shadowColor);
		particleSettingsDisplay.setBorderWidth(0.7f);
		particleSettingsDisplay.setBorderEdge(0.08f);
		particleSettingsDisplay.setShadowOffset(0.007f, 0.007f);
		particleSettingsDisplay.show();

		Logger.log("Text was created");

		// Particles

		TextureComponent explosionTexture = ImageLoader.loadTexture("textures/particles/explosion.png");
		TextureComponent glowTexture = ImageLoader.loadTexture("textures/particles/glow.png");
		TextureComponent defaultTex = ImageLoader.loadTexture("textures/complex_sample.png");

		explosionParticleTex = new ParticleTexture(explosionTexture, 4, true);
		glowParticleTex = new ParticleTexture(glowTexture, 1, true);
		ParticleTexture particleTex = new ParticleTexture(defaultTex, 1, false);

		AppearanceBehavior fadeOutBehavior = new FadeOutBehavior(1, 0);
		MovementBehavior flameBehavior = new FlameBehavior(-90, 15);
		MovementBehavior constantVelocity = new ConstantVelocityBehavior(0, 360);

		particleBehavior = new ParticleBehavior(constantVelocity, fadeOutBehavior);
		particleBehavior.getAppearanceBehavior().initSize(6, 200);
		particleBehavior.getMovementBehavior().initSpeed(100, 120);
		particleBehavior.initLife(2f, 4f);
		particleBehavior.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_YELLOW);

		AppearanceBehavior appearanceBehavior2 = new FadeOutBehavior(1, 0);
		appearanceBehavior2.setParticleColor(new Vector4f(0, 0, 0, 1));
		ParticleBehavior particleBehavior2 = new ParticleBehavior(constantVelocity, appearanceBehavior2);
		particleBehavior2.getAppearanceBehavior().initSize(6, 200);
		particleBehavior2.getMovementBehavior().initSpeed(100, 120);
		particleBehavior2.initLife(2f, 4f);
		particleBehavior2.getAppearanceBehavior().setParticleColor(Colors.PARTICLE_YELLOW);

		particleEmitter1 = new ParticleEmitter(new Vector3f(), glowParticleTex, particleBehavior);
		particleEmitter2 = new LightParticleEmitter(new Vector3f(), glowParticleTex, particleBehavior);
		particleEmitter3 = new TexturedParticleEmitter(new Vector3f(), particleTex, particleBehavior2);
		particleEmitter4 = new ParticleEmitter(new Vector3f(), glowParticleTex, particleBehavior);

		particleEmitter1.initPositionDelta(0, 0);
		particleEmitter1.setBoxMode(false);
		particleEmitter2.initPositionDelta(0, 0);
		particleEmitter2.setBoxMode(false);
		particleEmitter3.initPositionDelta(0, 0);
		particleEmitter3.setBoxMode(false);
		particleEmitter4.initPositionDelta(0, 0);
		particleEmitter4.setBoxMode(false);

		particleEmitter = particleEmitter1;


		// LIGHT SOURCE TEST
		if (particleEmitter2 instanceof LightParticleEmitter lightEmitter) {
			lightEmitter.initInnerRadius(2, 25);
			lightEmitter.initOuterRadius(5, 30);
			lightEmitter.initLightIntensity(0.1f, 0.4f);
			lightEmitter.initLightRadius(90, 250);
			lightEmitter.setLightMode(LightParticle.SMOOTH);
			lightEmitter.setLightColor(Colors.GOLD_3);
		}
		if (particleEmitter3 instanceof TexturedParticleEmitter texturedEmitter) {
			texturedEmitter.initStartOffset(0, 0, 0.5f, 0.5f);
			texturedEmitter.initEndOffset(0.25f, 0.25f, 0.5f, 0.5f);
//			texturedEmitter.getParticleBehavior().getAppearanceBehavior().setParticleColor(new Vector4f(0, 0, 0, 1));
		}
		// END_TEST

		Logger.log("Particles were initialized");

		GameLoader.LOAD_COMPLETE = true;

		// Run the game loop!
		while (!glfwWindowShouldClose(primaryWindowID)) {
			// clear window
			MasterRenderer.primaryWindowRenderer.prepare();

			// poll input
			KeyInput.update();
			MouseInput.update();

//			DisplayManager.checkToggleFullscreen();

			// run game logic
			// game.update();
			ParticleMaster.update();

			// color selection
			runParticleTest();

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render();
			MasterRenderer.getTargetWindow().swapBuffers();

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
//			sync.sync(Display.FPS_CAP);

//			Logger.log("A frame was completed");
		}

		// Clean up when done.

		// game.cleanUp();
		Loader.cleanUp();
		MasterRenderer.cleanUp();
		TextMaster.cleanUp();
		ParticleMaster.cleanUp();

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(primaryWindowID);
		glfwDestroyWindow(primaryWindowID);

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
			particleEmitter.setParticleTexture(useExplosionTexture ? explosionParticleTex : glowParticleTex);
		}

		if (KeyInput.isPressed(S)) {
			sourceSwitch = ++sourceSwitch % 3;
			switch (sourceSwitch) {
				case 0 -> particleEmitter = particleEmitter1;
				case 1 -> particleEmitter = particleEmitter2;
				case 2 -> particleEmitter = particleEmitter3;
			}
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
		if (MouseInput.wheelUp()) {
			if (KeyInput.isShift())
				particleBehavior.setParticleLifeMin(particleBehavior.getParticleLifeMin() + 0.1f);
			else
				particleBehavior.setParticleLifeMax(particleBehavior.getParticleLifeMax() + 0.1f);
		}
		else if (MouseInput.wheelDown()) {
			if (KeyInput.isShift())
				particleBehavior.setParticleLifeMin(particleBehavior.getParticleLifeMin() - 0.1f);
			else
				particleBehavior.setParticleLifeMax(particleBehavior.getParticleLifeMax() - 0.1f);
		}
		// RIGHT CLICK to increase/decrease starting position delta
		if (MouseInput.isPressed(MouseInput.RIGHT)) {
			if (KeyInput.isCtrl()) {
				if (KeyInput.isShift())
					particleEmitter.setPositionDeltaMin(particleEmitter.getPositionDeltaMin() - 1);
				else
					particleEmitter.setPositionDeltaMax(particleEmitter.getPositionDeltaMax() - 1);
			}
			else {
				if (KeyInput.isShift())
					particleEmitter.setPositionDeltaMin(particleEmitter.getPositionDeltaMin() + 1);
				else
					particleEmitter.setPositionDeltaMax(particleEmitter.getPositionDeltaMax() + 1);
			}

		}

		// generating particles
		if (MouseInput.isPressed(MouseInput.LEFT)) {
			particleEmitter.setPosition(new Vector3f(MouseInput.getMousePosi(), 0));
			int particleBatchCount = 1;
			for (int i = 0; i < particleBatchCount; ++i) {
				particleEmitter.generateParticles();
			}
		}

		// TEST: Toggling post-processing effects
		if (KeyInput.isShift()) {
			if (KeyInput.isPressed(KeyInput.C)) { // C for contrast
				MasterRenderer.primaryWindowRenderer.getPostProcessor().setStageEnabled("contrast", !MasterRenderer.primaryWindowRenderer.getPostProcessor().isStageEnabled("contrast"));
			}
			if (KeyInput.isPressed(KeyInput.I)) { // C for invert
				MasterRenderer.primaryWindowRenderer.getPostProcessor().setStageEnabled("invertColor", !MasterRenderer.primaryWindowRenderer.getPostProcessor().isStageEnabled("invertColor"));
			}
		}

		// updating GUI display
		Window currentWindow = MasterRenderer.getTargetWindow();
		performanceDisplay.replaceText("Tick: " + DisplayManager.getCurrentFrameDeltaRaw() + "ms            FPS: " + (1.0f / DisplayManager.getFrameTimeRaw()));
		mousePositionDisplay.replaceText("Mouse position: (" + MouseInput.getX() + ", " + MouseInput.getY() +
				") - Scaling: [" + currentWindow.getMouseXRatio() + ", " + currentWindow.getMouseYRatio() + "]");
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
				+ "Min. Delta: " + particleEmitter.getPositionDeltaMin() + "\n"
				+ "Max. Delta: " + particleEmitter.getPositionDeltaMax()
		);
	}

}
