package com.floober.engine.util.conversion;

import com.floober.engine.assets.loaders.ImageLoader;
import com.floober.engine.renderEngine.textures.TextureComponent;
import com.floober.engine.util.Logger;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class ImageConverter {

	/**
	 * Convert a BufferedImage to an array of Textures with the given width.
	 * @param image The BufferedImage to split up.
	 * @param textureWidth The width of each texture.
	 * @return An array of Textures.
	 */
	public static TextureComponent[] convertToTextureArray(BufferedImage image, int textureWidth) {
		// warn if the image does not evenly divide by the width
		if (image.getWidth() % textureWidth != 0)
			Logger.logWarning("BufferedImage width does not divide evenly by given width (" + image.getWidth() + ")");
		// initialize the results array
		TextureComponent[] results = new TextureComponent[image.getWidth() / textureWidth];
		// loop through and populate the array
		for (int i = 0; i < results.length; ++i) {
			BufferedImage subimage = image.getSubimage(i * textureWidth,  0, textureWidth, image.getHeight());
			ByteBuffer imageData = ImageLoader.getBufferedImageData(subimage);
			results[i] = ImageLoader.loadTexture(imageData);
		}
		// return the results
		return results;
	}

	/**
	 * Convert a BufferedImage to an array of Textures with the given dimensions.
	 * @param image The BufferedImage to split up.
	 * @param textureWidth The width of each texture.
	 * @param textureHeight The height of each texture.
	 * @return An array of Textures.
	 */
	public static TextureComponent[] convertToTextureArray(BufferedImage image, int textureWidth, int textureHeight) {
		// warn if the image does not evenly divide by the given dimensions
		if (image.getWidth() % textureWidth != 0)
			Logger.logWarning("BufferedImage width does not divide evenly by given width (" + image.getWidth() + ")");
		if (image.getHeight() % textureHeight != 0)
			Logger.logWarning("BufferedImage height does not divide evenly by given height (" + image.getHeight() + ")");
		// initialize the results array
		int numRows = image.getHeight() / textureHeight;
		int numCols = image.getWidth() / textureWidth;
		TextureComponent[] results = new TextureComponent[numRows * numCols];
		// loop through and populate the array
		for (int i = 0; i < results.length; i++) {
			BufferedImage subimage = image.getSubimage(i % numCols * textureWidth, i / numRows * textureHeight, textureWidth, textureHeight);
			ByteBuffer imageData = ImageLoader.getBufferedImageData(subimage);
			results[i] = ImageLoader.loadTexture(imageData);
		}
		// return the results
		return results;
	}

}