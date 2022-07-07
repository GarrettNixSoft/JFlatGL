package com.floober.engine.core.assets.loaders;

import com.floober.engine.core.assets.loaders.gameassets.temp.RawFontType;
import com.floober.engine.core.assets.loaders.gameassets.temp.RawTextureAtlas;
import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.audio.Sound;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.conversion.ImageConverter;
import com.floober.engine.core.util.conversion.StringConverter;
import com.floober.engine.core.util.file.FileUtil;

import java.awt.image.BufferedImage;

import static com.floober.engine.core.util.file.FileUtil.SEPARATOR;

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

	public static RawFontType loadFont(String font, String key) {
		String path = "fonts" + SEPARATOR + font + SEPARATOR + font;
		RawTexture rawTexture = ImageLoader.loadTexture(path + ".png");
//		Logger.log("Loaded raw texture: " + path + ".png");
		RawTextureAtlas fontAtlas = new RawTextureAtlas(key, rawTexture, 1, true);
		String fontFile = loadFontFile(SEPARATOR + path + ".fnt");
		return new RawFontType(key, fontAtlas, fontFile);
	}

	private static String loadFontFile(String path) {
		return StringConverter.combineAll(FileUtil.getResDataFile(path));
	}

	// SHUTDOWN
	public static void cleanUp() {
		ImageLoader.cleanUp();
	}

}