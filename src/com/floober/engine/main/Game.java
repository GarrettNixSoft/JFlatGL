package com.floober.engine.main;

import com.floober.engine.assets.Fonts;
import com.floober.engine.assets.Music;
import com.floober.engine.assets.Sfx;
import com.floober.engine.assets.Textures;
import com.floober.engine.audio.Sound;
import com.floober.engine.fonts.fontMeshCreator.FontType;
import com.floober.engine.gameState.GameStateManager;
import com.floober.engine.loaders.GameLoader;
import com.floober.engine.loaders.Loader;
import com.floober.engine.loaders.assets.*;
import com.floober.engine.textures.Texture;
import com.floober.engine.textures.TextureAtlas;

public class Game {

	// game components
	private final Textures textures;
	private final Music music;
	private final Sfx sfx;
	private final Fonts fonts;

	// handling game states
	private final GameStateManager gsm;

	public Game() {
		textures = new Textures();
		music = new Music();
		sfx = new Sfx();
		fonts = new Fonts();
		gsm = new GameStateManager(this);
	}

	public void init(Loader loader) {
		load(loader);
	}

	private void load(Loader loader) {
		GameLoader gameLoader = new GameLoader(this, loader);
		gameLoader.load();
	}

	// RUN GAME LOGIC
	public void update() {
		gsm.update();
	}

	// RENDER GAME INTERNALLY
	public void render() {
		gsm.render();
	}

	// GET GAME COMPONENTS
	// assets
	public Textures getTextures() { return textures; }
	public Music getMusic() { return music; }
	public Sfx getSfx() { return sfx; }
	public Fonts getFonts() { return fonts; }
	// ...
	// game control flow
	public GameStateManager getGSM() { return gsm; }

	// SHORTCUTS
	public Texture getTexture(String key) { return textures.getTexture(key); }
	public TextureAtlas getTextureAtlas(String key) { return textures.getTextureAtlas(key); }
	public Sound getMusicTrack(String key) { return music.getMusic("key"); }
	public Sound getSoundEffect(String key) { return sfx.getSfx("key"); }
	public FontType getFont(String key) { return fonts.getFont(key); }

}