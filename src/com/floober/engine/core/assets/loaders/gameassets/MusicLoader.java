package com.floober.engine.core.assets.loaders.gameassets;

import com.floober.engine.core.assets.loaders.AssetLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.audio.Sound;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.renderers.LoadRenderer;
import com.floober.engine.core.util.Globals;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.file.FileUtil;
import com.floober.engine.test.RunGame;

import java.io.File;

public class MusicLoader extends AssetLoader {

	public MusicLoader() {
		directory = FileUtil.getJSON("/assets/music_directory.json");
	}

	@Override
	protected void loadRecursive() {
		File root = new File("res/music");
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
		Globals.musicTotal = directory.keySet().size();
		Globals.LOAD_STAGE = LoadRenderer.MUSIC;
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// report current asset
			LoadRenderer.reportCurrentAsset(key);
			// get the path for this file
			String path = directory.getString(key);
			// Report the load attempt
			Logger.logLoad("Loading music: " + path);
			// load this sound file
			Sound sound = Loader.loadMusic(path);
			// add it to the game
			Game.getMusic().addMusic(key, sound);
			// report the load count
			Globals.musicCount++;
			// render the load screen
			if (Config.USE_LOAD_RENDERER) RunGame.loadRenderer.render();
			// done
		}
	}

}
