package com.gnix.jflatgl.core.assets.loaders.gameassets;

import com.gnix.jflatgl.core.assets.loaders.AssetLoader;
import com.gnix.jflatgl.core.assets.loaders.GameLoader;
import com.gnix.jflatgl.core.assets.loaders.Loader;
import com.gnix.jflatgl.core.audio.Sound;
import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.file.FileUtil;

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
		GameLoader.musicTotal = directory.keySet().size();
		GameLoader.LOAD_STAGE = GameLoader.MUSIC;
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// report current asset
			GameLoader.currentAsset = key;
			// get the path for this file
			String path = directory.getString(key);
			// Report the load attempt
			Logger.logLoad("Loading music: " + path);
			// load this sound file
			Sound sound = Loader.loadMusic(path);
			// add it to the game
			Game.getMusic().addMusic(key, sound);
			// report the load count
			GameLoader.musicCount++;
			// done
		}
	}

	@Override
	public void finish() {
		// nothing
	}

}
