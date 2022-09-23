package com.gnix.jflatgl.core.assets.loaders.gameassets.temp;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.assets.loaders.RawTexture;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.renderEngine.textures.TextureSet;

public record RawTextureSet(String key, RawTexture texture, int texWidth, int texHeight, boolean hasTransparency) {

	public TextureSet convert() {
		TextureComponent textureComponent = texture.convertToOpenGLTexture();
		return new TextureSet(textureComponent, texWidth, texHeight, hasTransparency);
	}

	public void addToGame() {
		TextureSet textureSet = convert();
		Game.getTextures().addTextureSet(key, textureSet);
	}

}
