package com.floober.engine.textures;

public class TextureAtlas {

	private final int textureID;
	private final int numRows;

	public TextureAtlas(int textureID, int numRows) {
		this.textureID = textureID;
		this.numRows = numRows;
	}

	public int getTextureID() {
		return textureID;
	}
	public int getNumRows() {
		return numRows;
	}

}