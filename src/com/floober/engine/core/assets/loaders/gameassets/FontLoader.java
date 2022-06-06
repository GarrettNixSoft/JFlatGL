package com.floober.engine.core.assets.loaders.gameassets;

import com.floober.engine.core.assets.loaders.AssetLoader;
import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.renderers.LoadRenderer;
import com.floober.engine.core.util.Globals;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.file.FileUtil;
import com.floober.engine.test.RunGame;

public class FontLoader extends AssetLoader {

	public FontLoader() {
		directory = FileUtil.getJSON("/assets/fonts_directory.json");
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
			if (Config.USE_LOAD_RENDERER) RunGame.loadRenderer.render();
			// done
		}
	}
}
