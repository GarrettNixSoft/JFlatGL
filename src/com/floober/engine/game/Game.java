package com.floober.engine.game;

import com.floober.engine.animation.Animation;
import com.floober.engine.assets.*;
import com.floober.engine.audio.Sound;
import com.floober.engine.entity.core.player.NullPlayer;
import com.floober.engine.entity.core.player.Player;
import com.floober.engine.entity.effects.EffectsHandler;
import com.floober.engine.game.gameState.GameStateManager;
import com.floober.engine.game.gameState.states.PlayState;
import com.floober.engine.loaders.GameLoader;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.renderEngine.textures.TextureAtlas;
import com.floober.engine.renderEngine.textures.TextureComponent;
import com.floober.engine.renderEngine.textures.TextureSet;
import com.floober.engine.util.Logger;
import com.floober.engine.util.Session;
import com.floober.engine.util.configuration.Config;

import java.util.HashMap;

public class Game {

	// global access to the game
	public static final Game instance = new Game();

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

	public static final int PLAY_AREA_TOP = 100;
	public static final int PLAY_AREA_BOTTOM = Config.DEFAULT_HEIGHT - 100;
	public static final int PLAY_AREA_HEIGHT = Config.DEFAULT_HEIGHT;

	public Game() {
		textures = new Textures();
		music = new Music();
		sfx = new Sfx();
		fonts = new Fonts();
		animations = new Animations();
		session = new Session();
	}

	public static void init() {
		instance.load();
		instance.gsm = new GameStateManager(instance);
	}

	private void load() {
		GameLoader gameLoader = new GameLoader(this);
		gameLoader.load();
	}

	// RUN GAME LOGIC
	public static void update() {
		instance.gsm.update();
		instance.music.update();
		instance.sfx.update();
	}

	// RENDER GAME INTERNALLY
	public static void render() {
		instance.gsm.render();
	}

	// request game exit
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
	public static TextureComponent getTexture(String key) {
		return instance.textures.getTexture(key);
	}

	public static TextureComponent[] getTextures(String... keys) {
		TextureComponent[] results = new TextureComponent[keys.length];
		for (int i = 0; i < keys.length; ++i) {
			results[i] = instance.textures.getTexture(keys[i]);
		}
		return results;
	}

	public static TextureSet getTextureSet(String key) {
		return instance.textures.getTextureSet(key);
	}

	public static TextureSet getStaticSet(String key) {
		return Textures.generateStaticSet(getTexture(key));
	}

	public static Texture[] getTextureArray(String key) {
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