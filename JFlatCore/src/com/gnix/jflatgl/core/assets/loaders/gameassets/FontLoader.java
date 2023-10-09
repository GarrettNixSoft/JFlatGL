package com.gnix.jflatgl.core.assets.loaders.gameassets;

import com.gnix.jflatgl.core.assets.loaders.AssetLoader;
import com.gnix.jflatgl.core.assets.loaders.GameLoader;
import com.gnix.jflatgl.core.assets.loaders.Loader;
import com.gnix.jflatgl.core.assets.loaders.gameassets.temp.RawFontType;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.file.FileUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FontLoader extends AssetLoader {

	private final List<RawFontType> rawFontTypes;

	public FontLoader() {
		directory = FileUtil.getOrCreateResourceDataJSON("/assets/fonts_directory.json", new JSONObject());
		rawFontTypes = new ArrayList<>();
	}

	@Override
	protected void loadRecursive() {
		loadDirectory(); // I'm not implementing this in recursive mode.
	}

	@Override
	protected void loadDirectory() {
		GameLoader.fontTotal = directory.keySet().size();
		GameLoader.LOAD_STAGE = GameLoader.FONTS;
		// iterate over the directory's key set
		for (String key : directory.keySet()) {
			// report current asset
			GameLoader.currentAsset = key;
			// get the path for this file
			String path = directory.getString(key);
			// Report the load attempt
			Logger.logLoad("Loading font: " + path);
			// load this sound file
			RawFontType font = Loader.loadFont(path, key);
			rawFontTypes.add(font);
			// report the load count
			GameLoader.fontCount++;
			// done
		}
	}

	public void convertAndAddAll() {
		for (RawFontType fontType : rawFontTypes) {
			fontType.addToGame();
		}
	}

	@Override
	public void finish() {
		convertAndAddAll();
	}
}
