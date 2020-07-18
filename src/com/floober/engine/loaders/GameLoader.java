package com.floober.engine.loaders;

import com.floober.engine.main.Game;
import com.floober.engine.loaders.assets.*;

import java.util.ArrayList;
import java.util.List;

public class GameLoader {

	private final List<AssetLoader> loaders = new ArrayList<>();

	public GameLoader(Game game, Loader loader) {
		AssetLoader.Mode mode = AssetLoader.Mode.DIRECTORY;
		loaders.add(new TextureLoader(game, loader));
		loaders.add(new MusicLoader(game, loader));
		loaders.add(new SfxLoader(game, loader));
		loaders.add(new FontLoader(game, loader));
		// loaders for other things (animations, etc)
		// TODO implement this per project if you need more asset types to load
		// set modes
		for (AssetLoader assetLoader : loaders) {
			assetLoader.setMode(mode);
		}
	}

	public void load() {
		for (AssetLoader loader : loaders) {
			loader.load();
		}
	}

}
