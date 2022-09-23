package com.gnix.jflatgl.core.assets.loaders.gameassets.temp;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.assets.loaders.RawTexture;
import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;

public record RawTextureAtlas(String key, RawTexture texture, int numRows, boolean hasTransparency) {

	public void addToGame() {
		TextureAtlas textureAtlas = convert();
		Game.getTextures().addTextureAtlas(key, textureAtlas);
	}

	public TextureAtlas convert() {
		TextureComponent temp = texture.convertToOpenGLTexture();
		return new TextureAtlas(temp, numRows, hasTransparency);
	}

}
