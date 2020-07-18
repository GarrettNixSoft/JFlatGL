package com.floober.engine.renderEngine.elements;

import com.floober.engine.textures.Texture;

public class TextureElement extends RenderElement {

	private final Texture texture;

	public TextureElement(Texture texture, float x, float y, float z, float width, float height, boolean centered) {
		super(x, y, z);
		this.texture = texture;
		this.width = width;
		this.height = height;
		transform(centered);
	}

	public TextureElement(Texture texture, float x, float y, float z, boolean centered) {
		super(x, y, z);
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		transform(centered);
	}

	public Texture getTexture() {
		return texture;
	}
}