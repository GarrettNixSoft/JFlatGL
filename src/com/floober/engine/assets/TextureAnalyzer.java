package com.floober.engine.assets;

import com.floober.engine.assets.loaders.ImageLoader;
import com.floober.engine.util.Logger;
import com.floober.engine.util.file.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.image.BufferedImage;

import static com.floober.engine.util.file.FileUtil.*;

public class TextureAnalyzer {

	public static final String CACHE_LOCATION = GAME_DIR + SEPARATOR + "resourceDataCache" + SEPARATOR + "textureData.json";

	private static JSONObject textureData;

	/**
	 * Load the texture cache data from disk. This operation is independent
	 * of all other game systems and can be called at any time during the load
	 * sequence before textures are loaded.
	 */
	public static void init() {
		try {
			Logger.log("Loading JSON file: " + CACHE_LOCATION);
			textureData = getOrCreateJSON(CACHE_LOCATION);
		}
		catch (JSONException e) {
			textureData = new JSONObject();
		}
	}

	/**
	 * Determine whether a texture is known to the analyzer.
	 * "Known" refers to whether there is an entry for that
	 * texture in the texture data JSON file.
	 * @param textureKey the key for the texture in question
	 * @return {@code true} if the texture has an entry in the JSON file
	 */
	public static boolean isKnown(String textureKey) {
		String textureInfo = textureData.optString(textureKey, null);
		return textureInfo != null;
	}

	public static boolean knownTransparencyValue(String textureKey) {
		return textureData.optBoolean(textureKey);
	}

	/**
	 * Given a String representing a file path to an image, load
	 * that image as a BufferedImage and determine whether it
	 * contains any pixels with less than full opacity. Save
	 * the result to the JSON file using the texture's key.
	 * @param path the path to the image file
	 * @param key the key by which the image is reference by the asset manager
	 * @return {@code true} if any pixel in the image has an alpha value of less than 255
	 */
	public static boolean findTransparencyInTexture(String path, String key) {
		// get the texture as a buffered image
		BufferedImage image = ImageLoader.loadBufferedImage("textures" + SEPARATOR + path);
		// search for transparency in the image
		boolean hasTransparency = false;
		int width = image.getWidth();
		int height = image.getHeight();
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				int pixel = image.getRGB(c, r);
				int alpha = pixel >>> 24;
				if (alpha < 0xff) {
					hasTransparency = true;
					break;
				}
			}
			if (hasTransparency) break;
		}
		// save data to cache
		textureData.put(key, hasTransparency);
		Logger.logLoad("Updated cache entry for " + path + ": " + hasTransparency);
//		Logger.log(textureData.toString());
		// update file
		FileUtil.writeJSON(textureData, CACHE_LOCATION);
		// return result
		return hasTransparency;
	}

}
