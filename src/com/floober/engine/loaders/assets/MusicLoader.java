package com.floober.engine.loaders.assets;

import com.floober.engine.audio.Sound;
import com.floober.engine.main.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.util.Logger;

import java.io.File;

public class MusicLoader extends AssetLoader {

	public MusicLoader(Game game,  Loader loader) {
		super(game, loader);
		directory = loader.getJSON("/assets/music_directory.json");
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
				Sound sound = loader.loadMusic(file.getPath());
				game.getMusic().addMusic(key, sound);
			}
		}
	}

	@Override
	protected void loadDirectory() {
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// get the path for this file
			String path = directory.getString(key);
			// Report the load attempt
			Logger.logLoad("Loading music: " + path);
			// load this sound file
			Sound sound = loader.loadMusic(path);
			// add it to the game
			game.getMusic().addMusic(key, sound);
			// done
		}
	}

}
