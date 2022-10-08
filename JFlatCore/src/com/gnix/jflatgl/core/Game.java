package com.gnix.jflatgl.core;

import com.gnix.jflatgl.animation.Animation;
import com.gnix.jflatgl.core.assets.*;
import com.gnix.jflatgl.core.assets.loaders.AssetLoader;
import com.gnix.jflatgl.core.assets.loaders.GameLoader;
import com.gnix.jflatgl.core.assets.loaders.Loader;
import com.gnix.jflatgl.core.audio.AudioMaster;
import com.gnix.jflatgl.core.audio.Sound;
import com.gnix.jflatgl.core.gameState.GameStateManager;
import com.gnix.jflatgl.core.input.*;
import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.renderers.MasterRenderer;
import com.gnix.jflatgl.core.renderEngine.textures.TextureOutliner;
import com.gnix.jflatgl.core.renderEngine.util.Layers;
import com.gnix.jflatgl.core.splash.SplashRenderer;
import com.gnix.jflatgl.core.splash.SplashScreen;
import com.gnix.jflatgl.core.util.color.Colors;
import com.gnix.jflatgl.core.util.configuration.Config;
import com.gnix.jflatgl.core.util.configuration.Settings;
import com.gnix.jflatgl.core.util.time.TimeScale;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.fonts.fontRendering.TextMaster;
import com.gnix.jflatgl.core.renderEngine.particles.ParticleMaster;
import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.renderEngine.textures.TextureSet;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.Session;
import com.gnix.jflatgl.extension.EngineExtension;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.gnix.jflatgl.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

/**
 * This class represents the Game itself. It provides convenience
 * functions for fetching all assets (textures, audio, etc.) and
 * for triggering audio events (playing music or sfx).
 */

public class Game {

	// global access to the game
	public static Game instance;

	// engine extensions
	private final List<EngineExtension> engineExtensions = new ArrayList<>();

	// game components
	private final GameLoader loader;
	private final Textures textures;
	private final Music music;
	private final Sfx sfx;
	private final Fonts fonts;
	private final Animations animations;

	private final Session session;

	// handling game states
	private GameStateManager gsm;

	// flag for requesting quit
	private boolean closeRequest;

	// FPS counter
	private GUIText fpsDisplay;

	/**
	 * Create the game. This will initialize all
	 * asset containers.
	 */
	public Game() {
		loader = new GameLoader();
		textures = new Textures();
		music = new Music();
		sfx = new Sfx();
		fonts = new Fonts();
		animations = new Animations();
		session = new Session();
	}

	/**
	 * Add a custom AssetLoader subclass to the load sequence.
	 * If your project requires a custom type of asset to be
	 * loaded, you can extend the AssetLoader class with your
	 * own code for loading that asset type and add it to the
	 * game loader here.
	 * @param assetLoader a custom loader to run when loading the game
	 */
	public static void addCustomAssetLoader(AssetLoader assetLoader) {
		instance.loader.addCustomAssetLoader(assetLoader);
	}

	public static void addEngineExtension(EngineExtension extension) {
		instance.engineExtensions.add(extension);
	}

	public static void loadConfig() {
		// Load user preferences/settings and game flags
		Config.load();
		Settings.load();
		// Load control mappings
		Controls.init();
	}

	/**
	 * Calling this method will load all game assets
	 * specified in the JSON directories contained in
	 * resourceData/assets. Once loading is complete,
	 * the GameStateManager will be initialized.
	 */
	public static void init(SplashRenderer splashRenderer, GameStateManager gsm) {

		// Set up logging.
		Logger.setLoggerConfig();

		// Set up OpenAL.
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// update display windows and timings
		DisplayManager.updateDisplay();

		// initialize OpenGL now to prepare it for a splash render window if necessary
		DisplayManager.initOpenGL();

		// Set up OpenAL.
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);

		// Prepare the Gamepad input
		GamepadInput.init();

		// initialize the cursor
		Cursor.init();

		// initialize the instance
		instance = new Game();
		instance.gsm = gsm;

		// the game itself (load assets)
		GameLoader gameLoader = new GameLoader();

		// TODO: allow custom asset loaders to be inserted here

		// finalize all asset loaders
		gameLoader.prepare();

		// begin loading
		instance.load(gameLoader, splashRenderer);

		// Clear the text master if it was used
		if (Config.USE_SPLASH_SCREEN) {
			TextMaster.cleanUp();
		}

		// Create the window and set up OpenGL and GLFW.
		DisplayManager.initPrimaryGameWindow();

		// finish loading (after creating the main game window! this ensures OpenGL assets are properly loaded)
		gameLoader.finish();

		// Re-initialize the TextMaster on the proper context
		TextMaster.clear();
		TextMaster.init();

		// Next, prepare the GSM
		instance.gsm.init();

		// load particles AFTER textures are loaded (some particles need to load textures from the game's pool)
		ParticleMaster.initGlobals();

