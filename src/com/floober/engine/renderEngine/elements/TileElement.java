package com.floober.engine.renderEngine.elements;

import com.floober.engine.renderEngine.textures.TextureAtlas;
import org.joml.Vector2f;

public class TileElement extends TextureElement {

	private final TextureAtlas textureAtlas;
	private final Vector2f texOffsets;

	public TileElement(TextureAtlas textureAtlas, int atlasIndex, float x, float y, float z, int tileSize) {
		super(textureAtlas, x, y, z, tileSize, tileSize, false);
		this.textureAtlas = textureAtlas;
		texOffsets = new Vector2f();
		setTextureOffset(texOffsets, atlasIndex);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % textureAtlas.getNumRows();
		int row = index / textureAtlas.getNumRows();
		offset.x = (float) column / textureAtlas.getNumRows();
		offset.y = (float) row / textureAtlas.getNumRows();
//		Logger.log("Offsets for index " + index + " computed: " + offset);
	}

	// GETTERS
	public TextureAtlas getTextureAtlas() { return textureAtlas; }
	public Vector2f getTexOffsets() {
		return texOffsets;
	}

	// SETTERS
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		transform(false);
	}

	public void setTextureIndex(int index) {
		setTextureOffset(texOffsets, index);
	}

}