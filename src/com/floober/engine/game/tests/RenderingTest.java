package com.floober.engine.game.tests;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.GameWindow;
import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.particles.behavior.ParticleBehavior;
import com.floober.engine.renderEngine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.renderEngine.particles.behavior.appearance.FadeOutBehavior;
import com.floober.engine.renderEngine.particles.behavior.movement.ConstantVelocityBehavior;
import com.floober.engine.renderEngine.particles.behavior.movement.MovementBehavior;
import com.floober.engine.renderEngine.particles.emitters.ParticleEmitter;
import com.floober.engine.renderEngine.ppfx.PostProcessing;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import com.floober.engine.renderEngine.textures.TextureComponent;
import com.floober.engine.util.Logger;
import com.floober.engine.util.color.Colors;
import com.floober.engine.util.configuration.Config;
import com.floober.engine.util.input.KeyInput;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.time.Sync;
import com.floober.engine.util.time.Timer;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.display.GameWindow.windowID;
import static org.lwjgl.glfw.GLFW.*;

public class RenderingTest {

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		Config.FULLSCREEN = false;

		// Create the window and set up OpenGL and GLFW.
		GameWindow.initGame();

		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Initialize the game.
		Game.init();
		// game components
		Sync sync = new Sync();
		// master components
		TextMaster.init();
		ParticleMaster.init();
		ParticleMaster.initGlobals();
		PostProcessing.init();

		// TEST

		TextureComponent texture = Game.getTexture("default");
//		TextureComponent texture2 = Game.getTexture("default2");
		TextureComponent texture3 = Game.getTexture("default3");
//		TextureElement element1 = new TextureElement(texture, 0, 0, 15, false);
//		TextureElement element2 = new TextureElement(texture, 32, 0, 0, false);
//		TextureElement element3 = new TextureElement(texture, 0, 32, 0, false);
//		TextureElement element4 = new TextureElement(texture, 32, 32, 0, false);
//		TextureElement testStackElement = new TextureElement(texture2, 0, 0, 10, false);

		OutlineElement testBounds = new OutlineElement(Colors.RED, Display.centerX(), Display.centerY(), MasterRenderer.TOP_LAYER, 128, 32, 2, true);

		TextureElement testRotateElement = new TextureElement(texture3, Display.centerX(), Display.centerY(), MasterRenderer.TOP_LAYER, true);
		Logger.log("Position of testRotateElement: (" + testRotateElement.getX() + ", " + testRotateElement.getY() + ")");

		TextureElement testCropElement = new TextureElement(texture, Display.WIDTH / 2f, Display.HEIGHT / 2f, 0, 64, 64, true);
		testCropElement.setTextureOffset(new Vector4f(0.25f, 0.25f, 0.75f, 0.75f));

		Timer timer = new Timer(18f);

		ParticleEmitter engineParticleEmitter;
		// movement behavior
		MovementBehavior engineParticleMovement = new ConstantVelocityBehavior(170, 190);
		engineParticleMovement.initSpeed(-15, -75);
		// appearance
		AppearanceBehavior engineParticleAppearance = new FadeOutBehavior(1, 0);
		engineParticleAppearance.setParticleColor(Colors.PARTICLE_ORANGE);
		engineParticleAppearance.initSize(8, 16);
		engineParticleAppearance.initSize(80, 160);
		// behavior config
		ParticleBehavior engineParticleBehavior = new ParticleBehavior(engineParticleMovement, engineParticleAppearance);
		engineParticleBehavior.initLife(0.15f, 0.5f);
//		engineParticleBehavior.initLife(3f, 5f);
		// create the emitter
		engineParticleEmitter = new ParticleEmitter(new Vector3f(Display.center(), MasterRenderer.DEFAULT_LAYER), ParticleMaster.GLOW_PARTICLE_TEXTURE, engineParticleBehavior);
		engineParticleEmitter.setBatchCount(10);
		engineParticleEmitter.initPositionDelta(0, 15);
		engineParticleEmitter.setParticleDelay(0.02f);

		// END_TEST

		// Run the game loop!
		while (!glfwWindowShouldClose(windowID)) {
			// clear window
			MasterRenderer.prepare();

			// poll input
			KeyInput.update();
			MouseInput.update();

			DisplayManager.checkToggleFullscreen();

			// run game logic
			Game.update();
			ParticleMaster.update();

			// render game internally
			Game.render();
//			MasterRenderer.addTextureElement(element1);
//			MasterRenderer.addTextureElement(element2);
//			MasterRenderer.addTextureElement(element3);
//			MasterRenderer.addTextureElement(element4);
//			MasterRenderer.addTextureElement(testStackElement);
//			MasterRenderer.addTextureElement(testCropElement);

			if (timer.finished()) timer.restart();

			testRotateElement.setPosition(new Vector3f(Display.center(), 0));
			testRotateElement.setRotation(360 * timer.getProgress());
			testRotateElement.transform();
			MasterRenderer.addTextureElement(testRotateElement);

			// ***
			// TODO: create a particle emitter and test particle rendering!
			// (it doesn't seem to work for player particle trails)
			// ***

			// update engine particles
			engineParticleEmitter.setPosition(new Vector3f(MouseInput.getMousePos(), MasterRenderer.DEFAULT_LAYER));
			engineParticleEmitter.update();

//			Render.drawOutline(testBounds);

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

		// Clean up GLFW
		Callbacks.glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}