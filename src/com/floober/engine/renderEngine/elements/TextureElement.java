package com.floober.engine.renderEngine.elements;

import com.floober.engine.textures.Texture;
import org.joml.Vector4f;

public class TextureElement extends RenderElement {

	private final Texture texture;
	private Vector4f textureOffset;

	public TextureElement(Texture texture, float x, float y, float z, float width, float height, boolean centered) {
		super(x, y, z);
		this.texture = texture;
		this.width = width;
		this.height = height;
		transform(centered);
		setTextureOffset(new Vector4f(0, 0, 1, 1));
	}

	public TextureElement(Texture texture, float x, float y, float z, boolean centered) {
		super(x, y, z);
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		transform(centered);
		setTextureOffset(new Vector4f(0, 0, 1, 1));
	}

	public void setTextureOffset(Vector4f textureOffset) {
		this.textureOffset = textureOffset;
	}

	public Texture getTexture() {
		return texture;
	}
	public Vector4f getTextureOffset() { return textureOffset; }
}