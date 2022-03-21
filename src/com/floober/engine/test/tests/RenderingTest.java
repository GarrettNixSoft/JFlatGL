package com.floober.engine.test.tests;

import com.floober.engine.animation.Animation;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.particles.behavior.ParticleBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.appearance.AppearanceBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.appearance.FadeOutBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.ConstantVelocityBehavior;
import com.floober.engine.core.renderEngine.particles.behavior.movement.MovementBehavior;
import com.floober.engine.core.renderEngine.particles.emitters.ParticleEmitter;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.textures.TextureSet;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.math.Collisions;
import com.floober.engine.core.util.time.Sync;
import com.floober.engine.core.util.time.Timer;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.Callbacks;

import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

public class RenderingTest {

	public static void main(String[] args) {

		// Set up logging.
		Logger.setLoggerConfig();

		Config.FULLSCREEN = false;

		// Create the window and set up OpenGL and GLFW.
		DisplayManager.initPrimaryGameWindow();

		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Initialize the game.
		Game.init();
		// game components
		Sync sync = new Sync();

		// TEST
		Window gameWindow = DisplayManager.getPrimaryGameWindow();

		TextureComponent texture = Game.getTexture("default");
//		TextureComponent texture2 = Game.getTexture("default2");
		TextureComponent texture3 = Game.getTexture("default2");
//		TextureElement element1 = new TextureElement(texture, 0, 0, 15, false);
//		TextureElement element2 = new TextureElement(texture, 32, 0, 0, false);
//		TextureElement element3 = new TextureElement(texture, 0, 32, 0, false);
//		TextureElement element4 = new TextureElement(texture, 32, 32, 0, false);
//		TextureElement testStackElement = new TextureElement(texture2, 0, 0, 10, false);

		OutlineElement testBounds = new OutlineElement(Colors.RED, gameWindow.centerX(), gameWindow.centerY(), Layers.TOP_LAYER, 128, 32, 2, true);

		TextureElement testRotateElement = new TextureElement(texture3, gameWindow.centerX(), gameWindow.centerY(), Layers.TOP_LAYER, true);
		Logger.log("Position of testRotateElement: (" + testRotateElement.getX() + ", " + testRotateElement.getY() + ")");

		TextureElement testCropElement = new TextureElement(texture, gameWindow.getWidth() / 2f, gameWindow.getHeight() / 2f, 0, 64, 64, true);
		testCropElement.setTextureOffset(new Vector4f(0.25f, 0.25f, 0.75f, 0.75f));

		Timer timer = new Timer(18f);

		ParticleEmitter engineParticleEmitter;
		// movement behavior
		MovementBehavior engineParticleMovement = new ConstantVelocityBehavior(80, 100);
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
		engineParticleEmitter = new ParticleEmitter(new Vector3f(gameWindow.center(), Layers.DEFAULT_LAYER), ParticleMaster.GLOW_PARTICLE_TEXTURE, engineParticleBehavior);
		engineParticleEmitter.setBatchCount(10);
		engineParticleEmitter.initPositionDelta(0, 15);
		engineParticleEmitter.setParticleDelay(0.005f);



		// TEST ANIMATION!
		TextureSet testSet = Game.getTextureSet("default_set");
		Animation testAnimation = new Animation(testSet, 333);

		// TEST COLLISIONS!
		OutlineElement testBigRect = new OutlineElement(Colors.RED, Window.mainCenterX(), Window.mainCenterY(), Layers.DEFAULT_LAYER, 400, 400, 3, true);
		RectElement testBigRectFill = new RectElement(Colors.RED, Window.mainCenterX(), Window.mainCenterY(), Layers.DEFAULT_LAYER, 400, 400, true);

		RectElement testMoveElem = new RectElement(Colors.GREEN, Window.mainCenterX(), Window.mainCenterY(), Layers.DEFAULT_LAYER + 3, 50, 50, true);



		// END_TEST

		Logger.log("Starting game loop!");
		// Run the game loop!
		while (!glfwWindowShouldClose(primaryWindowID)) {
			// clear window
			MasterRenderer.primaryWindowRenderer.prepare();

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

			testAnimation.update();

			testRotateElement.setTexture(testAnimation.getCurrentFrame());

			testRotateElement.setPosition(new Vector3f(gameWindow.center(), 0));
			testRotateElement.setRotation(360 * timer.getProgress());
			testRotateElement.transform();
			MasterRenderer.currentRenderTarget.addTextureElement(testRotateElement);

			// ***
			// create a particle emitter and test particle rendering!
			// (it doesn't seem to work for player particle trails)
			// ***

			// update engine particles
//			engineParticleEmitter.setPosition(new Vector3f(MouseInput.getMousePos(), MasterRenderer.DEFAULT_LAYER));
//			engineParticleEmitter.update();

			testMoveElem.setPosition(MouseInput.getX(), MouseInput.getY(), testMoveElem.getLayer());

			testBigRect.transform();
			testBigRect.render();
			testBigRectFill.transform();
			testBigRectFill.render();
			testMoveElem.transform();
			testMoveElem.render();

//			Vector4f testMoveBox = new Vector4f(testMoveElem.getX(), testMoveElem.getY(), testMoveElem.getX() + testMoveElem.getWidth(), testMoveElem.getY() + testMoveElem.getHeight());
//			Vector4f bigBox = new Vector4f(testBigRect.getX(), testBigRect.getY(), testBigRect.getX() + testBigRect.getWidth(), testBigRect.getY() + testBigRect.getHeight());
			Vector4f testMoveBox = Collisions.createCollisionBox(testMoveElem.getPixelPosition(), testMoveElem.getScale());
			Vector4f bigBox = Collisions.createCollisionBox(testBigRect.getPixelPosition(), testBigRect.getScale());

//			Logger.log(bigBox);
//			Logger.log(testMoveBox);
//			Logger.log(testMoveElem.getPosition());

			if (Collisions.intersects(testMoveBox, bigBox)) {
				float overlap = Collisions.overlapProportion(testMoveBox, bigBox);
//				Logger.log("overlap = " + overlap);
				Vector4f mix = Colors.mix(Colors.GREEN, Colors.BLUE, overlap);
				testMoveElem.setColor(mix);
			} else testMoveElem.setColor(Colors.GREEN);
//			Logger.log("Collision");

//			Render.drawOutline(testBounds);

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render();
			MasterRenderer.getTargetWindow().swapBuffers();

			// update display and poll events
			DisplayManager.updateDisplay();

			// sync time
			sync.sync(DisplayManager.FPS_CAP);
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
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

	}

}