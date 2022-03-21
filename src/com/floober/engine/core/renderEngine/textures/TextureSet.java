package com.floober.engine.core.renderEngine.textures;

import org.joml.Vector4f;

import java.util.Iterator;
import java.util.NoSuchElementException;

public record TextureSet(TextureComponent baseTex, int texWidth, int texHeight, boolean hasTransparency) implements Iterable<TextureComponent> {

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
		Texture texCopy = new Texture(baseTex.id(), texWidth, texHeight);
		Vector4f offsets = getTextureOffset(index);
		TextureComponent frame = new TextureComponent(texCopy);
		frame.setTextureOffset(offsets);
		frame.setHasTransparency(hasTransparency);
		return frame;
	}

	public Iterator<TextureComponent> iterator() {
		return new Iterator<>() {

			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < getNumTextures();
			}

			@Override
			public TextureComponent next() {
				return getFrame(index++);
			}

		};
	}

}