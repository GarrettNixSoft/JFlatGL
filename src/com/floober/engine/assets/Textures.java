package com.floober.engine.assets;

import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.renderEngine.textures.TextureAtlas;

import java.util.HashMap;
import java.util.Map;

public class Textures {

	// STORING TEXTURES
	private final Map<String, Texture> textures = new HashMap<>();
	private final Map<String, TextureAtlas> textureAtlases = new HashMap<>();

	// LOADING TEXTURES
	public void addTexture(String key, Texture texture) {
		textures.put(key, texture);
	}
	public void addTextureAtlas(String key, TextureAtlas textureAtlas) {
		textureAtlases.put(key, textureAtlas);
	}

	// RETRIEVING TEXTURES
	public Texture getTexture(String key) {
		return textures.get(key);
	}
	public TextureAtlas getTextureAtlas(String key) {
		return textureAtlases.get(key);
	}

}