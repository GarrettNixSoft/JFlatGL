package com.floober.engine.loaders.assets;

import com.floober.engine.main.Game;
import com.floober.engine.loaders.ImageLoader;
import com.floober.engine.loaders.Loader;
import com.floober.engine.textures.Texture;
import com.floober.engine.textures.TextureAtlas;
import com.floober.engine.util.Logger;
import com.floober.engine.util.file.FileUtil;
import org.json.JSONObject;

import java.io.File;

public class TextureLoader extends AssetLoader {

	public TextureLoader(Game game,  Loader loader) {
		super(game, loader);
		directory = loader.getJSON("/assets/texture_directory.json");
	}

	@Override
	protected void loadRecursive() {
		File root = FileUtil.getFile("res/textures");
		loadFilesFromDirectory(root);
	}

	private void loadFilesFromDirectory(File folder) {
		File[] files = folder.listFiles();
		if (files == null) return;
		for (File file : files) {
			if (file.isDirectory()) {
				loadFilesFromDirectory(file);
			}
			else if (file.getName().toLowerCase().endsWith(".png")) {
				String key = file.getName().substring(0, file.getName().lastIndexOf('.'));
				Texture texture = ImageLoader.loadTexture(file.getPath());
				game.getTextures().addTexture(key, texture);
			}
		}
	}

	@Override
	protected void loadDirectory() {
		loadTextures();
		loadTextureAtlases();
	}

	private void loadTextures() {
		// get the texture directory list
		JSONObject textureDirectory = directory.getJSONObject("textures");
		// iterate over the directory's key set
		for (String key : textureDirectory.keySet()) {
			// get the associated path
			String path = textureDirectory.getString(key);
			// log the load attempt
			Logger.logLoad("Loading texture: " + path);
			// load the file at that location
			Texture texture = loader.loadTexture(path);
			// warn if it's null
			if (texture == null) Logger.logError("Texture [id=" + key + "] returned null!");
			// add it to the game
			game.getTextures().addTexture(key, texture);
			// done
		}
	}

	private void loadTextureAtlases() {
		// get the texture atlas directory list
		JSONObject textureAtlasDirectory = directory.getJSONObject("texture_atlases");
		// iterate over the directory's key set
		for (String key : textureAtlasDirectory.keySet()) {
			// get the texture atlas object
			JSONObject atlasObject = textureAtlasDirectory.getJSONObject(key);
			// get the associated path
			String path = atlasObject.getString("path");
			// get the number of rows
			int numRows = atlasObject.getInt("rows");
			// load the atlas
			Texture texture = loader.loadTexture(path);
			// warn if it's null
			if (texture == null) Logger.logError("Texture [id=" + key + "] returned null!");
			// convert it to an atlas
			assert texture != null; // shut up, compiler
			TextureAtlas textureAtlas = new TextureAtlas(texture.getId(), texture.getWidth(), texture.getHeight(), numRows);
			// add it to the game
			game.getTextures().addTextureAtlas(key, textureAtlas);
			// done
		}
	}

}
