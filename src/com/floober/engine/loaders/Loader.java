package com.floober.engine.loaders;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.audio.Sound;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.util.Logger;
import com.floober.engine.util.conversion.ImageConverter;
import com.floober.engine.util.conversion.StringConverter;
import com.floober.engine.util.file.FileUtil;
import com.floober.engine.util.file.ResourceLoader;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Loader {

	// LOAD METHODS
	public static JSONObject getJSON(String file) {
		// load file data
		ArrayList<String> fileData = FileUtil.getFileData(file);
		String combined = StringConverter.combineAll(fileData);
		// create JSON parser
		return new JSONObject(combined);
	}

	public static JSONObject getJSON(File file) {
		// load file data
		ArrayList<String> fileData = FileUtil.getFileData(file);
		String combined = StringConverter.combineAll(fileData);
		// create JSON parser
		return new JSONObject(combined);
	}

	public static String[] getFileRaw(String path) {
		try {
			InputStream in = ResourceLoader.getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			List<String> lines = new ArrayList<>();
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			return lines.toArray(new String[0]);
		} catch (Exception e) {
			Logger.logError(e.toString());
			return null;
		}
	}

	public static Texture loadTexture(String path) {
		path = "textures/" + path;
		return ImageLoader.loadTexture(path);
	}

	public static Texture[] loadTextureArray(String path, int width) {
		path = "textures/" + path;
		BufferedImage bufferedImage = ImageLoader.loadBufferedImage(path);
		return ImageConverter.convertToTextureArray(bufferedImage, width);
	}

	public static Texture[] loadTextureArray(String path, int width, int height) {
		path = "textures/" + path;
		BufferedImage bufferedImage = ImageLoader.loadBufferedImage(path);
		return ImageConverter.convertToTextureArray(bufferedImage, width, height);
	}

	public static Sound loadMusic(String path) {
		path = "music/" + path;
		return AudioMaster.loadSound(path);
	}

	public static Sound loadSfx(String path) {
		path = "sfx/" + path;
		return AudioMaster.loadSound(path);
	}

	public static FontType loadFont(String font) {
		String path = "fonts/" + font + "/" + font;
		int fontAtlas = loadFontAtlas(path + ".png");
		String fontFile = loadFontFile("/" + path + ".fnt");
		return new FontType(fontAtlas, fontFile);
	}

	private static int loadFontAtlas(String path) {
		return ImageLoader.loadTexture(path).id();
	}

	private static String loadFontFile(String path) {
		return StringConverter.combineAll(FileUtil.getFileData(path));
	}

	// SHUTDOWN
	public static void cleanUp() {
		ImageLoader.cleanUp();
	}

}