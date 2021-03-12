package com.floober.engine.loaders.assets;

import com.floober.engine.audio.Sound;
import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.renderers.LoadRenderer;
import com.floober.engine.util.Globals;
import com.floober.engine.util.Logger;

import java.io.File;

public class MusicLoader extends AssetLoader {

	public MusicLoader() {
		super();
		directory = Loader.getJSON("/assets/music_directory.json");
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
//			RunGame.loadRenderer.render();
			// done
		}
	}

}
