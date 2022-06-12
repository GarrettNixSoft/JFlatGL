package com.floober.engine.core.assets.loaders.gameassets;

import com.floober.engine.core.assets.loaders.AssetLoader;
import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.audio.Sound;
import com.floober.engine.core.Game;
import com.floober.engine.core.util.Globals;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.file.FileUtil;

import java.io.File;

public class SfxLoader extends AssetLoader {

	public SfxLoader() {
		directory = FileUtil.getJSON("/assets/sfx_directory.json");
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
		Globals.LOAD_STAGE = GameLoader.SFX;
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// report current asset
			Globals.currentAsset = key;
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
			// done
		}
	}

	@Override
	public void finish() {
		// nothing
	}
}
