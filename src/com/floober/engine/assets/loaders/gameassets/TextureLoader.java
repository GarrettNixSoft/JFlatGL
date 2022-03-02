package com.floober.engine.assets.loaders.gameassets;

import com.floober.engine.assets.TextureAnalyzer;
import com.floober.engine.assets.loaders.AssetLoader;
import com.floober.engine.assets.loaders.ImageLoader;
import com.floober.engine.assets.loaders.Loader;
import com.floober.engine.game.Game;
import com.floober.engine.renderEngine.renderers.LoadRenderer;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.renderEngine.textures.TextureAtlas;
import com.floober.engine.renderEngine.textures.TextureComponent;
import com.floober.engine.renderEngine.textures.TextureSet;
import com.floober.engine.util.Globals;
import com.floober.engine.util.Logger;
import com.floober.engine.util.file.FileUtil;
import org.json.JSONObject;

import java.io.File;

public class TextureLoader extends AssetLoader {

	public TextureLoader() {
		directory = FileUtil.getJSON("/assets/texture_directory.json");
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
				TextureComponent texture = ImageLoader.loadTexture(file.getPath());
				Game.getTextures().addTexture(key, texture);
			}
		}
	}

	@Override
	protected void loadDirectory() {
		Globals.texTotal =  directory.getJSONObject("textures").keySet().size() +
							directory.getJSONObject("texture_sets").keySet().size() +
							directory.getJSONObject("texture_arrays").keySet().size() +
							directory.getJSONObject("texture_atlases").keySet().size();
		Globals.LOAD_STAGE = LoadRenderer.TEXTURES;
		loadTextures();
		loadTextureSets();
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
			String path = textureDirectory.getString(key).replace("/", FileUtil.SEPARATOR);
			// log the load attempt
			Logger.logLoad("Loading texture: " + path);
			// load the file at that location
			TextureComponent texture = Loader.loadTexture(path);

			// check whether this texture is known
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				texture.setHasTransparency(TextureAnalyzer.knownTransparencyValue(key));
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				texture.setHasTransparency(TextureAnalyzer.findTransparencyInTexture(path, key));
			}

			// add it to the game
			Game.getTextures().addTexture(key, texture);
			// report the load count
			Globals.texCount++;
			// render the load screen
//			RunGame.loadRenderer.render();
			// done
		}
	}

	private void loadTextureSets() {
		// get the texture set directory list
		JSONObject textureSetDirectory = directory.getJSONObject("texture_sets");
		// iterate over the directory's key set
		for (String key : textureSetDirectory.keySet()) {
			// report current asset
			LoadRenderer.reportCurrentAsset(key);
			// get the texture set object
			JSONObject setObject = textureSetDirectory.getJSONObject(key);
			// get the associated path
			String path = setObject.getString("path").replace("/", FileUtil.SEPARATOR);
			// pre-load the texture
			TextureComponent rawTex = Loader.loadTexture(path);

			// check whether this texture set is known
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				rawTex.setHasTransparency(TextureAnalyzer.knownTransparencyValue(key));
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				rawTex.setHasTransparency(TextureAnalyzer.findTransparencyInTexture(path, key));
			}

			// get the width and height
			int width = setObject.getInt("tex_width");
			int height = setObject.optInt("tex_height", rawTex.height());
			// get optional transparency flag
			boolean hasTransparency = setObject.optBoolean("transparent", false);
			// build the object and add it
			TextureSet textureSet = new TextureSet(rawTex, width, height, rawTex.hasTransparency());
			Game.getTextures().addTextureSet(key, textureSet);
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
			String path = arrayObject.getString("path").replace("/", FileUtil.SEPARATOR);
			// get the width
			int width = arrayObject.getInt("width");
			// get the optional height
			int height = arrayObject.optInt("height");
			// load the array based on whether the height key exists
			TextureComponent[] textureArray;
			if (height != 0)
				textureArray = Loader.loadTextureArray(path, width, height);
			else
				textureArray = Loader.loadTextureArray(path, width);

			// check whether this texture array is known
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				boolean transparency = TextureAnalyzer.knownTransparencyValue(key);
				for (TextureComponent component : textureArray) component.setHasTransparency(transparency);
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				boolean transparency = TextureAnalyzer.findTransparencyInTexture(path, key);
				for (TextureComponent component : textureArray) component.setHasTransparency(transparency);
			}


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
			String path = atlasObject.getString("path").replace("/", FileUtil.SEPARATOR);
			// get the number of rows
			int numRows = atlasObject.getInt("rows");
			// load the atlas
			TextureComponent texture = Loader.loadTexture(path);

			// check whether this texture is known
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				texture.setHasTransparency(TextureAnalyzer.knownTransparencyValue(key));
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				texture.setHasTransparency(TextureAnalyzer.findTransparencyInTexture(path, key));
			}

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
