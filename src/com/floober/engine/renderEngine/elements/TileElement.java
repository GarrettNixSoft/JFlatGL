package com.floober.engine.renderEngine.elements;

import com.floober.engine.textures.TextureAtlas;
import org.joml.Vector2f;

public class TileElement extends RenderElement {

	private final TextureAtlas textureAtlas;
	private final Vector2f texOffsets;

	public TileElement(TextureAtlas textureAtlas, int atlasIndex, float x, float y, float z) {
		super(x, y, z);
		this.textureAtlas = textureAtlas;
		texOffsets = new Vector2f();
		setTextureOffset(texOffsets, atlasIndex);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % textureAtlas.getNumRows();
		int row = index / textureAtlas.getNumRows();
		offset.x = (float) column / textureAtlas.getNumRows();
		offset.y = (float) row / textureAtlas.getNumRows();
	}

	public TextureAtlas getTextureAtlas() { return textureAtlas; }
	public Vector2f getTexOffsets() {
		return texOffsets;
	}

}