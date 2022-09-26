package com.gnix.jflatgl.core.assets.loaders.gameassets;

import com.gnix.jflatgl.core.assets.TextureAnalyzer;
import com.gnix.jflatgl.core.assets.loaders.*;
import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.assets.loaders.gameassets.temp.RawTextureArray;
import com.gnix.jflatgl.core.assets.loaders.gameassets.temp.RawTextureAtlas;
import com.gnix.jflatgl.core.assets.loaders.gameassets.temp.RawTextureComponent;
import com.gnix.jflatgl.core.assets.loaders.gameassets.temp.RawTextureSet;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.file.FileUtil;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextureLoader extends AssetLoader {

	private final List<RawTextureComponent> rawTextureComponents;
	private final List<RawTextureSet> rawTextureSets;
	private final List<RawTextureArray> rawTextureArrays;
	private final List<RawTextureAtlas> rawTextureAtlases;

	public TextureLoader() {
		directory = FileUtil.getJSON("/assets/texture_directory.json");
		rawTextureComponents = new ArrayList<>();
		rawTextureSets = new ArrayList<>();
		rawTextureArrays = new ArrayList<>();
		rawTextureAtlases = new ArrayList<>();
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
				RawTexture texture = ImageLoader.loadTexture(file.getPath());
				Game.getTextures().addTexture(key, texture.convertToOpenGLTexture());
			}
		}
	}

	@Override
	protected void loadDirectory() {
		GameLoader.texTotal =  directory.getJSONObject("textures").keySet().size() +
							directory.getJSONObject("texture_sets").keySet().size() +
							directory.getJSONObject("texture_arrays").keySet().size() +
							directory.getJSONObject("texture_atlases").keySet().size();
		GameLoader.LOAD_STAGE = GameLoader.TEXTURES;
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
			GameLoader.currentAsset = key;
			// get the associated path
			String path = textureDirectory.getString(key).replace("/", FileUtil.SEPARATOR);
			// log the load attempt
			Logger.logLoad("Loading texture: " + path);

			boolean hasTransparency;

			// check whether this texture is known
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				hasTransparency = TextureAnalyzer.knownTransparencyValue(key);
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				hasTransparency = TextureAnalyzer.findTransparencyInTexture(path, key);
			}
			// load the file at that location
			RawTexture texture = Loader.loadTexture(path);

			RawTextureComponent textureComponent = new RawTextureComponent(key, texture, hasTransparency);
			rawTextureComponents.add(textureComponent);

			// report the load count
			GameLoader.texCount++;
			// done
		}
	}

	private void loadTextureSets() {
		// get the texture set directory list
		JSONObject textureSetDirectory = directory.getJSONObject("texture_sets");
		// iterate over the directory's key set
		for (String key : textureSetDirectory.keySet()) {
			// report current asset
			GameLoader.currentAsset = key;
			// get the texture set object
			JSONObject setObject = textureSetDirectory.getJSONObject(key);
			// get the associated path
			String path = setObject.getString("path").replace("/", FileUtil.SEPARATOR);
			// pre-load the texture
			RawTexture rawTex = Loader.loadTexture(path);

			boolean hasTransparency;

			// check whether this texture set is known
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				hasTransparency = TextureAnalyzer.knownTransparencyValue(key);
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				hasTransparency = TextureAnalyzer.findTransparencyInTexture(path, key);
			}

			// get the width and height
			int width = setObject.getInt("tex_width");
			int height = setObject.optInt("tex_height", rawTex.height());

			// build the object and add it
			RawTextureSet textureSet = new RawTextureSet(key, rawTex, width, height, hasTransparency);
			rawTextureSets.add(textureSet);

			// report the load count
			GameLoader.texCount++;
			// done
		}
	}

	private void loadTextureArrays() {
		// get the texture array directory list
		JSONObject textureArrayDirectory = directory.getJSONObject("texture_arrays");
		// iterate over the directory's key set
		for (String key : textureArrayDirectory.keySet()) {
			// report current asset
			GameLoader.currentAsset = key;
			// get the texture array object
			JSONObject arrayObject = textureArrayDirectory.getJSONObject(key);
			// get the associated path
			String path = arrayObject.getString("path").replace("/", FileUtil.SEPARATOR);
			// get the width
			int width = arrayObject.getInt("width");
			// get the optional height
			int height = arrayObject.optInt("height");
			// Load the texture
			RawTexture texture = Loader.loadTexture(path);
			// It must be square
			if (texture.width() / width != texture.height() / height)
				throw new IllegalArgumentException("Texture array is not square");
			// throw an exception if the image does not evenly divide by the given dimensions
			if (texture.width() % width != 0)
				throw new IllegalArgumentException("Texture array image width does not divide evenly by given width (" + texture.width() + " / " + width + ")");
			if (texture.height() % height != 0)
				throw new IllegalArgumentException("Texture array image height does not divide evenly by given height (" + texture.height() + " / " + height + ")");
			// check whether this texture array is known
			boolean hasTransparency;
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				hasTransparency = TextureAnalyzer.knownTransparencyValue(key);
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				hasTransparency = TextureAnalyzer.findTransparencyInTexture(path, key);
			}
			// compute the number of rows
			int numRows = texture.height() / height;
			// create the atlas
			RawTextureAtlas rawTextureAtlas = new RawTextureAtlas(key, texture, numRows, hasTransparency);

			// pack it all into the array
			RawTextureArray textureArray = new RawTextureArray(key, rawTextureAtlas, width, height, hasTransparency);
			rawTextureArrays.add(textureArray);

			// report the load count
			GameLoader.texCount++;
			// done
		}
	}

	private void loadTextureAtlases() {
		// get the texture atlas directory list
		JSONObject textureAtlasDirectory = directory.getJSONObject("texture_atlases");
		// iterate over the directory's key set
		for (String key : textureAtlasDirectory.keySet()) {
			// report current asset
			GameLoader.currentAsset = key;
			// get the texture atlas object
			JSONObject atlasObject = textureAtlasDirectory.getJSONObject(key);
			// get the associated path
			String path = atlasObject.getString("path").replace("/", FileUtil.SEPARATOR);
			// get the number of rows
			int numRows = atlasObject.getInt("rows");
			// load the atlas
			RawTexture texture = Loader.loadTexture(path);

			boolean hasTransparency;

			// check whether this texture is known
			if (TextureAnalyzer.isKnown(key)) { // if it is, set its transparency value to the known one
//				Logger.log("Found data for this texture in the cache!");
				hasTransparency = TextureAnalyzer.knownTransparencyValue(key);
			}
			else { 								// otherwise, analyze it to determine whether it contains transparency
//				Logger.log("No data found, analyzing texture");
				hasTransparency = TextureAnalyzer.findTransparencyInTexture(path, key);
			}

			// convert it to an atlas
			RawTextureAtlas textureAtlas = new RawTextureAtlas(key, texture, numRows, hasTransparency);
			rawTextureAtlases.add(textureAtlas);

			// report the load count
			GameLoader.texCount++;
			// done
		}
	}

	public void convertAndAddAll() {
		convertTextures();
		convertTextureSets();
		convertTextureArrays();
		convertTextureAtlases();
	}

	private void convertTextures() {
		for (RawTextureComponent textureComponent : rawTextureComponents) {
			textureComponent.addToGame();
		}
	}

	private void convertTextureSets() {
		for (RawTextureSet textureSet : rawTextureSets) {
			textureSet.addToGame();
		}
	}

	private void convertTextureArrays() {
		for (RawTextureArray textureArray : rawTextureArrays) {
			textureArray.addToGame();
		}
	}

	private void convertTextureAtlases() {
		for (RawTextureAtlas textureAtlas : rawTextureAtlases) {
			textureAtlas.addToGame();
		}
	}

	@Override
	public void finish() {
		convertAndAddAll();
	}

}
