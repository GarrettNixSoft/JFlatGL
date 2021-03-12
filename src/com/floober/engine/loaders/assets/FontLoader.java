package com.floober.engine.loaders.assets;

import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.renderEngine.renderers.LoadRenderer;
import com.floober.engine.util.Globals;
import com.floober.engine.util.Logger;

public class FontLoader extends AssetLoader {

	public FontLoader() {
		super();
		directory = Loader.getJSON("/assets/fonts_directory.json");
	}

	@Override
	protected void loadRecursive() {
		loadDirectory(); // I'm not implementing this in recursive mode.
	}

	@Override
	protected void loadDirectory() {
		Globals.fontTotal = directory.keySet().size();
		Globals.LOAD_STAGE = LoadRenderer.FONTS;
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// report current asset
			LoadRenderer.reportCurrentAsset(key);
			// get the path for this file
			String path = directory.getString(key);
			// Report the load attempt
			Logger.logLoad("Loading font: " + path);
			// load this sound file
			FontType font = Loader.loadFont(path);
			// add it to the game
			Game.getFonts().addFont(key, font);
			// report the load count
			Globals.fontCount++;
			// render the loading screen
//			RunGame.loadRenderer.render();
			// done
		}
	}
}
