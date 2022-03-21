package com.floober.engine.core.renderEngine.textures;

import org.joml.Vector4f;

public class TextureComponent {

	// standard texture data
	private final Texture texture;
	private final Vector4f textureOffset = new Vector4f(0, 0, 1, 1);
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

	public TextureComponent(int textureID, int width, int height) {
		this.texture = new Texture(textureID, width, height);
		this.hasTransparency = false;
	}

	public TextureComponent(int textureID, int width, int height, boolean hasTransparency) {
		this.texture = new Texture(textureID, width, height);
		this.hasTransparency = hasTransparency;
	}

	public TextureComponent(TextureComponent other) {
		this.texture = other.texture;
		this.textureOffset.set(other.textureOffset);
		this.hasTransparency = other.hasTransparency;
		this.alpha = other.alpha;
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