		// Init FPS display
		instance.fpsDisplay = new GUIText("FPS: ", 0.5f, Game.getFont("default"), new Vector3f(0, 0, 0), 1);
		instance.fpsDisplay.setLayer(Layers.TOP_LAYER);
		instance.fpsDisplay.setColor(Colors.GREEN);
		instance.fpsDisplay.setWidth(0.5f);
		instance.fpsDisplay.setEdge(0.2f);

		if (Settings.getSettingBoolean("show_fps")) {
			instance.fpsDisplay.show();
		}

	}

	/**
	 * Load the game. Run on the game's instance.
	 */
	private void load(GameLoader loader, SplashRenderer splashRenderer) {

		// if the splash screen is enabled, initialize it
		if (Config.USE_SPLASH_SCREEN) {
			SplashScreen.init();
			SplashScreen.prepare(splashRenderer);
		} else {
			// Initialize the texture analyzer if the splash screen did not already do so
			TextureAnalyzer.init();
		}

		// begin the loader thread
		loader.start();

		// if the splash screen is enabled,
		if (Config.USE_SPLASH_SCREEN) {

			// render to it while the game loads
			while (!GameLoader.DATA_LOAD_COMPLETE) {
				SplashScreen.render();
			}

			// then close the splash screen once loading completes
			SplashScreen.close();
		}

		ModelLoader.cleanUp();
	}

	/**
	 * Update the Game instance. Polls input, calls {@code update()} on
	 * the TimeScale, then the GameStateManager, Music, and SFX objects, and
	 * finally updates the GUIManager.
	 */
	public static void update() {
		pollInput();
		// time
		TimeScale.update();
		// game components
		instance.gsm.update();
		instance.music.update();
		instance.sfx.update();

		// Engine extensions
		for (EngineExtension extension : instance.engineExtensions) {
			extension.update();
		}

		// clear the key flag
		KeyInput.KEY_PRESSED = false;

		// FPS display
		if (Settings.getSettingBoolean("show_fps")) {
			float fps = 1.0f / DisplayManager.getFrameTimeRaw();
			String fpsStr = String.format("FPS: %.2f", fps);
			instance.fpsDisplay.replaceText(fpsStr);
		}
	}

	// RENDER GAME INTERNALLY
	/**
	 * Calls the GSM's {@code render()} method, rendering all
	 * of the game's current elements to the framebuffer. Note
	 * this does NOT render to the screen; the framebuffer must
	 * be processed by the PostProcessing system first before it
	 * appears on screen.
	 */
	public static void render() {
		// core engine render call
		instance.gsm.render();
		// Engine extensions
		for (EngineExtension extension : instance.engineExtensions) {
			extension.render();
		}
	}

	// request game exit
	/**
	 * Sets the {@code closeRequest} flag, which will cause the
	 * game loop to exit on the next iteration.
	 */
	public static void quit() {
		instance.closeRequest = true;
	}

	/**
	 * Run the game loop to completion.
	 */
	public static void runGameLoop() {
		while (!(glfwWindowShouldClose(primaryWindowID) || closeRequested())) {

			DisplayManager.prepareFrame();

			// run game logic
			update();

			// clear window
			MasterRenderer.primaryWindowRenderer.prepare(true);

			// render game internally
			render();

			// render to the screen
			MasterRenderer.primaryWindowRenderer.render(true);

			// update display windows and timings
			DisplayManager.updateDisplay();
			MasterRenderer.getTargetWindow().swapBuffers();
		}
	}

	private static void pollInput() {
		// update each input method
		KeyInput.update();
		GamepadInput.update();
		Cursor.update();
		// update the Controls
		Controls.update();
	}

	// ACTIONS

	/**
	 * Play a sound effect.
	 * @param sfxID the ID of the audio file in sfx_directory.json
	 */
	public static void playSfx(String sfxID) {
		try {
			instance.sfx.playSfx(sfxID);
		}
		catch (Exception e) {
			Logger.logError("An error occurred while attempting to play Sound Effect [id=\"" + sfxID + "\"]: " + e.getClass() + "; " + e.getMessage());
		}
	}

	/**
	 * Play a sound effect on a specific channel.
	 * @param sfxID the ID of the audio file in sfx_directory.json
	 * @param channel the index of the desired channel
	 */
	public static void playSfx(String sfxID, int channel) {
		instance.sfx.playSfx(channel, sfxID);
	}

	/**
	 * Play a sound effect starting at a specified timestamp.
	 * @param sfxID the ID of the audio file in sfx_directory.json
	 * @param startTime the time in seconds to begin playing from
	 */
	public static void playSfxFrom(String sfxID, float startTime) {
		instance.sfx.playSfxFrom(sfxID, startTime);
	}

	/**
	 * Play a music track.
	 * @param musicID the ID of the audio file in music_directory.json
	 */
	public static void playMusic(String musicID) {
		instance.music.playMusic(musicID);
	}

	/**
	 * Loop a music track.
	 * @param musicID the ID of the audio file in music_directory.json
	 */
	public static void loopMusic(String musicID) {
		instance.music.loopMusic(musicID);
	}

	/**
	 * Play a music track starting at a specified timestamp.
	 * @param musicID the ID of the audio file in music_directory.json
	 * @param startTime the time in seconds to begin playing from
	 */
	public static void playMusicFrom(String musicID, float startTime) {
		int channel = instance.music.playMusicFrom(musicID, startTime);
		Logger.log("Now playing music \"" + musicID + "\" on channel #" + channel);
	}

	/**
	 * Fade the currently playing music to a new volume level. The
	 * new level can be higher or lower than the current volume, within
	 * the range of [0, 1].
	 * @param channel the desired channel to fade
	 * @param target the new target volume
	 * @param time the duration during which to perform the transition
	 */
	public static void fadeMusic(int channel, float target, float time) {
		instance.music.fadeMusic(channel, target, time);
	}

	// GET GAME COMPONENTS
	// assets
	public static Textures getTextures() { return instance.textures; }
	public static Music getMusic() { return instance.music; }
	public static Sfx getSfx() { return instance.sfx; }
	public static Fonts getFonts() { return instance.fonts; }
	public static Animations getAnimations() { return instance.animations; }
	public static Session getSession() { return instance.session; }

	/**
	 * Check if some game element has requested that the game close.
	 * @return true if the {@code quit()} method has been called
	 */
	public static boolean closeRequested() {
		return instance.closeRequest;
	}

	// SHORTCUTS
	public static GUIText getFPSDisplay() {
		return instance.fpsDisplay;
	}

	public static int centerX() {
		return MasterRenderer.getTargetWindow().centerX();
	}

	public static int centerY() {
		return MasterRenderer.getTargetWindow().centerY();
	}

	public static int width() {
		return Config.INTERNAL_WIDTH;
	}

	public static int height() {
		return Config.INTERNAL_HEIGHT;
	}

	public static Vector2f screenSize() {
		return new Vector2f(width(), height());
	}

	public static float getFrameTime() {
		return DisplayManager.getFrameTimeSeconds();
	}

	// Textures
	/**
	 * Get a textureComponent from the stored Textures.
	 * @param key the key to the component
	 * @return the corresponding {@code TextureComponent}, or {@code null} if it does not exist.
	 */
	public static TextureComponent getTexture(String key) {
		return instance.textures.getTexture(key);
	}

	/**
	 * Get an array of texture components.
	 * @param keys any number of keys to retrieve and store in the array.
	 * @return an array populated with the {@code TextureComponent}s corresponding to the given keys.
	 */
	public static TextureComponent[] getTextures(String... keys) {
		TextureComponent[] results = new TextureComponent[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			results[i] = getTexture(keys[i]);
		}
		return results;
	}

	public static TextureSet getTextureSet(String key) {
		return instance.textures.getTextureSet(key);
	}

	/**
	 * Get a texture set composed of only one TextureComponent.
	 * This is a convenience method for getting a texture set in
	 * cases where only a single, unchanging texture is needed, but
	 * a texture set object is required.
	 * @param key the key to the {@code TextureComponent} to wrap
	 * @return a {@code TextureSet} containing one {@code TextureComponent}
	 */
	public static TextureSet getStaticSet(String key) {
		return Textures.generateStaticSet(getTexture(key));
	}

	public static TextureComponent[] getTextureArray(String key) {
		return instance.textures.getTextureArray(key);
	}

	public static TextureAtlas getTextureAtlas(String key) {
		return instance.textures.getTextureAtlas(key);
	}

	// Animations
	public static Animation getAnimation(String key) {
		Animation result = instance.animations.getAnimation(key);
		if (result == null) Logger.logError(Logger.HIGH, "Animation requested does not exist: " + key);
		return result;
	}

	public static HashMap<String, Animation> getAnimationSet(String key) { return instance.animations.getAnimationSet(key); }

	// Audio
	public static Sound getMusicTrack(String key) { return instance.music.getMusic("key"); }
	public static Sound getSoundEffect(String key) { return instance.sfx.getSfx("key"); }

	// Fonts
	public static FontType getFont(String key) { return instance.fonts.getFont(key); }


	/**
	 * Clean up game components to close the game.
	 * Clears all loaded assets and terminates GLFW.
	 */
	public static void cleanUp() {
		// Clean up when done.
		Loader.cleanUp();
		MasterRenderer.cleanUp();
		TextMaster.cleanUp();
		AudioMaster.cleanUp();
		TextureOutliner.cleanUp();
		ModelLoader.cleanUp();

		// Clean up GLFW
		DisplayManager.cleanUp();
		GamepadInput.cleanUp();

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

		// save user settings/preferences and game flags
		Settings.save();

		// undo load flags
		GameLoader.LOAD_FINALIZED = false;
	}

}
