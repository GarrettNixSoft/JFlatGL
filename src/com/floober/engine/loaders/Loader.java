package com.floober.engine.loaders;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.audio.Sound;
import com.floober.engine.fonts.fontMeshCreator.FontType;
import com.floober.engine.main.Game;
import com.floober.engine.textures.Texture;
import com.floober.engine.textures.TextureAtlas;
import com.floober.engine.util.conversion.StringConverter;
import com.floober.engine.util.file.FileUtil;
import org.json.JSONObject;

import java.util.ArrayList;

public class Loader {

	// LOAD METHODS
	public JSONObject getJSON(String file) {
		// load file data
		ArrayList<String> fileData = FileUtil.getFileData(file);
		String combined = StringConverter.combineAll(fileData);
		// create JSON parser
		return new JSONObject(combined);
	}

	public Texture loadTexture(String path) {
		path = "textures/" + path;
		return ImageLoader.loadTexture(path);
	}

	public Sound loadMusic(String path) {
		path = "music/" + path;
		return AudioMaster.loadSound(path);
	}

	public Sound loadSfx(String path) {
		path = "sfx/" + path;
		return AudioMaster.loadSound(path);
	}

	public FontType loadFont(String font) {
		String path = "fonts/" + font + "/" + font;
		int fontAtlas = loadFontAtlas(path + ".png");
		String fontFile = loadFontFile("/" + path + ".fnt");
		return new FontType(fontAtlas, fontFile);
	}

	private int loadFontAtlas(String path) {
		return ImageLoader.loadTexture(path).getId();
	}

	private String loadFontFile(String path) {
		return StringConverter.combineAll(FileUtil.getFileData(path));
	}

	// SHUTDOWN
	public void cleanUp() {
		ImageLoader.cleanUp();
	}

}