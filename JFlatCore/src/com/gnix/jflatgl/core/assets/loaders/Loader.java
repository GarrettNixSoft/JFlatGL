package com.gnix.jflatgl.core.assets.loaders;

import com.gnix.jflatgl.core.assets.loaders.gameassets.temp.RawFontType;
import com.gnix.jflatgl.core.assets.loaders.gameassets.temp.RawTextureAtlas;
import com.gnix.jflatgl.core.audio.AudioMaster;
import com.gnix.jflatgl.core.audio.Sound;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.configuration.Config;
import com.gnix.jflatgl.core.util.conversion.ImageConverter;
import com.gnix.jflatgl.core.util.conversion.StringConverter;
import com.gnix.jflatgl.core.util.file.FileUtil;
import com.gnix.jflatgl.core.util.file.ResourceLoader;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import static com.gnix.jflatgl.core.util.file.FileUtil.SEPARATOR;

public class Loader {

	public static RawTexture loadTexture(String path) {
		path = "textures" + SEPARATOR + path;
		return ImageLoader.loadTexture(path);
	}

	public static TextureComponent loadTextureConverted(String path) {
		path = "textures" + SEPARATOR + path;
//		Logger.log("Loaded raw texture: " + path);
		return ImageLoader.loadTexture(path).convertToOpenGLTexture();
	}

	public static RawTexture[] loadTextureArray(String path, int width) {
		path = "textures" + SEPARATOR + path;
		BufferedImage bufferedImage = ImageLoader.loadBufferedImage(path);
		return ImageConverter.convertToTextureArray(bufferedImage, width);
	}

	public static RawTexture[] loadTextureArray(String path, int width, int height) {
		path = "textures" + SEPARATOR + path;
		BufferedImage bufferedImage = ImageLoader.loadBufferedImage(path);
		return ImageConverter.convertToTextureArray(bufferedImage, width, height);
	}

	public static Sound loadMusic(String path) {
		path = "res" + SEPARATOR + "music" + SEPARATOR + path;
		return AudioMaster.loadSound(path);
	}

	public static Sound loadSfx(String path) {
		path = "res" + SEPARATOR + "sfx" + SEPARATOR + path;
		return AudioMaster.loadSound(path);
	}

	public static FontType loadFontConverted(String font) {
		return loadFont(font, "").convert();
	}

	public static FontType loadFontConverted(String font, double aspectRatio) {
		return loadFont(font, "", aspectRatio).convert();
	}

	public static RawFontType loadFont(String font, String key) {
		String path = "fonts" + SEPARATOR + font + SEPARATOR + font;
		RawTexture rawTexture = ImageLoader.loadTexture(path + ".png");
//		Logger.log("Loaded raw texture: " + path + ".png");
		RawTextureAtlas fontAtlas = new RawTextureAtlas(key, rawTexture, 1, true);
		String fontFile = loadFontFile(SEPARATOR + path + ".fnt");
		return new RawFontType(key, fontAtlas, fontFile, (double) Config.INTERNAL_WIDTH / Config.INTERNAL_HEIGHT);
	}

	public static RawFontType loadFont(String font, String key, double aspectRatio) {
		String path = "fonts" + SEPARATOR + font + SEPARATOR + font;
		RawTexture rawTexture = ImageLoader.loadTexture(path + ".png");
//		Logger.log("Loaded raw texture: " + path + ".png");
		RawTextureAtlas fontAtlas = new RawTextureAtlas(key, rawTexture, 1, true);
		String fontFile = loadFontFile(SEPARATOR + path + ".fnt");
		return new RawFontType(key, fontAtlas, fontFile, aspectRatio);
	}

	private static String loadFontFile(String path) {
		return StringConverter.combineAll(FileUtil.getResDataFile(path));
	}

	// SHUTDOWN
	public static void cleanUp() {
		ImageLoader.cleanUp();
	}

	/**
	 * A utility function to help the game to retrieve InputStreams in both
	 * the development environment and when exported. It will attempt to load
	 * from an exported environment first, to preserve performance when the
	 * game is deployed; if that attempt fails, it will assume the game is
	 * being run in development (an IDE) and try that load method.
	 * @param path The path of the file to load.
	 * @return An InputStream associated with the path, or null if both attempts fail.
	 */
	public static InputStream tryGetInputStream(String path) {
		// declare the input stream
		InputStream in = null;
		// open the stream; how this executes depends on whether the game is running in-IDE or as an exported JAR
		try {
			in = ResourceLoader.getResourceAsStream(path); // exported game
		}
		catch (Exception e1) {
			try {
				Logger.logLoadError("Export-load failed, trying in-development load...");
				in = ResourceLoader.getResourceAsStream("res" + SEPARATOR + path); // in development
			} catch (Exception e2) {
				Logger.logLoadError("In-development load failed. Resource could not be found.");
			}
		}
		Logger.logLoad("Load success!");
		return in;
	}
}
