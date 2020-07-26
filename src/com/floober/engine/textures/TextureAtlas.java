package com.floober.engine.textures;

import org.joml.Vector4f;

/**
 * A TextureAtlas represents an image with multiple textures laid out
 * in a square grid of a given size. Useful for drawing elements that
 * change textures frequently, such as animated elements; to get a specific
 * texture from the atlas, just request the offset vector for that texture's
 * index, and pass that vector to the shader to use as texture coordinates.
 */
public class TextureAtlas extends Texture {

	private final int numRows;

	public TextureAtlas(int textureID, int width, int height, int numRows) {
		super(textureID, width, height);
		this.numRows = numRows;
	}

	public int getNumRows() {
		return numRows;
	}

	public Vector4f getTextureOffset(int index) {
		float xPerIndex = (float) width / numRows;
		float yPerIndex = (float) height / numRows;
		int xIndex = index % numRows;
		int yIndex = index / numRows;
		return new Vector4f(xIndex * xPerIndex, yIndex * yPerIndex, xPerIndex, yPerIndex);
	}

}