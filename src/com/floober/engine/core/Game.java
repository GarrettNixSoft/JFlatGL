package com.floober.engine.core;

import com.floober.engine.animation.Animation;
import com.floober.engine.core.assets.*;
import com.floober.engine.core.assets.loaders.AssetLoader;
import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.audio.Sound;
import com.floober.engine.core.gameState.GameStateManager;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.models.ModelLoader;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.textures.TextureOutliner;
import com.floober.engine.core.splash.SplashRenderer;
import com.floober.engine.core.splash.SplashScreen;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.time.TimeScale;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.textures.TextureAtlas;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.textures.TextureSet;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.Session;
import com.floober.engine.gui.GUIManager;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Objects;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.glfw.GLFW.*;

/**
 * This class represents the Game itself. It provides convenience
 * functions for fetching all assets (textures, audio, etc.) and
 * for triggering audio events (playing music or sfx).
 */

public class Game {

	// global access to the game
	public static Game instance;

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

	public static void loadConfig() {
		// Load user preferences/settings and game flags
		Config.load();
		Settings.load();
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
		TextMaster.init();

		// Next, prepare the GSM
		instance.gsm.init();

		// load particles AFTER textures are loaded (some particles need to load textures from the game's pool)
		ParticleMaster.initGlobals();

		// Init FPS display
		instance.fpsDisplay = new GUIText("FPS: ", 0.5f, Game.getFont("default"), new Vector3f(0, 0, 0), 1, false);
		instance.fpsDisplay.setColor(Colors.GREEN);
		instance.fpsDisplay.setWidth(0.5f);
		instance.fpsDisplay.setEdge(0.2f);

		if (Settings.getSettingBoolean("show_fps"))
			instance.fpsDisplay.show();

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
		// GUI components
		GUIManager.update();
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
		instance.gsm.render();
		GUIManager.render();
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
		KeyInput.update();
		MouseInput.update();
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

	public static float centerX() {
		return MasterRenderer.getTargetWindow().centerX();
	}

	public static float centerY() {
		return MasterRenderer.getTargetWindow().centerY();
	}

	public static int width() {
		return Config.INTERNAL_WIDTH;
	}

	public static int height() {
		return Config.INTERNAL_HEIGHT;
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
		if (result == null) Logger.logError("Animation requested does not exist: " + key, Logger.HIGH);
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

		// Clean up GLFW
		DisplayManager.cleanUp();

		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free(); // shut up, compiler

		// save user settings/preferences and game flags
		Settings.save();
	}

}