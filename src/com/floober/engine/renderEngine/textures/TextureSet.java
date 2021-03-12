package com.floober.engine.renderEngine.textures;

import org.joml.Vector4f;

import java.util.NoSuchElementException;

public record TextureSet(Texture baseTex, int texWidth, int texHeight) {

	public int getNumTextures() { return (baseTex().width() / texWidth) * (baseTex().height() / texHeight); }

	public Vector4f getTextureOffset(int index) {
		// validate index
		int totalTextures = getNumTextures();
		if (index >= totalTextures || index < 0) throw new NoSuchElementException("Tried to fetch a texture from a texture set that was out of bounds. (" + totalTextures + " textures exist, tried to fetch index " + index + ")");
		// index validated; compute texture coordinates
		int texturesPerRow = (baseTex.width() / texWidth);
		int row = index / texturesPerRow;
		int col = index % texturesPerRow;
		float frameSizeX = (float) texWidth / baseTex.width();
		float frameSizeY = (float) texHeight / baseTex.height();
		return new Vector4f(col * frameSizeX, row * frameSizeY, frameSizeX, frameSizeY);
	}

	public TextureComponent getFrame(int index) {
		Texture texCopy = baseTex.copy();
		Vector4f offsets = getTextureOffset(index);
		TextureComponent frame = new TextureComponent(texCopy);
		frame.setTextureOffset(offsets);
		return frame;
	}

}