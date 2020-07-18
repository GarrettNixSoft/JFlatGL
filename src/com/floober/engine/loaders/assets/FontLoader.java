package com.floober.engine.loaders.assets;

import com.floober.engine.fonts.fontMeshCreator.FontType;
import com.floober.engine.main.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.util.Logger;

public class FontLoader extends AssetLoader {

	public FontLoader(Game game, Loader loader) {
		super(game, loader);
		directory = loader.getJSON("/assets/fonts_directory.json");
	}

	@Override
	protected void loadRecursive() {
		loadDirectory(); // I'm not implementing this in recursive mode.
	}

	@Override
	protected void loadDirectory() {
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// get the path for this file
			String path = directory.getString(key);
			// Report the load attempt
			Logger.logLoad("Loading font: " + path);
			// load this sound file
			FontType font = loader.loadFont(path);
			// add it to the game
			game.getFonts().addFont(key, font);
			// done
		}
	}
}
