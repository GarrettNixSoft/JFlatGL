package com.floober.engine.loaders.assets;

import com.floober.engine.game.Game;
import com.floober.engine.game.RunGame;
import com.floober.engine.loaders.ImageLoader;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.renderers.LoadRenderer;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.renderEngine.textures.TextureAtlas;
import com.floober.engine.util.Globals;
import com.floober.engine.util.Logger;
import com.floober.engine.util.file.FileUtil;
import org.json.JSONObject;

import java.io.File;

public class TextureLoader extends AssetLoader {

	public TextureLoader() {
		super();
		directory = Loader.getJSON("/assets/texture_directory.json");
	}

	@Override
	protected void loadRecursive() {
		File root = FileUtil.getFile("res/textures");
		loadFilesFromFolder(root);
	}

	private void loadFilesFromFolder(File folder) {
		File[] files = folder.listFiles();
		if (files == null) return;
		for (File file : files) {
			if (file.isDirectory()) {
				loadFilesFromFolder(file);
			}
			else if (file.getName().toLowerCase().endsWith(".png")) {
				String key = file.getName().substring(0, file.getName().lastIndexOf('.'));
				Texture texture = ImageLoader.loadTexture(file.getPath());
				Game.getTextures().addTexture(key, texture);
			}
		}
	}

	@Override
	protected void loadDirectory() {
		Globals.texTotal = directory.getJSONObject("textures").keySet().size() + directory.getJSONObject("texture_arrays").keySet().size() + directory.getJSONObject("texture_atlases").keySet().size();
		Globals.LOAD_STAGE = LoadRenderer.TEXTURES;
		loadTextures();
		loadTextureArrays();
		loadTextureAtlases();
	}

	private void loadTextures() {
		// get the texture directory list
		JSONObject textureDirectory = directory.getJSONObject("textures");
		// iterate over the directory's key set
		for (String key : textureDirectory.keySet()) {
			// report current asset
			LoadRenderer.reportCurrentAsset(key);
			// get the associated path
			String path = textureDirectory.getString(key);
			// log the load attempt
			Logger.logLoad("Loading texture: " + path);
			// load the file at that location
			Texture texture = Loader.loadTexture(path);
			// add it to the game
			Game.getTextures().addTexture(key, texture);
			// report the load count
			Globals.texCount++;
			// render the load screen
//			RunGame.loadRenderer.render();
			// done
		}
	}

	private void loadTextureArrays() {
		// get the texture array directory list
		JSONObject textureArrayDirectory = directory.getJSONObject("texture_arrays");
		// iterate over the directory's key set
		for (String key : textureArrayDirectory.keySet()) {
			// report current asset
			LoadRenderer.reportCurrentAsset(key);
			// get the texture array object
			JSONObject arrayObject = textureArrayDirectory.getJSONObject(key);
			// get the associated path
			String path = arrayObject.getString("path");
			// get the width
			int width = arrayObject.getInt("width");
			// get the optional height
			int height = arrayObject.optInt("height");
			// load the array based on whether the height key exists
			Texture[] textureArray;
			if (height != 0)
				textureArray = Loader.loadTextureArray(path, width, height);
			else
				textureArray = Loader.loadTextureArray(path, width);
			// add it to the game
			Game.getTextures().addTextureArray(key, textureArray);
			// report the load count
			Globals.texCount++;
			// render the loading screen
//			RunGame.loadRenderer.render();
			// done
		}
	}

	private void loadTextureAtlases() {
		// get the texture atlas directory list
		JSONObject textureAtlasDirectory = directory.getJSONObject("texture_atlases");
		// iterate over the directory's key set
		for (String key : textureAtlasDirectory.keySet()) {
			// report current asset
			LoadRenderer.reportCurrentAsset(key);
			// get the texture atlas object
			JSONObject atlasObject = textureAtlasDirectory.getJSONObject(key);
			// get the associated path
			String path = atlasObject.getString("path");
			// get the number of rows
			int numRows = atlasObject.getInt("rows");
			// load the atlas
			Texture texture = Loader.loadTexture(path);
			// convert it to an atlas
			TextureAtlas textureAtlas = new TextureAtlas(texture.id(), texture.width(), texture.height(), numRows);
			// add it to the game
			Game.getTextures().addTextureAtlas(key, textureAtlas);
			// report the load count
			Globals.texCount++;
			// render the load screen
//			RunGame.loadRenderer.render();
			// done
		}
	}

}
