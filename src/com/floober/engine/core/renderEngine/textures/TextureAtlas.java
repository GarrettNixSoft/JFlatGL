package com.floober.engine.core.renderEngine.textures;

import org.joml.Vector4f;

/**
 * A TextureAtlas represents an image with multiple textures laid out
 * in a square grid of a given size. Useful for drawing elements that
 * change textures frequently, such as animated elements; to get a specific
 * texture from the atlas, just request the offset vector for that texture's
 * index, and pass that vector to the shader to use as texture coordinates.
 */
public record TextureAtlas(TextureComponent texture, int numRows, boolean hasTransparency) {

	public int getNumTextures() {
		return numRows * numRows;
	}

	public int width() {
		return texture.width();
	}

	public int height() {
		return texture.height();
	}

	public Vector4f getTextureOffset(int index) {
		float xPerIndex = (float) texture.width() / numRows;
		float yPerIndex = (float) texture.height() / numRows;
		int xIndex = index % numRows;
		int yIndex = index / numRows;
		return new Vector4f(xIndex * xPerIndex, yIndex * yPerIndex, xPerIndex, yPerIndex);
	}

}