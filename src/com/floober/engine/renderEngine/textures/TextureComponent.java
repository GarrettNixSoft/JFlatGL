package com.floober.engine.renderEngine.textures;

import org.joml.Vector4f;

public class TextureComponent {

	// standard texture data
	private final Texture texture;
	private final Vector4f textureOffset = new Vector4f();
	private boolean hasTransparency;
	private float alpha = 1;

	public TextureComponent(Texture texture) {
		this.texture = texture;
		hasTransparency = false;
	}

	public TextureComponent(Texture texture, boolean hasTransparency) {
		this.texture = texture;
		this.hasTransparency = hasTransparency;
	}

	// GETTERS
	public Texture texture() { return texture; }

	public int id() { return texture.id(); }

	public int width() {
		return texture.width();
	}

	public int height() {
		return texture.height();
	}

	public Vector4f getTextureOffset() {
		return textureOffset;
	}

	public boolean hasTransparency() {
		return hasTransparency;
	}

	public float getAlpha() {
		return alpha;
	}

	// SETTERS
	public void setTextureOffset(Vector4f textureOffset) {
		this.textureOffset.set(textureOffset);
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

}