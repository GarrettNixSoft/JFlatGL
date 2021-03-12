package com.floober.engine.loaders.assets;

import com.floober.engine.audio.Sound;
import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.renderers.LoadRenderer;
import com.floober.engine.util.Globals;
import com.floober.engine.util.Logger;

import java.io.File;

public class SfxLoader extends AssetLoader {

	public SfxLoader() {
		super();
		directory = Loader.getJSON("/assets/sfx_directory.json");
	}

	@Override
	protected void loadRecursive() {
		File root = new File("res/sfx");
		loadFilesFromDirectory(root);
	}

	private void loadFilesFromDirectory(File folder) {
		File[] files = folder.listFiles();
		if (files == null) return;
		for (File file : files) {
			if (file.isDirectory()) {
				loadFilesFromDirectory(file);
			}
			else if (file.getName().toLowerCase().endsWith(".wav")) {
				String key = file.getName().substring(0, file.getName().lastIndexOf('.'));
				Sound sound = Loader.loadMusic(file.getPath());
				Game.getMusic().addMusic(key, sound);
			}
		}
	}

	@Override
	protected void loadDirectory() {
		Globals.sfxTotal = directory.keySet().size();
		Globals.LOAD_STAGE = LoadRenderer.SFX;
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// report current asset
			LoadRenderer.reportCurrentAsset(key);
			// get the path for this file
			String path = directory.getString(key);
			// Report the load attempt
			Logger.logLoad("Loading sfx: " + path);
			// load this sound file
			Sound sound = Loader.loadSfx(path);
			// add it to the game
			Game.getSfx().addSfx(key, sound);
			// report load count
			Globals.sfxCount++;
			// render load screen
//			RunGame.loadRenderer.render();
			// done
		}
	}
}
