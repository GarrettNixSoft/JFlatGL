package com.floober.engine.core.util.conversion;

import com.floober.engine.core.assets.loaders.ImageLoader;
import com.floober.engine.core.assets.loaders.RawTexture;
import com.floober.engine.core.assets.loaders.gameassets.temp.RawTextureArray;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.Logger;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.nio.ByteBuffer;

public class ImageConverter {

	/**
	 * Convert a BufferedImage to an array of Textures with the given width.
	 * @param image The BufferedImage to split up.
	 * @param textureWidth The width of each texture.
	 * @return An array of Textures.
	 */
	public static RawTexture[] convertToTextureArray(BufferedImage image, int textureWidth) {
		// warn if the image does not evenly divide by the width
		if (image.getWidth() % textureWidth != 0)
			throw new IllegalArgumentException("BufferedImage width does not divide evenly by given width (" + image.getWidth() + " / " + textureWidth + ")");
		// initialize the results array
		RawTexture[] results = new RawTexture[image.getWidth() / textureWidth];
		// loop through and populate the array
		for (int i = 0; i < results.length; ++i) {
			BufferedImage subimage = image.getSubimage(i * textureWidth,  0, textureWidth, image.getHeight());
			ByteBuffer imageData = ImageLoader.getBufferedImageData(subimage);
			results[i] = new RawTexture(imageData, image.getType(), subimage.getWidth(), subimage.getHeight());
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
	public static RawTexture[] convertToTextureArray(BufferedImage image, int textureWidth, int textureHeight) {
		// warn if the image does not evenly divide by the given dimensions
		if (image.getWidth() % textureWidth != 0)
			throw new IllegalArgumentException("BufferedImage width does not divide evenly by given width (" + image.getWidth() + " / " + textureWidth + ")");
		if (image.getHeight() % textureHeight != 0)
			throw new IllegalArgumentException("BufferedImage height does not divide evenly by given height (" + image.getHeight() + " / " + textureHeight + ")");
		// initialize the results array
		int numRows = image.getHeight() / textureHeight;
		int numCols = image.getWidth() / textureWidth;
		RawTexture[] results = new RawTexture[numRows * numCols];
		// loop through and populate the array
		for (int i = 0; i < results.length; i++) {
			BufferedImage subimage = image.getSubimage(i % numCols * textureWidth, i / numRows * textureHeight, textureWidth, textureHeight);
			ByteBuffer imageData = ImageLoader.getBufferedImageData(subimage);
			results[i] = new RawTexture(imageData, image.getType(), textureWidth, textureHeight);
		}
		// return the results
		return results;
	}

}