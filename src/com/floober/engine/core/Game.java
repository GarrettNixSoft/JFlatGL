package com.floober.engine.core;

import com.floober.engine.animation.Animation;
import com.floober.engine.core.assets.*;
import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.audio.Sound;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.gametitle.gameState.GameStateManager;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.textures.TextureAtlas;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.textures.TextureSet;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.Session;

import java.util.HashMap;

/**
 * This class represents the Game itself. It provides convenience
 * functions for fetching all assets (textures, audio, etc.) and
 * for triggering audio events (playing music or sfx).
 */

public class Game {

	// global access to the game
	public static Game instance;

	// game components
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

	/**
	 * Create the game. This will initialize all
	 * asset containers.
	 */
	public Game() {
		textures = new Textures();
		music = new Music();
		sfx = new Sfx();
		fonts = new Fonts();
		animations = new Animations();
		session = new Session();
	}

	/**
	 * Calling this method will load all game assets
	 * specified in the JSON directories contained in
	 * resourceData/assets. Once loading is complete,
	 * the GameStateManager will be initialized.
	 */
	public static void init() {
		// Create the window and set up OpenGL and GLFW.
		DisplayManager.initPrimaryGameWindow();
		// Set up OpenAL.
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		// master components
		TextMaster.init();
		TextureAnalyzer.init();
		// initialize the instance
		instance = new Game();
		// the game itself (load assets)
		instance.load();
		instance.gsm = new GameStateManager(instance);
		// load particles AFTER textures are loaded (some particles need to load textures from the game's pool)
		ParticleMaster.initGlobals();
	}

	/**
	 * Load the game. Run on the game's instance.
	 */
	private void load() {
		GameLoader gameLoader = new GameLoader(this);
		gameLoader.load();
	}

	// RUN GAME LOGIC

	/**
	 * Update the Game instance. Calls {@code update()} on
	 * the GameStateManager, Music, and SFX objects.
	 */
	public static void update() {
		instance.gsm.update();
		instance.music.update();
		instance.sfx.update();
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
	}

	// request game exit
	/**
	 * Sets the {@code closeRequest} flag, which will cause the
	 * game loop to exit on the next iteration.
	 */
	public static void quit() {
		instance.closeRequest = true;
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

}