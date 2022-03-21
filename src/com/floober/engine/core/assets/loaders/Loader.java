package com.floober.engine.core.assets.loaders;

import com.floober.engine.core.audio.AudioMaster;
import com.floober.engine.core.audio.Sound;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.conversion.ImageConverter;
import com.floober.engine.core.util.conversion.StringConverter;
import com.floober.engine.core.util.file.FileUtil;

import java.awt.image.BufferedImage;

import static com.floober.engine.core.util.file.FileUtil.SEPARATOR;

public class Loader {

	public static TextureComponent loadTexture(String path) {
		path = "textures" + SEPARATOR + path;
		return ImageLoader.loadTexture(path);
	}

	public static TextureComponent[] loadTextureArray(String path, int width) {
		path = "textures" + SEPARATOR + path;
		BufferedImage bufferedImage = ImageLoader.loadBufferedImage(path);
		return ImageConverter.convertToTextureArray(bufferedImage, width);
	}

	public static TextureComponent[] loadTextureArray(String path, int width, int height) {
		path = "textures" + SEPARATOR + path;
		BufferedImage bufferedImage = ImageLoader.loadBufferedImage(path);
		return ImageConverter.convertToTextureArray(bufferedImage, width, height);
	}

	public static Sound loadMusic(String path) {
		path = "music" + SEPARATOR + path;
		return AudioMaster.loadSound(path);
	}

	public static Sound loadSfx(String path) {
		path = "sfx" + SEPARATOR + path;
		return AudioMaster.loadSound(path);
	}

	public static FontType loadFont(String font) {
		String path = "fonts" + SEPARATOR + font + SEPARATOR + font;
		int fontAtlas = loadFontAtlas(path + ".png");
		String fontFile = loadFontFile(SEPARATOR + path + ".fnt");
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