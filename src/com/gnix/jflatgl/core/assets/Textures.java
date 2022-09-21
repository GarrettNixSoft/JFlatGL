package com.gnix.jflatgl.core.assets;

import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.renderEngine.textures.TextureSet;
import com.gnix.jflatgl.core.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class Textures {

	// STORING TEXTURES
	private final Map<String, TextureComponent> textures = new HashMap<>();
	private final Map<String, TextureSet> textureSets = new HashMap<>();
	private final Map<String, TextureComponent[]> textureArrays = new HashMap<>();
	private final Map<String, TextureAtlas> textureAtlases = new HashMap<>();

	// LOADING TEXTURES
	public void addTexture(String key, TextureComponent texture) {
		textures.put(key, texture);
	}
	public void addTextureSet(String key, TextureSet textureSet) { textureSets.put(key, textureSet); }
	public void addTextureArray(String key, TextureComponent[] textureArray) {
		textureArrays.put(key, textureArray);
	}
	public void addTextureAtlas(String key, TextureAtlas textureAtlas) {
		textureAtlases.put(key, textureAtlas);
	}

	// RETRIEVING TEXTURES
	public TextureComponent getTexture(String key) {
		TextureComponent texture = textures.get(key);
		if (texture == null) Logger.logError("Texture with key " + key + " does not exist");
		return texture;
	}

	public TextureSet getTextureSet(String key) {
		return textureSets.get(key);
	}

	public TextureComponent[] getTextureArray(String key) {
		return textureArrays.get(key);
	}

	public TextureAtlas getTextureAtlas(String key) {
		return textureAtlases.get(key);
	}

	public static TextureSet generateStaticSet(TextureComponent textureComponent) {
		return new TextureSet(textureComponent, textureComponent.width(), textureComponent.height(), textureComponent.hasTransparency());
	}

}
