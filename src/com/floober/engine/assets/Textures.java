package com.floober.engine.assets;

import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.renderEngine.textures.TextureAtlas;
import com.floober.engine.renderEngine.textures.TextureComponent;
import com.floober.engine.renderEngine.textures.TextureSet;
import com.floober.engine.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class Textures {

	// STORING TEXTURES
	private final Map<String, Texture> textures = new HashMap<>();
	private final Map<String, TextureSet> textureSets = new HashMap<>();
	private final Map<String, Texture[]> textureArrays = new HashMap<>();
	private final Map<String, TextureAtlas> textureAtlases = new HashMap<>();

	// LOADING TEXTURES
	public void addTexture(String key, Texture texture) {
		textures.put(key, texture);
	}
	public void addTextureSet(String key, TextureSet textureSet) { textureSets.put(key, textureSet); }
	public void addTextureArray(String key, Texture[] textureArray) {
		textureArrays.put(key, textureArray);
	}
	public void addTextureAtlas(String key, TextureAtlas textureAtlas) {
		textureAtlases.put(key, textureAtlas);
	}

	// RETRIEVING TEXTURES
	public TextureComponent getTexture(String key) {
		Texture texture = textures.get(key);
		if (texture == null) Logger.logError("Texture with key " + key + " does not exist");
		return new TextureComponent(texture);
	}

	public TextureSet getTextureSet(String key) {
		return textureSets.get(key);
	}

	public Texture[] getTextureArray(String key) { return textureArrays.get(key); }
	public TextureAtlas getTextureAtlas(String key) {
		return textureAtlases.get(key);
	}

	public static TextureComponent wrapTexture(Texture texture) {
		return new TextureComponent(texture);
	}

	public static TextureSet generateStaticSet(TextureComponent textureComponent) {
		return new TextureSet(textureComponent.texture(), textureComponent.width(), textureComponent.height(), textureComponent.hasTransparency());
	}

}